package com.greathammer.eqm.view;

import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JLabel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.greathammer.eqm.util.Constant;
import com.greathammer.eqm.util.NetWorkUtil;
import com.greathammer.eqm.util.ShortcutManager;
import com.greathammer.eqm.util.Toast;
import com.greathammer.eqm.view.v2.MainJFrame;
import com.greathammer.eqm.view.v2.ScreensaverJFrame;
import com.greathammer.eqm.view.v2.SettingJFrame;
import com.greathammer.serial.EqmSerialTool;

/**
 *
 * @author FelixYin
 */
public abstract class BaseJFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Log log = LogFactory.getLog(BaseJFrame.class);

	public static final String ORIGINAL_PNG = "/com/greathammer/eqm/view/resource/original.png";

	public static final String BLUE_JPG = "/com/greathammer/eqm/view/resource/v2/blue.jpg";
	public static final String YELLOW_JPG = "/com/greathammer/eqm/view/resource/v2/yellow.jpg";
	public static final String ORANGE_JPG = "/com/greathammer/eqm/view/resource/v2/orange.jpg";
	public static final String RED_JPG = "/com/greathammer/eqm/view/resource/v2/red.jpg";

	public static final String BLUE_GIF = "/com/greathammer/eqm/view/resource/v2/blue.gif";

	public static final String YELLOW_GIF = "/com/greathammer/eqm/view/resource/v2/yellow.gif";

	public static final String ORANGE_GIF = "/com/greathammer/eqm/view/resource/v2/orange.gif";

	public static final String RED_GIF = "/com/greathammer/eqm/view/resource/v2/red.gif";

	// private static final String AUDIO_WAV =
	// "/com/greathammer/eqm/view/resource/v2/audio.wav";

	public static final String BLUE_WAV = "/com/greathammer/eqm/view/resource/v2/blue.wav";

	public static final String YELLOW_WAV = "/com/greathammer/eqm/view/resource/v2/yellow.wav";

	public static final String ORANGE_WAV = "/com/greathammer/eqm/view/resource/v2/orange.wav";

	public static final String RED_WAV = "/com/greathammer/eqm/view/resource/v2/red.wav";

	public static final String COUNT_DOWN_WAV = "/com/greathammer/eqm/view/resource/v2/coutdown.wav";

	protected Bootstrap client = null;

//	private static boolean isNetWorkOk;

	private boolean isShow = false;
