package com.fuq.demo.tool.redis;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.Assert;

import java.nio.charset.Charset;


/**
 * 序列化接口
 * @author fuqCalendar
 *
 */
public class RedisKeySerializer implements RedisSerializer<String> {

	private final Charset charset;

	public RedisKeySerializer() {
		this(Charset.forName("UTF8"));
	}

	public RedisKeySerializer(Charset charset) {
		Assert.notNull(charset, "Charset must not be null!");
		this.charset = charset;
	}

	@Override
	public String deserialize(byte[] bytes) {
		String key =  (bytes == null ? null : new String(bytes, charset));
		return key;
	}

	@Override
	public byte[] serialize(String string) {
		byte[] bytes = ( string == null ? null : string.getBytes(charset));
		return bytes;
	}
}
