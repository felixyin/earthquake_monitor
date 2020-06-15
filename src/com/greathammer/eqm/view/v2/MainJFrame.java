package com.greathammer.eqm.view.v2;

import java.awt.Color;
import java.awt.Container;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.greathammer.eqm.earthquake.yujing.EarthquakeYuJing;
import com.greathammer.eqm.earthquake.yujing.EarthquakeYuJingListener;
import com.greathammer.eqm.earthquake.yujing.EarthquakeYuJingManagerForProduction;
import com.greathammer.eqm.earthquake.yujing.EarthquakeYuJingManagerForTest;
import com.greathammer.eqm.earthquake.yujing.IEarthquakeYuJingManager;
import com.greathammer.eqm.util.AudioPlayWaveUtil;
import com.greathammer.eqm.util.Constant;
import com.greathammer.eqm.util.NetWorkUtil;
import com.greathammer.eqm.util.Toast;
import com.greathammer.eqm.view.BaseJFrame;
import com.greathammer.eqm.view.Bootstrap;
import com.greathammer.serial.EqmSerialTool;

/**
 *
 * @author FelixYin
 */
public class MainJFrame extends BaseJFrame {

	private static final long serialVersionUID = 1L;

	private static Log log = LogFactory.getLog(MainJFrame.class);

	private Timer timer;

	private Timer switchScreensaveTimer;

	private IEarthquakeYuJingManager manager = null;

	public IEarthquakeYuJingManager getManager() {
		return manager;
	}

	public void setManager(IEarthquakeYuJingManager manager) {
		this.manager = manager;
	}

	public MainJFrame() {
		super.construction();
		this.bindDataSource();
	}

	public MainJFrame(Bootstrap client) {
		log.info("-->开始创建地震预警界面");

		try {
			super.construction();

			Container c = this.getContentPane();
			c.setBackground(Color.BLACK);
			this.client = client;
			this.bindDataSource();
			this.manager.start();
		} catch (Exception e) {
			log.error(e);
		}

		boolean isNetOk = NetWorkUtil.isNetworkOk();
		if (Constant.isTestMode()) {// 演示版
			if (isNetOk) {
				new Toast(this, "青岛科信提示您：您运行的是演示版本。", 5000, Toast.msg).start();
			} else {
				new Toast(this, "青岛科信提示您：您运行的是演示版本；系统网络异常。", 5000, Toast.error).start();
			}
		} else {
			if (!isNetOk) {
				new Toast(this, "青岛科信提示您：系统网络异常。", 5000, Toast.error).start();
			}
		}

		log.info("-->已显示地震预警界面");
	}

	public void bindDataSource() {
		log.info("开始绑定地震数据源");
		testLabel.setText("");

		if (Constant.isProductionMode()) {// 正式版
			log.info("您运行的是正式版本");
			manager = new EarthquakeYuJingManagerForProduction();
		} else if (Constant.isTestMode()) {// 演示版
			log.info("您运行的是演示版本");
			manager = new EarthquakeYuJingManagerForTest();
			testLabel.setText("您运行的是演示版本");
		}

		log.info("已经启用版本:" + manager.getClass().getSimpleName());

		manager.setListener(new EarthquakeYuJingListener() {

			@Override
			public void doEvent(EarthquakeYuJing event, String text) {
				log.info("接收到新的地震消息：" + event.toString());

				Constant.setEARTHQUAKING(true);

				if (switchScreensaveTimer != null) {
					switchScreensaveTimer.cancel();
				}
				switchScreensaveTimer = new Timer();

				if (timer != null) {
					timer.cancel();
				}
				timer = new Timer();

				earthquakeYuJing1.change(event);

				serialDevice();

				showCountDown();

				flashLight();

				playAudio();

				// switchScreensaver();

				showMainJFrame();
			}

		});

		log.info("已经绑定地震数据源");
	}

