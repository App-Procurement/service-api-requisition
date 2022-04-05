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

import com.amazonaws.services.rds.model.Option;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.domain.Document;
import com.synectiks.procurement.domain.Quotation;
import com.synectiks.procurement.repository.DocumentRepository;
import com.synectiks.procurement.repository.QuotationActivityRepository;
import com.synectiks.procurement.repository.QuotationRepository;
import com.synectiks.procurement.web.rest.errors.DataNotFoundException;
import com.synectiks.procurement.web.rest.errors.IdNotFoundException;
import com.synectiks.procurement.web.rest.errors.NegativeIdException;

@ActiveProfiles("Quotaion service test")
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class QuotationServiceTest {

	@InjectMocks
	QuotationService quotationService;

	@Autowired
	QuotationService quotationServiceAuto;

	@Mock
	QuotationRepository quotationRepository;

	@Mock
	private QuotationActivityRepository quotationActivityRepository;

	@Mock
	DocumentService documentService;

	@Mock
	VendorService vendorService;

	@Mock
	PurchaseOrderService purchaseOrderService;

	@Mock
	DocumentRepository documentRepository;

	private ObjectMapper mapper = new ObjectMapper();

	Quotation quotation = new Quotation();

	@Test
	public void addQuotationTest() {

		ObjectNode obj = mapper.createObjectNode();

		obj.put("quotationNo", "4");
		obj.put("status", "b");
		obj.put("documentId", "1");
		obj.put("vendorId", "1");
		obj.put("purchaseOrderId", "1");

		try {
			quotation = quotationService.addQuotation(obj);
			assertThat(quotation).isNull(); // it will be null because document, vendor and purchaseOrder are not present

		} catch (NumberFormatException | IdNotFoundException | NegativeIdException | DataNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void searchQuotationTest() throws NegativeIdException {

		quotation.setQuotationNo("a");
		quotation.setStatus("b");

		List<Quotation> list1 = new ArrayList<>();
		list1.add(quotation);

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", null);

		Mockito.when(quotationRepository.findAll(Sort.by(Direction.DESC, "id"))).thenReturn(list1);

		List<Quotation> list;
		try {
			list = quotationService.searchQuotation(map);
			assertThat(list).hasSize(1);
		} catch (NumberFormatException | IdNotFoundException | NegativeIdException | DataNotFoundException e) {
			e.printStackTrace();
		}

	}

	@Test
	public void updateQuotationTest() {

		ObjectNode obj = mapper.createObjectNode();

		obj.put("id", "1");
		obj.put("quotationNo", "aa");
		obj.put("status", "bb");

		quotation.setId(1L);
		quotation.setQuotationNo("a");
		quotation.setStatus("b");

		Quotation quotation3 = new Quotation();
		quotation3.setId(1L);
		quotation3.setQuotationNo("aa");
		quotation3.setStatus("bb");

		Optional<Quotation> quotation2 = Optional.of(quotation);

		Mockito.when(quotationRepository.findById(1L)).thenReturn(quotation2);

		Mockito.when(quotationRepository.save(quotation)).thenReturn(quotation3);

		Quotation quotation4;
		try {
			quotation4 = quotationService.updateQuotation(obj);
			assertThat(quotation4.getQuotationNo()).isEqualTo("aa");
			assertThat(quotation4.getStatus()).isEqualTo("bb");
		} catch (NegativeIdException | IdNotFoundException | DataNotFoundException e) {
			e.printStackTrace();
		}

	}
}
