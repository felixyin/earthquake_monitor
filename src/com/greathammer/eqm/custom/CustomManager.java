package com.greathammer.eqm.custom;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 本程序接收地震预警信息
 *
 * @author FelixYin
 */
public class CustomManager implements Runnable {

	private static ServerSocket server;

	public CustomManager() {
		try {
			if (null == server) {
				server = new ServerSocket(33456);
			}
		} catch (IOException ex) {
			Logger.getLogger(CustomManager.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private static CustomListener listener;

	public CustomManager setListener(CustomListener listener) {
		CustomManager.listener = listener;
		return this;
	}

	@Override
	public void run() {
		try {
			System.out.println("---------->开始监听...");
			while (true) {
				Socket socket = server.accept();
				System.out.println("---------->有链接接入");
				listener.doEvent(socket.getInputStream());
			}

		} catch (IOException ex) {
			Logger.getLogger(CustomManager.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

}
