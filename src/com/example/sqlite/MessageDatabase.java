package com.example.sqlite;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.example.utils.MyMessage;
import com.example.utils.UserInfo;

public class MessageDatabase {
	private Context mContext;
	
	private String sql;
	private String createSql = "create table " + DatabaseInfo.messageTableName 
			+ " (gname vchar(20), title varchar(50), content varchar(200), time varchar(20))";
	
	public MessageDatabase(Context mContext) {
		this.mContext = mContext;
	}
	
	public void read() {
		//读取本地数据库的信息
		DatabaseOperation db = new DatabaseOperation(mContext);
		sql = createSql;
		db.open(DatabaseInfo.databaseName, sql);
		
		sql = "select * from " + DatabaseInfo.messageTableName;
		Cursor cursor = db.find(sql);
		
		List<MyMessage> messages = new ArrayList<MyMessage>();
		MyMessage message;
		
		while (cursor.moveToNext()) {
			message = new MyMessage();
			
			message.setGname(cursor.getString(cursor.getColumnIndex("gname")));
			message.setTitle(cursor.getString(cursor.getColumnIndex("title")));
			message.setContent(cursor.getString(cursor.getColumnIndex("content")));
			message.setTime(cursor.getString(cursor.getColumnIndex("time")));
			
			messages.add(message);
		}
		
		UserInfo.myMessage = messages;
		db.close();
	}
	
	public void write() {
		//将网络数据放入本地数据库
		DatabaseOperation db = new DatabaseOperation(mContext);
		sql = createSql;
		db.open(DatabaseInfo.databaseName, sql);
		
		sql = "delete from " + DatabaseInfo.messageTableName;
		db.delete(sql);
		
		for (Iterator<MyMessage> iterator = UserInfo.myMessage.iterator(); iterator.hasNext();) {
			MyMessage myMessage = (MyMessage) iterator.next();
			sql = "insert into " + DatabaseInfo.messageTableName 
					+ " (gname, title, content, time) values ";
			sql += "('" + myMessage.getGname() + "', ";
			sql += "'" + myMessage.getTitle() + "', ";
			sql += "'" + myMessage.getContent() + "', ";
			sql += "'" + myMessage.getTime() + "')";
			
			db.insert(sql);
		}
		
		db.close();
	}
}
