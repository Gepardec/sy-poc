package com.gepardec.sy_poc.tools;

import java.lang.reflect.Field;

public class ObjectXPath {
	public static Object get(Object object, String xpath){
		if(xpath == null || object == null){
			return null;
		} else {
			String[] fields = xpath.split("\\/");
			Object result = object;
			for(String field : fields){
				try {
					Field f = result.getClass().getDeclaredField(field);
					f.setAccessible(true);
					result = f.get(result);
				} catch (NoSuchFieldException e) {
					System.err.println("No field found " + field + " in object " + result);
					return null;
				} catch (SecurityException e) {
					System.err.println("Security exception!");
					e.printStackTrace();
					return null;
				} catch (IllegalArgumentException e) {
					System.err.println("IllegalArgument!");
					e.printStackTrace();
					return null;
				} catch (IllegalAccessException e) {
					System.err.println("IllegalAccess!");
					e.printStackTrace();
					return null;
				}
			}
			return result;
		}
	}
}