//
//	public static int getNetWorkStatus() {
//		return isNetWorkOk ? 0 : 1;
//	}
//
//	public boolean isNetWorkOk() {
//		return isNetWorkOk;
//	}
//
//	public void setNetWorkOk(boolean isNetWorkOk) {
//		BaseJFrame.isNetWorkOk = isNetWorkOk;
//	}

	public boolean isShow() {
		return isShow;
	}

	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}

	public void construction() {
		// TODO 临时关闭全屏，打包前需要打开下面的注释
		this.setUndecorated(true);
		this.pack();
		this.initComponents();
		this.getGraphicsConfiguration().getDevice().setFullScreenWindow(this);

		// this.setExtendedState(JFrame.MAXIMIZED_BOTH);

		final BaseJFrame baseJFrame = this;
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				EqmSerialTool.resetDevice();
				new Toast(baseJFrame, "关闭处理中...", 3000, Toast.success).start();
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					log.error(e);
				}
				System.exit(0);
			}
		});
		this.showNetworkStatus();
		// this.registerConfigKey();
		// this.registerSwitchKey();
		// this.registerLoopTestKey();
	}

	public abstract Bootstrap getClient();

	public abstract void initComponents();

	public abstract BaseJFrame getInstance();

	public abstract JLabel getStatusLabel();

	public void showNetworkStatus() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Calendar cal = Calendar.getInstance();
				int hour = cal.get(Calendar.HOUR_OF_DAY);
				if (hour == 6 || hour == 9 || hour == 12 || hour == 15 || hour == 18 || hour == 21) {
					JLabel statusLabel = getStatusLabel();
					if (null == statusLabel)
						return;
					boolean isNetWorkOk = NetWorkUtil.isNetworkOk();
					if (isNetWorkOk) {
						statusLabel.setText(Constant.COMPANY_NAME + "(网络正常)");
						statusLabel.setForeground(new java.awt.Color(255, 255, 255));
					} else {
						statusLabel.setText(Constant.COMPANY_NAME + "(网络异常)");
						statusLabel.setForeground(new java.awt.Color(255, 0, 0));
					}
				}
			}
		}, 5000, 1000 * 60 * 60 * 1); // 每1小时一次ping网络是否联通
	}

	public void registerSwitchKey() {
		final BaseJFrame baseJFrame = getInstance();

		if (baseJFrame instanceof MainJFrame) {
			log.info("注册可以回到地震界面的ctrl+a快捷键");
			ShortcutManager.getInstance().addShortcutListener(new ShortcutManager.ShortcutListener() {
				public void handle() {
					MainJFrame mainJFrame = getClient().getMainJFrame();
					ScreensaverJFrame screensaverJFrame = getClient().getScreensaverJFrame();
					if (null != mainJFrame && null != screensaverJFrame) {
						mainJFrame.myShow();
						screensaverJFrame.myHide();
						log.info("您按下了ctrl+a，您刚才在屏保界面，现已回到地震界面");
					}
				}
			}, KeyEvent.VK_CONTROL, KeyEvent.VK_A);
		} else if (baseJFrame instanceof ScreensaverJFrame) {
			log.info("注册可以回到屏保界面的ctrl+z快捷键");
			ShortcutManager.getInstance().addShortcutListener(new ShortcutManager.ShortcutListener() {
				public void handle() {
					MainJFrame mainJFrame = getClient().getMainJFrame();
					ScreensaverJFrame screensaverJFrame = getClient().getScreensaverJFrame();
					if (null != screensaverJFrame && null != mainJFrame) {
						screensaverJFrame.myShow();
						mainJFrame.myHide();
						log.info("您按下了ctrl+z，您刚才在地震界面，现已回到屏保界面");
					}
				}
			}, KeyEvent.VK_CONTROL, KeyEvent.VK_Z);
		}

	}

	public void registerConfigKey() {
		final BaseJFrame baseJFrame = getInstance();
		int key = 0;
		if (baseJFrame instanceof MainJFrame) {
			key = KeyEvent.VK_D;
		} else if (baseJFrame instanceof ScreensaverJFrame) {
			key = KeyEvent.VK_C;
		}
		ShortcutManager.getInstance().addShortcutListener(new ShortcutManager.ShortcutListener() {
			public void handle() {
				Constant.cantShow();
				myHide();
				new SettingJFrame();
			}
		}, KeyEvent.VK_CONTROL, key);
	}

	public void registerLoopTestKey() {
		final BaseJFrame baseJFrame = getInstance();
		int key = 0;
		if (baseJFrame instanceof MainJFrame) {
			key = KeyEvent.VK_S;
		} else if (baseJFrame instanceof ScreensaverJFrame) {
			key = KeyEvent.VK_X;
		}
		ShortcutManager.getInstance().addShortcutListener(new ShortcutManager.ShortcutListener() {
			public void handle() {
				if (Constant.IS_LOOP_FOR_TEST) {
					Constant.IS_LOOP_FOR_TEST = false;
					new Toast(baseJFrame, "已禁用循环演示。", 3000, Toast.success).start();
				} else {
					Constant.IS_LOOP_FOR_TEST = true;
					new Toast(baseJFrame, "已启用循环演示。", 3000, Toast.success).start();
				}
			}
		}, KeyEvent.VK_CONTROL, key);
	}

	public void active(JFrame frame) {
		if (frame.getExtendedState() == JFrame.ICONIFIED) {
			frame.setExtendedState(JFrame.NORMAL);
			log.info("jframe normal");
		}
		frame.toFront();
		log.info("窗口已经显示到前台了:" + frame.getName());
	}

	public void myShow() {
		if (Constant.isCanShow()) {
			this.isShow = true;
			this.isUndecorated();
			this.pack();
			this.getGraphicsConfiguration().getDevice().setFullScreenWindow(this);
			this.repaint();
			this.setVisible(true);
			active(this);
			// this.show();
		}
	}

	public void myHide() {
		this.isShow = false;
		this.setVisible(false);
		// this.hide();
	}

	public void mySleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException ex) {
			log.error(ex);
		}
	}

	@SuppressWarnings("deprecation")
	public void killThread(Thread thread) {
		if (null != thread) {
			thread.stop();
			// thread.interrupt();
			// try {
			// thread.join();
			// } catch (InterruptedException e) {
			// log.error(e);
			// }
			thread = null;
		}
	}

	public void showMainJFrame() {
		if (getClient().getMainJFrame() != null && !getClient().getMainJFrame().isShow()) {
			log.info("显示地震界面");
			getClient().getMainJFrame().myShow();
		} else {
			log.info("这两次地震相邻发生，上一个地震界面还没有关闭，不需要再次显示地震界面");
		}

		if (getClient().getImageJFrame() != null && getClient().getImageJFrame().isShow()) {
			log.info("隐藏图片界面");
			getClient().getImageJFrame().myHide();
		} else {
			log.info("图片界面已经关闭着，不需要再次关闭");
		}

		if (getClient().getScreensaverJFrame() != null && getClient().getScreensaverJFrame().isShow()) {
			log.info("隐藏屏保界面");
			getClient().getScreensaverJFrame().myHide();
		} else {
			log.info("屏保界面本来就是隐藏的，不需要再次隐藏");
		}
	}

}
