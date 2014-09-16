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

public class HttpLinker {
	public String link(HashMap<String, String> params, String page) {
		String url = HttpUrl.baseUrl + page;
		String httpUrl = url;
		String result = "";

		// 用网址url生成HttpPost连接对象
		HttpPost httpRequest = new HttpPost(httpUrl);
	
		// 使用NameValuePair来保存要传递的Post参数
		List<NameValuePair> pa = new ArrayList<NameValuePair>();
	
		// 要往链接里传递的各个参数
		for (Map.Entry<String, String> entry : params.entrySet()) {
			pa.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
    	
    	try {
			//设置参数集
			HttpEntity httpEntity = new UrlEncodedFormEntity(pa, "utf-8");
			// httpRequest设置链接后面的参数
			httpRequest.setEntity(httpEntity);
			
			// 取得默认的HttpClient
			HttpClient httpclient = new DefaultHttpClient();
			// 取得HttpResponse并发送请求
			HttpResponse httpResponse = httpclient.execute(httpRequest);
			
			// 发送请求后，如果结果是HttpStatus.SC_OK表示连接成功
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				// 取得返回的字符串;
				result = EntityUtils.toString(httpResponse.getEntity());
			} 
    	} 
    	catch (Exception e) {
    		e.printStackTrace();
    	}
		
		return result;
	}
}
