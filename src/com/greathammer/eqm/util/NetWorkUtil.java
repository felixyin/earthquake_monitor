package com.greathammer.eqm.util;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

/**
 * 
 * @author FelixYin
 *
 */
public class NetWorkUtil {

	// private static Log log = LogFactory.getLog(NetWorkUtil.class);

	private static boolean IS_NETWORK_OK = false;

	private NetWorkUtil() {
	}

	public static boolean isNetworkOk() {
		IS_NETWORK_OK = NetworkHelper.getInstance().isReachIp("139.224.1.36"); // 永久可用的ip
		return IS_NETWORK_OK;
		// try {
		// Content returnContent =
		// Request.Get("http://114.215.151.15:9005/front/helpCenter?id=194").execute()
		// .returnContent();
		// String body = returnContent.asString();
		// boolean b = (null != body && body.contains("巨锤时代"));
		// log.debug("监测网络状态为(true:正常,false:异常)：" + b);
		// return b;
		// } catch (IOException e) {
		// log.error(e);
		// }
		// return false;
	}

	public static void main(String[] args) throws ClientProtocolException, IOException {
		System.out.println(isNetworkOk());
	}

	public static int getNetWorkStatus() {
		return IS_NETWORK_OK ? 0 : 1;
	}

}
