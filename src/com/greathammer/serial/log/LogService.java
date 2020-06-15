package com.greathammer.serial.log;

public interface LogService {
	public <T> void debugLog(T logThings);

	public <T> void releaseLog(T logThings);
}
