package com.greathammer.eqm.util;
 
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
 

/**
 * 测试网络连通性
 * 
 * @author donald
 * 
 */
public class NetworkHelper {
	private static Log log = LogFactory.getLog(NetworkHelper.class);
	
	private static NetworkHelper instance = null;
	public static synchronized NetworkHelper getInstance(){
		if(instance == null){
			instance = new NetworkHelper();
		}
		return instance;
		
	}

	/**
	 * 测试本地能否ping ip
	 * 
	 * @param ip
	 * @return
	 */
	public boolean isReachIp(String ip) {
		boolean isReach = false;
		try {
			InetAddress address = InetAddress.getByName(ip);// ping this IP

			if (address instanceof java.net.Inet4Address) {
				log.info(ip + " is ipv4 address");
			} else if (address instanceof java.net.Inet6Address) {
				log.info(ip + " is ipv6 address");
			} else {
				log.info(ip + " is unrecongized");
			}
			if (address.isReachable(5000)) {
				isReach = true;
				log.info("SUCCESS - ping " + ip
						+ " with no interface specified");
			} else {
				isReach = false;
				log.info("FAILURE - ping " + ip
						+ " with no interface specified");
			}
		} catch (Exception e) {
			log.error("error occurs:" + e.getMessage());
		}
		return isReach;
	}

	/**
	 * 测试本地所有的网卡地址都能ping通 ip
	 * 
	 * @param ip
	 * @return
	 */
	public  boolean isReachNetworkInterfaces(String ip) {
		boolean isReach = false;
		try {
			InetAddress address = InetAddress.getByName(ip);// ping this IP

			if (address instanceof java.net.Inet4Address) {
				log.info(ip + " is ipv4 address");
			} else if (address instanceof java.net.Inet6Address) {
				log.info(ip + " is ipv6 address");
			} else {
				log.info(ip + " is unrecongized");
			}
			if (address.isReachable(5000)) {
				isReach = true;
				log.info("SUCCESS - ping " + ip
						+ " with no interface specified");
			} else {
				isReach = false;
				log.info("FAILURE - ping " + ip
						+ " with no interface specified");
			}
			if (isReach) {
				log.info("-------Trying different interfaces--------");
				Enumeration<NetworkInterface> netInterfaces = NetworkInterface
						.getNetworkInterfaces();
				while (netInterfaces.hasMoreElements()) {
					NetworkInterface ni = netInterfaces.nextElement();
					log.info("Checking interface, DisplayName:"
							+ ni.getDisplayName() + ", Name:" + ni.getName());
					if (address.isReachable(ni, 0, 5000)) {
						isReach = true;
						log.info("SUCCESS - ping " + ip);
					} else {
						isReach = false;
						log.info("FAILURE - ping " + ip);
					}
					Enumeration<InetAddress> ips = ni.getInetAddresses();
					while (ips.hasMoreElements()) {
						log.info("IP: " + ips.nextElement().getHostAddress());
					}
					log.info("-----------------check now NetworkInterface is done--------------------------");
				}
			}
		} catch (Exception e) {
			log.error("error occurs:" + e.getMessage());
		}
		return isReach;
	}

