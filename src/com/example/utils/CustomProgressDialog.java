package com.example.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import com.example.R;

public class CustomProgressDialog extends ProgressDialog {  
    public CustomProgressDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.progress_dialog);  
    }  
}  