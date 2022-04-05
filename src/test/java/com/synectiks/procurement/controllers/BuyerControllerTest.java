//package com.synectiks.procurement.controllers;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.node.ObjectNode;
//import com.synectiks.procurement.business.service.BuyerService;
//
//import cucumber.api.java.Before;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@WebMvcTest(controllers = BuyerController.class )
//@ActiveProfiles("test")
//public class BuyerControllerTest {
//
//	@Autowired
//	private MockMvc mockMvc;
//	
//	@MockBean
//	private BuyerService buyerService;
//	
//	private ObjectMapper mapper;
//	
//	@Before
//	public void setUp() {
//		mapper=new ObjectMapper();
//	}
//
//  @Test
//  public void addBuyerTest() throws Exception {
//	  ObjectNode node=new 
//	  String request ="";
//      MvcResult mvcResult=mockMvc.perform(
//    		  post("/api/buyer").contentType(MediaType.APPLICATION_JSON)
//    		  .with(csrf())
//    		  .content(request)
//    		  ).andExpect(status().isOk())
//    		  .andReturn();
//  }
//
//  @Test
//  public void deleteBuyerTest() {
//    throw new RuntimeException("Test not implemented");
//  }
//
//  @Test
//  public void getBuyerTest() {
//    throw new RuntimeException("Test not implemented");
//  }
//
//  @Test
//  public void searchBuyerTest() {
//    throw new RuntimeException("Test not implemented");
//  }
//
//  @Test
//  public void updateBuyerTest() {
//    throw new RuntimeException("Test not implemented");
//  }
//}
