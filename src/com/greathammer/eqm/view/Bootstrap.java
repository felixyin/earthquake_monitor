package com.greathammer.eqm.view;

import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.greathammer.eqm.util.Constant;
import com.greathammer.eqm.util.Toast;
import com.greathammer.eqm.util.UTF2GBK;
import com.greathammer.eqm.view.v2.ImageJFrame;
import com.greathammer.eqm.view.v2.MainJFrame;
import com.greathammer.eqm.view.v2.ScreensaverJFrame;
import com.greathammer.eqm.view.v2.SettingJFrame;
import com.greathammer.serial.EqmSerialTool;

/**
 * 主程序
 *
 * @author FelixYin
 *
 */
public class Bootstrap extends JFrame {

	private static final long serialVersionUID = 1L;

	private static Log log = LogFactory.getLog(Bootstrap.class);

	private Image icon = getToolkit().getImage(Bootstrap.class.getResource("resource/hello.gif"));

	private MainJFrame mainJFrame;

	private ScreensaverJFrame screensaverJFrame;

	private ImageJFrame imageJFrame;

	public MainJFrame getMainJFrame() {
		return mainJFrame;
	}

	public ScreensaverJFrame getScreensaverJFrame() {
		return screensaverJFrame;
	}

	public ImageJFrame getImageJFrame() {
		return imageJFrame;
	}

