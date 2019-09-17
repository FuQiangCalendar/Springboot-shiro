package com.fuq.demo.tool.bean;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 	消息实体类
 * @author fuqCalendar
 * @date 2019年9月16日17:09:19
 *
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ChatBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//发送单人
	private String userName;
	
	//文本消息
	private String message;
	
	//发送多人
	private List<String> userList;
	
	//非文本对象
	private Object obj;

}
