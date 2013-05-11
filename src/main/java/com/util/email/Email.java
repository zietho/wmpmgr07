package com.util.email;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.server.util.ConfigurationHelper;

public class Email {
	private static String PROTOCOL = "smtp";
	private static String HOST = "mail.redefine.at";
	private static String PORT = "25";
	private static String USER = "wienerhelden@redefine.at";
	private static String PASSWORD = "whASEWS2012";
	
	private String subject;
	private String message;
	private String from;
	private String to;
	
	public Email() {
		super();
	}
	
	public Email(String subject, String message, String from, String to) {
		super();
		this.subject = subject;
		this.message = message;
		this.from = from;
		this.to = to;
	}
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	
	public void send() {
		String override = ConfigurationHelper.getValue("overridemailto.active");
		if(override != null && override.equals("true"))
			to = ConfigurationHelper.getValue("overridemailto.address");
		
		if(subject==null || message==null || from==null || to==null || message.equals("") || from.equals("") || to.equals(""))
			return;
		
		
		new SendMailThread(subject, message, from, to).start();
	}
	
	private class SendMailThread extends Thread{
		private String subject;
		private String message;
		private String from;
		private String to;
		
		
		public SendMailThread(String subject, String message, String from,
				String to) {
			super();
			this.subject = subject;
			this.message = message;
			this.from = from;
			this.to = to;
		}

		public void run() {
			Properties props = new Properties();
			props.put("mail.transport.protocol", PROTOCOL);
			props.put("mail.host", HOST);
			props.put("mail.port", PORT);
			props.put("mail.smtp.starttls.enable","false");
			props.put("mail.smtp.debug", "true");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.user", USER);
			props.put("mail.smtp.password", PASSWORD);
			//props.put("mail.smtp.socketFactory.port", PORT);
			//props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			//props.put("mail.smtp.socketFactory.fallback", "false");
	
			SMTPAuthenticator auth = new SMTPAuthenticator();
			Session mailSession = Session.getDefaultInstance(props, auth);
			//Session mailSession = Session.getDefaultInstance(props, null);
			mailSession.setDebug(true);
			Transport transport;
			try {
				transport = mailSession.getTransport();
		
		
				MimeMessage mimemessage = new MimeMessage(mailSession);
				mimemessage.setSubject(subject);
				mimemessage.setFrom(new InternetAddress(from));
				mimemessage.setContent(message , "text/html; charset=ISO-8859-1");
				mimemessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		
				transport.connect();
				transport.sendMessage(mimemessage,
						mimemessage.getRecipients(Message.RecipientType.TO));
				transport.close();
			} catch (NoSuchProviderException e) {
				e.printStackTrace();
				return;
			} catch (MessagingException e) {
				e.printStackTrace();
				return;
			}
		}
	}
	
	private class SMTPAuthenticator extends Authenticator
	{
	    public PasswordAuthentication getPasswordAuthentication()
	    {
	        return new PasswordAuthentication(USER, PASSWORD);
	    }
	}
}
