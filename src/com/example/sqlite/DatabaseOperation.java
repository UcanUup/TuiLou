package com.example.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseOperation {
	
	private Context mContext;
	
	private DatabaseHelper databaseHelper;
	private SQLiteDatabase db;
	
	public DatabaseOperation(Context mContext) {
		this.mContext = mContext;
	}
	
	// �����ݿ�
	public void open(String dbName, String createSql) {
		databaseHelper = new DatabaseHelper(mContext, dbName, createSql);
	}
	
	// �ر����ݿ�
	public void close() {
		if (db != null)
			db.close();
		
		if (databaseHelper != null)
			databaseHelper.close();
	}
	
	// ��������
	public void insert(String sql) {
		db = databaseHelper.getWritableDatabase();
		db.execSQL(sql);
	}
	
	// ��������
	public void update(String sql) {
		db = databaseHelper.getWritableDatabase();
		db.execSQL(sql);
	}
	
	// ɾ������
	public void delete(String sql) {
		db = databaseHelper.getWritableDatabase();
		db.execSQL(sql);
	}
	
	// ��������
	public Cursor find(String sql) {
		db = databaseHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery(sql, null);
		
		return cursor;
	}
	
}
