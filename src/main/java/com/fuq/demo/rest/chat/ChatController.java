package com.fuq.demo.rest.chat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fuq.demo.tool.bean.ChatBean;
import com.fuq.demo.tool.result.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "聊天控制器Controller", tags = "聊天控制器")
@RequestMapping("/chat")
public class ChatController {
	
	@Autowired
	private WebSocketServer webSocketServer;
	
	@PostMapping("/sendText_One")
	@ApiOperation(value = "发送消息",tags = "发送单人文本消息，参数只需赋值userName,Message")
	public Result sendTextToOne (@RequestBody ChatBean chatBean) {
		webSocketServer.sendTextMessage(chatBean.getUserName(), chatBean.getMessage());
		return Result.success();
	}
	
	@PostMapping("/sendText_Many")
	@ApiOperation(value = "发送消息",tags = "发送多人文本消息，参数只需赋值useList,message")
	public Result sendTextToMany (@RequestBody ChatBean chatBean) {
		List<String> userList = chatBean.getUserList();
		for (String userName : userList) {
			webSocketServer.sendTextMessage(chatBean.getUserName(), chatBean.getMessage());
		}
		return Result.success();
	}
	
	@PostMapping("/sendObj_One")
	@ApiOperation(value = "发送消息",tags = "发送单人非文本消息，参数只需赋值userName,obj")
	public Result sendObjToOne (@RequestBody ChatBean chatBean) {
		webSocketServer.sendObjMessage(chatBean.getUserName(), chatBean.getObj());
		return Result.success();
	}
	
	@PostMapping("/sendObj_Many")
	@ApiOperation(value = "发送消息",tags = "发送多人非文本消息，参数只需赋值useList,obj")
	public Result sendObjToMany (@RequestBody ChatBean chatBean) {
		List<String> userList = chatBean.getUserList();
		for (String userName : userList) {
			webSocketServer.sendObjMessage(chatBean.getUserName(), chatBean.getObj());
		}
		return Result.success();
	}
}
