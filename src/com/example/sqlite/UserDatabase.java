package com.example.sqlite;

import android.content.Context;
import android.database.Cursor;

import com.example.utils.UserInfo;

public class UserDatabase {
	
	private Context mContext;
	
	private String sql;
	
	// 建表语句，每次都会使用
	private String createSql = "create table " + DatabaseInfo.userTableName
			+ " (email varchar(20), name varchar(20))";
	
	public UserDatabase(Context mContext) {
		this.mContext = mContext;
	}
	
	// 读取本地数据库的信息
	public void read() {
		DatabaseOperation db = new DatabaseOperation(mContext);
		sql = createSql;
		db.open(DatabaseInfo.databaseName, sql);
		
		sql = "select * from " + DatabaseInfo.userTableName;
		Cursor cursor = db.find(sql);
		
		while (cursor.moveToNext()) {
			UserInfo.email = cursor.getString(cursor.getColumnIndex("email"));
			UserInfo.userName = cursor.getString(cursor.getColumnIndex("name"));
		}
		
		db.close();
	}
	
	// 将数据放入本地数据库
	public void write() {
		DatabaseOperation db = new DatabaseOperation(mContext);
		sql = createSql;
		db.open(DatabaseInfo.databaseName, sql);
		
		sql = "delete from " + DatabaseInfo.userTableName;
		db.delete(sql);
		
		// 插入数据库中
		sql = "insert into " + DatabaseInfo.userTableName;
		sql += " " + "(email, name) values ";
		sql += "('" + UserInfo.email + "',";
		sql += " '" + UserInfo.userName + "')";
		db.insert(sql);
		
		db.close();
	}
	
	// 删除数据库
	public void delete() {
		DatabaseOperation db = new DatabaseOperation(mContext);
		sql = createSql;
		db.open(DatabaseInfo.databaseName, sql);
		
		sql = "delete from " + DatabaseInfo.userTableName;
		db.delete(sql);
		
		db.close();
	}
	
}
