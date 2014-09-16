package com.example.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Validation {
	
	// 输入的字符串是否是邮箱
	public static boolean isEmailValid(String email) {
		  boolean isValid = false;
		  String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		  CharSequence inputStr = email;
		  Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		  Matcher matcher = pattern.matcher(inputStr);
	
		  if (matcher.matches()) {
			  isValid = true;
		  }
		  
		  return isValid;
	}
	
	// 判断网络是否可用
	public static boolean isNetAvailable(Context context) {  
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);    
        NetworkInfo info = manager.getActiveNetworkInfo();  
        return (info != null && info.isAvailable());  
    }  
	
}
