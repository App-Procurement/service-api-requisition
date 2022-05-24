package com.synectiks.procurement.config;

import org.graylog2.gelfclient.GelfMessage;
import org.graylog2.gelfclient.GelfMessageBuilder;
import org.graylog2.gelfclient.GelfMessageLevel;
import org.graylog2.gelfclient.transport.GelfTransport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.synectiks.procurement.ProcurementApp;

public class CustomLogger {

	private Logger logger;
	
	public CustomLogger(Class<?> clazz) {
		logger = LoggerFactory.getLogger(clazz);
	}
	
	public static CustomLogger getLogger(Class<?> clazz) {
		return new CustomLogger(clazz);
	}
	
	public void debug(String msg) {
		GelfTransport gelfTransport = ProcurementApp.getBean(GelfTransport.class);
		send(msg, gelfTransport, GelfMessageLevel.DEBUG);
		this.logger.debug(msg);
	}
	
	public void info(String msg) {
		GelfTransport gelfTransport = ProcurementApp.getBean(GelfTransport.class);
		send(msg, gelfTransport, GelfMessageLevel.INFO);
		this.logger.info(msg);
	}
	
	public void warn(String msg) {
		GelfTransport gelfTransport = ProcurementApp.getBean(GelfTransport.class);
		send(msg, gelfTransport, GelfMessageLevel.WARNING);
		this.logger.warn(msg);
	}
	
	public void error(String msg) {
		GelfTransport gelfTransport = ProcurementApp.getBean(GelfTransport.class);
		send(msg, gelfTransport, GelfMessageLevel.ERROR);
		this.logger.error(msg);
	}
	
	public void error(String msg, Exception e) {
		GelfTransport gelfTransport = ProcurementApp.getBean(GelfTransport.class);
		send(msg, gelfTransport, GelfMessageLevel.ERROR);
		this.logger.error(msg, e);
	}
	
	public void error(String msg, String e) {
		GelfTransport gelfTransport = ProcurementApp.getBean(GelfTransport.class);
		send(msg, gelfTransport, GelfMessageLevel.ERROR);
		this.logger.error(msg, e);
	}
	
	private void send(String msg, GelfTransport gelfTransport, GelfMessageLevel lvl) {
		GelfMessageBuilder builder = new GelfMessageBuilder(msg, ProcurementApp.getGelfHost())
                .level(lvl);
		GelfMessage message = builder.build();
		try {
			gelfTransport.send(message);
		}catch(InterruptedException e) {
			
		}
	}
	
}
