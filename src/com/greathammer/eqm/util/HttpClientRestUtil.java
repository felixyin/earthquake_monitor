package com.greathammer.eqm.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpClientRestUtil {

	public static void main(String[] args) {

		DeployPoint deployPoint = new DeployPoint();
		deployPoint.setCityName("潍坊sdfdf ");
		deployPoint.setProductionVersion(false);

		String url = "http://localhost:8080/deployPoint/saveSetting";

		String result = post(url, deployPoint);
		System.out.println(result);

	}

	public static String post(String url, Object obj) {
		DefaultHttpClient httpClient = new DefaultHttpClient();

		try {

			HttpPost postRequest = new HttpPost(url);

			ObjectMapper mapper = new ObjectMapper();
			String json = mapper.writeValueAsString(obj);
			StringEntity input = new StringEntity(json, "UTF-8");
			input.setContentType("application/json");

			postRequest.setEntity(input);
			postRequest.addHeader(HTTP.CONTENT_TYPE, "application/json;charset=UTF-8");

			HttpResponse response = httpClient.execute(postRequest);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));

			StringBuffer sb = new StringBuffer();
			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}

			String resultStr = sb.toString();

			return resultStr;

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			httpClient.getConnectionManager().shutdown();
		}
		return "";
	}

}
