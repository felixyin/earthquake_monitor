package com.greathammer.eqm.view.v2;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Image;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.greathammer.eqm.custom.CustomListener;
import com.greathammer.eqm.custom.CustomManager;
import com.greathammer.eqm.util.Constant;
import com.greathammer.eqm.view.BaseJFrame;
import com.greathammer.eqm.view.Bootstrap;

/**
 * 图片显示窗口
 *
 * @author FelixYin
 *
 */
public class ImageJFrame extends BaseJFrame {

	private static Log log = LogFactory.getLog(ImageJFrame.class);

	private static final long serialVersionUID = 1L;

	private Bootstrap bootstrap;

	private Image fullScreenImage;
	private final JLabel lbl = new JLabel();
	private Thread thread;

	public void bindDataSource() {
		log.info("绑定图片显示数据源");
		CustomManager cm = new CustomManager();
		cm.setListener(new CustomListener() {
			@Override
			public void doEvent(InputStream is) {
				try {
					if (Constant.isEARTHQUAKING()) {
						log.info("地震正在进行中，所有操作都必须以地震为主");
						return;
					}

					byte[] imageBytes = IOUtils.toByteArray(is);
					log.info("图片监听接收到数据和指令:" + imageBytes.length);

					if (imageBytes.length == 10) { // 指令
						String cmd = new String(imageBytes);
						log.info("接收到的指令是：" + cmd);

						if (cmd.contains("close")) {
							// if (getClient().getImageJFrame().isShow()) {
							getClient().getImageJFrame().myHide();
							log.info("隐藏当前图片显示窗口");
							// }
							getClient().getMainJFrame().myHide();
							getClient().getScreensaverJFrame().myShow();
							log.info("---------->图片窗口已经关闭");
						}

					} else { // 显示图片

						// fullScreenImage = ImageIO.read();
						// if (!thiz.isShowing()) {
						lbl.setIcon(new ImageIcon(imageBytes));
						getClient().getMainJFrame().myHide();
						getClient().getScreensaverJFrame().myHide();
						getClient().getImageJFrame().myShow();
						// }
						log.info("---------->图片窗口已显示");

					}
				} catch (Exception ex) {
					Logger.getLogger(ImageJFrame.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		});
		thread = new Thread(cm, "显示图片到屏幕的线程");
		thread.start();
	}

	public ImageJFrame(Bootstrap bootstrap) {
		this.bootstrap = bootstrap;
		super.construction();
		this.initComponents();
	}

	@Override
	public Bootstrap getClient() {
		return bootstrap;
	}

	@Override
	public void initComponents() {

		// this.setBounds(640, 340, 640, 400);
		// this.setExtendedState(JFrame.MAXIMIZED_BOTH);

		// this.isUndecorated();
		// this.setUndecorated(true);
		// this.setBackground(Color.BLACK);
		// this.pack();
		// this.getGraphicsConfiguration().getDevice().setFullScreenWindow(this);

		Container c = this.getContentPane();
		c.setBackground(Color.GRAY);
		c.setLayout(new FlowLayout());
		c.add(lbl);
		if (null != fullScreenImage) {
			lbl.setIcon(new ImageIcon(fullScreenImage));
		}

		this.bindDataSource();
		this.setVisible(false);

		// new Thread() {
		// @Override
		// public void run() {
		// try {
		// Thread.sleep(10 * 1000);
		// } catch (InterruptedException ex) {
		// Logger.getLogger(ImageJFrame.class.getName()).log(Level.SEVERE, null,
		// ex);
		// }
		//// thread.stop();
		// dispose();
		// }
		//
		// }.start();
	}

	@Override
	public BaseJFrame getInstance() {
		return this;
	}

	@Override
	public JLabel getStatusLabel() {
		return null;
	}

}
