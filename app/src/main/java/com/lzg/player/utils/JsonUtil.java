package com.lzg.player.utils;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.TypeReference;
import com.socks.library.KLog;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 *  This class is used as a Json utility. 
 *  The base functionality comes from the Gson 
 */
public final class JsonUtil {

	private JsonUtil() {}

	/** 
     * To Json Converter using Goolge's Gson Package 
 
     * this method converts a simple object to a json string 
 
     *  
     * @param obj 
     * @return a json string 
     */  
	public static <T> String toJson(final T obj) {
		return JSON.toJSONString(obj);
	}

	/** 
     * Converts a map of objects using Google's Gson Package 
     *  
     * @param map 
     * @return a json string 
     */  
	public static String toJson(final Map<String, ?> map) {
		return JSON.toJSONString(map);
	}
	
	/** 
     * Converts a collection of objects using Google's Gson Package 
     *  
     * @param list 
     * @return a json string array 
     */  
	public static <T> String toJson(final List<T> list) {
		return JSON.toJSONString(list);
	}
	
	/** 
     * Returns the specific object given the Json String 
     * @param <T> 
     * @param jsonString
     * @return a specific object as defined by the user calling the method
     */  
	public static <T> T fromJson(final String jsonString, final Class<T> classOfT) {
		try {
			return JSON.parseObject(jsonString, classOfT);
		} catch (Exception e) {
			KLog.e("json can not convert to " + classOfT.getName(), e);
			return null;
		}
	}
	
	/** 
     * Returns the specific object given the Json String 
     * @param <T> 
     * @param jsonString 
     * @return a specific object as defined by the user calling the method
     */  
	public static <T> T fromJson(final String jsonString, final Type type) {
		try {
			return JSON.parseObject(jsonString, type);
		} catch (Exception e) {
			KLog.e("json can not convert to " + type.getClass().getName(), e);
			return null;
		}
	}

	public static <T> T fromJson(final String jsonString, final TypeReference<T> type) {
		try {
			return JSON.parseObject(jsonString, type);
		} catch (Exception e) {
			KLog.e("json can not convert to " + type.getClass().getName(), e);
			return null;
		}
	}
	
	/** 
     * Returns a list of specified object from the given json array 
     * @param <T> 
     * @param jsonString 
     * @param type the type defined by the user 
     * @return a list of specified objects as given in the json array 
     */
	public static <T> List<T> fromJsonToList(final String jsonString, final Type type) {
		try {
			return JSON.parseObject(jsonString, type);
		} catch (Exception e) {
			KLog.e("json can not convert to " + type.getClass().getName(), e);
			return null;
		}
	}
	
	/** 
     * Returns a list of specified object from the given json reader
     * @param <T> 
     * @param json 
     * @param type the type defined by the user 
     * @return a list of specified objects as given in the json array 
     */
	public static <T> T fromJson(final JSONReader json, final Type type) {
		try {
			return json.readObject(type);
		} catch (Exception e) {
			KLog.e("json can not convert to " + type.getClass().getName(), e);
			return null;
		}
	}
	
	public static <T> T fromJsonByAssets(final Context context,
			final String jsonFile, final Type type) {
		
		return JsonUtil.fromJson(getJsonReaderFromAssets(context, jsonFile), type);
	}
	
	public static JSONReader getJsonReaderFromAssets(final Context context, final String jsonFile) {
		try {
			return new JSONReader(new InputStreamReader(
					context.getAssets().open(jsonFile), Charset.forName("UTF-8")));
		} catch (Exception e) {
//			Logger.e(e, null);
			return null;
		}
	}
}
