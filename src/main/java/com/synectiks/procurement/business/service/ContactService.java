package com.synectiks.procurement.business.service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.config.Constants;
import com.synectiks.procurement.domain.Contact;
import com.synectiks.procurement.domain.ContactActivity;
import com.synectiks.procurement.repository.ContactActivityRepository;
import com.synectiks.procurement.repository.ContactRepository;
import com.synectiks.procurement.web.rest.errors.DataNotFoundException;
import com.synectiks.procurement.web.rest.errors.IdNotFoundException;
import com.synectiks.procurement.web.rest.errors.NegativeIdException;

@Service
public class ContactService {
	private static final Logger logger = LoggerFactory.getLogger(ContactService.class);

	@Autowired
	private ContactRepository contactRepository;
	@Autowired
	ContactActivityRepository contactActivityRepository;

	@Transactional
	public Contact addContact(ObjectNode obj) {
		Contact contact = new Contact();

		if (obj.get("firstName") != null) {
			contact.setFirstName(obj.get("firstName").asText());
		}
		if (obj.get("middleName") != null) {
			contact.setMiddleName(obj.get("middleName").asText());
		}
		if (obj.get("lastName") != null) {
			contact.setLastName(obj.get("lastName").asText());
		}
		if (obj.get("phoneNumber") != null) {
			contact.setPhoneNumber(obj.get("phoneNumber").asText());
		}
		if (obj.get("email") != null) {
			contact.setEmail(obj.get("email").asText());
		}
		if (obj.get("status") != null) {
			contact.setStatus(obj.get("status").asText());
		}
		if (obj.get("designation") != null) {
			contact.setDesignation(obj.get("designation").asText());
		}
		if (obj.get("isActive") != null) {
			contact.setIsActive(obj.get("isActive").asText());
		}
		if (obj.get("inviteStatus") != null) {
			contact.setInviteStatus(obj.get("inviteStatus").asText());
		}
		if (obj.get("invitationLink") != null) {
			contact.setInvitationLink(obj.get("invitationLink").asText());
		}

		if (obj.get("jobType") != null) {
			contact.setJobType(obj.get("jobType").asText());
		}
		if (obj.get("notes") != null) {
			contact.setNotes(obj.get("notes").asText());
		}
		if (obj.get("user") != null) {
			contact.setCreatedBy(obj.get("user").asText());
			contact.setUpdatedBy(obj.get("user").asText());
		} else {
			contact.setCreatedBy(Constants.SYSTEM_ACCOUNT);
			contact.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
		}

		Instant now = Instant.now();
		contact.setInviteSentOn(now);
		contact.setCreatedOn(now);
		contact.setUpdatedOn(now);
		contact = contactRepository.save(contact);
		if (contact != null) {
			ContactActivity contactActivity = new ContactActivity();
			BeanUtils.copyProperties(contact, contactActivity);
			contactActivity.setContactId(contact.getId());
			contactActivity = contactActivityRepository.save(contactActivity);
			logger.info("Contact activity add successfully");
		}
		return contact;
	}

	@Transactional
	public Contact updateContact(ObjectNode obj)
			throws NegativeIdException, IdNotFoundException, DataNotFoundException {

		if (org.apache.commons.lang3.StringUtils.isBlank(obj.get("id").asText())) {
			logger.error("Contact could not be updated. Contact id not found");
			throw new IdNotFoundException(Constants.ID_NOT_FOUND_ERROR_MESSAGE);
		}

		Long con = Long.parseLong(obj.get("id").asText());
		if (con < 0) {
			throw new NegativeIdException(Constants.NEGATIVE_ID_ERROR_MESSAGE);
		}
		Optional<Contact> ur = contactRepository.findById(Long.parseLong(obj.get("id").asText()));
		if (!ur.isPresent()) {
			logger.info("Contact id not found");
			throw new DataNotFoundException(Constants.DATA_NOT_FOUND_ERROR_MESSAGE);
		}
		Contact contact = ur.get();
		if (obj.get("firstName") != null) {
			contact.setFirstName(obj.get("firstName").asText());
		}
		if (obj.get("middleName") != null) {
			contact.setMiddleName(obj.get("middleName").asText());
		}
		if (obj.get("lastName") != null) {
			contact.setLastName(obj.get("lastName").asText());
		}
		if (obj.get("phoneNumber") != null) {
			contact.setPhoneNumber(obj.get("phoneNumber").asText());
		}
		if (obj.get("email") != null) {
			contact.setEmail(obj.get("email").asText());
		}
		if (obj.get("status") != null) {
			contact.setStatus(obj.get("status").asText());
		}
		if (obj.get("designation") != null) {
			contact.setDesignation(obj.get("designation").asText());
		}
		if (obj.get("isActive") != null) {
			contact.setIsActive(obj.get("isActive").asText());
		}
		if (obj.get("inviteStatus") != null) {
			contact.setInviteStatus(obj.get("inviteStatus").asText());
		}
		if (obj.get("invitationLink") != null) {
			contact.setInvitationLink(obj.get("invitationLink").asText());
		}
		if (obj.get("jobType") != null) {
			contact.setJobType(obj.get("jobType").asText());
		}
		if (obj.get("notes") != null) {
			contact.setNotes(obj.get("notes").asText());
		}
		if (obj.get("user") != null) {
			contact.setCreatedBy(obj.get("user").asText());
			contact.setUpdatedBy(obj.get("user").asText());
		} else {
			contact.setCreatedBy(Constants.SYSTEM_ACCOUNT);
			contact.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
		}
		contact.setUpdatedOn(Instant.now());
		contact = contactRepository.save(contact);
		if (contact != null) {
			ContactActivity contactActivity = new ContactActivity();
			BeanUtils.copyProperties(contact, contactActivity);
			contactActivity.setContactId(contact.getId());
			contactActivity = contactActivityRepository.save(contactActivity);
			logger.info("Contact activity update successfully");
		}
		return contact;
	}

