package br.com.browseframework.util.mail;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public abstract class AbstractEMailSender {

	/**
	 * Creats a simple message to send.
	 * @param from
	 * @param to
	 * @param subject
	 * @param message
	 */
	public void sendMail(String from, String to, String subject, String message) {
		final SimpleMailMessage msg = new SimpleMailMessage();
		
		msg.setTo(to);
		msg.setFrom(from);
		msg.setSubject(subject);
		msg.setText(message);

		getMailSender().send(msg);
	}
	
	/**
	 * Retrieves the mail sender
	 * @return
	 */
	protected abstract MailSender getMailSender();
}
