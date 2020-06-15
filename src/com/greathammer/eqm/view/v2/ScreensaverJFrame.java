/*
* 
 */
package com.greathammer.eqm.view.v2;

import javax.swing.JLabel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.greathammer.eqm.date.DateMessage;
import com.greathammer.eqm.date.DateMessageListener;
import com.greathammer.eqm.date.DateMessageManager;
import com.greathammer.eqm.util.Constant;
import com.greathammer.eqm.view.BaseJFrame;
import com.greathammer.eqm.view.Bootstrap;
import com.greathammer.eqm.weather.WeatherMessage;
import com.greathammer.eqm.weather.WeatherMessageListener;
import com.greathammer.eqm.weather.WeatherMessageManager;

/**
 *
 * @author FelixYin
 */
public class ScreensaverJFrame extends BaseJFrame {

	private static final long serialVersionUID = 1L;

	private static Log log = LogFactory.getLog(ScreensaverJFrame.class);

	private Bootstrap client;

	public ScreensaverJFrame() {
		super.construction();

		this.bindDateSource();
		this.bindWeatherSource();
	}

	public ScreensaverJFrame(Bootstrap client) {
		log.info("-->开始创建天气预报和日期界面");

		this.client = client;

		try {
			super.construction();
			this.bindDateSource();
			this.bindWeatherSource();
			this.showNetworkStatus();
		} catch (Exception e) {
			log.error(e);
		}

		log.info("-->已显示天气预报和日期界面");
	}

	@Override
	public Bootstrap getClient() {
		return client;
	}

	public void bindDateSource() {
		log.info("开始绑定日期数据源");

		new DateMessageManager().addListener(new DateMessageListener() {
			@Override
			public void doEvent(DateMessage event) {
				dateMessage1.change(event);
				log.trace("接收到日期或者时间的改变");
			}
		}).start();

		log.info("已经绑定日期数据源");
	}

