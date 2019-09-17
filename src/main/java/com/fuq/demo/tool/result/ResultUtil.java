package com.fuq.demo.tool.result;

import java.io.IOException;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.fuq.demo.tool.string.StringUtil;

/**
 * Result 工具类
 * @author FuqCanlendar
 * @date 2019年9月4日17:37:31
 */
public class ResultUtil {
	
	/**
	 * @Title: transformMapKeyToLower   
	 * @Description: (将map的key统一转换为小写) 
	 * @author: FuqCalendar   
	 * @param: @param map
	 * @param: @return      
	 * @return: Map<String,Object>      
	 * @throws
	 */
	public static Map<String,Object> transformMapKeyToLower(Map<String,Object> map){
		Map<String, Object> resultMap = new HashMap<>();
		if(map == null || map.isEmpty()){
			return resultMap;
		}
		
		Set<Entry<String, Object>> entrySet = map.entrySet();
		for(Entry<String, Object> entry : entrySet){
			String newKey = entry.getKey().toLowerCase();
			if (entry.getValue() instanceof List) {
				List<Map<String, Object>> newList = (List<Map<String, Object>>) entry.getValue();
				entry.setValue(transformMapKeyListToLower(newList));
			}else if (entry.getValue() instanceof Clob) {
				try {
					entry.setValue(StringUtil.getCloToString((Clob) entry.getValue()));
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			resultMap.put(newKey, entry.getValue());
		}		
		return resultMap;
	}
	
	/**
	 * @Title: transformMapKeyListToLower   
	 * @Description: (将集合中的map的key统一转换为小写) 
	 * @author: FuqCalendar   
	 * @param: @param list
	 * @param: @return      
	 * @return: List<Map<String,Object>>      
	 * @throws
	 */
	public static List<Map<String,Object>> transformMapKeyListToLower(List<Map<String,Object>> list){
		List<Map<String,Object>> resultList = new ArrayList<>();
		if(list == null || list.isEmpty()){
			return resultList;
		}
		for(Map<String,Object> map : list){
			resultList.add(transformMapKeyToLower(map));
		}
		return resultList;
	}
	
	/**
	 * @Title: transformMapKeyToLower   
	 * @Description: TODO(将map的key统一转换为小写) 
	 * @author: FuqCalendar   
	 * @param: @param map
	 * @param: @return      
	 * @return: Map<String,Object>      
	 * @throws
	 */
	public static Map<String,String> transformMapKeyToLowerValueString (Map<String,String> map){
		Map<String, String> resultMap = new HashMap<>();
		if(map == null || map.isEmpty()){
			return resultMap;
		}
		Set<String> keySet = map.keySet();
		for(String key : keySet){
			String newKey = key.toLowerCase();
			resultMap.put(newKey, map.get(key));
		}		
		return resultMap;
	}
	
	/**
	 * @Title: transformMapKeyListToLower   
	 * @Description: TODO(将集合中的map的key统一转换为小写) 
	 * @author: FuqCalendar   
	 * @param: @param list
	 * @param: @return      
	 * @return: List<Map<String,Object>>      
	 * @throws
	 */
	public static List<Map<String,String>> transformMapKeyListToLowerValueString (List<Map<String,String>> list){
		List<Map<String,String>> resultList = new ArrayList<>();
		if(list == null || list.isEmpty()){
			return resultList;
		}
		for(Map<String,String> map : list){
			resultList.add(transformMapKeyToLowerValueString (map));
		}
		return resultList;
	}
}
