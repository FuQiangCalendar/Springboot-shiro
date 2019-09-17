package com.fuq.demo.tool.constants;

/**
 * 缓存key
 * @Company 中国经邦
 * @Author fuqCalendar
 * @Time 2019年9月16日16:06:53
 */
public final class CacheKeyConstants {

	/**
	 * SQL缓存Key
	 */
	public static final String SQLMAP = "sqlMap";


	/**
	 * EH缓存管理器的key
	 */
	public static final String DATAS_CACHEMANAGER = "ehgt";


	/**
	 * EH初始化数据的缓存
	 */
	public static final String INIT_CAHE_KEY = DATAS_CACHEMANAGER+"InitCache";

	/**
	 * EH查询数据的缓存
	 */
	public static final String QUERY_CAHE_KEY = DATAS_CACHEMANAGER+"SearchCache";


	/**
	 * REDIS缓存管理器的key  cacheManager  redisCacheManager
	 */
	public static final String REDIS_CACHEMANAGER = "cacheManager";


	////今天清理到这，余下的部分有时间再来清理！gjx-2018-08-13



	
	/**
	 * 公司集合缓存key
	 */
	public static final String COMPANY_KEY = "company";


	
	public static final String LICENSE_KEY = "license";


}
