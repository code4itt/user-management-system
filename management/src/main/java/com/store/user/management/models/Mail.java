package com.store.user.management.models;

import java.util.Map;

public class Mail {

	private String mailTo;
	
	//private String mailFrom;
	
	private Map<String, Object> props ;
	
	private String subject;

	
	public String getMailTo() {
		return mailTo;
	}
/*
	public String getMailFrom() {
		return mailFrom;
	}
*/
	public Map<String, Object> getProps() {
		return props;
	}

	public String getSubject() {
		return subject;
	}

	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}
/*
	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}
*/
	public void setProps(Map<String, Object> props) {
		this.props = props;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	
}