	/**
	 * 获取能与远程主机指定端口建立连接的本机ip地址
	 * @param remoteAddr
	 * @param port
	 * @return
	 */
	public  String getReachableIP(InetAddress remoteAddr, int port) {
		String retIP = null;
		Enumeration<NetworkInterface> netInterfaces;
		try {
			netInterfaces = NetworkInterface.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = netInterfaces.nextElement();
				Enumeration<InetAddress> localAddrs = ni.getInetAddresses();
				while (localAddrs.hasMoreElements()) {
					InetAddress localAddr = localAddrs.nextElement();
					if (isReachable(localAddr, remoteAddr, port, 5000)) {
						retIP = localAddr.getHostAddress();
						break;
					}
				}
			}
		} catch (SocketException e) {
			log.error("Error occurred while listing all the local network addresses:"
					+ e.getMessage());
		}
		if (retIP == null) {
			log.info("NULL reachable local IP is found!");
		} else {
			log.info("Reachable local IP is found, it is " + retIP);
		}
		return retIP;
	}
    /**
     * 获取能与远程主机指定端口建立连接的本机ip地址
     * @param remoteIp
     * @param port
     * @return
     */
	public  String getReachableIP(String remoteIp, int port) {

		String retIP = null;
		InetAddress remoteAddr = null;
		Enumeration<NetworkInterface> netInterfaces;
		try {
			remoteAddr = InetAddress.getByName(remoteIp);
			netInterfaces = NetworkInterface.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = netInterfaces.nextElement();
				Enumeration<InetAddress> localAddrs = ni.getInetAddresses();
				while (localAddrs.hasMoreElements()) {
					InetAddress localAddr = localAddrs.nextElement();
					if (isReachable(localAddr, remoteAddr, port, 5000)) {
						retIP = localAddr.getHostAddress();
						break;
					}
				}
			}
		} catch (UnknownHostException e) {
			log.error("Error occurred while listing all the local network addresses:"+ e.getMessage());
		}catch (SocketException e) {
			log.error("Error occurred while listing all the local network addresses:"+ e.getMessage());
		}
		if (retIP == null) {
			log.info("NULL reachable local IP is found!");
		} else {
			log.info("Reachable local IP is found, it is " + retIP);
		}
		return retIP;
	}
	/**
	 * 测试localInetAddr能否与远程的主机指定端口建立连接相连
	 * 
	 * @param localInetAddr
	 * @param remoteInetAddr
	 * @param port
	 * @param timeout
	 * @return
	 */
	public  boolean isReachable(InetAddress localInetAddr,
			InetAddress remoteInetAddr, int port, int timeout) {
		boolean isReachable = false;
		Socket socket = null;
		try {
			socket = new Socket();
			// 端口号设置为 0 表示在本地挑选一个可用端口进行连接
			SocketAddress localSocketAddr = new InetSocketAddress(
					localInetAddr, 0);
			socket.bind(localSocketAddr);
			InetSocketAddress endpointSocketAddr = new InetSocketAddress(
					remoteInetAddr, port);
			socket.connect(endpointSocketAddr, timeout);
			log.info("SUCCESS - connection established! Local: "
					+ localInetAddr.getHostAddress() + " remote: "
					+ remoteInetAddr.getHostAddress() + " port" + port);
			isReachable = true;
		} catch (IOException e) {
			log.error("FAILRE - CAN not connect! Local: "
					+ localInetAddr.getHostAddress() + " remote: "
					+ remoteInetAddr.getHostAddress() + " port" + port);
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					log.error("Error occurred while closing socket:"
							+ e.getMessage());
				}
			}
		}
		return isReachable;
	}

	/**
	 * 测试localIp能否与远程的主机指定端口建立连接相连
	 * 
	 * @param localIp
	 * @param remoteIp
	 * @param port
	 * @param timeout
	 * @return
	 */
	public  boolean isReachable(String localIp, String remoteIp,
			int port, int timeout) {
		boolean isReachable = false;
		Socket socket = null;
		InetAddress localInetAddr = null;
		InetAddress remoteInetAddr = null;
		try {
			localInetAddr = InetAddress.getByName(localIp);
			remoteInetAddr = InetAddress.getByName(remoteIp);
			socket = new Socket();
			// 端口号设置为 0 表示在本地挑选一个可用端口进行连接
			SocketAddress localSocketAddr = new InetSocketAddress(
					localInetAddr, 0);
			socket.bind(localSocketAddr);
			InetSocketAddress endpointSocketAddr = new InetSocketAddress(
					remoteInetAddr, port);
			socket.connect(endpointSocketAddr, timeout);
			log.info("SUCCESS - connection established! Local: "
					+ localInetAddr.getHostAddress() + " remote: "
					+ remoteInetAddr.getHostAddress() + " port" + port);
			isReachable = true;
		} catch (IOException e) {
			log.error("FAILRE - CAN not connect! Local: "
					+ localInetAddr.getHostAddress() + " remote: "
					+ remoteInetAddr.getHostAddress() + " port" + port);
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					log.error("Error occurred while closing socket:"
							+ e.getMessage());
				}
			}
		}
		return isReachable;
	}

	public static void main(String[] args) {
          String ip = "139.224.1.36";
		if(NetworkHelper.getInstance().isReachIp(ip)){
        	  log.info("=======本机可以ping通ip："+ip);
          }
          else{
        	  log.info("=======本机ping不通ip："+ip);
          }
          if(NetworkHelper.getInstance().isReachNetworkInterfaces(ip)){
        	  log.info("=======本机所有网卡可以ping通ip："+ip);
          }
          else{
        	  log.info("=======本机所有网卡ping不通ip："+ip);
          }
          String localIp = NetworkHelper.getInstance().getReachableIP(ip,8081);
          if(null != localIp && !"".equals(localIp)){
        	  log.info("=======本机可以与ip："+ip+",port:"+8081+"建立连接的IP："+localIp);
          }
          else{
        	  log.info("=======本机不能与ip："+ip+",port:"+8081+"建立连接的IP");
          }
	}

}