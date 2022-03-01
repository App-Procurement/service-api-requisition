package com.synectiks.procurement.business.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.domain.Requisition;
import com.synectiks.procurement.domain.Roles;
import com.synectiks.procurement.repository.RequisitionRepository;
import com.synectiks.procurement.repository.RolesRepository;

@Service
public class HomeService {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeService.class);

	@Autowired
	private RequisitionRepository requisitionRepository;
	@Autowired
	private RolesRepository rolesRepository;

	public List<Requisition> userdata(ObjectNode user) throws JSONException, MessagingException, IOException {
		
        Roles roles = new Roles();
        if (user.get("role") != null) {
			roles.setName(user.get("role").asText());
		}

        Optional<Roles> rol = rolesRepository.findOne(Example.of(roles));
		if (!rol.isPresent()) {
			logger.error("Role not found");
			return null;
		}
		
		Requisition requisition = new Requisition();
		
		if(user.get("role")!=null) {
		requisition.setCreatedBy(user.get("role").asText());
		}
		List<Requisition> list = requisitionRepository.getAllRequisition("rejected","completed");
//		Properties properties = new Properties();
//		properties.setProperty("mail.store.protocol", "pop3s");
//		properties.setProperty("mail.pop3s.host", user.get("pop3").asText());
//		properties.setProperty("mail.pop3s.port", "995");
//		properties.put("mail.pop3.starttles.enable", "true");
//		Session emailSession = Session.getDefaultInstance(properties);
//		Store store = emailSession.getStore("pop3s");
//		store.connect(user.get("pop3").asText(), user.get("username").asText(), user.get("password").asText());
//		Folder emailFolder = store.getFolder("INBOX");
//		emailFolder.open(Folder.READ_ONLY);
//		Message messages[] = emailFolder.getMessages();
//		int i = ((messages.length) - 1);
//		Message message = messages[i];
//		System.out.println("---------------------------------");
//		System.out.println("Email Number " + (i + 1));
//		System.out.println("Subject: " + message.getSubject());
//		System.out.println("From: " + message.getFrom()[0]);
//		System.out.println("Text: " + message.getContent().toString());
//		Date receivedDate = message.getReceivedDate();
//		System.out.println("Received Date : " + receivedDate.toString());
//		emailFolder.close(true);
//		store.close();
//	

		return list;
		
		
	}
}
