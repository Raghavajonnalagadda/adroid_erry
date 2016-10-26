package com.populace.berrycollege.models;

import android.content.Context;
import android.os.Parcel;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GTCObject extends ParseObject implements Serializable{
	final static String ICO_FIELD_KEYVALS = "KeyVals";
	final static SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZ yyyy");
	SerializableHashMap<String,String> keyVals;
	
	public void clean(){
		remove("strClassName");
		remove("createdAtCopy");
		remove("updatedAtCopy");
		ArrayList<String> keys = new ArrayList<String>();
		for(String key: keySet()){
			if(key.contains("_serialized")){
				keys.add(key);
			}
		}
		for(String key:keys){
			remove(key);
		}
	}
	public SerializableHashMap<String,String> getKeyVals(){
		if(keyVals == null){
			HashMap<?,?> map =  (HashMap<?, ?>) getMap(ICO_FIELD_KEYVALS);
			if(map != null){
				keyVals = new SerializableHashMap<String,String>((HashMap<String,String>)(HashMap<?,?>)map);
			}else{
				keyVals = new SerializableHashMap<String,String>();
				this.remove(ICO_FIELD_KEYVALS);
				//put(ICO_FIELD_KEYVALS,keyVals.getJSONObject());
			}
		}
		return keyVals;
	}
	public void setKeyVals(SerializableHashMap<String,String> t){
		keyVals = t;
		//put(ICO_FIELD_KEYVALS,t);
	}
	public JSONObject fields(Context context, boolean skipFiles, int level){
		JSONObject obj = new JSONObject();
		
		try {
			obj.put("objectId", this.getObjectId());
			if(this.getCreatedAt() != null){
				obj.put("createdAtCopy", this.getCreatedAt().toString());
			}
			if(this.getUpdatedAt() != null){
			obj.put("updatedAtCopy", this.getUpdatedAt().toString());
			}
			obj.put("strClassName", this.getClassName());
			
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		for(int i = 0; i < this.keySet().size(); i++){
			try{
			String key = (String)this.keySet().toArray()[i];
			Object object = this.get(key);
			if(object instanceof ParseFile){
				if(!skipFiles){
                ParseFile  file = (ParseFile) object;
                	String[] tokens = file.getName().split("-");
                	String name = removeExtension(tokens[tokens.length - 1]);
                	
                 	
                	FileOutputStream out;
					try {
						obj.put(key + "_file", name);
						out = context.openFileOutput(name, Context.MODE_WORLD_READABLE);
						
						String fullPath = "/data/data/" + context.getPackageName() + "/files/"+name+".png";
						
						File file_check = new File(fullPath);
						if(file_check.exists())      
						{
							System.out.println("Andy data parse true");	
						}
						else
						{

							byte[] data = file.getData();
		                    out.write(data);
		                    out.close();
							System.out.println("Andy data parse false");	
						}
						
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ParseException e) {
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
                
            }else if(object instanceof ParseGeoPoint){
            	ParseGeoPoint  gp = (ParseGeoPoint) object;
            	try {
					obj.put(key, gp.getLatitude() + "," + gp.getLongitude());
				} catch (JSONException e) {
					
					e.printStackTrace();
				}

            }else if(object instanceof ParseObject){
            	if(level <= 0){
            	try {
					ParseObject obju = ((ParseObject)object).fetch();
					obj.put(key + "_serialized", GTCObject.fields(((ParseObject)obju),context, true, level + 1).toString());
				} catch (ParseException e) {
					
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}							
            	}
        		
            }else if(object instanceof HashMap){
            	try {
            		@SuppressWarnings("rawtypes")
					SerializableHashMap map = new SerializableHashMap((HashMap)object);
					obj.put(key, map.serialize());
				} catch (JSONException e) {
					
					e.printStackTrace();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
            }else if(object instanceof JSONObject){
            	try {
            		@SuppressWarnings("rawtypes")
					SerializableHashMap map = new SerializableHashMap(JsonHelper.toMap(this.getJSONObject(key)));
					obj.put(key, map.serialize());
				} catch (JSONException e) {
					
					e.printStackTrace();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
            }else{
            
            	try {
            		obj.put(key, JsonHelper.toJSON(object));
				} catch (JSONException e) {
					
					e.printStackTrace();
				}

            }
			}catch (Exception e) {
					
					e.printStackTrace();
			}
		}
		return obj;
	}
	public static JSONObject fields(ParseObject pobj, final Context context, boolean skipFiles, int level){
		final JSONObject obj = new JSONObject();
		
		try {
			obj.put("objectId", pobj.getObjectId());
			if(pobj.getCreatedAt() != null){
				pobj.put("createdAtCopy", pobj.getCreatedAt().toString());
			}
			if(pobj.getUpdatedAt() != null){
				obj.put("updatedAtCopy", pobj.getUpdatedAt().toString());
			}
			obj.put("strClassName", pobj.getClassName());
			
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		for(int i = 0; i < pobj.keySet().size(); i++){
			try{
			String key = (String)pobj.keySet().toArray()[i];
			final Object object = pobj.get(key);
			if(object instanceof ParseFile){
				if(!skipFiles){
                ParseFile  file = (ParseFile) object;
                	String[] tokens = file.getName().split("-");
                	String name = removeExtension(tokens[tokens.length - 1]);
                	
                	FileOutputStream out;
					try {
						obj.put(key + "_file", name);
						out = context.openFileOutput(name, Context.MODE_WORLD_READABLE);
						byte[] data = file.getData();
	                    out.write(data);
	                    out.close();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ParseException e) {
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
                
            }else if(object instanceof ParseGeoPoint){
            	ParseGeoPoint  gp = (ParseGeoPoint) object;
            	try {
					obj.put(key, gp.getLatitude() + "," + gp.getLongitude());
				} catch (JSONException e) {
					
					e.printStackTrace();
				}

            }else if(object instanceof ParseObject){
            	if(level <= 0){
            		try {
						ParseObject obju = ((ParseObject)object).fetch();
						obj.put(key + "_serialized", GTCObject.fields(((ParseObject)obju),context, true,level + 1).toString());
					} catch (ParseException e) {
						
						e.printStackTrace();
					} catch (JSONException e) {
						e.printStackTrace();
					}							
							
            	}
            		
				
            }else if(object instanceof HashMap){
            	try {
            		@SuppressWarnings("rawtypes")
					SerializableHashMap map = new SerializableHashMap((HashMap)object);
					obj.put(key, map.serialize());
				} catch (JSONException e) {
					
					e.printStackTrace();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
            }else if(object instanceof JSONObject){
            	try {
            		@SuppressWarnings("rawtypes")
					SerializableHashMap map = new SerializableHashMap(JsonHelper.toMap(pobj.getJSONObject(key)));
					obj.put(key, map.serialize());
				} catch (JSONException e) {
					
					e.printStackTrace();
				} catch (IOException e) {
					
					e.printStackTrace();
				}
            }else{
            
            	try {
            		obj.put(key, JsonHelper.toJSON(object));
				} catch (JSONException e) {
					
					e.printStackTrace();
				}

            }
			}catch(Exception e){
				
			}
		}
		return obj;
	}
	
	public void load(JSONObject fields){
		this.setKeyVals(new SerializableHashMap<String,String>());
		Iterator it = fields.keys();
        while(it.hasNext()){
        	try{
        		String key = (String) it.next();
        		Object obj;
    			obj = fields.get(key);
    			put(key,obj);
    			if(key.compareTo("objectId") == 0){
    				this.setObjectId((String) obj);
    			}
    			if(key.compareTo("createdAtCopy") == 0){
    				this.put("createdAt", df.parse((String)obj));
    			}
    			if(key.compareTo("updatedAtCopy") == 0){
    				this.put("updatedAt", df.parse((String)obj));
    			}
    			if(key.compareTo("keyVals") == 0){
    				this.keyVals = new SerializableHashMap<String,String>((String)obj);
    			}
    					
        	}catch(Exception ex){
        		ex.printStackTrace();
        	}
        }
	}
	public static void load(ParseObject pobj, JSONObject fields){
		
		Iterator it = fields.keys();
        while(it.hasNext()){
        	try{
        		String key = (String) it.next();
        		Object obj;
    			obj = fields.get(key);
    			pobj.put(key,obj);
    			if(key.compareTo("objectId") == 0){
    				pobj.setObjectId((String) obj);
    			}
    			if(key.compareTo("createdAtCopy") == 0){
    				pobj.put("createdAt", df.parse((String)obj));
    			}
    			if(key.compareTo("updatedAtCopy") == 0){
    				pobj.put("updatedAt", df.parse((String)obj));
    			}
    			
    					
        	}catch(Exception ex){
        		ex.printStackTrace();
        	}
        }
	}
	public GTCObject() {
		super();
	}
	public GTCObject(String theClassName) {
		super(theClassName);
		this.setKeyVals(new SerializableHashMap<String,String>());
	}
	public GTCObject(SerializableHashMap<String,String> dict){
		Iterator it = dict.keySet().iterator();
		while(it.hasNext()){
			String key = (String)it.next();
			Object obj;
			obj = dict.get(key);
			put(key,obj);
		}
		setKeyVals(dict);
	}
	public GTCObject(JSONObject fields){
		
        this.setKeyVals(new SerializableHashMap<String,String>());
        while(fields.keys().hasNext()){
        	try{
        		String key = (String) fields.keys().next();
        		Object obj;
    			obj = this.get(key);
    			put(key,obj);
    			this.keyVals.put(key, (String) obj);
        	}catch(Exception ex){
        		ex.printStackTrace();
        	}
        }
	}
	public GTCObject(ArrayList<HashMap<String,Object>> fields){
		
        this.setKeyVals(new SerializableHashMap<String,String>());
        for(int i  =0 ; i < fields.size();i++){
        	try{
        	HashMap<String,Object> field =fields.get(i); 
        	this.put((String)field.get(GTCConstants.GTCFieldName), (String)field.get(GTCConstants.GTCFieldValue));
        	SerializableHashMap<String,String> h = (SerializableHashMap<String, String>) this.getKeyVals();
        	h.put((String)field.get(GTCConstants.GTCFieldName), (String)field.get(GTCConstants.GTCFieldValue));
        	this.setKeyVals(h);
        	}catch(Exception ex){
        		ex.printStackTrace();
        	}
        }
	}
	public GTCObject(Parcel in) {

		Iterator it = this.keySet().iterator();
		while(it.hasNext()){
			String key = (String)it.next();
			Object obj;
			try {
				obj = this.get(key);
				if(obj.getClass().equals(String.class)){
					put(key,in.readString());
				}else if (obj.getClass().equals(Integer.class)){
					put(key,in.readInt());
				}else if (obj.getClass().equals(Double.class)){
					put(key,in.readDouble());
				}else if (obj.getClass().equals(JSONArray.class)){
					put(key,in.readArray(GTCObject.class.getClassLoader()));
				}else if (obj.getClass().equals(JSONObject.class)){
					put(key,in.readHashMap(GTCObject.class.getClassLoader()));
				}else if (obj.getClass().equals(ArrayList.class)){
					put(key,in.readArrayList(GTCObject.class.getClassLoader()));
				}
			}catch(Exception e){
				
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public Object get(String key){
		return getProxy(key);
	}
	
	@Override
	public String getString(String key){
		
		String str = getProxy(key);
		if(str == null){
			return "";
		}
		return str;
	}
	
	@Override
	public Date getDate(String key){
		Object obj = getProxy(key);
		if(obj != null){
		if(obj.getClass().equals(String.class)){
			try {
				return df.parse((String)obj);
			} catch (java.text.ParseException e) {
				
				return null;
			}
		}else{
			return (Date)obj;
		}
		}else{
			return null;
		}
		
		
	}
	
	@Override
	public boolean getBoolean(String key){
		boolean obj = false;
		try{
		if(this.containsKey(key)){
			obj = super.getBoolean(key);
		}else if(this.containsKey(key.toLowerCase())){
			obj =super.getBoolean(key.toLowerCase());
		}
		}catch(Exception e){
			
		}
		return obj;
	}
	@Override
	public byte[] getBytes(String key){
		return getProxy(key);
	}
	@Override
	public double getDouble(String key){
		double obj = 0.0;
		try{
		if(this.containsKey(key)){
			obj = super.getDouble(key);
		}else if(this.containsKey(key.toLowerCase())){
			obj =super.getDouble(key.toLowerCase());
		}
		}catch(Exception e){
			
		}
		return obj;
	}
	@Override
	public int getInt(String key){
		int obj = 0;
		try{
		if(this.containsKey(key)){
			obj = super.getInt(key);
		}else if(this.containsKey(key.toLowerCase())){
			obj =super.getInt(key.toLowerCase());
		}
		}catch(Exception e){
			
		}
		return obj;
	}
	@Override
	public JSONArray getJSONArray(String key){
		return getProxy(key);
	}
	@Override
	public JSONObject getJSONObject(String key){
		return getProxy(key);
	}
	@Override
	public List<?> getList(String key){
		Object obj = getProxy(key);
		ArrayList<Object> listdata = null;
		if(obj != null){
		if(obj.getClass().equals(JSONArray.class)){
			listdata = new ArrayList<Object>();     
			JSONArray jArray = (JSONArray)obj; 
			if (jArray != null) { 
			   for (int i=0;i<jArray.length();i++){ 
			    try {
					listdata.add(jArray.get(i));
				} catch (JSONException e) {
					
					e.printStackTrace();
				}
			   } 
			}
		}else if(obj.getClass().equals(List.class)){
			listdata = (ArrayList<Object>) obj;
		}}else{
			listdata = new ArrayList<Object>();
		}
		return listdata;
	}
	@Override
	public long getLong(String key){
		long obj = 0;
		try{
		if(this.containsKey(key)){
			obj = super.getLong(key);
		}else if(this.containsKey(key.toLowerCase())){
			obj =super.getLong(key.toLowerCase());
		}
		}catch(Exception e){
			
		}
		return obj;
	}
	@Override
	public Map<?,?> getMap(String key){
		return getProxy(key);
	}
	@Override
	public Number getNumber(String key){
		return getProxy(key);
	}
	@Override
	public ParseFile getParseFile(String key){
		return getProxy(key);
	}
	public static ParseObject deserializeObject(String json){
		JSONObject jobj;
		try {
			jobj = new JSONObject(json);
			ParseObject obj = ParseObject.create(jobj.getString("strClassName"));
	    	GTCObject.load(obj, jobj);
	    	return obj;
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	return null;
	}
	
	@Override
	public ParseObject getParseObject(String key){
		ParseObject obj = getProxy(key);
		if(obj == null){
			try {
				obj = GTCObject.deserializeObject(getString(key +"_serialized"));
				
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch(Exception e){
				e.printStackTrace();
			}
		}
		return obj;
	}
	@Override
	public ParseGeoPoint getParseGeoPoint(String key){
		return getProxy(key);
	}
	@Override
	public ParseUser getParseUser(String key){
		return getProxy(key);
	}
	@Override
	public ParseRelation getRelation(String key){
		return getProxy(key);
	}
	
	public <T> T getProxy(String key){
		T obj = null;
		try{
		if(this.containsKey(key)){
			obj = (T)super.get(key);
		}else if(this.containsKey(key.toLowerCase())){
			obj = (T)super.get(key.toLowerCase());
		}else if(this.containsKey(key.toUpperCase())){
			obj = (T)super.get(key.toUpperCase());
		}else if(this.containsKey(key.toUpperCase(Locale.getDefault()))){
			obj = (T)super.get(key.toUpperCase(Locale.getDefault()));
		}else if(this.containsKey(key.toLowerCase(Locale.getDefault()))){
			obj = (T)super.get(key.toLowerCase(Locale.getDefault()));
		}else{
			for(String k:super.keySet()){
				if(k.charAt(1) == '_'){
					String ok = k.substring(2);
					if(ok.compareToIgnoreCase(key) == 0){
						obj = (T)super.get(k);
						break;
					}
				}
			}
		}
		
		}catch(Exception e){
			
		}
		return obj;
	}
	@Override
	public void put(String key, Object val){
		if(key.charAt(1) == '_'){
			key = key.substring(2);
		}
		if(val != null){
			super.put(key, val);
			try{
				if(val.getClass().equals(Date.class)){
					getKeyVals().put(key, df.format((Date)val));
				}else{
					getKeyVals().put(key, (String)val);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	public static String removeExtension(String name) {
	    // These first few lines the same as Justin's
	    
	    	    // Now we know it's a file - don't need to do any special hidden
	    // checking or contains() checking because of:
	    final int lastPeriodPos = name.lastIndexOf('.');
	    if (lastPeriodPos <= 0)
	    {
	        // No period after first character - return name as it was passed in
	        return name;
	    }
	    else
	    {
	        // Remove the last period and everything after it
	        return name.substring(0, lastPeriodPos);
	    }
	}
}
