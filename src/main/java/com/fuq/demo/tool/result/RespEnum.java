package com.fuq.demo.tool.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum RespEnum {
	
	SUCCESS(200, "操作成功！"),
	FAIL(0, "操作失败！"),
	;
	private Integer code;
	
	private String msg;
}
