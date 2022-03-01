package com.synectiks.procurement.controllers;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MailsController {

	@PostMapping("/latestEmail")
	public static void latestEmail(String pop3Host, String storeType, String user, String password) {
		try {
			Properties properties = new Properties();
			properties.setProperty("mail.store.protocol", "pop3s");
			properties.setProperty("mail.pop3s.host", pop3Host);
			properties.setProperty("mail.pop3s.port", "995");
			properties.put("mail.pop3.starttles.enable", "true");
			Session emailSession = Session.getDefaultInstance(properties);
			Store store = emailSession.getStore("pop3s");
			store.connect(pop3Host, user, password);
			Folder emailFolder = store.getFolder("INBOX");
			emailFolder.open(Folder.READ_ONLY);
			Message messages[] = emailFolder.getMessages();
			int i = ((messages.length) - 1);
			Message message = messages[i];
			System.out.println("---------------------------------");
			System.out.println("Email Number " + (i + 1));
			System.out.println("Subject: " + message.getSubject());
			System.out.println("From: " + message.getFrom()[0]);
			System.out.println("Text: " + message.getContent().toString());
			Date receivedDate = message.getReceivedDate();
			System.out.println("Received Date : " + receivedDate.toString());
			emailFolder.close(true);
			store.close();

		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@PostMapping("/getAllMail")
	public static void receiveEmail(String pop3Host, String storeType, String user, String password) {
		try {
			Properties properties = new Properties();
			properties.setProperty("mail.store.protocol", "pop3s");
			properties.setProperty("mail.pop3s.host", pop3Host);
			properties.setProperty("mail.pop3s.port", "995");

			properties.put("mail.pop3.starttles.enable", "true");
			Session emailSession = Session.getDefaultInstance(properties);
			Store store = emailSession.getStore("pop3s");
			store.connect(pop3Host, user, password);

			Folder emailFolder = store.getFolder("INBOX");
			emailFolder.open(Folder.READ_ONLY);
			Message messages[] = emailFolder.getMessages();
			for (int i = 0; i < messages.length; i++) {
				Message message = messages[i];
				System.out.println("---------------------------------");
				System.out.println("Email Number " + (i + 1));
				System.out.println("Subject: " + message.getSubject());
				System.out.println("From: " + message.getFrom()[0]);
				System.out.println("Text: " + message.getContent().toString());
				Date receivedDate = message.getReceivedDate();
				System.out.println("Received Date : " + receivedDate.toString());

			}
			emailFolder.close(false);
			store.close();

		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PostMapping("/draftMail")
	public static void draftMail(String pop3Host, String storeType, String user, String password) {
		Folder draft;
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");
		try {
			Session session = Session.getDefaultInstance(props, null);
			Store store = session.getStore("imaps");
			store.connect("imap.gmail.com", user, password);
			draft = store.getFolder("[Gmail]/Drafts");
			System.out.println("No of Sent Messages : " + draft.getMessageCount());
			draft.open(Folder.READ_ONLY);
			Message messages[] = draft.search(new FlagTerm(new Flags(Flag.SEEN), true));
			FetchProfile fp = new FetchProfile();
			fp.add(FetchProfile.Item.ENVELOPE);
			fp.add(FetchProfile.Item.CONTENT_INFO);
			draft.fetch(messages, fp);
			for (int i = 0; i < messages.length; i++) {
				Message message = messages[i];
				System.out.println("---------------------------------");
				System.out.println("Email Number " + (i + 1));
				System.out.println("Subject: " + message.getSubject());
				System.out.println("From: " + message.getFrom()[0]);
				System.out.println("Text: " + message.getContent().toString());
				Date receivedDate = message.getReceivedDate();
				System.out.println("Received Date : " + receivedDate.toString());
			}
			draft.close(true);
			store.close();

		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@PostMapping("/getSentEmail")
	public static void getSentEmail(String pop3Host, String storeType, String user, String password) {
		Folder sent;
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");

		try {

			Session session = Session.getDefaultInstance(props, null);
			Store store = session.getStore("imaps");
			store.connect("imap.gmail.com", user, password);
			sent = store.getFolder("[Gmail]/Sent Mail");
			System.out.println("No of Sent Messages : " + sent.getMessageCount());
			sent.open(Folder.READ_ONLY);
			Message messages[] = sent.search(new FlagTerm(new Flags(Flag.SEEN), true));

			/* Use a suitable FetchProfile */
			FetchProfile fp = new FetchProfile();
			fp.add(FetchProfile.Item.ENVELOPE);
			fp.add(FetchProfile.Item.CONTENT_INFO);
			sent.fetch(messages, fp);
			printAllMessages(messages);
			sent.close(true);
			store.close();

		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void printAllMessages(Message[] messages) throws IOException, MessagingException {
		for (int i = messages.length - 1; i > 0; i--) {
			System.out.println("MESSAGE #" + (i + 1) + ":");
			printEnvelope(messages[i]);
		}

	}

	private static void printEnvelope(Message message) throws IOException, MessagingException {
		Address[] a;
		// FROM
		if ((a = message.getFrom()) != null) {
			for (int j = 0; j < a.length; j++) {
				System.out.println("FROM: " + a[j].toString());
			}
		}
		// TO
		if ((a = message.getRecipients(Message.RecipientType.TO)) != null) {
			for (int j = 0; j < a.length; j++) {
				System.out.println("TO: " + a[j].toString());
			}
		}
		String subject = message.getSubject();
		Date receivedDate = message.getReceivedDate();
		String content = message.getContent().toString();
		System.out.println("Subject : " + subject);
		System.out.println("Received Date : " + receivedDate.toString());
		System.out.println("Content : " + content);
		getContent(message);
	}

	private static void getContent(Message msg) {
		try {
			String contentType = msg.getContentType();
			System.out.println("Content Type : " + contentType);
			Multipart mp = (Multipart) msg.getContent();
			int count = mp.getCount();
			for (int i = 0; i < count; i++) {
				readMessageToFile(mp.getBodyPart(i));
			}
		} catch (Exception ex) {
			System.out.println("Exception arise at get Content");
			ex.printStackTrace();
		}
	}

	public static void readMessageToFile(Part p) throws Exception {
		// Dump input stream ..
		InputStream is = p.getInputStream();
		// If "is" is not already buffered, wrap a BufferedInputStream
		// around it.
		if (!(is instanceof BufferedInputStream)) {
			is = new BufferedInputStream(is);
		}
		int c;
		System.out.println("Message : ");
		while ((c = is.read()) != -1) {
			System.out.write(c);
		}

	}
}