	public void bindWeatherSource() {
		log.info("开始绑定天气数据源");

		new WeatherMessageManager().addListener(Constant.CITY_NAME, new WeatherMessageListener() {
			@Override
			public void doEvent(WeatherMessage message) {
				log.info("接收到天气的改变：" + message.toString());

				String tianQi = message.getStatus();
				log.info("天气是：" + tianQi);

				String tianQiImage = getWeatherIcon(tianQi);
				log.info("天气图片是：" + tianQiImage);

				tianqitu.setIcon(new javax.swing.ImageIcon(getClass().getResource(tianQiImage)));

				weatherMessage1.change(message);
			}

			private String getWeatherIcon(String tianQi) {
				String tianQiImage = "/com/greathammer/eqm/view/resource/weather/qing_yewan.png";
				if ("晴".equals(tianQi)) {
					tianQiImage = "/com/greathammer/eqm/view/resource/weather/qing_baitian.png";
				} else if ("多云".equals(tianQi)) {
					tianQiImage = "/com/greathammer/eqm/view/resource/weather/duoyun_baitian.png";
				} else if ("阴".equals(tianQi) || "阴天".equals(tianQi)) {
					tianQiImage = "/com/greathammer/eqm/view/resource/weather/yintian.png";
				} else if ("小雨".equals(tianQi)) {
					tianQiImage = "/com/greathammer/eqm/view/resource/weather/xiaoyu.png";
				} else if ("中雨".equals(tianQi)) {
					tianQiImage = "/com/greathammer/eqm/view/resource/weather/zhongyu.png";
				} else if ("大雨".equals(tianQi)) {
					tianQiImage = "/com/greathammer/eqm/view/resource/weather/dayu.png";
				} else if ("暴雨".equals(tianQi)) {
					tianQiImage = "/com/greathammer/eqm/view/resource/weather/baoyu.png";
				} else if ("阵雨".equals(tianQi)) {
					tianQiImage = "/com/greathammer/eqm/view/resource/weather/zhenyu.png";
				} else if ("雷阵雨".equals(tianQi)) {
					tianQiImage = "/com/greathammer/eqm/view/resource/weather/leizhenyu.png";
				} else if ("雷电".equals(tianQi)) {
					tianQiImage = "/com/greathammer/eqm/view/resource/weather/leidian.png";
				} else if ("冰雹".equals(tianQi)) {
					tianQiImage = "/com/greathammer/eqm/view/resource/weather/bingbao.png";
				} else if ("轻雾".equals(tianQi)) {
					tianQiImage = "/com/greathammer/eqm/view/resource/weather/qingwu.png";
				} else if ("雾".equals(tianQi)) {
					tianQiImage = "/com/greathammer/eqm/view/resource/weather/wu.png";
				} else if ("浓雾".equals(tianQi)) {
					tianQiImage = "/com/greathammer/eqm/view/resource/weather/nongwu.png";
				} else if ("霾".equals(tianQi)) {
					tianQiImage = "/com/greathammer/eqm/view/resource/weather/mai.png";
				} else if ("雨夹雪".equals(tianQi)) {
					tianQiImage = "/com/greathammer/eqm/view/resource/weather/yujiaxue.png";
				} else if ("小雪".equals(tianQi)) {
					tianQiImage = "/com/greathammer/eqm/view/resource/weather/xiaoxue.png";
				} else if ("中雪".equals(tianQi)) {
					tianQiImage = "/com/greathammer/eqm/view/resource/weather/zhongxue.png";
				} else if ("大雪".equals(tianQi)) {
					tianQiImage = "/com/greathammer/eqm/view/resource/weather/daxue.png";
				} else if ("暴雪".equals(tianQi)) {
					tianQiImage = "/com/greathammer/eqm/view/resource/weather/baoxue.png";
				} else if ("冻雨".equals(tianQi)) {
					tianQiImage = "/com/greathammer/eqm/view/resource/weather/dongyu.png";
				} else if ("霜冻".equals(tianQi)) {
					tianQiImage = "/com/greathammer/eqm/view/resource/weather/shuangdong.png";
				} else if ("4级风".equals(tianQi)) {
					tianQiImage = "/com/greathammer/eqm/view/resource/weather/4jifeng.png";
				} else if ("5级风".equals(tianQi)) {
					tianQiImage = "/com/greathammer/eqm/view/resource/weather/5jifeng.png";
				} else if ("6级风".equals(tianQi)) {
					tianQiImage = "/com/greathammer/eqm/view/resource/weather/6jifeng.png";
				} else if ("7级风".equals(tianQi)) {
					tianQiImage = "/com/greathammer/eqm/view/resource/weather/7jifeng.png";
				} else if ("8级风".equals(tianQi)) {
					tianQiImage = "/com/greathammer/eqm/view/resource/weather/8jifeng.png";
				} else if ("9级风".equals(tianQi)) {
					tianQiImage = "/com/greathammer/eqm/view/resource/weather/9jifeng.png";
				} else if ("10级风".equals(tianQi)) {
					tianQiImage = "/com/greathammer/eqm/view/resource/weather/10jifeng.png";
				} else if ("11级风".equals(tianQi)) {
					tianQiImage = "/com/greathammer/eqm/view/resource/weather/11jifeng.png";
				} else if ("12级风".equals(tianQi)) {
					tianQiImage = "/com/greathammer/eqm/view/resource/weather/12jifeng.png";
				} else if ("台风".equals(tianQi)) {
					tianQiImage = "/com/greathammer/eqm/view/resource/weather/taifeng.png";
				} else if ("浮尘".equals(tianQi)) {
					tianQiImage = "/com/greathammer/eqm/view/resource/weather/fuchen.png";
				} else if ("扬沙".equals(tianQi)) {
					tianQiImage = "/com/greathammer/eqm/view/resource/weather/yangsha.png";
				} else if ("沙尘暴".equals(tianQi)) {
					tianQiImage = "/com/greathammer/eqm/view/resource/weather/shachenbao.png";
				} else {
					tianQiImage = "/com/greathammer/eqm/view/resource/weather/qing_baitian.png";
				}
				return tianQiImage;
			}
		}).start();

		log.info("已经绑定天气数据源");
	}

