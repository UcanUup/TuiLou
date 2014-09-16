package com.example.sqlite;

import android.content.Context;
import android.database.Cursor;

import com.example.utils.UserInfo;

public class UserDatabase {
	
	private Context mContext;
	
	private String sql;
	
	// ������䣬ÿ�ζ���ʹ��
	private String createSql = "create table " + DatabaseInfo.userTableName
			+ " (email varchar(20), name varchar(20))";
	
	public UserDatabase(Context mContext) {
		this.mContext = mContext;
	}
	
	// ��ȡ�������ݿ����Ϣ
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
	
	// �����ݷ��뱾�����ݿ�
	public void write() {
		DatabaseOperation db = new DatabaseOperation(mContext);
		sql = createSql;
		db.open(DatabaseInfo.databaseName, sql);
		
		sql = "delete from " + DatabaseInfo.userTableName;
		db.delete(sql);
		
		// �������ݿ���
		sql = "insert into " + DatabaseInfo.userTableName;
		sql += " " + "(email, name) values ";
		sql += "('" + UserInfo.email + "',";
		sql += " '" + UserInfo.userName + "')";
		db.insert(sql);
		
		db.close();
	}
	
	// ɾ�����ݿ�
	public void delete() {
		DatabaseOperation db = new DatabaseOperation(mContext);
		sql = createSql;
		db.open(DatabaseInfo.databaseName, sql);
		
		sql = "delete from " + DatabaseInfo.userTableName;
		db.delete(sql);
		
		db.close();
	}
	
}
