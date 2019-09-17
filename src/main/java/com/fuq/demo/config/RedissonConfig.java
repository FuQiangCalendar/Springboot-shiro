/**  
 * All rights Reserved, Designed By www.cnbisoft.com
 * @Title:  RedissonConfig.java   
 * @Package com.cnbi.cloud.tjsp.config   
 * @Description:    配置    
 * @author: 经邦软件    
 * @date:   2018年12月7日 下午3:24:55   
 * @version V1.0 
 * @Copyright: 2018 www.cnbisoft.com Inc. All rights reserved. 
 * 注意：本内容仅限于安徽经邦软件技术有限公司内部传阅，禁止外泄以及用于其他的商业目
 */ 
package com.fuq.demo.config;


import java.time.Duration;
import java.util.HashMap;

import javax.annotation.Resource;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fuq.demo.tool.redis.RedisKeySerializer;

import net.sf.ehcache.management.CacheManager;

/**   
 * @ClassName:  RedissonConfig   
 * @Description:redission 配置
 * @author: cnbizhh 
 * @date:   2018年12月7日 下午3:24:55   
 *     
 * @Copyright: 2018 www.cnbisoft.com Inc. All rights reserved. 
 * 注意：本内容仅限于安徽经邦软件技术有限公司内部传阅，禁止外泄以及用于其他的商业目 
 */
@Configuration
@EnableCaching
public class RedissonConfig {
	
	/**   
	 * @Fields host : 主机名   
	 */ 
	@Value("${spring.redis.host}")
	private String host;
	
	/**   
	 * @Fields port : 端口  
	 */ 
	@Value("${spring.redis.port}")
	private String port;
	
	/**   
	 * @Fields password : 密码  
	 */ 
	@Value("${spring.redis.password}")
	private String password;
	
	
	/***
	 * 
	 * @Title: getRedissonClient   
	 * @Description: 获取 RedissonClient 
	 * @param: @return      
	 * @return: RedissonClient      
	 * @throws
	 */
	@Bean
	public RedissonClient getRedissonClient(){
		 Config config = new Config();
		 config.useSingleServer().setAddress("redis://" + host + ":" + port).setPassword(password);
		 return Redisson.create(config);
	}

	
	@Bean("myRedisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        // key的序列化类型
        redisTemplate.setKeySerializer(new RedisKeySerializer());
      //  redisTemplate.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
        //key haspmap序列化
        redisTemplate.setHashKeySerializer(new RedisKeySerializer());
        return redisTemplate;
    }
	
	/**
	 * redis2.0+ shiyong 使用RedisConnectionFactory 生成RedisCacheManager
	 */
	@Bean
	@Resource(name="myRedisTemplate")
	@Primary
    public RedisCacheManager myRedisCacheManager(RedisConnectionFactory redisConnectionFactory) {
//        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofSeconds(10)) //设置过期时间
//                .disableCachingNullValues()   //禁止缓存null对象
//               *//* .computePrefixWith(cacheName -> "yourAppName".concat(":").concat(cacheName).concat(":"))*//* //此处定义了cache key的前缀，避免公司不同项目之间的key名称冲突
//                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer())) //定义了key和value的序列化协议，同时hash key和hash value也被定义
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new Jackson2JsonRedisSerializer<>(Employee.class)));

        return RedisCacheManager.builder(redisConnectionFactory)
//                .cacheDefaults(cacheConfiguration)
                .build();
    }
	
	
	/**
	 * Redis2.0- 使用RedisTemplate 生成RedisCacheManager
	 */
//	@Bean
//	@Resource(name="cnbiRedisCacheManager")
//	@Primary
//	public RedisCacheManager cnbiRedisCacheManager(RedisTemplate<String, Object> redisTemplate) {
//		RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate);
//		/*
//		 * HashMap<String, Long> expires = new HashMap<>(1); expires.put("cacheSql",
//		 * 3600L); redisCacheManager.setExpires(expires);
//		 */
//		return redisCacheManager;
//	}
}