	public List<Contact> searchContact(Map<String, String> requestObj) throws NegativeIdException {
		Contact contact = new Contact();

		boolean isFilter = false;
		if (requestObj.get("id") != null) {
			Long contactId = Long.parseLong(requestObj.get("id"));
			if (contactId < 0) {
				throw new NegativeIdException(Constants.NEGATIVE_ID_ERROR_MESSAGE);
			}
			contact.setId(Long.parseLong(requestObj.get("id")));
			isFilter = true;
		}
		if (requestObj.get("firstName") != null) {
			contact.setFirstName(requestObj.get("firstName"));
			isFilter = true;
		}
		if (requestObj.get("lastName") != null) {
			contact.setLastName(requestObj.get("lastName"));
			isFilter = true;
		}
		if (requestObj.get("phoneNumber") != null) {
			contact.setPhoneNumber(requestObj.get("phoneNumber"));
			isFilter = true;
		}
		if (requestObj.get("email") != null) {
			contact.setEmail(requestObj.get("email"));
			isFilter = true;
		}
		if (requestObj.get("designation") != null) {
			contact.setDesignation(requestObj.get("designation"));
			isFilter = true;
		}
		if (requestObj.get("isActive") != null) {
			contact.setIsActive(requestObj.get("isActive"));
			isFilter = true;
		}
		if (requestObj.get("inviteStatus") != null) {
			contact.setInviteStatus(requestObj.get("inviteStatus"));
			isFilter = true;
		}
		if (requestObj.get("invitationLink") != null) {
			contact.setInvitationLink(requestObj.get("invitationLink"));
			isFilter = true;
		}
//		if (requestObj.get("inviteSentOn") != null) {
//			contact.setInviteSentOn(requestObj.get("inviteSentOn"));
//			isFilter = true;
//		}
		if (requestObj.get("jobType") != null) {
			contact.setJobType(requestObj.get("jobType"));
			isFilter = true;
		}
		if (requestObj.get("notes") != null) {
			contact.setNotes(requestObj.get("notes"));
			isFilter = true;
		}
		if (requestObj.get("createdOn") != null) {
			Instant inst = Instant.parse(requestObj.get("createdOn"));
			contact.setCreatedOn(inst);
			isFilter = true;
		}

		if (requestObj.get("createdBy") != null) {
			contact.setCreatedBy(requestObj.get("createdBy"));
			isFilter = true;
		}
		if (requestObj.get("updatedOn") != null) {
			Instant inst = Instant.parse(requestObj.get("updatedOn"));
			contact.setUpdatedOn(inst);
			isFilter = true;
		}
		if (requestObj.get("updatedBy") != null) {
			contact.setUpdatedBy(requestObj.get("updatedBy"));
			isFilter = true;
		}
		List<Contact> list = null;
		if (isFilter) {
			list = this.contactRepository.findAll(Example.of(contact), Sort.by(Direction.DESC, "id"));
		} else {
			list = this.contactRepository.findAll(Sort.by(Direction.DESC, "id"));
		}
		for (Contact con : list) {
			if (con.getId() != null) {
				ContactActivity ca = new ContactActivity();
				ca.setContactId(con.getId());
				List<ContactActivity> caList = contactActivityRepository.findAll(Example.of(ca));
				con.setActivityList(caList);
			}
		}

		logger.info("Contact search completed. Total records: " + list.size());
		return list;

	}

	public Contact getContact(Long id) {
		logger.info("Getting contact by id: " + id);
		Optional<Contact> ovn = contactRepository.findById(id);
		if (ovn.isPresent()) {
			logger.info("contact: " + ovn.get().toString());
			return ovn.get();
		}
		logger.warn("Contact not found");
		return null;
	}

	public boolean deleteContact(Long id) throws DataNotFoundException, NegativeIdException, IdNotFoundException {

		if (id == null) {
			logger.error("Contact could not be deleted. Contact id not found");
			throw new IdNotFoundException(Constants.ID_NOT_FOUND_ERROR_MESSAGE);
		}
		if (id < 0) {
			throw new NegativeIdException(Constants.NEGATIVE_ID_ERROR_MESSAGE);
		}

		Optional<Contact> contact = contactRepository.findById(id);

		if (!contact.isPresent()) {
			logger.error("Contact could not be deleted. Contact not found");
			throw new DataNotFoundException(Constants.DATA_NOT_FOUND_ERROR_MESSAGE);
		}
		contactRepository.deleteById(id);
		return true;
	}

}
