/*
 * 


 */
package com.greathammer.eqm.util;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author FelixYin
 */
public class AudioPlayWaveUtil extends Thread {

	private Log log = LogFactory.getLog(AudioPlayWaveUtil.class);

	private String fileName; // 播放文件名
	private Position curPosition; // 声道
	private final int EXTERNAL_BUFFER_SIZE = 524288; // 128k

	enum Position { // 声道
		LEFT, RIGHT, NORMAL
	};

	// 构造函数
	public AudioPlayWaveUtil(String wavFile) {
		this.fileName = wavFile;
		curPosition = Position.NORMAL;
	}

	public void run() {
		URL soundFile = getClass().getResource(fileName);
		// File soundFile = new File(fileName); // 播放的文件名
		// if (!soundFile.exists()) {
		// System.err.println("Wave file not found:" + fileName);
		// return;
		// }

		AudioInputStream audioInputStream = null; // 创建音频输入流对象
		try {
			audioInputStream = AudioSystem.getAudioInputStream(soundFile); // 创建音频对象
		} catch (UnsupportedAudioFileException e1) {
			log.error(e1);
			return;
		} catch (IOException e1) {
			log.error(e1);
			return;
		}

		AudioFormat format = audioInputStream.getFormat(); // 音频格式
		SourceDataLine auline = null; // 源数据线
		DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

		try {
			auline = (SourceDataLine) AudioSystem.getLine(info);
			auline.open(format);
		} catch (LineUnavailableException e) {
			log.error(e);
			return;
		} catch (Exception e) {
			log.error(e);
			return;
		}

		if (auline.isControlSupported(FloatControl.Type.PAN)) {
			FloatControl pan = (FloatControl) auline.getControl(FloatControl.Type.PAN);
			if (curPosition == Position.RIGHT) {
				pan.setValue(1.0f);
			} else if (curPosition == Position.LEFT) {
				pan.setValue(-1.0f);
			}
		}

		auline.start();
		int nBytesRead = 0;
		byte[] abData = new byte[EXTERNAL_BUFFER_SIZE];

		try {
			while (nBytesRead != -1) {
				nBytesRead = audioInputStream.read(abData, 0, abData.length);
				if (nBytesRead >= 0) {
					auline.write(abData, 0, nBytesRead);
				}
			}
		} catch (IOException e) {
			log.error(e);
			return;
		} finally {
			auline.drain();
			auline.close();
		}
	}

}
