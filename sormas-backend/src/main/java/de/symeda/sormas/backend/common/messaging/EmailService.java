package de.symeda.sormas.backend.common.messaging;

import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.symeda.sormas.backend.common.ConfigFacadeEjb.ConfigFacadeEjbLocal;

@Stateless(name = "EmailService")
@LocalBean
public class EmailService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Resource(name = "mail/MailSession")
	private Session mailSession;

	@EJB
	private ConfigFacadeEjbLocal configFacade;

	@Asynchronous
	public void sendEmail(String recipient, String subject, String content) throws AddressException, MessagingException {

		Thread.currentThread().setContextClassLoader(getClass().getClassLoader());

		MimeMessage message = new MimeMessage(mailSession);

		String senderAddress = configFacade.getEmailSenderAddress();
		String senderName = configFacade.getEmailSenderName();

		try {
			InternetAddress fromAddress = new InternetAddress(senderAddress, senderName);
			message.setFrom(fromAddress);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		}

		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient, false));

		message.setSubject(subject, "UTF-8");
		message.setContent(content, "text/plain; charset=utf-8");

		Transport.send(message);
		logger.info("Mail sent to {}.", recipient);
	}
}
