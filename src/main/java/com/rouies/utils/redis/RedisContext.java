package com.rouies.utils.redis;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisContext {
	
	private static HashMap<String, JedisPool> pools = new HashMap<String, JedisPool>();
	
	private static ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	
	public static void init(String poolName,JedisPoolConfig config,String host,int port,String passwd,int timeout){
		lock.writeLock().lock();
		try {
			JedisPool pool = new JedisPool(config, host,port,timeout,passwd);
			pools.put(poolName, pool);
		} catch (Exception e) {

		} finally {
			lock.writeLock().unlock();
		}
	}
	
	
	
	public static Jedis getClient(String poolName) throws RedisException{
		Jedis result = null;
		lock.readLock().lock();
		JedisPool pool = pools.get(poolName);
		if(pool != null){
			try {
				result = pool.getResource();
			} catch (Exception e) {
				throw new RedisException("验证redis链接信息失败",e);
			}
		}
		lock.readLock().unlock();
		return result;
	}
	
	public static Jedis getClient(String host,int port,String passwd,int timeout) throws RedisException{
		Jedis result = new Jedis(host, port, timeout);
		try {
			result.auth(passwd);
		} catch (Exception e) {
			throw new RedisException("验证redis链接信息失败",e);
		}
		return result;
	}
	
	public static void main(String[] args) throws RedisException {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(5);
		config.setMaxIdle(3);
		config.setMinIdle(1);
		//在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
		config.setTestOnBorrow(true);
//		RedisContext.init("test", config, "127.0.0.1", 6379, "rouies1", 0);
//		Jedis jedis = RedisContext.getJedis("test");
//		String set = jedis.set("name".getBytes(), "zhangsan".getBytes());
//		System.out.println(set);
//		jedis.close();
		Jedis jedis = RedisContext.getClient("127.0.0.1", 6379, "rouies", 0);
		Long set = jedis.setnx("name2".getBytes(), "zhangsan".getBytes());
		System.out.println(set);
		jedis.close();
	}
	
	
	
}
