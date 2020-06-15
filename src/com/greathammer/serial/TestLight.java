package com.greathammer.serial;

import java.util.ArrayList;
import java.util.Iterator;

import com.greathammer.serial.exception.NoSuchPort;
import com.greathammer.serial.exception.NotASerialPort;
import com.greathammer.serial.exception.PortInUse;
import com.greathammer.serial.exception.SendDataToSerialPortFailure;
import com.greathammer.serial.exception.SerialPortOutputStreamCloseFailure;
import com.greathammer.serial.exception.SerialPortParameterFailure;

import gnu.io.SerialPort;

public class TestLight {

	private static String COM = "/dev/tty.usbserial";

	public static String myJoin(String split, String[] arrStr) {
		StringBuffer sb = new StringBuffer();
		for (String arr : arrStr) {
			sb.append(arr).append(split);
		}
		String s = sb.toString();
		return s.substring(0, s.lastIndexOf(split));
	}

	public static void main(String[] args) throws SerialPortParameterFailure, NotASerialPort, NoSuchPort, PortInUse,
			SendDataToSerialPortFailure, SerialPortOutputStreamCloseFailure {
		ArrayList<String> findPort = SerialTool.findPort();
		for (Iterator<String> iterator = findPort.iterator(); iterator.hasNext();) {
			String com = (String) iterator.next();
			System.out.println(com);
		}

		final SerialPort serialPort = SerialTool.openPort(COM, 9600);

		String[] arrStr = new String[] { "16-12-02", "15:38:38", "0", "022", "0", "05", "", "", "" };

		// for (int i = 0; i < arrStr.length; i++) {
		// String item = arrStr[i];
		// arrStr[i] = ASC.stringConvertAsc(item);
		// }

		String data = "$" + myJoin(",", arrStr);

		String hex = CRC16M.getBufHexStr(data.getBytes());
		data = data + "," + hex;

		byte[] byteData = data.getBytes();
		SerialTool.sendToPort(serialPort, byteData);

		// SerialTool.closePort(serialPort);
		// SerialTool.closePort(serialPort);
		SerialTool.closePort(serialPort);

		System.exit(0);
	}
}