	@SuppressWarnings("rawtypes")
	public void initComponents() {
		bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

		jButton1 = new javax.swing.JButton();
		jButton2 = new javax.swing.JButton();
		jButton3 = new javax.swing.JButton();
		jButton4 = new javax.swing.JButton();
		dateMessage1 = new com.greathammer.eqm.date.DateMessage();
		weatherMessage1 = new com.greathammer.eqm.weather.WeatherMessage();
		jPanel2 = new javax.swing.JPanel();
		jPanel3 = new javax.swing.JPanel();
		jPanel9 = new javax.swing.JPanel();
		jPanel4 = new javax.swing.JPanel();
		jPanel11 = new javax.swing.JPanel();
		time = new javax.swing.JLabel();
		jLabel4 = new javax.swing.JLabel();
		nongLi = new javax.swing.JLabel();
		week = new javax.swing.JLabel();
		date = new javax.swing.JLabel();
		jPanel12 = new javax.swing.JPanel();
		jPanel10 = new javax.swing.JPanel();
		jPanel13 = new javax.swing.JPanel();
		city = new javax.swing.JLabel();
		jLabel9 = new javax.swing.JLabel();
		tianqitu = new javax.swing.JButton();
		status = new javax.swing.JLabel();
		temperature = new javax.swing.JLabel();
		jLabel13 = new javax.swing.JLabel();
		pollution = new javax.swing.JLabel();
		jPanel16 = new javax.swing.JPanel();
		direction = new javax.swing.JLabel();
		power = new javax.swing.JLabel();
		jLabel18 = new javax.swing.JLabel();
		jPanel15 = new javax.swing.JPanel();
		jLabel14 = new javax.swing.JLabel();
		jLabel15 = new javax.swing.JLabel();
		jPanel17 = new javax.swing.JPanel();
		title = new javax.swing.JLabel();
		lishtRed = new javax.swing.JLabel();
		lightOrange = new javax.swing.JLabel();
		lightYellow = new javax.swing.JLabel();
		lightBlue = new javax.swing.JLabel();

		jButton1.setBackground(new java.awt.Color(255, 51, 51));
		jButton1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255)));

		jButton2.setBackground(new java.awt.Color(255, 153, 0));

		jButton3.setBackground(new java.awt.Color(255, 255, 153));

		jButton4.setBackground(new java.awt.Color(51, 153, 255));

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		jPanel2.setBackground(new java.awt.Color(0, 0, 0));
		jPanel2.setMaximumSize(null);
		jPanel2.setPreferredSize(new java.awt.Dimension(1484, 861));

		jPanel3.setBackground(new java.awt.Color(0, 0, 0));

		javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
		jPanel3.setLayout(jPanel3Layout);
		jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 0, Short.MAX_VALUE));
		jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 0, Short.MAX_VALUE));

		jPanel9.setBackground(new java.awt.Color(0, 0, 0));
		jPanel9.setPreferredSize(new java.awt.Dimension(1420, 712));

		jPanel4.setBackground(new java.awt.Color(102, 102, 102));

		jPanel11.setBackground(new java.awt.Color(102, 102, 102));

		time.setFont(new java.awt.Font("宋体", 0, 130)); // NOI18N
		time.setForeground(new java.awt.Color(255, 204, 153));
		time.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

		org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
				org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, dateMessage1,
				org.jdesktop.beansbinding.ELProperty.create("${time}"), time,
				org.jdesktop.beansbinding.BeanProperty.create("text"));
		bindingGroup.addBinding(binding);

		jLabel4.setFont(new java.awt.Font("宋体", 1, 60)); // NOI18N
		jLabel4.setForeground(new java.awt.Color(255, 204, 153));
		jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

		binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
				org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, dateMessage1,
				org.jdesktop.beansbinding.ELProperty.create("${upDown}"), jLabel4,
				org.jdesktop.beansbinding.BeanProperty.create("text"));
		bindingGroup.addBinding(binding);

		nongLi.setFont(new java.awt.Font("宋体", 1, 60)); // NOI18N
		nongLi.setForeground(new java.awt.Color(255, 204, 153));
		nongLi.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

		binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
				org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, dateMessage1,
				org.jdesktop.beansbinding.ELProperty.create("${nongli}"), nongLi,
				org.jdesktop.beansbinding.BeanProperty.create("text"));
		bindingGroup.addBinding(binding);

		week.setFont(new java.awt.Font("宋体", 1, 60)); // NOI18N
		week.setForeground(new java.awt.Color(255, 204, 153));
		week.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

		binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
				org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, dateMessage1,
				org.jdesktop.beansbinding.ELProperty.create("${week}"), week,
				org.jdesktop.beansbinding.BeanProperty.create("text"));
		bindingGroup.addBinding(binding);

		date.setFont(new java.awt.Font("宋体", 1, 60)); // NOI18N
		date.setForeground(new java.awt.Color(255, 204, 153));
		date.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

		binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
				org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, dateMessage1,
				org.jdesktop.beansbinding.ELProperty.create("${date}"), date,
				org.jdesktop.beansbinding.BeanProperty.create("text"));
		bindingGroup.addBinding(binding);

		javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
		jPanel11.setLayout(jPanel11Layout);
		jPanel11Layout.setHorizontalGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel11Layout.createSequentialGroup().addContainerGap()
						.addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(nongLi, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(week, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(date, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addContainerGap())
				.addGroup(jPanel11Layout.createSequentialGroup().addGap(170, 170, 170)
						.addComponent(time, javax.swing.GroupLayout.PREFERRED_SIZE, 384,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(46, 46, 46).addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 159,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(166, Short.MAX_VALUE)));
		jPanel11Layout.setVerticalGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel11Layout.createSequentialGroup().addGap(56, 56, 56)
						.addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								.addComponent(time, javax.swing.GroupLayout.PREFERRED_SIZE, 144,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 144,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(48, 48, 48)
						.addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, 87,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(30, 30, 30)
						.addComponent(week, javax.swing.GroupLayout.PREFERRED_SIZE, 87,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(28, 28, 28)
						.addComponent(nongLi, javax.swing.GroupLayout.PREFERRED_SIZE, 87,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		jPanel12.setBackground(new java.awt.Color(102, 102, 102));

		javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
		jPanel12.setLayout(jPanel12Layout);
		jPanel12Layout.setHorizontalGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 0, Short.MAX_VALUE));
		jPanel12Layout.setVerticalGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 0, Short.MAX_VALUE));

		javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
		jPanel4.setLayout(jPanel4Layout);
		jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel4Layout.createSequentialGroup().addContainerGap()
						.addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addContainerGap()));
		jPanel4Layout
				.setVerticalGroup(
						jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(jPanel4Layout.createSequentialGroup().addContainerGap()
										.addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, 565,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(18, 18, 18)
										.addComponent(jPanel12, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addContainerGap()));

		jPanel10.setBackground(new java.awt.Color(102, 102, 102));

		jPanel13.setBackground(new java.awt.Color(102, 102, 102));
		jPanel13.setPreferredSize(new java.awt.Dimension(607, 316));

		city.setFont(new java.awt.Font("宋体", 1, 80)); // NOI18N
		city.setForeground(new java.awt.Color(255, 0, 0));
		city.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

		binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
				org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, weatherMessage1,
				org.jdesktop.beansbinding.ELProperty.create("${city}"), city,
				org.jdesktop.beansbinding.BeanProperty.create("text"));
		bindingGroup.addBinding(binding);

		jLabel9.setFont(new java.awt.Font("宋体", 1, 80)); // NOI18N
		jLabel9.setForeground(new java.awt.Color(255, 0, 0));

		binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
				org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, weatherMessage1,
				org.jdesktop.beansbinding.ELProperty.create("${currentTemp}"), jLabel9,
				org.jdesktop.beansbinding.BeanProperty.create("text"));
		bindingGroup.addBinding(binding);

		tianqitu.setBackground(new java.awt.Color(102, 102, 102));
		tianqitu.setIcon(
				new javax.swing.ImageIcon(getClass().getResource("/com/greathammer/eqm/view/resource/duoyun.png"))); // NOI18N
		tianqitu.setBorder(null);
		tianqitu.setMaximumSize(new java.awt.Dimension(82, 82));
		tianqitu.setMinimumSize(new java.awt.Dimension(82, 82));
		tianqitu.setPreferredSize(new java.awt.Dimension(82, 82));

		status.setFont(new java.awt.Font("宋体", 1, 80)); // NOI18N
		status.setForeground(new java.awt.Color(255, 0, 0));

		binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
				org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, weatherMessage1,
				org.jdesktop.beansbinding.ELProperty.create("${status}"), status,
				org.jdesktop.beansbinding.BeanProperty.create("text"));
		bindingGroup.addBinding(binding);

		temperature.setFont(new java.awt.Font("宋体", 1, 45)); // NOI18N
		temperature.setForeground(new java.awt.Color(255, 0, 0));

		binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
				org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, weatherMessage1,
				org.jdesktop.beansbinding.ELProperty.create("${temperature}"), temperature,
				org.jdesktop.beansbinding.BeanProperty.create("text"));
		bindingGroup.addBinding(binding);

		jLabel13.setFont(new java.awt.Font("宋体", 1, 45)); // NOI18N
		jLabel13.setForeground(new java.awt.Color(51, 255, 204));
		jLabel13.setText("空气质量");

		pollution.setFont(new java.awt.Font("宋体", 1, 45)); // NOI18N
		pollution.setForeground(new java.awt.Color(0, 255, 204));

		binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
				org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, weatherMessage1,
				org.jdesktop.beansbinding.ELProperty.create("${pollution}"), pollution,
				org.jdesktop.beansbinding.BeanProperty.create("text"));
		bindingGroup.addBinding(binding);

		jPanel16.setBackground(new java.awt.Color(102, 102, 102));

		direction.setFont(new java.awt.Font("宋体", 1, 45)); // NOI18N
		direction.setForeground(new java.awt.Color(51, 255, 204));

		binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
				org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, weatherMessage1,
				org.jdesktop.beansbinding.ELProperty.create("${direction}"), direction,
				org.jdesktop.beansbinding.BeanProperty.create("text"));
		bindingGroup.addBinding(binding);

		power.setFont(new java.awt.Font("宋体", 1, 40)); // NOI18N
		power.setForeground(new java.awt.Color(51, 255, 204));

		binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
				org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, weatherMessage1,
				org.jdesktop.beansbinding.ELProperty.create("${power}"), power,
				org.jdesktop.beansbinding.BeanProperty.create("text"));
		bindingGroup.addBinding(binding);

		jLabel18.setFont(new java.awt.Font("宋体", 1, 40)); // NOI18N
		jLabel18.setForeground(new java.awt.Color(51, 255, 204));
		jLabel18.setText("");

		javax.swing.GroupLayout jPanel16Layout = new javax.swing.GroupLayout(jPanel16);
		jPanel16.setLayout(jPanel16Layout);
		jPanel16Layout.setHorizontalGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel16Layout.createSequentialGroup()
						.addComponent(direction, javax.swing.GroupLayout.PREFERRED_SIZE, 252,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(18, 18, 18)
						.addComponent(power).addGap(18, 18, 18).addComponent(jLabel18,
								javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(48, Short.MAX_VALUE)));
		jPanel16Layout.setVerticalGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel16Layout.createSequentialGroup().addContainerGap().addGroup(jPanel16Layout
						.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(direction)
						.addGroup(jPanel16Layout.createSequentialGroup()
								.addGroup(jPanel16Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel18)
										.addComponent(power, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
								.addGap(0, 10, Short.MAX_VALUE)))));

		javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
		jPanel13.setLayout(jPanel13Layout);
		jPanel13Layout.setHorizontalGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						jPanel13Layout.createSequentialGroup().addGap(26, 26, 26)
								.addComponent(city, javax.swing.GroupLayout.PREFERRED_SIZE, 446,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 189,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(189, 189, 189))
				.addGroup(jPanel13Layout.createSequentialGroup().addGap(150, 150, 150).addGroup(jPanel13Layout
						.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(jPanel13Layout.createSequentialGroup().addComponent(jLabel13).addGap(60, 60, 60)
								.addComponent(pollution, javax.swing.GroupLayout.PREFERRED_SIZE, 191,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGroup(jPanel13Layout.createSequentialGroup().addGroup(jPanel13Layout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
								.addComponent(jPanel16, javax.swing.GroupLayout.Alignment.LEADING,
										javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGroup(javax.swing.GroupLayout.Alignment.LEADING,
										jPanel13Layout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
												.addComponent(temperature, javax.swing.GroupLayout.Alignment.LEADING,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(status, javax.swing.GroupLayout.Alignment.LEADING,
														javax.swing.GroupLayout.PREFERRED_SIZE, 245,
														javax.swing.GroupLayout.PREFERRED_SIZE)))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(tianqitu, javax.swing.GroupLayout.PREFERRED_SIZE, 192,
										javax.swing.GroupLayout.PREFERRED_SIZE)))
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		jPanel13Layout.setVerticalGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
						.addGap(34, 34, 34)
						.addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								.addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(city, javax.swing.GroupLayout.PREFERRED_SIZE, 119,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
						.addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								.addGroup(jPanel13Layout.createSequentialGroup()
										.addComponent(status, javax.swing.GroupLayout.PREFERRED_SIZE, 105,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(35, 35, 35).addComponent(temperature,
												javax.swing.GroupLayout.PREFERRED_SIZE, 60,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addComponent(tianqitu, javax.swing.GroupLayout.PREFERRED_SIZE, 200,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18)
						.addComponent(jPanel16, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(18, 18, 18)
						.addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
								.addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(pollution, javax.swing.GroupLayout.PREFERRED_SIZE, 68,
										javax.swing.GroupLayout.PREFERRED_SIZE))
						.addContainerGap()));

		javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
		jPanel10.setLayout(jPanel10Layout);
		jPanel10Layout.setHorizontalGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
						jPanel10Layout.createSequentialGroup().addContainerGap()
								.addComponent(jPanel13, javax.swing.GroupLayout.DEFAULT_SIZE, 917, Short.MAX_VALUE)
								.addContainerGap()));
		jPanel10Layout.setVerticalGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel10Layout.createSequentialGroup().addContainerGap()
						.addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, 563,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
		jPanel9.setLayout(jPanel9Layout);
		jPanel9Layout.setHorizontalGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel9Layout.createSequentialGroup().addContainerGap()
						.addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addContainerGap()));
		jPanel9Layout.setVerticalGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel9Layout.createSequentialGroup().addContainerGap()
						.addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						.addContainerGap()));

		jPanel15.setBackground(new java.awt.Color(0, 0, 0));
		jPanel15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153), 5));
		jPanel15.setPreferredSize(new java.awt.Dimension(1420, 67));

		jLabel14.setFont(new java.awt.Font("黑体", 1, 36)); // NOI18N
		jLabel14.setForeground(new java.awt.Color(255, 255, 255));
		jLabel14.setText("青岛地震局");

		jLabel15.setFont(new java.awt.Font("黑体", 0, 36)); // NOI18N
		jLabel15.setForeground(new java.awt.Color(255, 255, 255));
		jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
		jLabel15.setText("技术开发：青岛科信安全技术有限公司");

		javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
		jPanel15.setLayout(jPanel15Layout);
		jPanel15Layout.setHorizontalGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel15Layout.createSequentialGroup().addContainerGap()
						.addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 836,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 127, Short.MAX_VALUE)
						.addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 896,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap()));
		jPanel15Layout.setVerticalGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE)
				.addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
						Short.MAX_VALUE));

		jPanel17.setBackground(new java.awt.Color(0, 0, 0));
		jPanel17.setForeground(new java.awt.Color(255, 255, 255));

		title.setFont(new java.awt.Font("黑体", 1, 70)); // NOI18N
		title.setForeground(new java.awt.Color(255, 255, 255));
		title.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
		title.setText("地 震 预 警 信 息 发 布");
		title.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

		lishtRed.setIcon(
				new javax.swing.ImageIcon(getClass().getResource("/com/greathammer/eqm/view/resource/v2/red.jpg"))); // NOI18N
		lishtRed.setText("jLabel2");

		lightOrange.setIcon(
				new javax.swing.ImageIcon(getClass().getResource("/com/greathammer/eqm/view/resource/v2/orange.jpg"))); // NOI18N
		lightOrange.setText("jLabel2");

		lightYellow.setIcon(
				new javax.swing.ImageIcon(getClass().getResource("/com/greathammer/eqm/view/resource/v2/yellow.jpg"))); // NOI18N
		lightYellow.setText("jLabel2");

		lightBlue.setIcon(
				new javax.swing.ImageIcon(getClass().getResource("/com/greathammer/eqm/view/resource/v2/blue.jpg"))); // NOI18N
		lightBlue.setText("jLabel2");

		javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
		jPanel17.setLayout(jPanel17Layout);
		jPanel17Layout.setHorizontalGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel17Layout.createSequentialGroup().addContainerGap(188, Short.MAX_VALUE)
						.addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
										jPanel17Layout.createSequentialGroup()
												.addComponent(lightBlue, javax.swing.GroupLayout.PREFERRED_SIZE, 232,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addGap(200, 200, 200)
												.addComponent(lightYellow, javax.swing.GroupLayout.PREFERRED_SIZE, 232,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addGap(200, 200, 200)
												.addComponent(lightOrange, javax.swing.GroupLayout.PREFERRED_SIZE, 232,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addGap(200, 200, 200)
												.addComponent(lishtRed, javax.swing.GroupLayout.PREFERRED_SIZE,
														232, javax.swing.GroupLayout.PREFERRED_SIZE)
												.addGap(186, 186, 186))
								.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
										jPanel17Layout.createSequentialGroup()
												.addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 936,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addGap(477, 477, 477)))));
		jPanel17Layout.setVerticalGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel17Layout.createSequentialGroup().addContainerGap()
						.addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 85,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(lightBlue)
								.addComponent(lightOrange, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lishtRed, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(lightYellow, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))));

		javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
		jPanel2.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout
						.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
						.addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, 1908, Short.MAX_VALUE)
						.addGroup(jPanel2Layout.createSequentialGroup()
								.addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
						.addGap(32, 32, 32))
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
						jPanel2Layout.createSequentialGroup().addGap(0, 0, Short.MAX_VALUE).addComponent(jPanel15,
								javax.swing.GroupLayout.PREFERRED_SIZE, 1889, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(43, 43, 43)));
		jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel2Layout.createSequentialGroup()
						.addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(jPanel2Layout.createSequentialGroup().addGap(255, 255, 255).addComponent(
										jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
								.addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 592, Short.MAX_VALUE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel15,
								javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addGap(20, 20, 20)));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addGap(0, 0, 0)
						.addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 1920,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 1028, Short.MAX_VALUE));

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
			java.util.logging.Logger.getLogger(ScreensaverJFrame.class.getName()).log(java.util.logging.Level.SEVERE,
					null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(ScreensaverJFrame.class.getName()).log(java.util.logging.Level.SEVERE,
					null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(ScreensaverJFrame.class.getName()).log(java.util.logging.Level.SEVERE,
					null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(ScreensaverJFrame.class.getName()).log(java.util.logging.Level.SEVERE,
					null, ex);
		}

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new ScreensaverJFrame().setVisible(true);
			}
		});
	}

	private javax.swing.JLabel city;
	private javax.swing.JLabel date;
	private com.greathammer.eqm.date.DateMessage dateMessage1;
	private javax.swing.JLabel direction;
	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;
	private javax.swing.JButton jButton3;
	private javax.swing.JButton jButton4;
	private javax.swing.JLabel jLabel13;
	private javax.swing.JLabel jLabel14;
	private javax.swing.JLabel jLabel15;
	private javax.swing.JLabel jLabel18;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel9;
	private javax.swing.JPanel jPanel10;
	private javax.swing.JPanel jPanel11;
	private javax.swing.JPanel jPanel12;
	private javax.swing.JPanel jPanel13;
	private javax.swing.JPanel jPanel15;
	private javax.swing.JPanel jPanel16;
	private javax.swing.JPanel jPanel17;
	private javax.swing.JPanel jPanel2;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel4;
	private javax.swing.JPanel jPanel9;
	private javax.swing.JLabel lightBlue;
	private javax.swing.JLabel lightOrange;
	private javax.swing.JLabel lightYellow;
	private javax.swing.JLabel lishtRed;
	private javax.swing.JLabel nongLi;
	private javax.swing.JLabel pollution;
	private javax.swing.JLabel power;
	private javax.swing.JLabel status;
	private javax.swing.JLabel temperature;
	private javax.swing.JButton tianqitu;
	private javax.swing.JLabel time;
	private javax.swing.JLabel title;
	private com.greathammer.eqm.weather.WeatherMessage weatherMessage1;
	private javax.swing.JLabel week;
	private org.jdesktop.beansbinding.BindingGroup bindingGroup;

	@Override
	public BaseJFrame getInstance() {
		return this;
	}

	@Override
	public JLabel getStatusLabel() {
		return jLabel14;
	}
}
