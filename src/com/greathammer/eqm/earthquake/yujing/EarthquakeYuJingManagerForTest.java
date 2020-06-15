package com.greathammer.eqm.earthquake.yujing;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greathammer.eqm.util.Constant;

/**
 * 本程序接收地震预警信息，演示专用
 *
 * @author FelixYin
 */
public class EarthquakeYuJingManagerForTest implements IEarthquakeYuJingManager {

	private Log log = LogFactory.getLog(EarthquakeYuJingManagerForTest.class);

	private EarthquakeYuJingListener listener = null;
	List<EarthquakeYuJing> list = new ArrayList<EarthquakeYuJing>();
	String text = "";
	int i = 0;

	public EarthquakeYuJingManagerForTest() {
	}

	public EarthquakeYuJingManagerForTest(EarthquakeYuJingListener listener) {
		this.listener = listener;
		this.list = new EarthquakeqTestData().getList();
		this.i = this.list.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.greathammer.eqm.earthquake.yujing.IEarthquakeYuJingManager#
	 * setListener(com.greathammer.eqm.earthquake.yujing.
	 * EarthquakeYuJingListener)
	 */
	@Override
	public IEarthquakeYuJingManager setListener(EarthquakeYuJingListener listener) {
		this.listener = listener;
		this.list = new EarthquakeqTestData().getList();
		this.i = this.list.size() - 1;
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.greathammer.eqm.earthquake.yujing.IEarthquakeYuJingManager#start()
	 */
	@Override
	public void start() {
		// Constant.setRealEarthquake(false);
		if (isYanXi)
			return;
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if (i < 0 && !Constant.IS_LOOP_FOR_TEST) {
					return;
				} else if (i < 0 && Constant.IS_LOOP_FOR_TEST) {
					i = list.size() - 1;
				}

				EarthquakeYuJing eMessage = list.get(i);

				// new Thread(new myThread(eMessage)).start();
				i = i - 1;

				ObjectMapper mapper = new ObjectMapper();
				try {
					text = mapper.writeValueAsString(eMessage);
				} catch (JsonProcessingException ex) {
					log.error(ex);
				}

				listener.doEvent(eMessage, text);
			}
		}, Constant.TEST_EARTHQUAKE_FIRST_TIME, Constant.TEST_EARTHQUAKE_TIME);

	}

	class myThread implements Runnable {

		private EarthquakeYuJing message;

		int arrivalTime = 0;

		public myThread(EarthquakeYuJing message) {
			this.message = message;
			this.arrivalTime = message.getArrivalTime();
		}

		@Override
		public void run() {
			if (isYanXi)
				return;
			final Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					if (arrivalTime > 0) {
						arrivalTime = arrivalTime - 1;
						message.setArrivalTime((arrivalTime));
					} else {
						timer.cancel();
					}
				}
			}, 1000, 1000);

		}
	}

	public static void main(String[] args) {
	}

	private boolean isYanXi = false;

	@Override
	public void start(int i) {
		// Constant.setRealEarthquake(false);

		this.isYanXi = true;
		// do nothing
		EarthquakeYuJing eMessage = list.get(i);
		eMessage.setOtime("演习时间");

		ObjectMapper mapper = new ObjectMapper();
		try {
			text = mapper.writeValueAsString(eMessage);
		} catch (JsonProcessingException ex) {
			log.error(ex);
		}

		listener.doEvent(eMessage, text);
	}

	@Override
	public void start(String eqData) {
		// Constant.setRealEarthquake(false);
	}

}
