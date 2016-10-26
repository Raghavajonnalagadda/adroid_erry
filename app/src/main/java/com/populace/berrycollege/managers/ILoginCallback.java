package com.populace.berrycollege.managers;

import com.parse.ParseException;
import com.parse.ParseUser;

public interface ILoginCallback {
	public void onLogin(boolean success, ParseException e, ParseUser user);
}
