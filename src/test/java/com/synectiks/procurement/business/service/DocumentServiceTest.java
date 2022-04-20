package com.synectiks.procurement.business.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;



import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.domain.Buyer;
import com.synectiks.procurement.domain.Document;
import com.synectiks.procurement.repository.BuyerRepository;
import com.synectiks.procurement.repository.ContactRepository;
import com.synectiks.procurement.repository.DocumentRepository;
import com.synectiks.procurement.repository.RequisitionLineItemRepository;
import com.synectiks.procurement.web.rest.errors.DataNotFoundException;
import com.synectiks.procurement.web.rest.errors.IdNotFoundException;
import com.synectiks.procurement.web.rest.errors.NegativeIdException;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("Document test")
public class DocumentServiceTest {

	@InjectMocks
	DocumentService documentService;

	@Autowired
	DocumentService documentServiceAuto;

	@Mock 
	DocumentRepository documentRepository;

	@Mock
	private RequisitionLineItemRepository requisitionLineItemRepository;

	@Mock
	private ContactRepository contactRepository;
	
	private ObjectMapper mapper = new ObjectMapper();

	Document document = new Document();

	@Test
	public void addDocumentTest() {

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

		document = documentServiceAuto.addDocument(obj);

		assertThat(document.getFileName()).isEqualTo("a");
		assertThat(document.getFileType()).isEqualTo("b");
		assertThat(document.getFileSize()).isEqualTo(12L);
		assertThat(document.getLocalFilePath()).isEqualTo("f");
		assertThat(document.gets3Bucket()).isEqualTo("g");

	}

	@Test
	public void searchDocumentTest() throws NegativeIdException{

		document.setFileName("a");
		document.setFileType("b");

		List<Document> list1 = new ArrayList<>();
		list1.add(document);

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", null);

		Mockito.when(documentRepository.findAll(Sort.by(Direction.DESC, "id"))).thenReturn(list1);

		List<Document> list = documentService.searchDocument(map);

		assertThat(list).hasSize(1);
	}

	@Test
	public void updateDocumentTest() {

		ObjectNode obj = mapper.createObjectNode();

		obj.put("id", "1");
		obj.put("fileName", "a");
		obj.put("fileType", "b");
		obj.put("fileExt", "c");
		obj.put("fileSize", 12L);
		obj.put("storageLocation", "e");
		obj.put("localFilePath", "f");
		obj.put("s3Bucket", "g");
		obj.put("s3Url", "h");
		obj.put("contactId", "2");
		obj.put("requisitionLineItemId", "3");

		
		Document document3 = new Document();
		document3.setId(1L);
		document3.setFileName("a");
		document3.setFileType("b");
		
		document.setId(1L);
		document.setFileName("a");
		document.setFileType("b");

		Optional<Document> document2 = Optional.of(document);

		Mockito.when(documentRepository.findById(1L)).thenReturn(document2);
		Mockito.when(documentRepository.save(document)).thenReturn(document3);

		Document document4;
		try {
			document4 = documentService.updateDocument(obj);
			assertThat(document4.getFileName()).isEqualTo("a");
			assertThat(document4.getFileType()).isEqualTo("b");
		} catch (NegativeIdException | IdNotFoundException | DataNotFoundException e) {
			e.printStackTrace();
		}

	}
}
