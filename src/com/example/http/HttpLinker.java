package com.example.http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class HttpLinker {
	public String link(HashMap<String, String> params, String page) {
		String url = HttpUrl.baseUrl + page;
		String httpUrl = url;
		String result = "";

		// ����ַurl����HttpPost���Ӷ���
		HttpPost httpRequest = new HttpPost(httpUrl);
	
		// ʹ��NameValuePair������Ҫ���ݵ�Post����
		List<NameValuePair> pa = new ArrayList<NameValuePair>();
	
		// Ҫ�������ﴫ�ݵĸ�������
		for (Map.Entry<String, String> entry : params.entrySet()) {
			pa.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
    	
    	try {
			//���ò�����
			HttpEntity httpEntity = new UrlEncodedFormEntity(pa, "utf-8");
			// httpRequest�������Ӻ���Ĳ���
			httpRequest.setEntity(httpEntity);
			
			// ȡ��Ĭ�ϵ�HttpClient
			HttpClient httpclient = new DefaultHttpClient();
			// ȡ��HttpResponse����������
			HttpResponse httpResponse = httpclient.execute(httpRequest);
			
			// �����������������HttpStatus.SC_OK��ʾ���ӳɹ�
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// ȡ�÷��ص��ַ���;
				result = EntityUtils.toString(httpResponse.getEntity());
			} 
    	} 
    	catch (Exception e) {
    		e.printStackTrace();
    	}
		
		return result;
	}
}
