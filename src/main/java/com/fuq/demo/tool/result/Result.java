package com.fuq.demo.tool.result;

import com.fuq.demo.tool.enums.ResultEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Result<T> {
	/** http status code*/
	public int code;
	
	/** message */
    public String msg;
    
    /** 数据 */
    public T data;
    
    /**
     * 不传数据的构造
     * @param code
     * @param msg
     */
    public Result(int code, String msg) {
		super();
		this.code = code;
		this.msg = msg;
	}
    
    /**
     * 请求成功，返回默认状态码以及实时的信息和数据
     * @param msg
     * @param data
     * @author FuqCalendar
     * @date 2019年9月4日17:35:15
     * @return
     */
    public static Result success(String msg, Object data){
    	return new Result(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),data);
    }
    
    /**
     * 请求成功，返回默认状态码和信息，以及实时数据（重载1）
     * @param data
     * @author FuqCalendar
     * @date 2019年9月4日17:35:46
     * @return
     */
    public static Result success(Object data){
    	return new Result(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg(),data);
    }
    
    /**
     * 请求成功，返回默认状态码和信息（重载2）
     * @author FuqCalendar
     * @date 2019年9月4日17:36:01
     * @return
     */
    public static Result success(){
    	return new Result(ResultEnum.SUCCESS.getCode(),ResultEnum.SUCCESS.getMsg());
    }
    
    /**
     * 请求失败，返回默认状态码，以及实时的信息
     * @author FuqCalendar
     * @date 2019年9月4日17:36:51
     * @param msg
     * @return
     */
    public static  Result fail( String msg){
    	return new Result(ResultEnum.FAIL.getCode(),msg);
    }
    
    /**
     * 请求失败，返回默认状态码和信息
     * @author FuqCalendar
     * @date 2019年9月4日17:37:09
     * @return
     */
    public static  Result fail(){
    	return new Result(ResultEnum.FAIL.getCode(),ResultEnum.FAIL.getMsg());
    }
}
