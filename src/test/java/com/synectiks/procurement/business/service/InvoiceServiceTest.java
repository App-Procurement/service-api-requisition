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
import com.synectiks.procurement.domain.Invoice;
import com.synectiks.procurement.repository.DocumentRepository;
import com.synectiks.procurement.repository.InvoiceActivityRepository;
import com.synectiks.procurement.repository.InvoiceRepository;
import com.synectiks.procurement.repository.QuotationRepository;
import com.synectiks.procurement.web.rest.errors.DataNotFoundException;
import com.synectiks.procurement.web.rest.errors.IdNotFoundException;
import com.synectiks.procurement.web.rest.errors.NegativeIdException;

@ActiveProfiles("Invoice service test")
@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class InvoiceServiceTest {

	@InjectMocks
	InvoiceService invoiceService;

	@Autowired
	InvoiceService invoiceServiceAuto;

	@Mock
	InvoiceRepository invoiceRepository;
	
	@Mock
	InvoiceActivityRepository invoiceActivityRepository; 
	
	@Mock
	private QuotationRepository quotationRepository;

	@Mock
	private DocumentRepository documentRepository;

	private ObjectMapper mapper = new ObjectMapper();

	Invoice invoice = new Invoice();

	@Test
	public void addinvoiceTest() {

		ObjectNode obj = mapper.createObjectNode();

		obj.put("invoiceNo", "a");
		obj.put("modeOfPayment", "b");
		obj.put("txnRefNo", "c");
		obj.put("chequeOrDdNo", "d");
		obj.put("issuerBank", "e");
		obj.put("status", "f");
		obj.put("notes", "g");
		obj.put("documentId", "1");
		obj.put("quotationId", "1");
		
	
		invoice = invoiceServiceAuto.addInvoice(obj);

		assertThat(invoice.getInvoiceNo()).isEqualTo("a");
		assertThat(invoice.getModeOfPayment()).isEqualTo("b");
		assertThat(invoice.getTxnRefNo()).isEqualTo("c");
		assertThat(invoice.getChequeOrDdNo()).isEqualTo("d");
		assertThat(invoice.getIssuerBank()).isEqualTo("e");
		assertThat(invoice.getStatus()).isEqualTo("f");
		assertThat(invoice.getNotes()).isEqualTo("g");

	}

	@Test
	public void searchinvoiceTest() throws NegativeIdException {

		invoice.invoiceNo("a");
		invoice.setModeOfPayment("b");

		List<Invoice> list1 = new ArrayList<>();
		list1.add(invoice);

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", null);

		Mockito.when(invoiceRepository.findAll(Sort.by(Direction.DESC, "id"))).thenReturn(list1);

		List<Invoice> list = invoiceService.searchinvoice(map);

		assertThat(list).hasSize(1);
		
		for (Invoice e : list) {	
			assertThat(e.getInvoiceNo()).isEqualTo("a");
			assertThat(e.getModeOfPayment()).isEqualTo("b");	
		}
	}

	@Test
	public void updateinvoiceTest() {

		ObjectNode obj = mapper.createObjectNode();

		obj.put("id", "1");
		obj.put("invoiceNo", "aa");
		obj.put("modeOfPayment", "bb");
		
		invoice.setId(1L);
		invoice.setInvoiceNo("a");
		invoice.setModeOfPayment("b");
		
		Optional<Invoice> invoice2 = Optional.of(invoice);
		
		Mockito.when(invoiceRepository.findById(1L)).thenReturn(invoice2);

		Invoice invoice3 = new Invoice();
		invoice3.setId(1L);
		invoice3.setInvoiceNo("aa");
		invoice3.setModeOfPayment("bb");


		Mockito.when(invoiceRepository.save(invoice)).thenReturn(invoice3);

		Invoice invoice4;
		try {
			invoice4 = invoiceService.updateinvoice(obj);
			assertThat(invoice4.getInvoiceNo()).isEqualTo("aa");
			assertThat(invoice4.getModeOfPayment()).isEqualTo("bb");
		} catch (NegativeIdException | IdNotFoundException | DataNotFoundException e) {
			e.printStackTrace();
		}

	}
}
