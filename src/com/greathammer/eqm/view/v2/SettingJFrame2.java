/*
 * 


 */
package com.greathammer.eqm.view.v2;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import  com.greathammer.eqm.util.DeployPoint;
import com.greathammer.eqm.util.HttpClientRestUtil;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.greathammer.eqm.util.Constant;
import com.greathammer.eqm.util.Toast;
import com.greathammer.serial.EqmSerialTool;

/**
 *
 * @author FelixYin
 */
public class SettingJFrame2 extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Log log = LogFactory.getLog(SettingJFrame2.class);

	public SettingJFrame2() {
		log.info("-->开始创建地震预警界面");

		initComponents();
		bindDataSource();

		log.info("-->已创建地震预警界面");
	}

	private void bindDataSource() {
		log.info("开始读取配置数据");
		final SettingJFrame2 settingJFrame = this;
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent arg0) {
				EqmSerialTool.resetDevice();
				new Toast(settingJFrame, "关闭处理中...", 3000, Toast.success).start();
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					log.error(e);
				}
				System.exit(0);
			}
		});

		this.setTitle("高级参数配置");

		this.setBounds(750, 270, 420, 540);

		Properties p = new Properties();
		try {
			p.load(new InputStreamReader(new FileInputStream(Constant.C_WINDOWS_EQM_PROPERTIES), "UTF-8"));
		} catch (FileNotFoundException e) {
			log.error(e);
			System.exit(1);
		} catch (IOException e) {
			log.error(e);
			System.exit(1);
		}
		int IS_PRODUCTION_VERSION = Integer.parseInt(p.getProperty("IS_PRODUCTION_VERSION"));
		String CITY_NAME = p.getProperty("CITY_NAME");
		double LONGITUDE = Double.parseDouble(p.getProperty("LONGITUDE"));
		double LATITUDE = Double.parseDouble(p.getProperty("LATITUDE"));
		int AUDIO_WAV_INTENSITY = Integer.parseInt(p.getProperty("AUDIO_WAV_INTENSITY"));
		int AUDIO_WAV_COUNT = Integer.parseInt(p.getProperty("AUDIO_WAV_COUNT"));
		int IS_PLAY_COUNT_DOWN = Integer.parseInt(p.getProperty("IS_PLAY_COUNT_DOWN"));
		String COMPANY_NAME = p.getProperty("COMPANY_NAME");
		long TEST_EARTHQUAKE_FIRST_TIME = Long.parseLong(p.getProperty("TEST_EARTHQUAKE_FIRST_TIME"));
		long TEST_EARTHQUAKE_TIME = Long.parseLong(p.getProperty("TEST_EARTHQUAKE_TIME"));
		long SWITCH_SCREENSAVER_TIME_AFTER_EQM = Long.parseLong(p.getProperty("SWITCH_SCREENSAVER_TIME_AFTER_EQM"));
		int HELLO_SCREEN_SHOW_TIME = Integer.parseInt(p.getProperty("HELLO_SCREEN_SHOW_TIME"));
		int SCREENSAVER_SHOW_AFTER_EQM = Integer.parseInt(p.getProperty("SCREENSAVER_SHOW_AFTER_EQM"));
		int IS_SHOW_LIGHT = Integer.parseInt(p.getProperty("IS_SHOW_LIGHT"));
		int IS_PLAY_AUDIO = Integer.parseInt(p.getProperty("IS_PLAY_AUDIO"));

		// int EQMQ_AFTER_PRODUCTION_TIME =
		// Integer.parseInt(p.getProperty("EQMQ_AFTER_PRODUCTION_TIME"));

		if (IS_PRODUCTION_VERSION == 1) {
			radioProduction.setSelected(true);
		} else {
			radioTest.setSelected(true);
		}
		city.setText(CITY_NAME);
		longitude.setText(LONGITUDE + "");
		latitude.setText(LATITUDE + "");
		bjfz.setText(AUDIO_WAV_INTENSITY + "");
		bjpc.setText(AUDIO_WAV_COUNT + "");
		(IS_PLAY_COUNT_DOWN == 1 ? radioBaojing : radioBubaojing).setSelected(true);
		(IS_SHOW_LIGHT == 1 ? radioKaiDeng : radioGuanDeng).setSelected(true);
		(IS_PLAY_AUDIO == 1 ? radioKaiSheng : radioGuanSheng).setSelected(true);

		dwmc.setText(COMPANY_NAME);
		pbqhsj.setText(SWITCH_SCREENSAVER_TIME_AFTER_EQM + "");
		hypmsj.setText(HELLO_SCREEN_SHOW_TIME + "");
		dzjmsj.setText(SCREENSAVER_SHOW_AFTER_EQM + "");
		dycdzsj.setText(TEST_EARTHQUAKE_FIRST_TIME + "");
		dzjgsj.setText(TEST_EARTHQUAKE_TIME + "");
		// yxsj.setText(MANOEUVRE_DATA);

		log.info("读取配置数据完毕");

		// this.pack();
		this.setVisible(true);

		log.info("显示配置界面");
	}

	private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {
		log.info("您点击了保存配置按钮，开始保存数据");

		Properties p = new Properties();
		p.setProperty("IS_PRODUCTION_VERSION", radioProduction.isSelected() ? "1" : "0");
		p.setProperty("CITY_NAME", city.getText());
		p.setProperty("LONGITUDE", longitude.getText());
		p.setProperty("LATITUDE", latitude.getText());
		p.setProperty("AUDIO_WAV_INTENSITY", bjfz.getText());
		p.setProperty("AUDIO_WAV_COUNT", bjpc.getText());
		p.setProperty("IS_PLAY_COUNT_DOWN", radioBaojing.isSelected() ? "1" : "0");
		p.setProperty("IS_SHOW_LIGHT", radioKaiDeng.isSelected() ? "1" : "0");
		p.setProperty("IS_PLAY_AUDIO", radioKaiSheng.isSelected() ? "1" : "0");
		p.setProperty("COMPANY_NAME", dwmc.getText());
		p.setProperty("SWITCH_SCREENSAVER_TIME_AFTER_EQM", pbqhsj.getText());
		p.setProperty("HELLO_SCREEN_SHOW_TIME", hypmsj.getText());
		p.setProperty("SCREENSAVER_SHOW_AFTER_EQM", dzjmsj.getText());
		p.setProperty("TEST_EARTHQUAKE_FIRST_TIME", dycdzsj.getText());
		p.setProperty("TEST_EARTHQUAKE_TIME", dzjgsj.getText());
		p.setProperty("EQMQ_TCP_FUJIAN", Constant.EQMQ_TCP_FUJIAN);
		p.setProperty("EQMQ_PORT_FUJIAN", "" + Constant.EQMQ_PORT_FUJIAN);
		p.setProperty("EQMQ_TOPIC_FUJIAN", Constant.EQMQ_TOPIC_FUJIAN);
		p.setProperty("COM", Constant.COM);
		// p.setProperty("EQMQ_AFTER_PRODUCTION_TIME", "" +
		// Math.round(Constant.EQMQ_AFTER_PRODUCTION_TIME / 1000 / 60));

		log.info("已经读取您最新配置的数据");

		try {

			p.store(new OutputStreamWriter(new FileOutputStream(Constant.C_WINDOWS_EQM_PROPERTIES), "UTF-8"), "");
			log.info("已经写入最新配置数据");

			try{
			DeployPoint deployPoint = new DeployPoint();
			deployPoint.setProductionVersion(radioProduction.isSelected());
			deployPoint.setCityName(city.getText());
			deployPoint.setLongitude(Double.parseDouble(longitude.getText()));
			deployPoint.setLatitude(Double.parseDouble(latitude.getText()));
			deployPoint.setAudioWavCount(Integer.parseInt(bjpc.getText()));
			deployPoint.setAudioWavIntensity(Integer.parseInt(bjfz.getText()));
			deployPoint.setPlayCountDown(radioBaojing.isSelected());
			deployPoint.setShowLight(radioKaiDeng.isSelected());
			deployPoint.setPlayAudio(radioKaiSheng.isSelected());
			deployPoint.setCompanyName(dwmc.getText());
			deployPoint.setSwitchScreensaverTimer(Long.parseLong(pbqhsj.getText()));
			deployPoint.setHelloScreenShowTime(Integer.parseInt(hypmsj.getText()));
			deployPoint.setScreensaverShowAfter(Integer.parseInt(dzjmsj.getText()));
			deployPoint.setTestFirstTime(Long.parseLong(dycdzsj.getText()));
			deployPoint.setTestTime(Long.parseLong(dzjgsj.getText()));
			deployPoint.setLoopForTest(Constant.IS_LOOP_FOR_TEST);
			deployPoint.setCom(Constant.COM);
			
			String result = HttpClientRestUtil.post(Constant.SAVE_SETTING, deployPoint);
			if("ok".equals(result)){
				log.info("已经将数据同步服务器");
			}
			}catch(Exception e){
				
			}
			
//			Constant.initConstant();
			JOptionPane.showMessageDialog(null, "保存成功，重启应用后生效", "青岛科信提示您", JOptionPane.CLOSED_OPTION);

			log.info("配置结束，系统退出，重启后配置生效");
			System.exit(0);
		} catch (IOException ex) {
			Logger.getLogger(SettingJFrame2.class.getName()).log(Level.SEVERE, null, ex);
		}

	}

	private void initComponents() {

		buttonGroup1 = new javax.swing.ButtonGroup();
		buttonGroup2 = new javax.swing.ButtonGroup();
		buttonGroup3 = new javax.swing.ButtonGroup();
		buttonGroup4 = new javax.swing.ButtonGroup();
		jPanel1 = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		radioProduction = new javax.swing.JRadioButton();
		radioTest = new javax.swing.JRadioButton();
		jLabel2 = new javax.swing.JLabel();
		city = new javax.swing.JTextField();
		jLabel3 = new javax.swing.JLabel();
		longitude = new javax.swing.JTextField();
		jLabel4 = new javax.swing.JLabel();
		latitude = new javax.swing.JTextField();
		jLabel15 = new javax.swing.JLabel();
		bjfz = new javax.swing.JTextField();
		jLabel16 = new javax.swing.JLabel();
		bjpc = new javax.swing.JTextField();
		jLabel17 = new javax.swing.JLabel();
		jLabel18 = new javax.swing.JLabel();
		jLabel19 = new javax.swing.JLabel();
		jLabel20 = new javax.swing.JLabel();
		djsLabel = new javax.swing.JLabel();
		radioBaojing = new javax.swing.JRadioButton();
		radioBubaojing = new javax.swing.JRadioButton();
		jLabel22 = new javax.swing.JLabel();
		dwmc = new javax.swing.JTextField();
		bjdgLabel = new javax.swing.JLabel();
		jLabel24 = new javax.swing.JLabel();
		radioKaiDeng = new javax.swing.JRadioButton();
		radioGuanDeng = new javax.swing.JRadioButton();
		radioKaiSheng = new javax.swing.JRadioButton();
		radioGuanSheng = new javax.swing.JRadioButton();
		jPanel2 = new javax.swing.JPanel();
		jLabel5 = new javax.swing.JLabel();
		pbqhsj = new javax.swing.JTextField();
		jLabel6 = new javax.swing.JLabel();
		jLabel7 = new javax.swing.JLabel();
		hypmsj = new javax.swing.JTextField();
		jLabel8 = new javax.swing.JLabel();
		jLabel9 = new javax.swing.JLabel();
		dzjmsj = new javax.swing.JTextField();
		jLabel10 = new javax.swing.JLabel();
		testPanel = new javax.swing.JPanel();
		jLabel11 = new javax.swing.JLabel();
		dycdzsj = new javax.swing.JTextField();
		jLabel12 = new javax.swing.JLabel();
		jLabel13 = new javax.swing.JLabel();
		dzjgsj = new javax.swing.JTextField();
		jLabel14 = new javax.swing.JLabel();
		saveBtn = new javax.swing.JButton();
		jPanel3 = new javax.swing.JPanel();
		jScrollPane1 = new javax.swing.JScrollPane();
		yxsj = new javax.swing.JTextArea();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setMinimumSize(new java.awt.Dimension(780, 300));

		jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("基本配置"));

		jLabel1.setText("切换版本");

		buttonGroup1.add(radioProduction);
		radioProduction.setSelected(true);
		radioProduction.setText("生产版本");

		buttonGroup1.add(radioTest);
		radioTest.setText("测试版本");

		jLabel2.setText("所在城市");

		city.setText("青岛");

		jLabel3.setText("经度");

		longitude.setText("120");

		jLabel4.setText("维度");

		latitude.setText("36");

		jLabel15.setText("报警阈值");

		bjfz.setText("6");
		bjfz.setToolTipText("烈度阈值超过多少会播放报警音频；报警灯光照常显示。");

		jLabel16.setText("报警频次(废弃)");

		bjpc.setEditable(false);
		bjpc.setText("3");
		bjpc.setToolTipText("地震报警音频播放次数");

		jLabel17.setText("度");

		jLabel18.setText("次");

		jLabel19.setText("度");

		jLabel20.setText("度");

		djsLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		djsLabel.setText("倒计时开关");

		buttonGroup2.add(radioBaojing);
		radioBaojing.setSelected(true);
		radioBaojing.setText("开");
		radioBaojing.setToolTipText("选择后会播放十秒倒计时");

		buttonGroup2.add(radioBubaojing);
		radioBubaojing.setText("关");
		radioBubaojing.setToolTipText("取消十秒倒计时");

		jLabel22.setText("单位名称");

		dwmc.setText("青岛地震局");
		dwmc.setToolTipText("显示在状态栏最左侧的文字");

		bjdgLabel.setText("报警灯光开关");

		jLabel24.setText("报警声音开关");

		buttonGroup3.add(radioKaiDeng);
		radioKaiDeng.setSelected(true);
		radioKaiDeng.setText("开");

		buttonGroup3.add(radioGuanDeng);
		radioGuanDeng.setText("关");

		buttonGroup4.add(radioKaiSheng);
		radioKaiSheng.setSelected(true);
		radioKaiSheng.setText("开");

		buttonGroup4.add(radioGuanSheng);
		radioGuanSheng.setText("关");

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
						jPanel1Layout.createSequentialGroup().addContainerGap(35, Short.MAX_VALUE)
								.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(jLabel22, javax.swing.GroupLayout.Alignment.TRAILING)
										.addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(
												jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
										.addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
										.addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
										.addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING)
										.addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING,
												javax.swing.GroupLayout.PREFERRED_SIZE, 89,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(djsLabel, javax.swing.GroupLayout.Alignment.TRAILING,
												javax.swing.GroupLayout.PREFERRED_SIZE, 80,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(bjdgLabel, javax.swing.GroupLayout.Alignment.TRAILING)
										.addComponent(jLabel24, javax.swing.GroupLayout.Alignment.TRAILING))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
								.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addGroup(jPanel1Layout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
												.addComponent(city)
												.addGroup(jPanel1Layout.createSequentialGroup()
														.addComponent(radioProduction).addGap(18, 18, 18).addComponent(
																radioTest))
												.addGroup(jPanel1Layout
														.createSequentialGroup()
														.addGroup(jPanel1Layout.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING, false)
																.addComponent(bjfz).addComponent(
																		bjpc, javax.swing.GroupLayout.PREFERRED_SIZE,
																		71, javax.swing.GroupLayout.PREFERRED_SIZE))
														.addGap(5, 5, 5)
														.addGroup(jPanel1Layout
																.createParallelGroup(
																		javax.swing.GroupLayout.Alignment.LEADING)
																.addComponent(jLabel17).addComponent(jLabel18)))
												.addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout
														.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING,
																false)
														.addComponent(latitude,
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(longitude,
																javax.swing.GroupLayout.Alignment.LEADING,
																javax.swing.GroupLayout.PREFERRED_SIZE, 71,
																javax.swing.GroupLayout.PREFERRED_SIZE))
														.addPreferredGap(
																javax.swing.LayoutStyle.ComponentPlacement.RELATED)
														.addGroup(jPanel1Layout
																.createParallelGroup(
																		javax.swing.GroupLayout.Alignment.LEADING)
																.addComponent(jLabel19).addComponent(jLabel20)))
												.addGroup(
														jPanel1Layout.createSequentialGroup().addComponent(radioBaojing)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																.addComponent(radioBubaojing))
												.addComponent(dwmc, javax.swing.GroupLayout.PREFERRED_SIZE, 186,
														javax.swing.GroupLayout.PREFERRED_SIZE))
										.addGroup(jPanel1Layout.createSequentialGroup().addComponent(radioKaiSheng)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
												.addComponent(radioGuanSheng))
										.addGroup(jPanel1Layout.createSequentialGroup().addComponent(radioKaiDeng)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
												.addComponent(radioGuanDeng)))
								.addGap(49, 49, 49)));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup().addGap(18, 18, 18)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel1).addComponent(radioProduction).addComponent(radioTest))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(city, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(jLabel2))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel3)
								.addComponent(longitude, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(jLabel20))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(latitude, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(jLabel4).addComponent(jLabel19))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel15)
								.addComponent(bjfz, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(jLabel17))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel16)
								.addComponent(bjpc, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(jLabel18))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(radioBaojing).addComponent(radioBubaojing).addComponent(djsLabel))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(bjdgLabel).addComponent(radioKaiDeng).addComponent(radioGuanDeng))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel24).addComponent(radioKaiSheng).addComponent(radioGuanSheng))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel22).addComponent(dwmc, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(23, 23, 23)));

		longitude.getAccessibleContext().setAccessibleName("");

		jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("通用时间配置"));

		jLabel5.setText("屏保切换时间");

		pbqhsj.setText("40");
		pbqhsj.setToolTipText("多少时间过后没有收到地震预警则自动切换到屏保界面");

		jLabel6.setText("秒");

		jLabel7.setText("欢迎屏幕时间");

		hypmsj.setText("3");
		hypmsj.setToolTipText("欢迎屏幕的显示时间");

		jLabel8.setText("秒");

		jLabel9.setText("地震界面时间");

		dzjmsj.setText("10");
		dzjmsj.setToolTipText("地震主屏幕显示完毕后，等待多少时间显示屏保");

		jLabel10.setText("秒");

		javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel2Layout.createSequentialGroup().addGap(83, 83, 83)
						.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								.addGroup(jPanel2Layout.createSequentialGroup().addComponent(jLabel9)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(dzjmsj))
								.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
										jPanel2Layout.createSequentialGroup().addComponent(jLabel7)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
												.addComponent(hypmsj))
								.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
										jPanel2Layout.createSequentialGroup().addComponent(jLabel5)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
												.addComponent(pbqhsj, javax.swing.GroupLayout.PREFERRED_SIZE, 71,
														javax.swing.GroupLayout.PREFERRED_SIZE)))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(jLabel6).addComponent(jLabel8).addComponent(jLabel10))
						.addContainerGap(99, Short.MAX_VALUE)));
		jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel2Layout.createSequentialGroup()
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel5)
								.addComponent(pbqhsj, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(jLabel6))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel7)
								.addComponent(hypmsj, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(jLabel8))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel9)
								.addComponent(dzjmsj, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(jLabel10))
						.addGap(29, 29, 29)));

		testPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("模拟地震配置"));

		jLabel11.setText("第一次地震时间");

		dycdzsj.setText("20");
		dycdzsj.setToolTipText("模拟的地震，启动应用后第一次地震来的时间");

		jLabel12.setText("秒");

		jLabel13.setText("地震间隔时间");

		dzjgsj.setText("60");
		dzjgsj.setToolTipText("模拟的地震，每隔多少秒来一次");

		jLabel14.setText("秒");

		javax.swing.GroupLayout testPanelLayout = new javax.swing.GroupLayout(testPanel);
		testPanel.setLayout(testPanelLayout);
		testPanelLayout.setHorizontalGroup(testPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(testPanelLayout.createSequentialGroup().addGap(69, 69, 69)
						.addGroup(testPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
								.addComponent(jLabel13).addComponent(jLabel11))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(testPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								.addComponent(dzjgsj).addComponent(dycdzsj, javax.swing.GroupLayout.PREFERRED_SIZE, 72,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(testPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(jLabel12).addComponent(jLabel14,
										javax.swing.GroupLayout.Alignment.TRAILING))
						.addContainerGap(99, Short.MAX_VALUE)));
		testPanelLayout
				.setVerticalGroup(testPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(testPanelLayout.createSequentialGroup().addContainerGap().addGroup(testPanelLayout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
								.addComponent(jLabel11).addComponent(dycdzsj, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(jLabel12))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										testPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(jLabel13)
												.addComponent(dzjgsj, javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(jLabel14))
								.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		saveBtn.setBackground(new java.awt.Color(102, 102, 102));
		saveBtn.setFont(new java.awt.Font("宋体", 1, 36)); // NOI18N
		saveBtn.setText("保    存");
		saveBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveBtnActionPerformed(evt);
			}
		});

		jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("演戏数据"));
		jPanel3.setMaximumSize(new java.awt.Dimension(0, 0));

		yxsj.setColumns(20);
		yxsj.setRows(5);
		jScrollPane1.setViewportView(yxsj);

		javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
		jPanel3.setLayout(jPanel3Layout);
		jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel3Layout.createSequentialGroup().addContainerGap()
						.addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 413, Short.MAX_VALUE)
						.addContainerGap()));
		jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel3Layout.createSequentialGroup().addContainerGap()
						.addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
						.addContainerGap()));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout
						.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addComponent(jPanel3,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addGap(332, 332, 332))
						.addGroup(layout.createSequentialGroup()
								.addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addGroup(layout.createSequentialGroup()
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addGroup(layout
														.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING,
																false)
														.addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
														.addComponent(testPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
										.addGroup(layout.createSequentialGroup().addGap(85, 85, 85).addComponent(
												saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)))
								.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout
				.createSequentialGroup().addContainerGap(24, Short.MAX_VALUE)
				.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
						.addGroup(layout.createSequentialGroup()
								.addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 0,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGroup(layout.createSequentialGroup()
								.addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 129,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(testPanel, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 85,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(21, 21, 21)))));

		pack();
	}// </editor-fold>

	public static void main(String args[]) {
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(SettingJFrame2.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(SettingJFrame2.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(SettingJFrame2.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(SettingJFrame2.class.getName()).log(java.util.logging.Level.SEVERE, null,
					ex);
		}
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new SettingJFrame2().setVisible(true);
			}
		});
	}

	private javax.swing.JLabel bjdgLabel;
	private javax.swing.JTextField bjfz;
	private javax.swing.JTextField bjpc;
	private javax.swing.ButtonGroup buttonGroup1;
	private javax.swing.ButtonGroup buttonGroup2;
	private javax.swing.ButtonGroup buttonGroup3;
	private javax.swing.ButtonGroup buttonGroup4;
	private javax.swing.JTextField city;
	private javax.swing.JLabel djsLabel;
	private javax.swing.JTextField dwmc;
	private javax.swing.JTextField dycdzsj;
	private javax.swing.JTextField dzjgsj;
	private javax.swing.JTextField dzjmsj;
	private javax.swing.JTextField hypmsj;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel10;
	private javax.swing.JLabel jLabel11;
	private javax.swing.JLabel jLabel12;
	private javax.swing.JLabel jLabel13;
	private javax.swing.JLabel jLabel14;
	private javax.swing.JLabel jLabel15;
	private javax.swing.JLabel jLabel16;
	private javax.swing.JLabel jLabel17;
	private javax.swing.JLabel jLabel18;
	private javax.swing.JLabel jLabel19;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel20;
	private javax.swing.JLabel jLabel22;
	private javax.swing.JLabel jLabel24;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JLabel jLabel8;
	private javax.swing.JLabel jLabel9;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTextField latitude;
	private javax.swing.JTextField longitude;
	private javax.swing.JTextField pbqhsj;
	private javax.swing.JRadioButton radioBaojing;
	private javax.swing.JRadioButton radioBubaojing;
	private javax.swing.JRadioButton radioGuanDeng;
	private javax.swing.JRadioButton radioGuanSheng;
	private javax.swing.JRadioButton radioKaiDeng;
	private javax.swing.JRadioButton radioKaiSheng;
	private javax.swing.JRadioButton radioProduction;
	private javax.swing.JRadioButton radioTest;
	private javax.swing.JButton saveBtn;
	private javax.swing.JPanel testPanel;
	private javax.swing.JTextArea yxsj;
}
