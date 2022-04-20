package com.synectiks.procurement.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.business.service.DocumentService;
import com.synectiks.procurement.domain.Document;
import com.synectiks.procurement.repository.DocumentRepository;
import com.synectiks.procurement.web.rest.TestUtil;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("Document controller test")
@AutoConfigureMockMvc
@WithMockUser
class DocumentControllerTest {

	@Autowired
	private DocumentRepository documentRepository;

	@Autowired
	private MockMvc mockMvc;
	
	@Mock
	DocumentService documentService;
	

	Document document = new Document();
	
	private ObjectMapper mapper = new ObjectMapper();

	@Test
	@Transactional
	@Order(1)
	public void testAdddocumentController() throws IOException, Exception {
		
		ObjectNode obj = mapper.createObjectNode();

		obj.put("fileName", "a");
		obj.put("fileType", "b");
		obj.put("fileExt", "c");
		obj.put("fileSize", 12L);
		obj.put("storageLocation", "e");
		obj.put("localFilePath", "f");
		obj.put("s3Bucket", "g");
		obj.put("s3Url", "h");
		obj.put("contactId", "1");
		obj.put("requisitionLineItemId", "1");

		int databaseSizeBeforeCreate = documentRepository.findAll().size();

	
		mockMvc.perform(post("/api/document").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(obj)));

		List<Document> document1 = documentRepository.findAll();
		assertThat(document1).hasSize(databaseSizeBeforeCreate + 1);
		Document testdocument = document1.get(document1.size() - 1);

		assertThat(testdocument.getFileName()).isEqualTo("a");
		assertThat(testdocument.getFileType()).isEqualTo("b");
		assertThat(testdocument.getFileSize()).isEqualTo(12L);
		assertThat(testdocument.getLocalFilePath()).isEqualTo("f");
		assertThat(testdocument.gets3Bucket()).isEqualTo("g");
	}

	@Test
	@Transactional
	@Order(2)
	public void testUpdatedocumentController() throws IOException, Exception {

//	    	Id Generated Automatically using MockMvc

		document.setFileName("a");
		document.setFileExt("b");
		document.setFileSize(12L);
		document.setStorageLocation("d");
		document.setLocalFilePath("e");
		document.sets3Bucket("f");
		document.setStatus("h");

		documentRepository.saveAndFlush(document);

		// Initialize the database

		int databaseSizeBeforeUpdate = documentRepository.findAll().size();

		// Update the document

		Document updateddocument = documentRepository.findById(document.getId()).get();
		// Disconnect from session so that the updates on updateddocument are not directly
		// saved in db

		ObjectNode obj = mapper.createObjectNode();

		obj.put("id", document.getId().toString());
		obj.put("fileName", "aa");
		obj.put("fileType", "bb");
		obj.put("fileSize", 13L);
		obj.put("contactId", "2");
		obj.put("requisitionLineItemId", "3");
		 
		
		mockMvc.perform(put("/api/document").contentType(MediaType.APPLICATION_JSON)
				.content(TestUtil.convertObjectToJsonBytes(obj))).andExpect(status().isOk());

		// Validate the document in the database
		List<Document> documentList = documentRepository.findAll();
		
		assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
		Document testDocument = documentList.get(documentList.size() - 1);
		
		assertThat(testDocument.getFileName()).isEqualTo("aa");
		assertThat(testDocument.getFileType()).isEqualTo("bb");

		assertThat(testDocument.getFileSize()).isEqualTo(13L);

	}
	
	


	@Test
	@Transactional
	@Order(3)
	public void deletedocument() throws Exception {

//	    	Id Generated Automatically using MockMvc

		document.setStatus("ACTIVE");
		

		documentRepository.saveAndFlush(document);

		// Deactivate the document

		mockMvc.perform(delete("/api/document/{id}", document.getId()).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		// Validate the database contains one less item
		Optional<Document> document1 = documentRepository.findById(document.getId());
		assertThat(document1.get().getStatus()).isEqualTo("DEACTIVE");

	}
	
	
//	@Test
//	@Transactional
//	@Order(3)
//	public void testSearchdocumentController() throws IOException, Exception {
//
////	    	Id Generated Automatically using MockMvc
//
//		document.setFirstName("a");
//		document.setMiddleName("b");
//		document.setLastName("c");
//		document.phoneNumber("d");
//		document.setEmail("e");
//		document.setAddress("f");
//		document.setZipCode("g");
//		document.setStatus("h");
//
//		documentRepository.saveAndFlush(document);
//
//		// Initialize the database
//
//		int databaseSizeBeforeUpdate = documentRepository.findAll().size();
//
//		// Search the document
//
//	
//	}
//	
	

}
