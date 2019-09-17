package com.fuq.demo.tool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Result 枚举类
 * @author FuqCalendar
 * @date 2019��9��4��13:49:30
 *
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ResultEnum {
	
	SUCCESS(200, "请求成功！"),
	FAIL(0, "请求失败！");
	
	private Integer code;
	
	private String msg;

}
