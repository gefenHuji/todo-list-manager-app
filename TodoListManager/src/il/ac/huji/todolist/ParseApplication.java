package il.ac.huji.todolist;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

import android.app.Application;

public class ParseApplication extends Application {
	
	@Override
	public void onCreate() {
		super.onCreate();
		Parse.initialize(this, "6at2ZUKo62cZcjuGbYmuLdxenjPD7dY8yymzFCJa", "BuVn3V3TEtVheoFaAVc7PMlrGtTHeIa4ULbQGHuj");
		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
		
		defaultACL.setPublicReadAccess(true);
		
		ParseACL.setDefaultACL(defaultACL, true);
	}
}
