package com.rouies.utils.redis;

public class RedisException extends Exception{

	private static final long serialVersionUID = -2746080068985051482L;
	
	public RedisException(String message){
		super(message);
	}
	
	public RedisException(String message,Throwable throwable){
		super(message,throwable);
	}
	
}