	private void serialDevice() {
		EqmSerialTool.showEQM(earthquakeYuJing1.getLight(), earthquakeYuJing1.getArrivalTime(),
				NetWorkUtil.getNetWorkStatus(), earthquakeYuJing1.getIntensity());
		log.info("-------------------------------发送给硬件相关信息，用于报警");
	}

	// private void switchScreensaver() {
	// timer.schedule(new TimerTask() {
	// @Override
	// public void run() {
	// log.info("已经过了" + Constant.SWITCH_SCREENSAVER_TIME_AFTER_EQM +
	// "毫秒时间过后没有收到地震预警,则自动关闭地震界面，并显示屏保界面");
	//
	// getClient().getScreensaverJFrame().myShow();
	// getClient().getMainJFrame().myHide();
	//
	// Constant.setEARTHQUAKING(false);
	// }
	// }, Constant.SWITCH_SCREENSAVER_TIME_AFTER_EQM /*+
	// (earthquakeYuJing1.getArrivalTime() * 1000)*/);
	// }

	private void showCountDown() {

		timer.scheduleAtFixedRate(new TimerTask() {
			int period = earthquakeYuJing1.getArrivalTime();

			@Override
			public void run() {
				if (period == 0) {
					mySleep(5000);
					timer.cancel();

					resetLight();
					log.info("倒计时结束，重置所有灯为灰色");

					EqmSerialTool.showDateTime();
					log.info("让终端显示时间");

					// log.error("==========>" + Constant.isProductionMode() +
					// "," + Constant.isRealEarthquake());
					// if (Constant.isProductionMode() &&
					// Constant.isRealEarthquake()) {
					// switchScreensaveTimer.schedule(new TimerTask() {
					// @Override
					// public void run() {
					// Constant.setEARTHQUAKING(false);
					// getClient().getImageJFrame().myHide();
					// getClient().getMainJFrame().myHide();
					// getClient().getScreensaverJFrame().myShow();
					// log.info("已经过了" +
					// Constant.SWITCH_SCREENSAVER_TIME_AFTER_EQM
					// + "毫秒时间过后没有收到地震预警,则自动关闭地震界面，并显示屏保界面");
					// }
					// }, Constant.EQMQ_AFTER_PRODUCTION_TIME);
					// } else {
					mySleep(Constant.SWITCH_SCREENSAVER_TIME_AFTER_EQM);
					Constant.setEARTHQUAKING(false);
					getClient().getImageJFrame().myHide();
					getClient().getMainJFrame().myHide();
					getClient().getScreensaverJFrame().myShow();
					log.info("已经过了" + Constant.SWITCH_SCREENSAVER_TIME_AFTER_EQM + "毫秒时间过后没有收到地震预警,则自动关闭地震界面，并显示屏保界面");
					// }

				} else {

					earthquakeYuJing1.setArrivalTime(--period);
					if (period == 10 && Constant.IS_PLAY_COUNT_DOWN == 1) {
						new AudioPlayWaveUtil(COUNT_DOWN_WAV).start();
						log.info("最后10秒开始播放倒计时音效");
					}
				}
			}
		}, 0, 1000);
	}

	private void playAudio() {
		if (earthquakeYuJing1.getIntensity() >= Constant.AUDIO_WAV_INTENSITY) {
			log.info("因计算后的烈度（" + earthquakeYuJing1.getIntensity() + "） >= 配置的烈度（" + Constant.AUDIO_WAV_INTENSITY
					+ "）报警音效阈值，所以开始播放音效" + Constant.AUDIO_WAV_COUNT + "次");
			String light = earthquakeYuJing1.getLight();
			timer.scheduleAtFixedRate(new TimerTask() {
				@Override
				public void run() {
					getWav(light).start();
				}
			}, 0, 1000 * 10);
		}
	}

	private AudioPlayWaveUtil getWav(String light) {
		// String audioPlayWav = "";
		// switch (light) {
		// case "red":
		// audioPlayWav = RED_WAV;
		// break;
		// case "orange":
		// audioPlayWav = ORANGE_WAV;
		// break;
		// case "yellow":
		// audioPlayWav = YELLOW_WAV;
		// break;
		// case "blue":
		// audioPlayWav = BLUE_WAV;
		// break;
		// default:
		// }
		// return new AudioPlayWaveUtil(audioPlayWav);
		return new AudioPlayWaveUtil(YELLOW_WAV);
	}

