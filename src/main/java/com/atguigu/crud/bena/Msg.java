package com.atguigu.crud.bena;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用的返回的类
 * @author Snow
 *
 */

public class Msg {
	//状态吗 100-成功，200-失败
	private int code;
	//提示信息
	private String message;
	
	//用户要返回给浏览器的数据
	private Map<String, Object> extend= new HashMap<String, Object>();
	
	public static Msg succes()
	{
		Msg result = new Msg();
		result.setCode(100);
		result.setMessage("处理成功");
		return result;
	}
	
	public Msg() {
		super();
	}

	public Msg(int code, String message, Map<String, Object> extend) {
		super();
		this.code = code;
		this.message = message;
		this.extend = extend;
	}

	public static Msg fail()
	{
		Msg result = new Msg();
		result.setCode(200);
		result.setMessage("处理failed");
		return result;
	}
	
	public Msg Add(String key, Object value)
	{
		this.getExtend().put(key,value);
		return this;
	}
	

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, Object> getExtend() {
		return extend;
	}

	public void setExtend(Map<String, Object> extend) {
		this.extend = extend;
	}
	
	
}
