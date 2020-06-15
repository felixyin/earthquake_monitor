package com.greathammer.serial.log;

public class DefaultLogService implements LogService {

	@Override
	public <T> void debugLog(T logThings) {
		System.out.println(logThings);
	}

	@Override
	public <T> void releaseLog(T logThings) {
	}

}