	private void flashLight() {
		resetLight();

		String lightGif = "";
		String light = earthquakeYuJing1.getLight();
		JLabel lightBtn = null;
		log.info("亮灯是：" + light);

		switch (light) {
		case "red":
			lightGif = RED_GIF;
			lightBtn = lightRed;
			break;
		case "orange":
			lightGif = ORANGE_GIF;
			lightBtn = lightOrange;
			break;
		case "yellow":
			lightGif = YELLOW_GIF;
			lightBtn = lightYellow;
			break;
		case "blue":
			lightGif = BLUE_GIF;
			lightBtn = lightBlue;
			break;
		default:
		}

		lightBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource(lightGif)));
	}

	private void resetLight() {
		lightRed.setIcon(new javax.swing.ImageIcon(getClass().getResource(RED_JPG)));
		lightOrange.setIcon(new javax.swing.ImageIcon(getClass().getResource(ORANGE_JPG)));
		lightYellow.setIcon(new javax.swing.ImageIcon(getClass().getResource(YELLOW_JPG)));
		lightBlue.setIcon(new javax.swing.ImageIcon(getClass().getResource(BLUE_JPG)));
	}

	@Override
	public void initComponents() {
		bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

		earthquakeYuJing1 = new com.greathammer.eqm.earthquake.yujing.EarthquakeYuJing();
		container = new javax.swing.JPanel();
		title = new javax.swing.JLabel();
		localIntensityLabel = new javax.swing.JLabel();
		arrivalTimeLable = new javax.swing.JLabel();
		localIntensity = new javax.swing.JLabel();
		duLabel = new javax.swing.JLabel();
		arrivalTime = new javax.swing.JLabel();
		miaoLabel = new javax.swing.JLabel();
		adviceLabel = new javax.swing.JLabel();
		advice = new javax.swing.JLabel();
		otimeLabel = new javax.swing.JLabel();
		locationLabel = new javax.swing.JLabel();
		magitudeLabel = new javax.swing.JLabel();
		otime = new javax.swing.JLabel();
		location = new javax.swing.JLabel();
		magitude = new javax.swing.JLabel();
		levelLabel = new javax.swing.JLabel();
		distanceLabel = new javax.swing.JLabel();
		distance = new javax.swing.JLabel();
		kmLabel = new javax.swing.JLabel();
		statusLabel = new javax.swing.JLabel();
		copyright = new javax.swing.JLabel();
		lightBlue = new javax.swing.JLabel();
		lightYellow = new javax.swing.JLabel();
		lightOrange = new javax.swing.JLabel();
		lightRed = new javax.swing.JLabel();
		testLabel = new javax.swing.JLabel();
		bg = new javax.swing.JLabel();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setSize(new java.awt.Dimension(1920, 1080));

		container.setBackground(new java.awt.Color(0, 0, 0));
		container.setFocusable(false);
		container.setOpaque(false);
		container.setPreferredSize(new java.awt.Dimension(1920, 1080));

		title.setFont(new java.awt.Font("黑体", 1, 70)); // NOI18N
		title.setForeground(new java.awt.Color(255, 255, 255));
		title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		title.setText("地 震 预 警 信 息 发 布");
		title.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

		localIntensityLabel.setFont(new java.awt.Font("黑体", 0, 44)); // NOI18N
		localIntensityLabel.setForeground(new java.awt.Color(255, 255, 255));
		localIntensityLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		localIntensityLabel.setText("预估影响本地烈度");

		arrivalTimeLable.setFont(new java.awt.Font("黑体", 0, 44)); // NOI18N
		arrivalTimeLable.setForeground(new java.awt.Color(255, 255, 255));
		arrivalTimeLable.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		arrivalTimeLable.setText("预计到达本地时间");

		localIntensity.setFont(new java.awt.Font("黑体", 1, 130)); // NOI18N
		localIntensity.setForeground(new java.awt.Color(255, 255, 51));
		localIntensity.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

		@SuppressWarnings("rawtypes")
		org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
				org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, earthquakeYuJing1,
				org.jdesktop.beansbinding.ELProperty.create("${intensity}"), localIntensity,
				org.jdesktop.beansbinding.BeanProperty.create("text"));
		bindingGroup.addBinding(binding);

		duLabel.setFont(new java.awt.Font("黑体", 0, 80)); // NOI18N
		duLabel.setForeground(new java.awt.Color(255, 255, 51));
		duLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		duLabel.setText("度");

		arrivalTime.setFont(new java.awt.Font("黑体", 1, 130)); // NOI18N
		arrivalTime.setForeground(new java.awt.Color(255, 255, 51));
		arrivalTime.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

		binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
				org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, earthquakeYuJing1,
				org.jdesktop.beansbinding.ELProperty.create("${arrivalTime}"), arrivalTime,
				org.jdesktop.beansbinding.BeanProperty.create("text"));
		bindingGroup.addBinding(binding);

		miaoLabel.setFont(new java.awt.Font("黑体", 0, 80)); // NOI18N
		miaoLabel.setForeground(new java.awt.Color(255, 255, 51));
		miaoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		miaoLabel.setText("秒");

		adviceLabel.setFont(new java.awt.Font("黑体", 0, 44)); // NOI18N
		adviceLabel.setForeground(new java.awt.Color(255, 255, 255));
		adviceLabel.setText("避震指导：");

		advice.setFont(new java.awt.Font("黑体", 0, 88)); // NOI18N
		advice.setForeground(new java.awt.Color(255, 255, 51));

		binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
				org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, earthquakeYuJing1,
				org.jdesktop.beansbinding.ELProperty.create("${advice}"), advice,
				org.jdesktop.beansbinding.BeanProperty.create("text"));
		bindingGroup.addBinding(binding);

		otimeLabel.setFont(new java.awt.Font("黑体", 0, 44)); // NOI18N
		otimeLabel.setForeground(new java.awt.Color(255, 0, 0));
		otimeLabel.setText("发震时间：");

		locationLabel.setFont(new java.awt.Font("黑体", 0, 44)); // NOI18N
		locationLabel.setForeground(new java.awt.Color(255, 0, 0));
		locationLabel.setText("震中位置：");

		magitudeLabel.setFont(new java.awt.Font("黑体", 0, 44)); // NOI18N
		magitudeLabel.setForeground(new java.awt.Color(255, 0, 0));
		magitudeLabel.setText("震    级：");

		otime.setFont(new java.awt.Font("黑体", 0, 44)); // NOI18N
		otime.setForeground(new java.awt.Color(255, 255, 51));

		binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
				org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, earthquakeYuJing1,
				org.jdesktop.beansbinding.ELProperty.create("${otime}"), otime,
				org.jdesktop.beansbinding.BeanProperty.create("text"));
		bindingGroup.addBinding(binding);

		location.setFont(new java.awt.Font("黑体", 0, 44)); // NOI18N
		location.setForeground(new java.awt.Color(255, 255, 51));

		binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
				org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, earthquakeYuJing1,
				org.jdesktop.beansbinding.ELProperty.create("${location}"), location,
				org.jdesktop.beansbinding.BeanProperty.create("text"));
		bindingGroup.addBinding(binding);

		magitude.setFont(new java.awt.Font("黑体", 0, 44)); // NOI18N
		magitude.setForeground(new java.awt.Color(255, 255, 51));

		binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
				org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, earthquakeYuJing1,
				org.jdesktop.beansbinding.ELProperty.create("${magitude}"), magitude,
				org.jdesktop.beansbinding.BeanProperty.create("text"));
		bindingGroup.addBinding(binding);

		levelLabel.setFont(new java.awt.Font("黑体", 0, 44)); // NOI18N
		levelLabel.setForeground(new java.awt.Color(255, 255, 51));
		levelLabel.setText("级");

		distanceLabel.setFont(new java.awt.Font("黑体", 0, 44)); // NOI18N
		distanceLabel.setForeground(new java.awt.Color(255, 0, 0));
		distanceLabel.setText("距离本地：");

		distance.setFont(new java.awt.Font("黑体", 0, 44)); // NOI18N
		distance.setForeground(new java.awt.Color(255, 255, 51));
		distance.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

		binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
				org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, earthquakeYuJing1,
				org.jdesktop.beansbinding.ELProperty.create("${distance}"), distance,
				org.jdesktop.beansbinding.BeanProperty.create("text"));
		bindingGroup.addBinding(binding);

		kmLabel.setFont(new java.awt.Font("黑体", 0, 44)); // NOI18N
		kmLabel.setForeground(new java.awt.Color(255, 255, 51));
		kmLabel.setText("公里");

		statusLabel.setFont(new java.awt.Font("黑体", 0, 36)); // NOI18N
		statusLabel.setForeground(new java.awt.Color(255, 255, 255));
		statusLabel.setText("  青岛市地震局");

		copyright.setFont(new java.awt.Font("黑体", 0, 36)); // NOI18N
		copyright.setForeground(new java.awt.Color(255, 255, 255));
		copyright.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		copyright.setText("技术支持：青岛科信安全技术有限公司");

		lightBlue.setIcon(
				new javax.swing.ImageIcon(getClass().getResource("/com/greathammer/eqm/view/resource/v2/blue.jpg"))); // NOI18N
		lightBlue.setText("jLabel2");

		lightYellow.setIcon(
				new javax.swing.ImageIcon(getClass().getResource("/com/greathammer/eqm/view/resource/v2/yellow.jpg"))); // NOI18N
		lightYellow.setText("jLabel2");

		lightOrange.setIcon(
				new javax.swing.ImageIcon(getClass().getResource("/com/greathammer/eqm/view/resource/v2/orange.jpg"))); // NOI18N
		lightOrange.setText("jLabel2");

		lightRed.setIcon(
				new javax.swing.ImageIcon(getClass().getResource("/com/greathammer/eqm/view/resource/v2/red.jpg"))); // NOI18N
		lightRed.setText("jLabel2");

		testLabel.setFont(new java.awt.Font("Lucida Grande", 0, 36)); // NOI18N
		testLabel.setForeground(new java.awt.Color(255, 0, 0));

		javax.swing.GroupLayout containerLayout = new javax.swing.GroupLayout(container);
		container.setLayout(containerLayout);
		containerLayout
				.setHorizontalGroup(containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(containerLayout.createSequentialGroup().addGap(66, 66, 66)
								.addComponent(statusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 693,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(59, 59, 59)
								.addComponent(testLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 340,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(
										copyright, javax.swing.GroupLayout.PREFERRED_SIZE, 668,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(59, 59,
										59))
						.addGroup(containerLayout.createSequentialGroup().addGroup(containerLayout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(containerLayout
										.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
										.addGroup(containerLayout.createSequentialGroup().addGap(322, 322, 322)
												.addComponent(adviceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 220,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(advice, javax.swing.GroupLayout.PREFERRED_SIZE, 1206,
														javax.swing.GroupLayout.PREFERRED_SIZE))
										.addGroup(containerLayout.createSequentialGroup().addGap(45, 45, 45)
												.addGroup(containerLayout
														.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(localIntensityLabel,
																javax.swing.GroupLayout.PREFERRED_SIZE, 910,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addGroup(containerLayout.createSequentialGroup()
																.addGap(262, 262, 262).addComponent(localIntensity,
																		javax.swing.GroupLayout.PREFERRED_SIZE, 182,
																		javax.swing.GroupLayout.PREFERRED_SIZE)
																.addGap(34, 34, 34).addComponent(duLabel,
																		javax.swing.GroupLayout.PREFERRED_SIZE, 119,
																		javax.swing.GroupLayout.PREFERRED_SIZE)))
												.addGroup(containerLayout
														.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
																containerLayout.createSequentialGroup()
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED,
																				512, Short.MAX_VALUE)
																		.addComponent(miaoLabel,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				130,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addGap(305, 305, 305))
														.addGroup(containerLayout.createSequentialGroup()
																.addGroup(containerLayout
																		.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.LEADING)
																		.addGroup(
																				containerLayout.createSequentialGroup()
																						.addPreferredGap(
																								javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																						.addComponent(arrivalTimeLable,
																								javax.swing.GroupLayout.PREFERRED_SIZE,
																								909,
																								javax.swing.GroupLayout.PREFERRED_SIZE))
																		.addGroup(
																				containerLayout.createSequentialGroup()
																						.addGap(217, 217, 217)
																						.addComponent(arrivalTime,
																								javax.swing.GroupLayout.PREFERRED_SIZE,
																								265,
																								javax.swing.GroupLayout.PREFERRED_SIZE)))
																.addGap(0, 0, Short.MAX_VALUE)))))
								.addGroup(containerLayout.createSequentialGroup().addGap(196, 196, 196)
										.addComponent(lightBlue, javax.swing.GroupLayout.PREFERRED_SIZE, 232,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(200, 200, 200)
										.addComponent(lightYellow, javax.swing.GroupLayout.PREFERRED_SIZE, 232,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(200, 200, 200)
										.addComponent(lightOrange, javax.swing.GroupLayout.PREFERRED_SIZE, 232,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(200, 200, 200).addComponent(lightRed,
												javax.swing.GroupLayout.PREFERRED_SIZE, 232,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGroup(containerLayout.createSequentialGroup().addGap(456, 456, 456).addComponent(
										title, javax.swing.GroupLayout.PREFERRED_SIZE, 1005,
										javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGroup(containerLayout.createSequentialGroup().addGap(118, 118, 118)
										.addGroup(containerLayout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
												.addComponent(otimeLabel, javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(locationLabel, javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(magitudeLabel, javax.swing.GroupLayout.PREFERRED_SIZE,
														233, javax.swing.GroupLayout.PREFERRED_SIZE))
										.addGap(36, 36, 36)
										.addGroup(containerLayout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
												.addComponent(otime, javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(location, javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addGroup(containerLayout.createSequentialGroup()
														.addComponent(magitude, javax.swing.GroupLayout.PREFERRED_SIZE,
																109, javax.swing.GroupLayout.PREFERRED_SIZE)
														.addPreferredGap(
																javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
														.addComponent(levelLabel,
																javax.swing.GroupLayout.PREFERRED_SIZE, 89,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addGap(476, 476, 476)
														.addComponent(distanceLabel,
																javax.swing.GroupLayout.PREFERRED_SIZE, 220,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addPreferredGap(
																javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
														.addComponent(distance)
														.addPreferredGap(
																javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
														.addComponent(kmLabel, javax.swing.GroupLayout.PREFERRED_SIZE,
																97, javax.swing.GroupLayout.PREFERRED_SIZE)))))
								.addContainerGap(18, Short.MAX_VALUE)));
		containerLayout.setVerticalGroup(containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(containerLayout.createSequentialGroup().addContainerGap()
						.addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 85,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(containerLayout.createSequentialGroup().addComponent(lightBlue).addGap(0, 1,
										Short.MAX_VALUE))
								.addComponent(lightOrange, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lightRed, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lightYellow, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								.addComponent(arrivalTimeLable, javax.swing.GroupLayout.DEFAULT_SIZE, 85,
										Short.MAX_VALUE)
								.addComponent(localIntensityLabel, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
										containerLayout.createSequentialGroup()
												.addComponent(arrivalTime, javax.swing.GroupLayout.PREFERRED_SIZE, 105,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addGap(15, 15, 15))
								.addGroup(containerLayout.createSequentialGroup().addGroup(containerLayout
										.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(localIntensity, javax.swing.GroupLayout.PREFERRED_SIZE, 113,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGroup(containerLayout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
												.addComponent(miaoLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 119,
														Short.MAX_VALUE)
												.addComponent(duLabel, javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
						.addGroup(containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(adviceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 116,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(advice, javax.swing.GroupLayout.PREFERRED_SIZE, 111,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(27, 27, 27)
						.addGroup(containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								.addComponent(otime, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(otimeLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 72,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								.addComponent(locationLabel, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(location, javax.swing.GroupLayout.PREFERRED_SIZE, 72,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
								.addComponent(distance, javax.swing.GroupLayout.Alignment.LEADING,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(distanceLabel, javax.swing.GroupLayout.Alignment.LEADING,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(levelLabel, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(magitude, javax.swing.GroupLayout.Alignment.LEADING,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(magitudeLabel, javax.swing.GroupLayout.Alignment.LEADING,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(kmLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 72,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(29, 29, 29)
						.addGroup(containerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								.addComponent(testLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 77, Short.MAX_VALUE)
								.addGroup(containerLayout
										.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
										.addComponent(copyright, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(statusLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 77,
												Short.MAX_VALUE)))
						.addGap(36, 36, 36)));

		bg.setBackground(new java.awt.Color(0, 0, 0));
		bg.setIcon(
				new javax.swing.ImageIcon(getClass().getResource("/com/greathammer/eqm/view/resource/background.jpg"))); // NOI18N
		bg.setAlignmentY(0.0F);
		bg.setFocusable(false);
		bg.setOpaque(true);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
						.addComponent(container, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(0, 0, Short.MAX_VALUE))
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup().addGap(0, 0, Short.MAX_VALUE).addComponent(bg)
								.addGap(0, 0, Short.MAX_VALUE))));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(container, javax.swing.GroupLayout.DEFAULT_SIZE, 1082, Short.MAX_VALUE)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup().addGap(0, 0, Short.MAX_VALUE).addComponent(bg)
								.addGap(0, 0, Short.MAX_VALUE))));

		bindingGroup.bind();

		pack();
	}

	public static void main(String args[]) {
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		}

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new MainJFrame().setVisible(true);
			}
		});
	}

	private javax.swing.JLabel advice;
	private javax.swing.JLabel adviceLabel;
	private javax.swing.JLabel arrivalTime;
	private javax.swing.JLabel arrivalTimeLable;
	private javax.swing.JLabel bg;
	private javax.swing.JPanel container;
	private javax.swing.JLabel copyright;
	private javax.swing.JLabel distance;
	private javax.swing.JLabel distanceLabel;
	private javax.swing.JLabel duLabel;
	private com.greathammer.eqm.earthquake.yujing.EarthquakeYuJing earthquakeYuJing1;
	private javax.swing.JLabel kmLabel;
	private javax.swing.JLabel levelLabel;
	private javax.swing.JLabel lightBlue;
	private javax.swing.JLabel lightOrange;
	private javax.swing.JLabel lightRed;
	private javax.swing.JLabel lightYellow;
	private javax.swing.JLabel localIntensity;
	private javax.swing.JLabel localIntensityLabel;
	private javax.swing.JLabel location;
	private javax.swing.JLabel locationLabel;
	private javax.swing.JLabel magitude;
	private javax.swing.JLabel magitudeLabel;
	private javax.swing.JLabel miaoLabel;
	private javax.swing.JLabel otime;
	private javax.swing.JLabel otimeLabel;
	private javax.swing.JLabel statusLabel;
	private javax.swing.JLabel testLabel;
	private javax.swing.JLabel title;
	private org.jdesktop.beansbinding.BindingGroup bindingGroup;

	@Override
	public Bootstrap getClient() {
		return client;
	}

	@Override
	public BaseJFrame getInstance() {
		return this;
	}

	@Override
	public JLabel getStatusLabel() {
		return statusLabel;
	}

	public EarthquakeYuJing getEarthquakeYuJing() {
		return this.earthquakeYuJing1;
	}

}