	/**
	 * 显示主界面
	 *
	 * @throws InterruptedException
	 */
	public void launchFrame() throws InterruptedException {
		log.info("开始显示欢迎界面");
		this.setBounds(640, 340, 640, 400);
		final Bootstrap bootstrap = this;
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				EqmSerialTool.resetDevice();
				new Toast(bootstrap, "关闭处理中...", 3000, Toast.success).start();
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					log.error(e);
				}
				System.exit(0);
			}
		});
		setImage();
		this.isUndecorated();
		this.setUndecorated(true);
		this.setResizable(false);
		this.setVisible(true);
		log.info("已显示欢迎界面");

		Constant.initConstant();

		// *************************** 注册快捷键 ***************************
		this.registerKey();
		this.showMinTray();

		Thread.sleep(Constant.HELLO_SCREEN_SHOW_TIME);

		setVisible(false);
		log.info("关闭欢迎界面");

		// *************************** 开启地震和屏保子界面 ***************************

		imageJFrame = new ImageJFrame(this);

		mainJFrame = new MainJFrame(this);

		log.info("在地震界面停顿" + Constant.SCREENSAVER_SHOW_AFTER_EQM + "秒再显示屏保界面");
		Thread.sleep(Constant.SCREENSAVER_SHOW_AFTER_EQM);

		screensaverJFrame = new ScreensaverJFrame(this);

		EqmSerialTool.showDateTime(); // 重置应急灯为显示时间

	}

	private void setImage() {
		JLabel lbl = new JLabel();
		Container c = this.getContentPane();
		c.setBackground(Color.GRAY);
		c.setLayout(new FlowLayout());
		c.add(lbl);
		lbl.setIcon(new ImageIcon(icon));
	}

	private static void firstStart() {
		JOptionPane.showConfirmDialog(null, "第一次启动需要进行适当配置", "青岛科信提示您", JOptionPane.CLOSED_OPTION);

		log.info("第一次启动需要进行适当配置，显示配置界面进行配置");

		new SettingJFrame();
	}

	private static void resetConfig() {
		log.info("找不到配置文件，所以先初始化配置文件");
		Set<String> set = new HashSet<String>();
		set.add("IS_PRODUCTION_VERSION=1");
		set.add("CITY_NAME=青岛");
		set.add("LONGITUDE=120");
		set.add("LATITUDE=36");
		set.add("AUDIO_WAV_INTENSITY=1");
		set.add("AUDIO_WAV_COUNT=3");
		set.add("IS_PLAY_COUNT_DOWN=1");
		set.add("IS_SHOW_LIGHT=1");
		set.add("IS_PLAY_AUDIO=1");
		set.add("COMPANY_NAME=青岛地震局");
		set.add("TEST_EARTHQUAKE_FIRST_TIME=10");
		set.add("TEST_EARTHQUAKE_TIME=60");
		set.add("SWITCH_SCREENSAVER_TIME_AFTER_EQM=35");
		set.add("SCREENSAVER_SHOW_AFTER_EQM=10");
		set.add("HELLO_SCREEN_SHOW_TIME=3");
		set.add("EQMQ_TCP_FUJIAN=tcp://218.5.2.74");
		set.add("EQMQ_PORT_FUJIAN=1884");
		set.add("EQMQ_TOPIC_FUJIAN=fjea/eewproject/eewmsg");
		set.add("EQMQ_TOPIC_FUJIAN_TEST=eewZGQ");
		set.add("IS_TOPIC_PRODUCTION_MODEL=1");//1是地震局台网的正式接口主题、0是测试
		set.add("COM=COM1");
		set.add("EQMQ_AFTER_PRODUCTION_TIME=60");

		Iterator<String> iterator = set.iterator();
		File file = new File(Constant.C_WINDOWS_EQM_PROPERTIES);
		OutputStreamWriter fw = null;
		BufferedWriter writer = null;
		try {
			fw = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			writer = new BufferedWriter(fw);
			while (iterator.hasNext()) {
				writer.write(iterator.next().toString());
				writer.newLine();// 换行
			}
			writer.flush();
		} catch (FileNotFoundException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		} finally {
			try {
				writer.close();
				fw.close();
			} catch (IOException e) {
				log.error(e);
			}
		}
	}

	public void registerKey() {
		Toolkit toolkit = Toolkit.getDefaultToolkit();

		// 注册应用程序全局键盘事件, 所有的键盘事件都会被此事件监听器处理.
		toolkit.addAWTEventListener(new java.awt.event.AWTEventListener() {
			private int _keyPressCount = 0;
			private int _settingPressCount = 0;
			private boolean isReady = false;

			final int K_ENTER = 10;
			final int K_0 = 96;
			final int K_1 = 97;
			final int K_2 = 98;
			final int K_3 = 99;
			final int K_4 = 100;
			final int K_5 = 101;
			final int K_6 = 102;
			final int K_7 = 103;
			final int K_8 = 104;
			final int K_9 = 105;
			final int K_MULTIPLY = 106;
			// final int K_PLUS = 107;
			// final int K_MINUS = 109;
			final int K_POINT = 110;
			// final int K_SLASH = 111;

			public boolean isKey(int kc) {
				return kE.getKeyCode() == kc && KeyEvent.KEY_PRESSED == kE.getID();
			}

			public void autoDisYanXi() {
				isReady = false;
				new Thread() {

					@Override
					public void run() {
						try {
							Thread.sleep(1000 * 20);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Constant.setProductionMode();
						mainJFrame.bindDataSource();
						mainJFrame.getEarthquakeYuJing().resetField();
						new Toast(mainJFrame, "已自动切换回正式版本", 5000, Toast.success).start();
						new Toast(screensaverJFrame, "已自动切换回正式版本", 5000, Toast.success).start();
					}

				}.start();
			}

			private KeyEvent kE;

			private boolean switchStatus = true;

			public void eventDispatched(AWTEvent event) {
				if (event.getClass() == KeyEvent.class) {
					this.kE = ((KeyEvent) event);
					if (isKey(K_POINT)) {
						_keyPressCount += 1;
						log.info("按下了" + kE.getKeyChar());
					} else if (isKey(K_ENTER)) {
						if (_keyPressCount == 3) {
							isReady = true;
							if (Constant.isProductionMode()) {
								new Toast(mainJFrame, "演习版", 2000, Toast.success).start();
								new Toast(screensaverJFrame, "演习版", 2000, Toast.success).start();
								log.info("运行的是演习版本");
							} else if (Constant.isTestMode()) {
								new Toast(mainJFrame, "演示版", 2000, Toast.success).start();
								new Toast(screensaverJFrame, "演示版", 2000, Toast.success).start();
								log.info("运行的是演示版本");
							}
						} else {
							isReady = false;
						}
						_keyPressCount = 0;
						_settingPressCount = 0;
						log.info("按下了" + kE.getKeyChar());
					}

					if (!isReady)
						return;

					if (isKey(K_0)) {
						new Toast(mainJFrame, "小键盘已经就绪，请输入命令", 2000, Toast.success).start();
						new Toast(screensaverJFrame, "小键盘已经就绪，请输入命令", 2000, Toast.success).start();
						log.info("按下了" + kE.getKeyChar());
						log.info("小键盘已经就绪");
					} else if (isKey(K_1) && Constant.isProductionMode()) {
						if (Constant.isEARTHQUAKING())
							return;
						// Constant.setRealEarthquake(false);
						Constant.setTestMode();
						mainJFrame.bindDataSource();
						mainJFrame.getManager().start(3);
						autoDisYanXi();
						log.info("按下了" + kE.getKeyChar());
						log.info("演习蓝灯");
					} else if (isKey(K_2) && Constant.isProductionMode() && isReady) {
						if (Constant.isEARTHQUAKING())
							return;
						// Constant.setRealEarthquake(false);
						Constant.setTestMode();
						mainJFrame.bindDataSource();
						mainJFrame.getManager().start(2);
						autoDisYanXi();
						log.info("按下了" + kE.getKeyChar());
						log.info("演习黄灯");
					} else if (isKey(K_3) && Constant.isProductionMode() && isReady) {
						if (Constant.isEARTHQUAKING())
							return;
						// Constant.setRealEarthquake(false);
						Constant.setTestMode();
						mainJFrame.bindDataSource();
						mainJFrame.getManager().start(1);
						autoDisYanXi();
						log.info("按下了" + kE.getKeyChar());
						log.info("演习橘灯");
					} else if (isKey(K_4) && Constant.isProductionMode() && isReady) {
						if (Constant.isEARTHQUAKING())
							return;
						// Constant.setRealEarthquake(false);
						Constant.setTestMode();
						mainJFrame.bindDataSource();
						mainJFrame.getManager().start(0);
						autoDisYanXi();
						log.info("按下了" + kE.getKeyChar());
						log.info("演习红灯");
					} else if (isKey(K_5)) {
						log.info("按下了" + kE.getKeyChar());
						log.info("预留扩展键");
					} else if (isKey(K_6)) {
						log.info("按下了" + kE.getKeyChar());
						log.info("预留扩展键");
					} else if (isKey(K_7)) {
						log.info("按下了" + kE.getKeyChar());
						try {
							if (this.switchStatus) {
								this.switchStatus = false;
								getImageJFrame().myHide();
								getMainJFrame().myHide();
								getScreensaverJFrame().myShow();
								log.info("您刚才在地震界面，现已回到屏保界面");
							} else {
								this.switchStatus = true;
								getImageJFrame().myHide();
								getScreensaverJFrame().myHide();
								getMainJFrame().myShow();
								log.info("您刚才在屏保界面，现已回到地震界面");
							}
						} catch (Exception e) {
							log.error(e);
						}
						// if (null != mainJFrame && null != screensaverJFrame
						// && screensaverJFrame.isShow()) {
						// mainJFrame.myShow();
						// screensaverJFrame.myHide();
						// log.info("您刚才在屏保界面，现已回到地震界面");
						// } else if (null != screensaverJFrame && null !=
						// mainJFrame && mainJFrame.isShow()) {
						// screensaverJFrame.myShow();
						// mainJFrame.myHide();
						// log.info("您刚才在地震界面，现已回到屏保界面");
						// }
					} else if (isKey(K_8) && Constant.isTestMode()) {
						log.info("按下了" + kE.getKeyChar());
						if (Constant.IS_LOOP_FOR_TEST) {
							Constant.IS_LOOP_FOR_TEST = false;
							new Toast(mainJFrame, "已禁用循环演示", 3000, Toast.success).start();
							new Toast(screensaverJFrame, "已禁用循环演示", 3000, Toast.success).start();
							log.info("禁用循环演示");
						} else {
							Constant.IS_LOOP_FOR_TEST = true;
							new Toast(mainJFrame, "已启用循环演示", 3000, Toast.success).start();
							new Toast(screensaverJFrame, "已启用循环演示", 3000, Toast.success).start();
							log.info("启用循环演示");
						}
					} else if (isKey(K_9)) {
						getImageJFrame().myHide();
						getScreensaverJFrame().myHide();
						getMainJFrame().myHide();
						log.info("按下了" + kE.getKeyChar());
						log.info("隐藏全部窗口");
					} else if (isKey(K_MULTIPLY)) {
						// *** pressed
						_settingPressCount += 1;
						if (_settingPressCount == 3) {
							log.info("按下了" + kE.getKeyChar() + "三次");
							isReady = false;
							mainJFrame.myHide();
							screensaverJFrame.myHide();
							new SettingJFrame();
						}
					}

				}
			}
		}, java.awt.AWTEvent.KEY_EVENT_MASK);
	}

	public void showMinTray() {
		if (SystemTray.isSupported()) {
			SystemTray tray = SystemTray.getSystemTray();
			Image icon = getToolkit()
					.getImage(getClass().getResource("/com/greathammer/eqm/view/resource/v2/tray-48.png"));

			TrayIcon trayicon = new TrayIcon(icon, UTF2GBK.utf82gbk("科信地震预警系统"));
			trayicon.setImageAutoSize(true);

			// 创建弹出菜单
			PopupMenu menu = new PopupMenu();
			trayicon.setPopupMenu(menu);

			MenuItem item = new MenuItem("exit");
			item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					EqmSerialTool.resetDevice();
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e1) {
						log.error(e1);
					}
					System.exit(0);
				}
			});
			menu.add(item);
			MenuItem item2 = new MenuItem("hide");
			item2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getImageJFrame().myHide();
					getMainJFrame().myHide();
					getScreensaverJFrame().myHide();
				}
			});
			menu.add(item2);
			MenuItem item3 = new MenuItem("show");
			item3.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getImageJFrame().myHide();
					getMainJFrame().myHide();
					getScreensaverJFrame().myShow();
				}
			});
			menu.add(item3);

			try {
				tray.add(trayicon);
			} catch (AWTException e) {
				e.printStackTrace();
			}
		}
	}

	/**	 
	 *
	 */
	public static void main(String[] args) {
		log.info("开始启动应用程序");
		if (new File(Constant.C_WINDOWS_EQM_PROPERTIES).exists()) {
			log.info("发现配置文件已经存在");
			try {
				new Bootstrap().launchFrame();
			} catch (InterruptedException e) {
				log.error(e);
			}
		} else {
			resetConfig();
			firstStart();
		}

	}
}
