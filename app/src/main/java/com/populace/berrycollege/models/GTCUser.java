package com.populace.berrycollege.models;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
@ParseClassName("User_Profile")
public class GTCUser extends GTCObject {
	final static String ICO_FIELD_TITLE = "title";
	final static String ICO_FIELD_FIRST_NAME = "FirstName";
	final static String ICO_FIELD_LAST_NAME = "LastName";
	final static String ICO_FIELD_FACEBOOK = "facebook_url";
	final static String ICO_FIELD_TWITTER= "twitter_url";
	final static String ICO_FIELD_LINKEDIN= "linkedin_url";
	final static String ICO_FIELD_ORGANIZATION = "organization";
	final static String ICO_FIELD_PHONE = "phone";
	final static String ICO_FIELD_STUDY_YEAR = "study_year";
	final static String ICO_FIELD_IMAGE= "image";
	final static String ICO_FIELD_THUMBNAIL = "thumbnail";
	final static String ICO_FIELD_USER = "user";
	final static String ICO_FIELD_USERNAME = "username";



	public static String getUsername() {
		return ICO_FIELD_USERNAME;
	}
	public void setUsername(String n){
		put(ICO_FIELD_USERNAME,n);
	}
	final static String ICO_FIELD_USER_EMAIL = "user_email";
	final static String ICO_FIELD_KEYVALS = "KeyVals";
	int id;
	Date start,end;
	public String getTitle(){
		return getString(ICO_FIELD_TITLE);
	}
	public void setTitle(String n){
		put(ICO_FIELD_TITLE,n);
	}
	public String getFirstName(){
		return getString(ICO_FIELD_FIRST_NAME);
	}
	public void setFirstName(String t){
		put(ICO_FIELD_FIRST_NAME,t);
	}
	public String getLastName(){
		return getString(ICO_FIELD_LAST_NAME);
	}
	public void setLastName(String d){
		put(ICO_FIELD_LAST_NAME,d);
	}
	public String getFacebook(){
		return getString(ICO_FIELD_FACEBOOK);
	}
	public void setFacebook(String t){
		put(ICO_FIELD_FACEBOOK,t);
	}
	public String getTwitter(){
		return getString(ICO_FIELD_TWITTER);
	}
	public void setTwitter(String d){
		put(ICO_FIELD_TWITTER,d);
	}
	public String getLinkedin(){
		return getString(ICO_FIELD_LINKEDIN);
	}
	public void setLinkedin(String d){
		put(ICO_FIELD_LINKEDIN,d);
	}
	public String getPhone(){
		return getString(ICO_FIELD_PHONE);
	}
	public void setPhone(String d){
		put(ICO_FIELD_PHONE,d);
	}
	public ParseFile getImage(){
		return this.getParseFile(ICO_FIELD_IMAGE);
	}
	public void setImage(ParseFile t){
		put(ICO_FIELD_IMAGE,t);
	}
	public ParseFile getThumbnail(){
		return this.getParseFile(ICO_FIELD_THUMBNAIL);
	}
	public void setThumbnail(ParseFile t){
		put(ICO_FIELD_THUMBNAIL,t);
	}
	public ParseUser getUser(){
		return this.getParseUser(ICO_FIELD_USER);
	}
	public void setUser(ParseUser t){
		put(ICO_FIELD_USER,t);
	}
	public String getOrganization(){
		return getString(ICO_FIELD_ORGANIZATION);
	}
	public void setOrganization(String t){
		put(ICO_FIELD_ORGANIZATION,t);
	}
	public String getUserEmail(){
		return getString(ICO_FIELD_USER_EMAIL);
	}
	public void setUserEmail(String t){
		put(ICO_FIELD_USER_EMAIL,t);
	}
	public String getStudyYear(){
		return getString(ICO_FIELD_STUDY_YEAR);
	}
	public void setStudyYear(String t){
		put(ICO_FIELD_STUDY_YEAR,t);
	}


	public GTCUser(){
		super();
		//this.setSpeakers(new ArrayList<String>());
	}
	public GTCUser(SerializableHashMap<String,String> dict){
		super(dict);

	}
	public GTCUser(ArrayList<HashMap<String,Object>> fields){
		super(fields);



	}




}
