package com.populace.berrycollege.managers;

import android.content.Context;

import com.populace.berrycollege.R;

import java.lang.reflect.Field;

public class PIResourceUtils {
	public static int getResId(String variableName, Context context, String className){
		return context.getResources().getIdentifier(variableName, className, context.getPackageName());
	}
	public static int getResId(String variableName, Context context, Class<?> c) {

		try {
			
			Field idField = c.getDeclaredField(variableName);
			return idField.getInt(idField);
			
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public static int getDrawableResId(String variableName, Context context) {
		int resId = PIResourceUtils
				.getResId(variableName, context, R.drawable.class);
		if(resId == -1){
			resId = PIResourceUtils.getResId(variableName, context, "drawable");
		}
		return resId;
	}

	public static int getAttrResId(String variableName, Context context) {
		
		int resId = PIResourceUtils.getResId(variableName, context, R.attr.class);
		if(resId == -1){
			resId = PIResourceUtils.getResId(variableName, context, "attr");
		}
		return resId;
	}

	public static int getLayoutResId(String variableName, Context context) {
		int resId = PIResourceUtils.getResId(variableName, context, R.layout.class);
		if(resId == -1){
			resId = PIResourceUtils.getResId(variableName, context, "layout");
		}
		return resId;
	}

	public static int getIdResId(String variableName, Context context) {
		int resId = PIResourceUtils.getResId(variableName, context, R.id.class);
		if(resId == -1){
			resId = PIResourceUtils.getResId(variableName, context, "id");
		}
		return resId;
	}

	public static int getMenuResId(String variableName, Context context) {
		int resId = PIResourceUtils.getResId(variableName, context, R.menu.class);
		if(resId == -1){
			resId = PIResourceUtils.getResId(variableName, context, "menu");
		}
		return resId;
	}

	public static int getStringResId(String variableName, Context context) {
		int resId = PIResourceUtils.getResId(variableName, context, R.string.class);
		if(resId == -1){
			resId = PIResourceUtils.getResId(variableName, context, "string");
		}
		return resId;
	}

	public static int getStyleResId(String variableName, Context context) {
		int resId = PIResourceUtils.getResId(variableName, context, R.style.class);
		if(resId == -1){
			resId = PIResourceUtils.getResId(variableName, context, "style");
		}
		return resId;
	}
	public static Field[] getDeclaredFields(Context context, String type){
		Class<?> c = null;
		try {
			
			c = Class.forName(context.getPackageName() + ".R$" + type);
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}
		return c.getDeclaredFields();
		
	}
}
