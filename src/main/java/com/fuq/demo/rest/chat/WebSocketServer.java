package com.fuq.demo.rest.chat;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fuq.demo.tool.dto.UserInfo;
import com.fuq.demo.tool.redis.RedisUtil;

import lombok.extern.slf4j.Slf4j;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@Component
@ServerEndpoint("/websocket_server/{username}")
//此注解相当于设置访问URL
public class WebSocketServer {
	
    private Session session;
    private String username;

    private static CopyOnWriteArraySet<WebSocketServer> webSockets =new CopyOnWriteArraySet<>();
    private static Map<String,Session> sessionPool = new HashMap<String,Session>();
    
    
    @OnOpen
    public void onOpen(@PathParam("username") String username,Session session) throws IOException {
        this.session = session;
        this.username = username;
        System.out.println(username);
        Object hget = RedisUtil.hget(RedisUtil.AUTH, username);
        if (hget != null) {
        	String rdeisUserName = ((UserInfo) RedisUtil.hget(RedisUtil.AUTH, username)).getUsername();
            
            if (username.equals(rdeisUserName)) {
            	sendTextMessage(username, "认证信息校验成功！");
//                sendMessage2User(MessageBean.builder().sendType(MessageBean.SendMessageType.LOGIN_SUCCESS).message("认证信息校验成功！").build());
                log.info("用户{}登录成功！token:{},redisToken:{}", username, rdeisUserName);
            }else {
            	session.getAsyncRemote().sendObject("认证信息校验未通过！");
//                sendMessage2User(MessageBean.builder().sendType(MessageBean.SendMessageType.LOGIN_FAIL).message("认证信息校验未通过！").build());
                log.info("用户{}登录失败！token:{},redisToken:{}", username, rdeisUserName);
            }
        }
        
        Map<Object, Object> authMap = RedisUtil.hmget(RedisUtil.AUTH);
        
        //添加到本地缓存
        webSockets.add(this);
        sessionPool.put(username, session);
        System.out.println("在线总人数为" + authMap.size());
        System.out.println("在线总人数为" + sessionPool.size() + ",【websocket消息】有新的连接，总数为:"+webSockets.size());
    }

    @OnClose
    public void onClose() {
    	try{
    		//移除redis中用户信息
    		RedisUtil.hdel(RedisUtil.AUTH, username);
        } catch (Exception e){
            log.error("移除redis中用户{}信息失败, token:{}！",username, e);
        }
    	webSockets.remove(this);
    	sessionPool.remove(username);
        log.info("用户{}退出成功！", username);
        
        //移除本地缓存
        webSockets.remove(this);
    	RedisUtil.hdel(RedisUtil.CLIENT, username);
        System.out.println("【websocket消息】连接断开，总数为:"+webSockets.size());
    }

    @OnMessage
    public void onMessage(String message) throws JsonParseException, JsonMappingException, IOException {
        System.out.println("【websocket消息】收到客户端消息:"+message);
    }
    
    // 此为广播消息
    public void sendAllMessage(String message) {
        for(WebSocketServer webSocket : webSockets) {
            System.out.println("【websocket消息】广播消息:"+message);
            try {
                webSocket.session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

 // 此为单点消息 (发送文本)
    public void sendTextMessage(String username, String message) {
        Session session = sessionPool.get(username);
        if (session != null) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 此为单点消息 (发送对象)
    public void sendObjMessage(String username, Object message) {
        Session session = sessionPool.get(username);
        if (session != null) {
            try {
                session.getAsyncRemote().sendObject(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    } 
    
    
}
