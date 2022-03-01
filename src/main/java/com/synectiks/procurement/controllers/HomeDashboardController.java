package com.synectiks.procurement.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
//import com.synectiks.procurement.business.service.GmailService;
import com.synectiks.procurement.business.service.HomeService;
import com.synectiks.procurement.domain.Requisition;
import com.synectiks.procurement.domain.Status;

@RestController
@RequestMapping("/api")
public class HomeDashboardController {
	private static final Logger logger = LoggerFactory.getLogger(HomeDashboardController.class);

//	@Autowired
//	private GmailService gmailService;
	@Autowired
	private HomeService homeService;

	@GetMapping("/Userdata")
	public ResponseEntity<Status> Userdata(@RequestBody ObjectNode user) {
		Status st = new Status();
		try {
			List<Requisition> list = homeService.userdata(user);
//		    Message list1 = gmailService.userdata(user);
			if (list == null) {
				logger.error("Search user data failed");
				st.setCode(HttpStatus.EXPECTATION_FAILED.value());
				st.setType("ERROR");
				st.setMessage("Search user data failed");
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
			}
			st.setCode(HttpStatus.OK.value());
			st.setType("SUCCESS");
			st.setMessage("Search user data successful");
			st.setObject(list);
			return ResponseEntity.status(HttpStatus.OK).body(st);
		} catch (Exception e) {
			logger.error("Search user data failed. Exception: ", e);
			st.setCode(HttpStatus.EXPECTATION_FAILED.value());
			st.setType("ERROR");
			st.setMessage("Search user data failed");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(st);
		}

	}
}
