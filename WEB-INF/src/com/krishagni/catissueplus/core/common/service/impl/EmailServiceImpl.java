package com.krishagni.catissueplus.core.common.service.impl;

import java.io.File;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.integration.mail.ImapMailReceiver;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.OpenSpecimenAppCtxProvider;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.domain.Email;
import com.krishagni.catissueplus.core.common.service.ConfigChangeListener;
import com.krishagni.catissueplus.core.common.service.ConfigurationService;
import com.krishagni.catissueplus.core.common.service.EmailProcessor;
import com.krishagni.catissueplus.core.common.service.EmailService;
import com.krishagni.catissueplus.core.common.service.TemplateService;
import com.krishagni.catissueplus.core.common.util.ConfigUtil;
import com.krishagni.catissueplus.core.common.util.MessageUtil;
import com.krishagni.catissueplus.core.common.util.Utility;

public class EmailServiceImpl implements EmailService, ConfigChangeListener, InitializingBean {
	private static final Log logger = LogFactory.getLog(EmailServiceImpl.class);
	
	private static final String MODULE = "email";
	
	private static final String TEMPLATE_SOURCE = "email-templates/";
	
	private static final String BASE_TMPL = "baseTemplate";
	
	private static final String FOOTER_TMPL = "footer";

	private JavaMailSender mailSender;
	
	private TemplateService templateService;
	
	private ThreadPoolTaskExecutor taskExecutor;
	
	private ConfigurationService cfgSvc;

	private DaoFactory daoFactory;

	private ImapMailReceiver mailReceiver;

	private ScheduledFuture<?> receiverFuture;

	private List<EmailProcessor> processors = new ArrayList<>();

	public void setTemplateService(TemplateService templateService) {
		this.templateService = templateService;
	}

