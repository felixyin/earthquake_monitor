package com.greathammer.serial;

import java.util.ArrayList;
import java.util.Iterator;

import com.greathammer.serial.exception.ReadDataFromSerialPortFailure;
import com.greathammer.serial.exception.SerialPortInputStreamCloseFailure;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class TestGps {

	public static void main(String[] args) throws Exception {
		ArrayList<String> findPort = SerialTool.findPort();
		for (Iterator<String> iterator = findPort.iterator(); iterator.hasNext();) {
			String port = (String) iterator.next();
			System.out.println(port);
		}

		// 4800, 9600, 19200, 38400, 5700, 115200
		final SerialPort serialPort = SerialTool.openPort("COM3", 4800);

		SerialTool.addListener(serialPort, new SerialPortEventListener() {

			@Override
			public void serialEvent(SerialPortEvent serialPortEvent) {

				switch (serialPortEvent.getEventType()) {

				case SerialPortEvent.BI: // 10 通讯中断
					System.out.println("通讯意外中断");
					break;

				case SerialPortEvent.OE: // 7 溢位（溢出）错误

				case SerialPortEvent.FE: // 9 帧错误

				case SerialPortEvent.PE: // 8 奇偶校验错误

				case SerialPortEvent.CD: // 6 载波检测

				case SerialPortEvent.CTS: // 3 清除待发送数据

				case SerialPortEvent.DSR: // 4 待发送数据准备好了

				case SerialPortEvent.RI: // 5 振铃指示

				case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2 输出缓冲区已清空
					break;

				case SerialPortEvent.DATA_AVAILABLE: // 1 串口存在可用数据

					System.out.println("found data");
					byte[] data = null;

					try {
						data = SerialTool.readFromPort(serialPort); // 读取数据，存入字节数组
						// System.out.println(new String(data));

						// 自定义解析过程，你在实际使用过程中可以按照自己的需求在接收到数据后对数据进行解析
						if (data == null || data.length < 1) { // 检查数据是否读取正确
							System.out.println("读取数据过程中未获取到有效数据！请检查设备或程序！");
						} else {
							String dataOriginal = new String(data); // 将字节数组数据转换位为保存了原始数据的字符串
							System.out.println(dataOriginal);

							String[] split = dataOriginal.split("\n");
							for (String row : split) {
								if (row.contains("GPGGA")) {
									System.out.println(row);

									String[] column = row.split(",");
									String jingDu = column[2];
									String weiDu = column[4];
									System.out.println(jingDu + ":" + weiDu);
								}
							}
						}

					} catch (ReadDataFromSerialPortFailure | SerialPortInputStreamCloseFailure e) {
						System.out.println("错误");
					}

					break;

				}

			}
		});

	}

}