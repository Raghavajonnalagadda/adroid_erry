package com.populace.berrycollege.managers;



import com.parse.ParseException;
import com.parse.ParseUser;

public interface IRegisterCallback {
	public void onRegister(boolean success, ParseException e, ParseUser user);
}
