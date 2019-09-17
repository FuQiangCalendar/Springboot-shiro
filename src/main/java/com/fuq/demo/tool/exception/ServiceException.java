package com.fuq.demo.tool.exception;

import com.fuq.demo.tool.result.RespEnum;

import lombok.Getter;
import lombok.Setter;

/**
 * 业务层异常处理类
 * @author FuqCalendar
 * @date 2019年9月5日13:34:40
 *
 */
@Setter
@Getter
public class ServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer code;

	private RespEnum respEnum;
	
	public ServiceException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public ServiceException(Integer code,String message) {
        super(message);
        this.code = code;
    }

    public ServiceException(RespEnum respEnum) {
        super(respEnum.getMsg());
        this.code = respEnum.getCode();
    }
	

	public ServiceException(String message) {
		super(message);
	}

	
	
	


	
	
}
