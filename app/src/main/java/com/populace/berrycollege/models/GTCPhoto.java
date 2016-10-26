package com.populace.berrycollege.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
@ParseClassName("Photo")
public class GTCPhoto extends GTCObject {
	final static String ICO_FIELD_IMAGE_LIKES = "imagelikes";
	final static String ICO_FIELD_COMMENTS_COUNT = "commentCount";
	final static String ICO_FIELD_IMAGE= "image";
	final static String ICO_FIELD_USER = "user";
	final static String ICO_FIELD_KEYVALS = "KeyVals";
	int id;
	final static String ICO_FIELD_USER_NAME  ="user_name";
	
	 final static String ICO_FIELD_PROFILE_PIC= "profile_pic";
	 
	 int temp_likes=1000;
	 int temp_comments=1000;
	 
		public int getTemp_likes() {
		return temp_likes;
	}
	public void setTemp_likes(int temp_likes) {
		this.temp_likes = temp_likes;
	}
	public int getTemp_comments() {
		return temp_comments;
	}
	public void setTemp_comments(int temp_comments) {
		this.temp_comments = temp_comments;
	}
		public ParseFile getProfile_pic() {
			return this.getParseFile(ICO_FIELD_PROFILE_PIC);
		}
		public void setProfile_pic(ParseFile t) {
			put(ICO_FIELD_PROFILE_PIC,t);
		}
	public String getUser_name() {
		return getString(ICO_FIELD_USER_NAME);
	}
	public void setUser_name(String user_name) {
		put(ICO_FIELD_USER_NAME,user_name);
	}
	public Integer getImageLikes(){
		return getInt(ICO_FIELD_IMAGE_LIKES);
	}
	public void setImageLikes(Integer n){
		put(ICO_FIELD_IMAGE_LIKES,n);
	}
	public Integer getCommentsCount(){
		return getInt(ICO_FIELD_COMMENTS_COUNT);
	}
	public void setCommentsCount(Integer i){
		put(ICO_FIELD_COMMENTS_COUNT,i);
	}
	
	public ParseFile getImage(){
		return this.getParseFile(ICO_FIELD_IMAGE);
	}
	public void setImage(ParseFile t){
		put(ICO_FIELD_IMAGE,t);
	}
	
	public ParseUser getUser(){
		return (ParseUser) this.getParseObject(ICO_FIELD_USER);
	}
	public void setUser(ParseUser t){
		put(ICO_FIELD_USER,t);
	}
	
	public GTCPhoto(){
		super();
		//this.setSpeakers(new ArrayList<String>());
	}
	public GTCPhoto(SerializableHashMap<String,String> dict){
		super(dict);
		
	}
	public GTCPhoto(ArrayList<HashMap<String,Object>> fields){
		super(fields);
        
       
        
	}
	
	
	
	
}
