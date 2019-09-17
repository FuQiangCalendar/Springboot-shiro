package com.fuq.demo.tool.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 数据库表操作枚举类
 * @author FuqCalendar
 * @date 2019��9��4��13:57:39
 *
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum TableOprateEnum {
	
	INSERT_SUCCESS(1001, "新增成功！"),
	INSERT_FAIL(1002, "新增失败！"),
	DELETE_SUCCESS(1003, "删除成功！"),
	DELETE_FAIL(1004, "删除失败！"),
	DELETE_NULL(100401, "系统无法匹配，删除失败！"),
	UPDATE_SUCCESS(1005, "修改成功！"),
	UPDATE_FAIL(1006, "修改失败！"),
	UPDATE_NULL(100601, "系统无法匹配，修改失败！"),
	SELECT_SUCCESS(1007, "查询成功！"),
	SELECT_NULL(1008, "未查到数据！"),
	SELECT_FAIL(1009,"查询失败！");
	;
	
	private Integer code;
	
	private String msg;
	
}
