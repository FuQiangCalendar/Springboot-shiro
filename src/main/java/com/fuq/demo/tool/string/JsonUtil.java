package com.fuq.demo.tool.string;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonParser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * json工具类
 * 
 * @author FuqCalendar
 * @date 2019年9月5日14:04:11
 */
public class JsonUtil implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	final static Log logger = LogFactory.getLog(JsonUtil.class);

	public static final ObjectMapper objectMapper = new ObjectMapper();

	public static Map<?, ?> json2Map(String json) {
		try {
			Map<?, ?> map = objectMapper.readValue(json, Map.class);
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("JSON【" + json + "】转Map出错了：" + e);
		}
		return null;
	}

	/**
	 * 生成JSON
	 * 
	 * @param object
	 */
	public static String generateJson(Object object) {
		try {
			if (null == object)
				return "null";
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			logger.error("JsonProcessingException \n" + e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("IOException \n" + e);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 生成JSON
	 * 
	 * @param object
	 */
	public static String generateJson2(Object object) {
		String json = (String) JSON.toJSON(object);
		return json;
	}

	public static Object conventString2Bean(String json, Class<?> classs) {
		try {
			objectMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
			return objectMapper.readValue(json, classs);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("json转对象出错了\n" + e);
		}
		return null;
	}

	/**
	 * 读取本地文件中JSON字
	 * 
	 * @param json
	 * @param classs
	 * @return
	 * @throws Exception
	 */
	public static Object conventString2BeanByJackson(String json, Class<?> classs) throws Exception {
		objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		return objectMapper.readValue(json, classs);
	}

	public static Object conventString2BeanByFastJson(String json, Class<?> classs) {
		try {
			return JSON.parseObject(json, classs);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("fastJson转对象出错了\n" + e);
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public static List conventString2ListByFastJson(String json, Class<?> classs) {
		List list = new ArrayList();
		try {
			list = JSON.parseArray(json, classs);
		} catch (Exception e) {
			logger.error("fastJson转list出错了\n" + e);
		}
		return list;
	}

	public static String object2json(Object obj) {
		StringBuilder json = new StringBuilder();
		if (obj == null) {
			json.append("\"\"");
		} else if (obj instanceof String || obj instanceof Integer || obj instanceof Float || obj instanceof Boolean
				|| obj instanceof Short || obj instanceof Double || obj instanceof Long || obj instanceof BigDecimal
				|| obj instanceof BigInteger || obj instanceof Byte) {
			json.append("\"").append(string2json(obj.toString())).append("\"");
		} else if (obj instanceof Object[]) {
			json.append(array2json((Object[]) obj));
		} else if (obj instanceof List) {
			json.append(list2json((List<?>) obj));
		} else if (obj instanceof Map) {
			json.append(map2json((Map<?, ?>) obj));
		} else if (obj instanceof Set) {
			json.append(set2json((Set<?>) obj));
		} else if (obj instanceof Character) {
		} else {
			json.append(bean2json(obj));
		}

		return StringUtil.replaceBlank(json.toString());
	}

	public static String bean2json(Object bean) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		PropertyDescriptor[] props = null;
		try {
			props = Introspector.getBeanInfo(bean.getClass(), Object.class).getPropertyDescriptors();
		} catch (IntrospectionException e) {
		}
		if (props != null) {
			for (int i = 0; i < props.length; i++) {
				try {
					String name = object2json(props[i].getName());
					String value = object2json(props[i].getReadMethod().invoke(bean));
					// System.out.println(name+"-------"+value+"----"+value.length());
					if (name.equals("\"attributes\"") && value.contains("attr")) {
						value = value.replace("\\", "");
						// System.out.println(name+"----attributes---gjx----"+value+"----"+value.length());
					}
					if (!value.equals("\"\"") && null != value) {
						json.append(name);
						json.append(":");
						json.append(value);
						json.append(",");
						// System.out.println(name+"-------"+value+"----"+value.length());
					} else {
						// System.out.println(name+"-----------------------------------------");
					}
				} catch (Exception e) {
				}
			}
			json.setCharAt(json.length() - 1, '}');
		} else {
			json.append("}");
		}
		return StringUtil.replaceBlank(json.toString());
	}

	public static String list2json(List<?> list) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (list != null && list.size() > 0) {
			for (Object obj : list) {
				json.append(object2json(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return json.toString();
	}

	public static String array2json(Object[] array) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (array != null && array.length > 0) {
			for (Object obj : array) {
				json.append(object2json(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return StringUtil.replaceBlank(json.toString());
	}

	public static String map2json(Map<?, ?> map) {
		StringBuilder json = new StringBuilder();
		json.append("{");
		if (map != null && map.size() > 0) {
			for (Object key : map.keySet()) {
				json.append(object2json(key));
				json.append(":");
				json.append(object2json(map.get(key)));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, '}');
		} else {
			json.append("}");
		}
		return StringUtil.replaceBlank(json.toString());
	}

	public static String set2json(Set<?> set) {
		StringBuilder json = new StringBuilder();
		json.append("[");
		if (set != null && set.size() > 0) {
			for (Object obj : set) {
				json.append(object2json(obj));
				json.append(",");
			}
			json.setCharAt(json.length() - 1, ']');
		} else {
			json.append("]");
		}
		return StringUtil.replaceBlank(json.toString());
	}

	public static String string2json(String s) {
		if (s == null)
			return "";
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char ch = s.charAt(i);
			switch (ch) {
			case '"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			/*
			 * case '/': sb.append("\\/"); break;
			 */
			default:
				if (ch >= '\u0000' && ch <= '\u001F') {
					String ss = Integer.toHexString(ch);
					sb.append("\\u");
					for (int k = 0; k < 4 - ss.length(); k++) {
						sb.append('0');
					}
					sb.append(ss.toUpperCase());
				} else {
					sb.append(ch);
				}
			}
		}
		return StringUtil.replaceBlank(sb.toString());
	}
	/**
	 * 读取本地文件中JSON字符�?
	 * 
	 * @param fileName
	 * @return
	 *//*
		 * private String getJson(String fileName) { //AssetManager assetManager =
		 * getAssets() Context.getAssets().open(filename); StringBuilder stringBuilder =
		 * new StringBuilder(); try { BufferedReader bf = new BufferedReader(new
		 * InputStreamReader(getAssets().open(fileName))); String line; while ((line =
		 * bf.readLine()) != null) { stringBuilder.append(line); } } catch (IOException
		 * e) { e.printStackTrace(); } return stringBuilder.toString(); }
		 */
}
