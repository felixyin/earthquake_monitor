package com.greathammer.eqm.util;

import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

public class HttpClientRestTest2 {

	public static void main(String[] args) throws UnsupportedEncodingException {
		
		HttpPost post = new HttpPost("http://localhost:8080/deployPoint/saveSetting");
        // 向服务器写json
		DeployPoint deployPoint = new DeployPoint();
		deployPoint.setCityName("潍坊");
		deployPoint.setProductionVersion(false);
		
		String json = new JSONObject(deployPoint).toString();
		System.out.println(json);
        StringEntity se = new StringEntity(json); 
        post.setEntity(se);
         post.addHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8");
        DefaultHttpClient client = new DefaultHttpClient();

        try {
            HttpResponse response = client.execute(post);
            System.out.println(response.toString());
            System.out.println(response.getStatusLine().toString());

        } catch (Exception e) {
            e.printStackTrace();
            post.abort();
        }
	}

}
