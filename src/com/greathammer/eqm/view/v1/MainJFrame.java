package com.greathammer.eqm.view.v1;

import javax.swing.JButton;
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

	/*
	 * public MainJFrame() { super.construction(); this.bindDataSource();
	 * this.showNetworkStatus(); }
	 */
	public MainJFrame(Bootstrap client) {
		log.info("-->开始创建地震预警界面");

		this.client = client;
		super.construction();
		this.bindDataSource();

		log.info("-->已显示地震预警界面");
	}

	public void bindDataSource() {
		log.info("开始绑定地震数据源");

		IEarthquakeYuJingManager manager = null;

		if (Constant.IS_PRODUCTION_VERSION == 1) {// 正式版

			log.info("您运行的是正式正式版本");
			manager = new EarthquakeYuJingManagerForProduction();
		} else if (Constant.IS_PRODUCTION_VERSION == 0) {// 演示版

			new Toast(this, "青岛科信提示您：您运行的是演示版本。", 5000, Toast.error).start();
			log.info("您运行的是演示版本");
			manager = new EarthquakeYuJingManagerForTest();
		}

		log.info("已经启用版本:" + manager.getClass().getSimpleName());

		manager.setListener(new EarthquakeYuJingListener() {

			@Override
			public void doEvent(EarthquakeYuJing event, String text) {

				log.info("接收到新的地震消息：" + event.toString());
				earthquakeYuJing1.change(event);

				serialDevice();

				showCountDown();

				flashLight();

				playAudio();

				switchScreensaver();

				showMainJFrame();
			}

		}).start();

		log.info("已经绑定地震数据源");
	}

	private void serialDevice() {
		EqmSerialTool.showEQM(earthquakeYuJing1.getLight(), earthquakeYuJing1.getArrivalTime(),
				NetWorkUtil.getNetWorkStatus(), earthquakeYuJing1.getIntensity());
	}

	private Thread switchScreensaverThread = null;

	private void switchScreensaver() {
		killThread(switchScreensaverThread);

		switchScreensaverThread = new Thread() {
			@Override
			public void run() {
				mySleep(Constant.SWITCH_SCREENSAVER_TIME_AFTER_EQM);
				log.info("已经过了" + Constant.SWITCH_SCREENSAVER_TIME_AFTER_EQM + "毫秒时间过后没有收到地震预警,则自动关闭地震界面，并显示屏保界面");

				Bootstrap bootstrap = getClient();
				bootstrap.getMainJFrame().myHide();
				bootstrap.getScreensaverJFrame().myShow();

				EqmSerialTool.resetDevice();
			}
		};

		switchScreensaverThread.start();
	}

	private Thread showCountDownThread = null;

	private void showCountDown() {

		killThread(showCountDownThread);

		showCountDownThread = new Thread() {
			@Override
			public void run() {

				log.info("开始" + earthquakeYuJing1.getArrivalTime() + "秒倒计时的更新显示：");
				for (int i = earthquakeYuJing1.getArrivalTime(); i > 0;) {
					mySleep(1000);
					earthquakeYuJing1.setArrivalTime(--i);

					playCountDownWav(i);

					if (i == 0) {
						mySleep(1000 * 5);

						log.info("倒计时结束，重置所有灯为灰色");
						resetLight();

						log.info("让终端显示时间");
						EqmSerialTool.resetDevice();

						killThread(playAudioThread);
					}
				}
			}

			private void playCountDownWav(int i) {
				if (i == 10 && Constant.IS_PLAY_COUNT_DOWN == 1) {
					log.info("最后10秒开始播放倒计时音效");
					new AudioPlayWaveUtil(COUNT_DOWN_WAV).start();
				}
			}
		};

		showCountDownThread.start();
	}

	private Thread playAudioThread = null;

	private void playAudio() {
		killThread(playAudioThread);

		playAudioThread = new Thread() {

			@Override
			public void run() {
				if (earthquakeYuJing1.getIntensity() >= Constant.AUDIO_WAV_INTENSITY) {
					log.info("因计算后的烈度（" + earthquakeYuJing1.getIntensity() + "） >= 配置的烈度（"
							+ Constant.AUDIO_WAV_INTENSITY + "）报警音效阈值，所以开始播放音效" + Constant.AUDIO_WAV_COUNT + "次");

					String light = earthquakeYuJing1.getLight();
					for (int i = 0; i < Constant.AUDIO_WAV_COUNT; i++) {
						getWav(light).start();
						mySleep(1000 * 10);
					}
				}
			}

		};

		playAudioThread.start();
	}

	private AudioPlayWaveUtil getWav(String light) {
		String audioPlayWav = "";
		switch (light) {
		case "red":
			audioPlayWav = RED_WAV;
			break;
		case "orange":
			audioPlayWav = ORANGE_WAV;
			break;
		case "yellow":
			audioPlayWav = YELLOW_WAV;
			break;
		case "blue":
			audioPlayWav = BLUE_WAV;
			break;
		default:
		}
		return new AudioPlayWaveUtil(audioPlayWav);
	}

	private void flashLight() {
		resetLight();

		String lightGif = "";
		String light = earthquakeYuJing1.getLight();
		JButton lightBtn = null;
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
		lightRed.setIcon(new javax.swing.ImageIcon(getClass().getResource(ORIGINAL_PNG)));
		lightOrange.setIcon(new javax.swing.ImageIcon(getClass().getResource(ORIGINAL_PNG)));
		lightYellow.setIcon(new javax.swing.ImageIcon(getClass().getResource(ORIGINAL_PNG)));
		lightBlue.setIcon(new javax.swing.ImageIcon(getClass().getResource(ORIGINAL_PNG)));
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void initComponents() {
		bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

		jButton1 = new javax.swing.JButton();
		jButton2 = new javax.swing.JButton();
		jButton3 = new javax.swing.JButton();
		jButton4 = new javax.swing.JButton();
		earthquakeYuJing1 = new com.greathammer.eqm.earthquake.yujing.EarthquakeYuJing();
		jPanel2 = new javax.swing.JPanel();
		jPanel1 = new javax.swing.JPanel();
		jPanel5 = new javax.swing.JPanel();
		lightRed = new javax.swing.JButton();
		jPanel6 = new javax.swing.JPanel();
		lightOrange = new javax.swing.JButton();
		jPanel7 = new javax.swing.JPanel();
		lightYellow = new javax.swing.JButton();
		jPanel8 = new javax.swing.JPanel();
		lightBlue = new javax.swing.JButton();
		jPanel3 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		jLabel5 = new javax.swing.JLabel();
		jLabel6 = new javax.swing.JLabel();
		jLabel7 = new javax.swing.JLabel();
		otime = new javax.swing.JLabel();
		magitude = new javax.swing.JLabel();
		location = new javax.swing.JLabel();
		distance = new javax.swing.JLabel();
		intensity = new javax.swing.JLabel();
		jLabel9 = new javax.swing.JLabel();
		jLabel10 = new javax.swing.JLabel();
		jLabel11 = new javax.swing.JLabel();
		jPanel4 = new javax.swing.JPanel();
		jPanel10 = new javax.swing.JPanel();
		jPanel11 = new javax.swing.JPanel();
		jPanel12 = new javax.swing.JPanel();
		jLabel3 = new javax.swing.JLabel();
		jLabel8 = new javax.swing.JLabel();
		jPanel13 = new javax.swing.JPanel();
		eqDesc = new javax.swing.JLabel();
		advice = new javax.swing.JLabel();
		arrivalTime = new javax.swing.JLabel();
		jPanel14 = new javax.swing.JPanel();
		jLabel20 = new javax.swing.JLabel();
		jLabel21 = new javax.swing.JLabel();
		jLabel22 = new javax.swing.JLabel();

		jButton1.setBackground(new java.awt.Color(255, 51, 51));
		jButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

		jButton2.setBackground(new java.awt.Color(255, 153, 0));

		jButton3.setBackground(new java.awt.Color(255, 255, 153));

		jButton4.setBackground(new java.awt.Color(51, 153, 255));

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setBackground(new java.awt.Color(0, 0, 0));

		jPanel2.setBackground(new java.awt.Color(0, 0, 0));
		jPanel2.setMaximumSize(null);
		jPanel2.setPreferredSize(new java.awt.Dimension(1484, 861));

		jPanel1.setBackground(new java.awt.Color(0, 0, 0));
		jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));

		jPanel5.setBackground(new java.awt.Color(0, 0, 0));

		lightRed.setBackground(new java.awt.Color(0, 0, 0));
		lightRed.setIcon(
				new javax.swing.ImageIcon(getClass().getResource("/com/greathammer/eqm/view/resource/original.png"))); // NOI18N
		lightRed.setBorder(null);
		lightRed.setBorderPainted(false);
		lightRed.setName("light_red"); // NOI18N

		javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
		jPanel5.setLayout(jPanel5Layout);
		jPanel5Layout.setHorizontalGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel5Layout.createSequentialGroup().addContainerGap().addComponent(lightRed,
						javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addContainerGap()));
		jPanel5Layout.setVerticalGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel5Layout.createSequentialGroup().addContainerGap().addComponent(lightRed,
						javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addContainerGap()));

		jPanel6.setBackground(new java.awt.Color(0, 0, 0));

		lightOrange.setBackground(new java.awt.Color(0, 0, 0));
		lightOrange.setIcon(
				new javax.swing.ImageIcon(getClass().getResource("/com/greathammer/eqm/view/resource/original.png"))); // NOI18N
		lightOrange.setBorder(null);

		javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
		jPanel6.setLayout(jPanel6Layout);
		jPanel6Layout.setHorizontalGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel6Layout.createSequentialGroup().addContainerGap().addComponent(lightOrange,
						javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addContainerGap()));
		jPanel6Layout.setVerticalGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel6Layout.createSequentialGroup().addContainerGap().addComponent(lightOrange,
						javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addContainerGap()));

		jPanel7.setBackground(new java.awt.Color(0, 0, 0));

		lightYellow.setBackground(new java.awt.Color(0, 0, 0));
		lightYellow.setIcon(
				new javax.swing.ImageIcon(getClass().getResource("/com/greathammer/eqm/view/resource/original.png"))); // NOI18N
		lightYellow.setBorder(null);

		javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
		jPanel7.setLayout(jPanel7Layout);
		jPanel7Layout.setHorizontalGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel7Layout.createSequentialGroup().addContainerGap().addComponent(lightYellow,
						javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addContainerGap()));
		jPanel7Layout.setVerticalGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel7Layout.createSequentialGroup().addContainerGap().addComponent(lightYellow,
						javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addContainerGap()));

		jPanel8.setBackground(new java.awt.Color(0, 0, 0));

		lightBlue.setBackground(new java.awt.Color(0, 0, 0));
		lightBlue.setIcon(
				new javax.swing.ImageIcon(getClass().getResource("/com/greathammer/eqm/view/resource/original.png"))); // NOI18N
		lightBlue.setBorder(null);

		javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
		jPanel8.setLayout(jPanel8Layout);
		jPanel8Layout.setHorizontalGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel8Layout.createSequentialGroup().addContainerGap().addComponent(lightBlue,
						javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addContainerGap()));
		jPanel8Layout.setVerticalGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel8Layout.createSequentialGroup().addContainerGap().addComponent(lightBlue,
						javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addContainerGap()));

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup().addGap(200, 200, 200)
						.addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(205, 205, 205)
						.addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(205, 205, 205)
						.addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 162, Short.MAX_VALUE)
						.addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(201, 201, 201)));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
						jPanel1Layout.createSequentialGroup().addContainerGap()
								.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
										.addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addGap(10, 10, 10)));

		jPanel3.setBackground(new java.awt.Color(0, 0, 0));

		jLabel1.setBackground(new java.awt.Color(0, 0, 0));
		jLabel1.setFont(new java.awt.Font("宋体", 1, 50)); // NOI18N
		jLabel1.setForeground(new java.awt.Color(255, 255, 255));
		jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		jLabel1.setText("地震预警信息发布平台");
		jLabel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

		jLabel2.setBackground(new java.awt.Color(0, 0, 0));
		jLabel2.setFont(new java.awt.Font("宋体", 1, 55)); // NOI18N
		jLabel2.setForeground(new java.awt.Color(255, 0, 51));
		jLabel2.setText("  发震地点：");

		jLabel4.setBackground(new java.awt.Color(0, 0, 0));
		jLabel4.setFont(new java.awt.Font("宋体", 1, 55)); // NOI18N
		jLabel4.setForeground(new java.awt.Color(255, 0, 51));
		jLabel4.setText("  发震时间：");

		jLabel5.setFont(new java.awt.Font("宋体", 1, 55)); // NOI18N
		jLabel5.setForeground(new java.awt.Color(255, 0, 51));
		jLabel5.setText("  震    级：");
		jLabel5.setMaximumSize(new java.awt.Dimension(352, 64));
		jLabel5.setMinimumSize(new java.awt.Dimension(352, 64));
		jLabel5.setPreferredSize(new java.awt.Dimension(352, 64));

		jLabel6.setFont(new java.awt.Font("宋体", 1, 55)); // NOI18N
		jLabel6.setForeground(new java.awt.Color(255, 0, 51));
		jLabel6.setText("  震中距您：");

		jLabel7.setFont(new java.awt.Font("宋体", 1, 55)); // NOI18N
		jLabel7.setForeground(new java.awt.Color(255, 0, 51));
		jLabel7.setText("  预估烈度：");

		otime.setFont(new java.awt.Font("宋体", 1, 55)); // NOI18N
		otime.setForeground(new java.awt.Color(255, 255, 0));
		otime.setPreferredSize(new java.awt.Dimension(522, 64));

		org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
				org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, earthquakeYuJing1,
				org.jdesktop.beansbinding.ELProperty.create("${otime}"), otime,
				org.jdesktop.beansbinding.BeanProperty.create("text"));
		bindingGroup.addBinding(binding);

		magitude.setFont(new java.awt.Font("宋体", 1, 55)); // NOI18N
		magitude.setForeground(new java.awt.Color(255, 255, 0));
		magitude.setPreferredSize(new java.awt.Dimension(522, 64));

		binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
				org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, earthquakeYuJing1,
				org.jdesktop.beansbinding.ELProperty.create("${magitude}"), magitude,
				org.jdesktop.beansbinding.BeanProperty.create("text"));
		bindingGroup.addBinding(binding);

		location.setFont(new java.awt.Font("宋体", 1, 55)); // NOI18N
		location.setForeground(new java.awt.Color(255, 255, 0));

		binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
				org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, earthquakeYuJing1,
				org.jdesktop.beansbinding.ELProperty.create("${location}"), location,
				org.jdesktop.beansbinding.BeanProperty.create("text"));
		bindingGroup.addBinding(binding);

		distance.setFont(new java.awt.Font("宋体", 1, 55)); // NOI18N
		distance.setForeground(new java.awt.Color(255, 255, 0));

		binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
				org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, earthquakeYuJing1,
				org.jdesktop.beansbinding.ELProperty.create("${distance}"), distance,
				org.jdesktop.beansbinding.BeanProperty.create("text"));
		bindingGroup.addBinding(binding);

		intensity.setFont(new java.awt.Font("宋体", 1, 88)); // NOI18N
		intensity.setForeground(new java.awt.Color(255, 255, 0));

		binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
				org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, earthquakeYuJing1,
				org.jdesktop.beansbinding.ELProperty.create("${intensity}"), intensity,
				org.jdesktop.beansbinding.BeanProperty.create("text"));
		bindingGroup.addBinding(binding);

		jLabel9.setFont(new java.awt.Font("宋体", 1, 55)); // NOI18N
		jLabel9.setForeground(new java.awt.Color(255, 255, 0));
		jLabel9.setText("级");

		jLabel10.setFont(new java.awt.Font("宋体", 1, 55)); // NOI18N
		jLabel10.setForeground(new java.awt.Color(255, 255, 0));
		jLabel10.setText("公里");

		jLabel11.setFont(new java.awt.Font("宋体", 1, 55)); // NOI18N
		jLabel11.setForeground(new java.awt.Color(255, 255, 0));
		jLabel11.setText("度");

		javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
		jPanel3.setLayout(jPanel3Layout);
		jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE,
						javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
				.addGroup(jPanel3Layout.createSequentialGroup().addGap(111, 111, 111)
						.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
								.addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING,
										javax.swing.GroupLayout.PREFERRED_SIZE, 360,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(60, 60, 60)
						.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(jPanel3Layout
										.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
										.addComponent(otime, javax.swing.GroupLayout.Alignment.TRAILING,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(location, javax.swing.GroupLayout.Alignment.TRAILING,
												javax.swing.GroupLayout.PREFERRED_SIZE, 855,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGroup(jPanel3Layout.createSequentialGroup()
										.addComponent(distance, javax.swing.GroupLayout.PREFERRED_SIZE, 172,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(18, 18, 18).addComponent(jLabel10,
												javax.swing.GroupLayout.PREFERRED_SIZE, 135,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGroup(jPanel3Layout.createSequentialGroup()
										.addComponent(magitude, javax.swing.GroupLayout.PREFERRED_SIZE, 102,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(29, 29, 29).addComponent(jLabel9,
												javax.swing.GroupLayout.PREFERRED_SIZE, 70,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGroup(jPanel3Layout.createSequentialGroup()
										.addComponent(intensity, javax.swing.GroupLayout.PREFERRED_SIZE, 54,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(5, 5, 5).addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE,
												80, javax.swing.GroupLayout.PREFERRED_SIZE)))
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel3Layout.createSequentialGroup()
						.addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 80,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(16, 16, 16)
						.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								.addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(location, javax.swing.GroupLayout.PREFERRED_SIZE, 91,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(8, 8, 8)
						.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 91,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(otime, javax.swing.GroupLayout.PREFERRED_SIZE, 85,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
								.addComponent(magitude, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE)
								.addComponent(distance, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(intensity, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))));

		jPanel4.setBackground(new java.awt.Color(0, 0, 0));

		jPanel10.setBackground(new java.awt.Color(51, 255, 0));
		jPanel10.setPreferredSize(new java.awt.Dimension(0, 3));

		javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
		jPanel10.setLayout(jPanel10Layout);
		jPanel10Layout.setHorizontalGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 0, Short.MAX_VALUE));
		jPanel10Layout.setVerticalGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 3, Short.MAX_VALUE));

		jPanel11.setBackground(new java.awt.Color(102, 255, 0));
		jPanel11.setPreferredSize(new java.awt.Dimension(0, 3));

		javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
		jPanel11.setLayout(jPanel11Layout);
		jPanel11Layout.setHorizontalGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 0, Short.MAX_VALUE));
		jPanel11Layout.setVerticalGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 3, Short.MAX_VALUE));

		jPanel12.setBackground(new java.awt.Color(102, 255, 0));
		jPanel12.setPreferredSize(new java.awt.Dimension(5, 65));

		javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
		jPanel12.setLayout(jPanel12Layout);
		jPanel12Layout.setHorizontalGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 5, Short.MAX_VALUE));
		jPanel12Layout.setVerticalGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 0, Short.MAX_VALUE));

		jLabel3.setFont(new java.awt.Font("宋体", 1, 55)); // NOI18N
		jLabel3.setForeground(new java.awt.Color(255, 0, 51));
		jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		jLabel3.setText("避震指导");

		jLabel8.setFont(new java.awt.Font("宋体", 1, 55)); // NOI18N
		jLabel8.setForeground(new java.awt.Color(255, 0, 51));
		jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		jLabel8.setText("到达时间");

		jPanel13.setBackground(new java.awt.Color(102, 255, 0));
		jPanel13.setPreferredSize(new java.awt.Dimension(3, 132));

		javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
		jPanel13.setLayout(jPanel13Layout);
		jPanel13Layout.setHorizontalGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 5, Short.MAX_VALUE));
		jPanel13Layout.setVerticalGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 0, Short.MAX_VALUE));

		eqDesc.setFont(new java.awt.Font("宋体", 1, 45)); // NOI18N
		eqDesc.setForeground(new java.awt.Color(255, 255, 0));
		eqDesc.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		eqDesc.setPreferredSize(new java.awt.Dimension(42, 64));

		binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
				org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, earthquakeYuJing1,
				org.jdesktop.beansbinding.ELProperty.create("${eqDesc}"), eqDesc,
				org.jdesktop.beansbinding.BeanProperty.create("text"));
		bindingGroup.addBinding(binding);

		advice.setFont(new java.awt.Font("宋体", 1, 45)); // NOI18N
		advice.setForeground(new java.awt.Color(255, 255, 0));
		advice.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		advice.setPreferredSize(new java.awt.Dimension(48, 64));

		binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
				org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, earthquakeYuJing1,
				org.jdesktop.beansbinding.ELProperty.create("${advice}"), advice,
				org.jdesktop.beansbinding.BeanProperty.create("text"));
		bindingGroup.addBinding(binding);

		arrivalTime.setFont(new java.awt.Font("宋体", 1, 120)); // NOI18N
		arrivalTime.setForeground(new java.awt.Color(255, 255, 0));
		arrivalTime.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

		binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
				org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, earthquakeYuJing1,
				org.jdesktop.beansbinding.ELProperty.create("${arrivalTime}"), arrivalTime,
				org.jdesktop.beansbinding.BeanProperty.create("text"));
		bindingGroup.addBinding(binding);

		jPanel14.setBackground(new java.awt.Color(0, 0, 0));
		jPanel14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));
		jPanel14.setForeground(new java.awt.Color(255, 255, 255));

		jLabel20.setFont(new java.awt.Font("宋体", 1, 35)); // NOI18N
		jLabel20.setForeground(new java.awt.Color(255, 255, 255));
		jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		jLabel20.setText("青岛地震局");
		jLabel20.setPreferredSize(new java.awt.Dimension(238, 15));

		jLabel21.setFont(new java.awt.Font("宋体", 1, 35)); // NOI18N
		jLabel21.setForeground(new java.awt.Color(255, 255, 255));
		jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		jLabel21.setText("技术支持：青岛科信安全技术有限公司");

		javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
		jPanel14.setLayout(jPanel14Layout);
		jPanel14Layout.setHorizontalGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel14Layout.createSequentialGroup().addContainerGap()
						.addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 550,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 698,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));
		jPanel14Layout.setVerticalGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel14Layout.createSequentialGroup().addContainerGap()
						.addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addContainerGap()));

		jLabel22.setFont(new java.awt.Font("宋体", 1, 60)); // NOI18N
		jLabel22.setForeground(new java.awt.Color(255, 255, 0));
		jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		jLabel22.setText("秒");
		jLabel22.setPreferredSize(new java.awt.Dimension(66, 69));

		javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
		jPanel4.setLayout(jPanel4Layout);
		jPanel4Layout
				.setHorizontalGroup(
						jPanel4Layout
								.createParallelGroup(
										javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(jPanel4Layout.createSequentialGroup().addContainerGap().addGroup(jPanel4Layout
										.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, 1849,
												Short.MAX_VALUE)
										.addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, 1849,
												Short.MAX_VALUE)
										.addGroup(jPanel4Layout.createSequentialGroup()
												.addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 1239,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 597,
														Short.MAX_VALUE))
										.addGroup(jPanel4Layout.createSequentialGroup().addGroup(jPanel4Layout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
												.addComponent(eqDesc, javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(advice, javax.swing.GroupLayout.DEFAULT_SIZE, 1232,
														Short.MAX_VALUE))
												.addGap(13, 13, 13)
												.addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 5,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(arrivalTime, javax.swing.GroupLayout.PREFERRED_SIZE, 219,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
												.addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 138,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addGap(133, 133, 133)))
										.addContainerGap()));
		jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel4Layout.createSequentialGroup().addContainerGap()
						.addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
								.addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(arrivalTime, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addGroup(jPanel4Layout.createSequentialGroup()
										.addComponent(eqDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 66,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(advice, javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(0, 0, Short.MAX_VALUE))
								.addComponent(jPanel13, javax.swing.GroupLayout.Alignment.TRAILING,
										javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));

		javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel2Layout.createSequentialGroup().addContainerGap()
						.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE))
						.addGap(0, 0, 0)));
		jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel2Layout.createSequentialGroup().addContainerGap()
						.addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(2, 2, 2)
						.addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addContainerGap()));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 1879, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 950, Short.MAX_VALUE));

		bindingGroup.bind();

		pack();
	}

	private javax.swing.JLabel advice;
	private javax.swing.JLabel arrivalTime;
	private javax.swing.JLabel distance;
	private com.greathammer.eqm.earthquake.yujing.EarthquakeYuJing earthquakeYuJing1;
	private javax.swing.JLabel eqDesc;
	private javax.swing.JLabel intensity;
	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;
	private javax.swing.JButton jButton3;
	private javax.swing.JButton jButton4;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel10;
	private javax.swing.JLabel jLabel11;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel20;
	private javax.swing.JLabel jLabel21;
	private javax.swing.JLabel jLabel22;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JLabel jLabel8;
	private javax.swing.JLabel jLabel9;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel10;
	private javax.swing.JPanel jPanel11;
	private javax.swing.JPanel jPanel12;
	private javax.swing.JPanel jPanel13;
	private javax.swing.JPanel jPanel14;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JPanel jPanel5;
	private javax.swing.JPanel jPanel6;
	private javax.swing.JPanel jPanel7;
	private javax.swing.JPanel jPanel8;
	private javax.swing.JButton lightBlue;
	private javax.swing.JButton lightOrange;
	private javax.swing.JButton lightRed;
	private javax.swing.JButton lightYellow;
	private javax.swing.JLabel location;
	private javax.swing.JLabel magitude;
	private javax.swing.JLabel otime;
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
		return jLabel20;
	}
}
