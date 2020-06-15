/*
 * 


 */
package com.greathammer.eqm.weather;

/**
 *
 * @author FelixYin
 * java获取新浪天气预报代码
 */
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 解析xml文档，包括本地文档和url
 * 
 * @deprecated 老版有bug，不能继续使用
 */
public class CommonsWeatherUtils {

	private Log log = LogFactory.getLog(CommonsWeatherUtils.class);

	InputStream inStream;

	Element root;

	public InputStream getInStream() {
		return inStream;
	}

	public void setInStream(InputStream inStream) {
		this.inStream = inStream;
	}

	public Element getRoot() {
		return root;
	}

	public void setRoot(Element root) {
		this.root = root;
	}

	public CommonsWeatherUtils() {
	}

	/**
	 * 通过输入流来获取新浪接口信息
	 *
	 * @param inStream
	 */
	public CommonsWeatherUtils(InputStream inStream) {
		if (inStream != null) {
			this.inStream = inStream;
			DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder domBuilder = domfac.newDocumentBuilder();
				Document doc = domBuilder.parse(inStream);
				root = doc.getDocumentElement();
			} catch (ParserConfigurationException e) {
				log.error(e);
			} catch (SAXException e) {
				log.error(e);
			} catch (IOException e) {
				log.error(e);
			}
		}
	}

	public CommonsWeatherUtils(URL url) {
		InputStream inStream = null;
		try {
			inStream = url.openStream();
		} catch (IOException e1) {
			log.error(e1);
		}
		if (inStream != null) {
			this.inStream = inStream;
			DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();
			try {
				DocumentBuilder domBuilder = domfac.newDocumentBuilder();
				Document doc = domBuilder.parse(inStream);
				root = doc.getDocumentElement();
			} catch (ParserConfigurationException e) {
				log.error(e);
			} catch (SAXException e) {
				log.error(e);
			} catch (IOException e) {
				log.error(e);
			}
		}
	}

	/**
	 * @param nodes
	 * @return 单个节点多个值以分号分隔
	 */
	public Map<String, String> getValue(String[] nodes) {
		if (inStream == null || root == null) {
			return null;
		}
		Map<String, String> map = new HashMap<String, String>();
		// 初始化每个节点的值为null
		for (int i = 0; i < nodes.length; i++) {
			map.put(nodes[i], null);
		}

		// 遍历第一节点
		NodeList topNodes = root.getChildNodes();
		if (topNodes != null) {
			for (int i = 0; i < topNodes.getLength(); i++) {
				Node book = topNodes.item(i);
				if (book.getNodeType() == Node.ELEMENT_NODE) {
					for (int j = 0; j < nodes.length; j++) {
						for (Node node = book.getFirstChild(); node != null; node = node.getNextSibling()) {
							if (node.getNodeType() == Node.ELEMENT_NODE) {
								if (node.getNodeName().equals(nodes[j])) {
									String val = node.getTextContent();
									String temp = map.get(nodes[j]);
									if (temp != null && !temp.equals("")) {
										temp = temp + ";" + val;
									} else {
										temp = val;
									}
									map.put(nodes[j], temp);
								}
							}
						}
					}
				}
			}
		}
		return map;
	}

	/**
	 * 根据城市获取天气
	 * 
	 * @param city
	 * @return
	 */
	public WeatherMessage getWeather(String city) {
		try {
			String encode = URLEncoder.encode(city, "GBK");
			String link = "http://php.weather.sina.com.cn/xml.php?city=" + encode + "&password=DJOYnieT8234jlsK&day=0";

			System.out.println(link);

			URL url = new URL(link);
			CommonsWeatherUtils parser = new CommonsWeatherUtils(url);
			String[] nodes = { "city", "status1", "temperature1", "status2", "temperature2", "direction1", "power1",
					"pollution_l" };
			Map<String, String> map = parser.getValue(nodes);
			// String tianqi = map.get(nodes[1]);
			String gaowen = map.get(nodes[2]);
			String diwen = map.get(nodes[4]);
			// String fengxiang = map.get(nodes[5]);
			// String fengli = map.get(nodes[6]);
			// String kongqizhuangkuang = map.get(nodes[7]);

			int d = 0;
			if (null == gaowen || "".equals(gaowen)) {
				d = Integer.parseInt(diwen);
			} else {
				int temp = Integer.parseInt(gaowen) + Integer.parseInt(diwen);
				d = temp / 2;
			}

			return new WeatherMessage(map.get(nodes[0]), map.get(nodes[1]),
					map.get(nodes[2]) + "-" + map.get(nodes[4]) + "℃", map.get(nodes[5]), map.get(nodes[6]),
					map.get(nodes[7]), d + "℃");
		} catch (UnsupportedEncodingException ex) {
			log.error(ex);
		} catch (MalformedURLException ex) {
			log.error(ex);
		}
		return null;
	}
}