	public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}
	
	public void setCfgSvc(ConfigurationService cfgSvc) {
		this.cfgSvc = cfgSvc;
	}

	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public void onConfigChange(String name, String value) {
		initializeMailSender();

		if (StringUtils.isBlank(name) ||
			name.equals("imap_server_host") ||
			name.equals("imap_server_port") ||
			name.equals("imap_poll_interval")) {
			initializeMailReceiver();
		}
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		initializeMailSender();
		initializeMailReceiver();
		cfgSvc.registerChangeListener(MODULE, this);		
	}
	
	private void initializeMailSender() {
		try {
			JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
			mailSender.setUsername(getAccountId());
			mailSender.setPassword(getAccountPassword());
			mailSender.setHost(getSmtpHost());
			mailSender.setPort(getSmtpPort());

			Properties props = new Properties();
			props.put("mail.smtp.timeout", 10000);
			props.put("mail.smtp.connectiontimeout", 10000);

			String startTlsEnabled = getStartTlsEnabled();
			String authEnabled = getSmtpAuthEnabled();
			if (StringUtils.isNotBlank(startTlsEnabled) && StringUtils.isNotBlank(authEnabled)) {
				props.put("mail.smtp.starttls.enable", startTlsEnabled);
				props.put("mail.smtp.auth", authEnabled);
			}

			mailSender.setJavaMailProperties(props);
			this.mailSender = mailSender;
		} catch (Exception e) {
			logger.error("Error initialising e-mail sender", e);
		}
	}
	
	@Override
	public boolean sendEmail(String emailTmplKey, String[] to, Map<String, Object> props) {
		return sendEmail(emailTmplKey, to, null, props);
	}

	@Override
	public boolean sendEmail(String emailTmplKey, String[] to, File[] attachments, Map<String, Object> props) {
		return sendEmail(emailTmplKey, to, null, attachments, props);
	}

	@Override
	public boolean sendEmail(String emailTmplKey, String[] to, String[] bcc, File[] attachments, Map<String, Object> props) {
		return sendEmail(emailTmplKey, null, to, bcc, attachments, props);
	}

	@Override
	public boolean sendEmail(String emailTmplKey, String emailTmpl, String[] to, Map<String, Object> props) {
		return sendEmail(emailTmplKey, emailTmpl, to, null, null, props);
	}

	@Override
	public boolean sendEmail(Email mail) {
		return sendEmail(mail, null);
	}

	public boolean sendEmail(Email mail, Map<String, Object> props) {
		try {
			if (!isEmailNotifEnabled()) {
				logger.debug("Email notification is disabled. Not sending email: " + mail.getSubject());
				return false;
			}

			if (props == null) {
				props = Collections.emptyMap();
			}


			MimeMessage mimeMessage;
			Map<String, String> replyToHeaders = (Map<String, String>) props.get("$replyToHeaders");
			if (replyToHeaders != null && !replyToHeaders.isEmpty()) {
				MimeMessage parent = mailSender.createMimeMessage();
				for (Map.Entry<String, String> header : replyToHeaders.entrySet()) {
					parent.setHeader(header.getKey(), header.getValue());
				}

				mimeMessage = (MimeMessage) parent.reply(false, true);
			} else {
				mimeMessage = mailSender.createMimeMessage();
			}

			MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
			message.setSubject(mail.getSubject());

			String[] toRcpts = filterEmailIds("To", mail.getToAddress());
			if (toRcpts.length == 0) {
				return false;
			}

			message.setTo(toRcpts);
			
			if (mail.getBccAddress() != null) {
				message.setBcc(filterEmailIds("Bcc", mail.getBccAddress()));
			}
			
			if (mail.getCcAddress() != null) {
				message.setCc(filterEmailIds("Cc", mail.getCcAddress()));
			}
			
			message.setText(mail.getBody(), true); // true = isHtml
			message.setFrom(getAccountId());
			
			if (mail.getAttachments() != null) {
				for (File attachment: mail.getAttachments()) {
					FileSystemResource file = new FileSystemResource(attachment);
					message.addAttachment(file.getFilename(), file);
				}
			}

			logger.info("Invoking task executor to send e-mail asynchronously: " + mimeMessage.getSubject());
			taskExecutor.submit(new SendMailTask(mimeMessage));
			return true;
		} catch (Exception e) {
			logger.error("Error sending e-mail", e);
			return false;
		}
	}

	@Override
	public void registerProcessor(EmailProcessor processor) {
		if (processors.contains(processor)) {
			return;
		}

		processors.add(processor);
		if (processors.size() == 1) {
			initializeMailReceiver();
		}
	}

	private boolean sendEmail(String tmplKey, String tmplContent, String[] to, String[] bcc, File[] attachments, Map<String, Object> props) {
		if (!isEmailNotifEnabled()) {
			return false;
		}

		boolean emailEnabled = cfgSvc.getBoolSetting("notifications", "email_" + tmplKey, true);
		if (!emailEnabled) {
			return false;
		}

		String adminEmailId = getAdminEmailId();

		if (StringUtils.isNotBlank(tmplContent)) {
			props.put("templateContent", tmplContent);
		} else {
			props.put("template", getTemplate(tmplKey));
		}

		props.put("footer", getFooterTmpl());
		props.put("appUrl", getAppUrl());
		props.put("adminEmailAddress", adminEmailId);
		props.put("adminPhone", cfgSvc.getStrSetting("email", "admin_phone_no", "Not Specified"));
		props.put("dateFmt", new SimpleDateFormat(ConfigUtil.getInstance().getDateTimeFmt()));
		props.put("urlEncoder", URLEncoder.class);
		String subject = getSubject(tmplKey, (Object[]) props.get("$subject"));
		String content = templateService.render(getBaseTmpl(), props);

		Email email = new Email();
		email.setSubject(subject);
		email.setBody(content);
		email.setToAddress(to);
		email.setBccAddress(bcc);
		email.setAttachments(attachments);

		boolean ccAdmin = BooleanUtils.toBooleanDefaultIfNull((Boolean) props.get("ccAdmin"), true);
		if (ccAdmin) {
			email.setCcAddress(new String[] { adminEmailId });
		}

		return sendEmail(email, props);
	}
	
	private String getSubject(String subjKey, Object[] subjParams) {
		return getSubjectPrefix() + MessageUtil.getInstance().getMessage(subjKey.toLowerCase() + "_subj", subjParams);
	}

	private String getSubjectPrefix() {
		String subjectPrefix = MessageUtil.getInstance().getMessage("email_subject_prefix");
		String deployEnv = ConfigUtil.getInstance().getStrSetting("common", "deploy_env", "");

		subjectPrefix += " " + StringUtils.substring(deployEnv, 0, 10);
		return "[" + subjectPrefix.trim() + "]: ";
	}

	private class SendMailTask implements Runnable {
		private MimeMessage mimeMessage;
		
		public SendMailTask(MimeMessage mimeMessage) {
			this.mimeMessage = mimeMessage;
		}
		
		public void run() {
			try {
				String rcpts = toString(mimeMessage.getAllRecipients());
				logger.info("Sending email '" + mimeMessage.getSubject() + "' to " + rcpts);
				mailSender.send(mimeMessage);
				logger.info("Email '" + mimeMessage.getSubject() + "' sent to " + rcpts);
			} catch(Exception e) {
				logger.error("Error sending e-mail ", e);
			}
		}

		private String toString(Address[] addresses) {
			return Stream.of(addresses).map(addr -> ((InternetAddress) addr).getAddress()).collect(Collectors.joining(", "));
		}
	}

	private String getTemplate(String tmplKey) {
		String localeTmpl = TEMPLATE_SOURCE + Locale.getDefault().toString() + "/" + tmplKey + ".vm";
		URL url = this.getClass().getClassLoader().getResource(localeTmpl);
		if (url == null) {
			localeTmpl = TEMPLATE_SOURCE + "default/" + tmplKey + ".vm";			
		}
		
		return localeTmpl;
	}
	
	private String getBaseTmpl() {
		return getTemplate(BASE_TMPL);
	}
	
	private String getFooterTmpl() {
		return getTemplate(FOOTER_TMPL);
	}

	private String[] filterEmailIds(String field, String[] emailIds) {
		String[] validEmailIds = filterInvalidEmails(emailIds);
		if (validEmailIds.length == 0) {
			logger.error("Invalid email IDs in " + field + " : " + toString(emailIds));
			return validEmailIds;
		}

		String[] nonContactEmailIds = filterContactEmails(validEmailIds);
		if (nonContactEmailIds.length == 0) {
			logger.debug("Only contact email IDs in " + field + " : " + toString(validEmailIds));
		}

		return nonContactEmailIds;
	}

	private String[] filterInvalidEmails(String[] emailIds) {
		if (emailIds == null) {
			return new String[0];
		}

		return Arrays.stream(emailIds).filter(Utility::isValidEmail).toArray(String[]::new);
	}

	@PlusTransactional
	private String[] filterContactEmails(String[] emailIds) {
		try {
			Map<String, String> emailIdTypes = daoFactory.getUserDao().getEmailIdUserTypes(Arrays.asList(emailIds));
			return Arrays.stream(emailIds).filter(emailId -> !"CONTACT".equals(emailIdTypes.get(emailId))).toArray(String[]::new);
		} catch (Throwable t) {
			logger.error("Error filtering contact email IDs", t);
			return emailIds;
		}
	}

	private String toString(String[] arr) {
		if (arr == null) {
			return StringUtils.EMPTY;
		}

		return StringUtils.join(arr, ",");
	}

	private void initializeMailReceiver() {
		if (mailReceiver != null) {
			mailReceiver = null;
		}

		if (receiverFuture != null) {
			receiverFuture.cancel(false);
			receiverFuture = null;
		}

		if (processors.isEmpty() || StringUtils.isBlank(getImapHost())) {
			logger.info("IMAP service is not configured. Will not poll for inbound emails.");
			return;
		}

		try {
			String url = isSecuredImap() ? "imaps://" : "imap://";
			url += URLEncoder.encode(getAccountId(), "UTF-8") + ":";
			url += URLEncoder.encode(getAccountPassword(), "UTF-8") + "@";
			url += getImapHost() + ":" + getImapPort() + "/" + getFolder().toUpperCase();

			Properties mailProperties = new Properties();
			mailProperties.setProperty("mail.store.protocol", isSecuredImap() ? "imaps" : "imap");

			ImapMailReceiver receiver = new ImapMailReceiver(url);
			receiver.setShouldMarkMessagesAsRead(true);
			receiver.setShouldDeleteMessages(false);
			receiver.setJavaMailProperties(mailProperties);
			receiver.setBeanFactory(OpenSpecimenAppCtxProvider.getAppCtx());
			receiver.afterPropertiesSet();
			mailReceiver = receiver;

			receiverFuture = Executors.newSingleThreadScheduledExecutor().scheduleWithFixedDelay(
				new ReceiveEmailTask(), getPollInterval(), getPollInterval(), TimeUnit.MINUTES);
		} catch (Exception e) {
			logger.error("Error initialising IMAP receiver", e);
		}
	}

	private class ReceiveEmailTask implements Runnable {
		@Override
		public void run() {
			try {
				if (mailReceiver == null) {
					return;
				}

				Object[] messages = mailReceiver.receive();
				for (Object message : messages) {
					handleMessage((MimeMessage) message);
				}
			} catch (Throwable t) {
				logger.error("Error receiving e-mail messages", t);
			}
		}
	}

	private void handleMessage(MimeMessage message) {
		try {
			Email email = toEmail(message);
			for (EmailProcessor processor : processors) {
				try {
					processor.process(email);
				} catch (Throwable t) {
					logger.error("Error processing the email by: " + processor.getName(), t);
				}
			}
		} catch (Throwable t) {
			logger.error("Error handling the email message", t);
		}
	}

	private Email toEmail(MimeMessage message)
	throws Exception {
		Map<String, String> headers = new HashMap<>();
		Enumeration<Header> headersIter = message.getAllHeaders();
		while (headersIter.hasMoreElements()) {
			Header header = headersIter.nextElement();
			headers.put(header.getName(), header.getValue());
		}

		Email email = new Email();
		email.setHeaders(headers);
		email.setSubject(message.getSubject());

		for (Address from : message.getFrom()) {
			if (from instanceof InternetAddress) {
				email.setFromAddress(((InternetAddress) from).getAddress());
			}
		}

		String text = getText(message);
		text = text.replaceAll("\r\n", "\n")
			.replaceAll(STRIP_EMAIL_REPLY_PATTERN, "")
			.trim();
		email.setBody(text);
		return email;
	}

	private String getText(Message message)
	throws Exception {
		if (message.isMimeType("text/plain")) {
			return message.getContent().toString();
		} else if (message.isMimeType("multipart/*")) {
			return getText((MimeMultipart) message.getContent());
		}

		return "";
	}

	private String getText(MimeMultipart mimeMultipart)
	throws Exception {
		int parts = mimeMultipart.getCount();
		String result = "";
		for (int i = 0; i < parts; ++i) {
			BodyPart part = mimeMultipart.getBodyPart(i);
			if (part.isMimeType("text/plain")) {
				result += "\n" + part.getContent().toString();
			} else if (part.getContent() instanceof MimeMultipart) {
				result += "\n" + getText((MimeMultipart) part.getContent());
			}
		}

		return result;
	}

	/**
	 *  Config helper methods
	 */
	private String getAccountId() {
		return cfgSvc.getStrSetting(MODULE, "account_id");
	}
	
	private String getAccountPassword() {
		return cfgSvc.getStrSetting(MODULE, "account_password");
	}
	
	private String getSmtpHost() {
		return cfgSvc.getStrSetting(MODULE, "smtp_server_host");
	}
	
	private Integer getSmtpPort() {
		return cfgSvc.getIntSetting(MODULE, "smtp_server_port", 25);
	}
	
	private String getStartTlsEnabled() {
		return cfgSvc.getStrSetting(MODULE, "starttls_enabled");
	}
	
	private String getSmtpAuthEnabled() {
		return cfgSvc.getStrSetting(MODULE, "smtp_auth_enabled");
	}
	
	private String getAdminEmailId() {
		return cfgSvc.getStrSetting(MODULE, "admin_email_id");
	}	
	
	private String getAppUrl() {
		return cfgSvc.getStrSetting("common", "app_url");
	}

	private boolean isEmailNotifEnabled() {
		return cfgSvc.getBoolSetting("notifications", "all", true);
	}

	private boolean isSecuredImap() {
		return true;
	}

	private String getImapHost() {
		return cfgSvc.getStrSetting(MODULE, "imap_server_host");
	}

	private Integer getImapPort() {
		return cfgSvc.getIntSetting(MODULE, "imap_server_port", 993);
	}

	private String getFolder() {
		return "INBOX";
	}

	private Integer getPollInterval() {
		return cfgSvc.getIntSetting(MODULE, "imap_poll_interval", 5);
	}

	private final static String STRIP_EMAIL_REPLY_PATTERN =
		"(?m)"   + // turn on multi-line regex matching
		"^>.*$|" + // any line starting with > is ignored
		"^On\\s+.*\\s+wrote:"; // strip On Wed, Mar 6, 2019 at 11:25 AM XYZ wrote:
}
