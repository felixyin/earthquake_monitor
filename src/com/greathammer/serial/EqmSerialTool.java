package com.greathammer.serial;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.greathammer.eqm.util.Constant;
import com.greathammer.eqm.util.NetWorkUtil;
import com.greathammer.serial.exception.NoSuchPort;
import com.greathammer.serial.exception.NotASerialPort;
import com.greathammer.serial.exception.PortInUse;
import com.greathammer.serial.exception.SendDataToSerialPortFailure;
import com.greathammer.serial.exception.SerialPortOutputStreamCloseFailure;
import com.greathammer.serial.exception.SerialPortParameterFailure;

import gnu.io.SerialPort;

/**
 * 
 * @author FelixYin
 *
 */
public class EqmSerialTool {

	private static Log log = LogFactory.getLog(EqmSerialTool.class);

	private static SerialPort serialPort = null;

	private static Timer timer;

	public static void initAfterClear() {
		if (null != timer) {
			timer.cancel();
			log.info("上一次串口传输线程还没有结束，现在取消执行");
		}

		timer = new Timer();
		log.info("重新开启一个串口传输线程");

		if (null != serialPort) {
			SerialTool.closePort(serialPort);
			log.info("关闭上一次打开的串口端口");
		}

		try {
			log.info("开始打开一个新的串口端口：" + Constant.COM);
			serialPort = SerialTool.openPort(Constant.COM, 9600);
			log.info("已经打开一个新的串口端口：" + Constant.COM);
		} catch (SerialPortParameterFailure e) {
			log.error(e);
		} catch (NotASerialPort e) {
			log.error(e);
		} catch (NoSuchPort e) {
			log.error(e);
		} catch (PortInUse e) {
			log.error(e);
		}
	}

	private static String convertSerialData(String[] arrStr) {
		StringBuffer sb = new StringBuffer();
		sb.append("$");
		for (String arr : arrStr) {
			sb.append(arr).append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		// String hex = CRC16M.getBufHexStr(sb.toString().getBytes());
		String hex = "";
		return sb.append(",").append(hex).toString();
	}

	public synchronized static void resetDevice() {
		EqmSerialTool.showDateTime();
		log.info("重置报警灯，显示时间");
	}

	public static synchronized void showDateTime() {
		initAfterClear();

		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {

				Date now = new Date();
				String[] dt = new SimpleDateFormat("yy-MM-dd#HH:mm:ss").format(now).split("#");
				// $16-12-04, 20:27:17, 0, 000, 0, 00, ,
				String[] arrStr = new String[] { dt[0], dt[1], "0", "000", "" + NetWorkUtil.getNetWorkStatus(), "00",
						"00", "", "" };
				String data = convertSerialData(arrStr);
				log.debug(data);
				try {
					SerialTool.sendToPort(serialPort, data.getBytes());
				} catch (SendDataToSerialPortFailure | SerialPortOutputStreamCloseFailure e) {
					log.error(e);
				}
			}
		}, 1000, 1000);
	}

	/**
	 * @param lightColor
	 * @param countDown
	 * @param networkStatus
	 * @param intensityStr
	 */
	public static synchronized void showEQM(final String light, final int countDown, final int networkStatus,
			final int intensity) {
		initAfterClear();

		timer.scheduleAtFixedRate(new TimerTask() {
			int i = countDown;
			String intensityStr = intensity < 10 ? "0" + intensity : "" + intensity;

			@Override
			public void run() {
				Date now = new Date();

				this.i--;
				String cd = "";
				if (this.i < 0) { // 地震倒计时结束
					initAfterClear();
					return;
				} else if (this.i >= 0 && this.i < 10) {
					cd = "00" + this.i;
				} else if (this.i >= 10 && this.i < 100) {
					cd = "0" + this.i;
				} else if (this.i >= 100 && this.i < 1000) {
					cd = "" + this.i;
				}

				String lightColor = "0";
				String audioPlay = "00";

				if (Constant.IS_SHOW_LIGHT == 1) {
					if ("blue".equals(light)) {
						lightColor = "1";
					} else if ("yellow".equals(light)) {
						lightColor = "2";
					} else if ("orange".equals(light)) {
						lightColor = "3";
					} else if ("red".equals(light)) {
						lightColor = "4";
					} else {
						lightColor = "0";
					}
				} else {
					lightColor = "0";
				}

				if (Constant.IS_PLAY_AUDIO == 1) {
					if (Constant.IS_PLAY_COUNT_DOWN == 1 && this.i <= 99) {
						if (intensity > Constant.AUDIO_WAV_INTENSITY) {
							audioPlay = "11";
						} else {
							audioPlay = "01";
						}
					} else {
						audioPlay = "10";
					}
				} else {
					if (Constant.IS_PLAY_COUNT_DOWN == 1) {
						audioPlay = "01";
					} else {
						audioPlay = "00";
					}
				}

				log.debug("------------->>>>>>light:" + light + ",setting-intensity:" + Constant.AUDIO_WAV_INTENSITY
						+ ",cal-intensity:" + intensity + ",light+sound:" + lightColor);

				String[] dt = new SimpleDateFormat("yy-MM-dd#HH:mm:ss").format(now).split("#");
				String[] arrStr = new String[] { dt[0], dt[1], lightColor, cd, "" + networkStatus, intensityStr,
						audioPlay, "", "" };
				// String[] arrStr = new String[] { "00-00-00", "00:00:00",
				// getLight(lightColor), cd, "" + networkStatus,
				// intensityStr, "", "" };
				String data = convertSerialData(arrStr);
				log.debug(data);

				try {
					SerialTool.sendToPort(serialPort, data.getBytes());
				} catch (SendDataToSerialPortFailure | SerialPortOutputStreamCloseFailure e) {
					log.error(e);
				}

			}
		}, 0, 1000);
	}

	private static void mySleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			log.error(e);
		}
	}

	public static void main(String[] args) {

		final Thread t1 = new Thread() {
			@Override
			public void run() {
				resetDevice();// 演示时间显示
			}
		};
		t1.start();

		final Thread t2 = new Thread() {
			@Override
			public void run() {
				mySleep(1000 * 10);
				// t1.stop();
				t1.interrupt();
				try {
					t1.join();
				} catch (InterruptedException e) {
					log.error(e);
				}
				showEQM("orange", 15, 0, 6);// 演示地震响
			}
		};
		t2.start();

		new Thread() {
			@Override
			public void run() {
				mySleep(1000 * 20);
				// t2.stop();
				t2.interrupt();
				try {
					t2.join();
				} catch (InterruptedException e) {
					log.error(e);
				}
				resetDevice();// 演示时间显示
			}
		}.start();

	}
}
