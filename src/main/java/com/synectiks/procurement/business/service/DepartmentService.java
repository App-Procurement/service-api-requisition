package com.synectiks.procurement.business.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.domain.Department;
import com.synectiks.procurement.repository.DepartmentRepository;

@Service
public class DepartmentService {
	private static final Logger logger = LoggerFactory.getLogger(DepartmentService.class);

	@Autowired
	private DepartmentRepository departmentRepository;

	public Department getDepartment(Long id) {
		logger.info("Getting department by id: " + id);
		Optional<Department> o = departmentRepository.findById(id);
		if (o.isPresent()) {
			logger.info("Department: " + o.get().toString());
			return o.get();
		}
		logger.warn("Department not found");
		return null;
	}

	public Department addDepartment(ObjectNode obj){
		Department department = new Department();
		if (obj.get("name") != null) {
			department.setName(obj.get("name").asText());
		}
		department = departmentRepository.save(department);
		logger.info("Department added successfully. " + department.toString());
		return department;
	}

	public Department updateDepartment(ObjectNode obj) {
		Optional<Department> ur = departmentRepository.findById(Long.parseLong(obj.get("id").asText()));
		if (!ur.isPresent()) {
			logger.warn("Department id not found");
			return null;
		}
		Department department = ur.get();
		department.setName(obj.get("name").asText());
		department = departmentRepository.save(department);
		logger.info("Updating department completed");
		return department;
	}

	public List<Department> searchDepartment(Map<String, String> requestObj) {
		Department department = new Department();
		boolean isFilter = false;
		if (requestObj.get("id") != null) {
			department.setId(Long.parseLong(requestObj.get("id")));
			isFilter = true;
		}
		if (requestObj.get("name") != null) {
			department.setName(requestObj.get("name"));
			isFilter = true;
		}

		List<Department> list = null;
		if (isFilter) {
			list = this.departmentRepository.findAll(Example.of(department), Sort.by(Direction.DESC, "id"));
		} else {
			list = this.departmentRepository.findAll(Sort.by(Direction.DESC, "id"));
		}

		logger.info("Department search completed. Total records: " + list.size());
		return list;

	}

	public void deleteDepartment(Long id) {
		departmentRepository.deleteById(id);
	}

	public List<Department> getAllDepartment() {
		List<Department> list = departmentRepository.findAll(Sort.by(Direction.ASC, "id"));
		logger.info("All department. Total records: " + list.size());
		return list;
	}
}
