package org.apache.james.jamesui.backend.client.mailstore;

public class Email {
	
	private String subject;
	private String content;
	private String from;
	private String sentDate;
	private int size;
	
	public Email(){
		
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getSentDate() {
		return sentDate;
	}

	public void setSentDate(String sentDate) {
		this.sentDate = sentDate;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
   }


}
