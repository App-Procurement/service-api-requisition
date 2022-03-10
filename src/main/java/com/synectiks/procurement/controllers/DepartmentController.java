package com.synectiks.procurement.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.business.service.DepartmentService;
import com.synectiks.procurement.config.BusinessValidationCodes;
import com.synectiks.procurement.domain.Department;
import com.synectiks.procurement.web.rest.errors.DataNotFoundException;
import com.synectiks.procurement.web.rest.errors.IdNotFoundException;
import com.synectiks.procurement.web.rest.errors.NegativeIdException;

import io.github.jhipster.web.util.HeaderUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Department APIs")
@RestController
@RequestMapping("/api")
//@Api(value = "/api", tags = "Department Management")
public class DepartmentController {
	private static final Logger logger = LoggerFactory.getLogger(DepartmentController.class);

	@Autowired
	private DepartmentService departmentService;

	@ApiOperation(value = "Create a new department")
	@PostMapping("/department")
	public ResponseEntity<Department> addDepartment(@RequestBody ObjectNode obj) {
		logger.info("Request to add department");
		try {
			Department department = departmentService.addDepartment(obj);
			return ResponseEntity.status(HttpStatus.OK).body(department);
		} catch (Exception e) {
			logger.error("department buyer failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
		
	}

	@ApiOperation(value = "Update an existing department")
	@PutMapping("/department")
	public ResponseEntity<Department> updateDepartment(@RequestBody ObjectNode obj) {
		logger.info("Request to update department");
		try {
			Department department = departmentService.updateDepartment(obj);
			return ResponseEntity.status(HttpStatus.OK).body(department);
		} catch (NegativeIdException e) {
			logger.error("Update department failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (IdNotFoundException e) {
			logger.error("Update department failed. IdNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.ID_NOT_FOUND.value()).body(null);
		}catch (DataNotFoundException e) {
			logger.error("Delete department failed. DataNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.DATA_NOT_FOUND.value()).body(null);
		}
		catch (Exception e) {
			logger.error("Update department failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}

	}

	@ApiOperation(value = "Search department")
	@GetMapping("/department")
	public ResponseEntity<List<Department>> searchDepartment(@RequestParam Map<String, String> requestObj) {
		logger.info("Request to get department on given filter criteria");
		try {
			List<Department> list = departmentService.searchDepartment(requestObj);
			return ResponseEntity.status(HttpStatus.OK).body(list);
		} catch (NegativeIdException e) {
			logger.error("Search department failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		}
		catch (Exception e) {
			logger.error("Search department failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
		
	}

	@ApiOperation(value = "Delete a department")
	@DeleteMapping("/department/{id}")
	public ResponseEntity<Boolean> deleteDepartment(@PathVariable Long id) {
		try {
			boolean dlDepartment = departmentService.deleteDepartment(id);
			if (dlDepartment) {
				return ResponseEntity.status(HttpStatus.OK).body(dlDepartment);
			} else {
				return ResponseEntity.status(HttpStatus.OK).body(dlDepartment);
			} 
			
		} catch (NegativeIdException e) {
			logger.error("Delete Department failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		} catch (IdNotFoundException e) {
			logger.error("Delete Department failed. IdNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.ID_NOT_FOUND.value()).body(null);
		} catch (DataNotFoundException e) {
			logger.error("Delete Department failed. DataNotFoundException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.DATA_NOT_FOUND.value()).body(null);
		} catch (Exception e) {
			logger.error("Delete Department failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
//		departmentService.deleteDepartment(id);
//		return ResponseEntity.noContent()
//				.headers(HeaderUtil.createEntityDeletionAlert("department", false, "department", id.toString()))
//				.build();
	}

	@ApiOperation(value = "Search a department by id")
	@GetMapping("/department/{id}")
	public ResponseEntity<Department> getDepartment(@PathVariable Long id) {
		logger.info("Getting department by id: " + id);
		Map<String, String> dep = new HashMap<>();
		try {
			Department department = null;
			dep.put("id", String.valueOf(id));
			List<Department> buyList = departmentService.searchDepartment(dep);
			if(buyList.size() > 0) {
				department = buyList.get(0);
			}
			return ResponseEntity.status(HttpStatus.OK).body(department);
		}catch (NegativeIdException e) {
			logger.error("Getting department by id failed. NegativeIdException: ", e.getMessage());
			return ResponseEntity.status(BusinessValidationCodes.NEGATIVE_ID_NOT_ALLOWED.value()).body(null);
		}catch (Exception e) {
			logger.error("Getting department by id failed. Exception: ", e);
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(null);
		}
 }

}
