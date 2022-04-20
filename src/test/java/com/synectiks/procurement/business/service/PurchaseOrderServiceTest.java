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
import com.synectiks.procurement.domain.PurchaseOrder;
import com.synectiks.procurement.repository.PurchaseOrderRepository;
import com.synectiks.procurement.repository.RequisitionRepository;
import com.synectiks.procurement.web.rest.errors.DataNotFoundException;
import com.synectiks.procurement.web.rest.errors.IdNotFoundException;
import com.synectiks.procurement.web.rest.errors.NegativeIdException;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@ActiveProfiles("PurchaseOrder test")
class PurchaseOrderServiceTest {

	@Autowired
	PurchaseOrderService purchaseOrderServiceAuto;
	
	@InjectMocks
	PurchaseOrderService purchaseOrderService;
	
	PurchaseOrder purchaseOrder = new PurchaseOrder();
	 
	private ObjectMapper mapper = new ObjectMapper();
	
	@Mock
	PurchaseOrderRepository purchaseOrderRepository;
	
	@Mock
	private RequisitionRepository requisitionRepository;
	
	@Test
	void testAddPurchaseOrder() {

		ObjectNode obj = mapper.createObjectNode();
		obj.put("poNo", "1234");
		obj.put("dueDate","2021-07-21");
		obj.put("termsAndConditions","aaa");
		obj.put("notes","bbbb");
		obj.put("requisitionId","1");
		
		purchaseOrder = purchaseOrderServiceAuto.addPurchaseOrder(obj);
		
		assertThat(purchaseOrder.getPoNo()).isEqualTo("1234");
		assertThat(purchaseOrder.getDueDate()).isEqualTo("2021-07-21");
		assertThat(purchaseOrder.getTermsAndConditions()).isEqualTo("aaa");
		assertThat(purchaseOrder.getNotes()).isEqualTo("bbbb");
//		assertThat(purchaseOrder.getRequisition()).isEqualTo("1");
		
	}
	
	@Test
	public void searchPurchaseOrderTest()  {

		purchaseOrder.setPoNo("1234");
		purchaseOrder.setTermsAndConditions("aaa");
		purchaseOrder.setNotes("bb");
		
		
		
		List<PurchaseOrder> list1 = new ArrayList<>();
		list1.add(purchaseOrder);

		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", null);
		
		Mockito.when(purchaseOrderRepository.findAll(Sort.by(Direction.DESC, "id"))).thenReturn(list1);
		
		List<PurchaseOrder> list;
		
		try {
			list = purchaseOrderService.searchPurchaseOrder(map);
			assertThat(list).hasSize(1);
		} catch (NegativeIdException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	
	}
	
	@Test
	public void updatePurchaseOrderTest() throws IdNotFoundException, NegativeIdException, DataNotFoundException {

		ObjectNode obj = mapper.createObjectNode();

		obj.put("id", "1");
		obj.put("poNo", "1234");
		obj.put("dueDate","2021-07-21");
		obj.put("termsAndConditions","aaa");
		obj.put("notes","bbbb");

		purchaseOrder.setPoNo("1234");
		purchaseOrder.setTermsAndConditions("aaa");
		purchaseOrder.setNotes("bb");
		PurchaseOrder purchaseOrder3 = new PurchaseOrder();
		
		purchaseOrder3.setId(1L);
		purchaseOrder3.setPoNo("1234");
		purchaseOrder3.setTermsAndConditions("aaa");
		purchaseOrder3.setNotes("bb");
	
		Optional<PurchaseOrder> purchaseOrder2 = Optional.of(purchaseOrder);

		Mockito.when(purchaseOrderRepository.findById(1L)).thenReturn(purchaseOrder2);

		Mockito.when(purchaseOrderRepository.save(purchaseOrder)).thenReturn(purchaseOrder3);

		PurchaseOrder purchaseOrder4;
	
			purchaseOrder4 = purchaseOrderService.updatePurchaseOrder(obj);
			assertThat(purchaseOrder4.getPoNo()).isEqualTo("1234");
			assertThat(purchaseOrder4.getTermsAndConditions()).isEqualTo("aaa");

	}

}
