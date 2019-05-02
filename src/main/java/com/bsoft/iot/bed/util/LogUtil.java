package com.bsoft.iot.bed.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {
	public static <T> Logger log(Class<T> clazz) {
		
		return LoggerFactory.getLogger(clazz);
	}
}
