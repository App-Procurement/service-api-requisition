package com.synectiks.procurement.business.service;

import java.time.Instant;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.config.Constants;
import com.synectiks.procurement.domain.Roles;
import com.synectiks.procurement.domain.RolesGroup;
import com.synectiks.procurement.repository.RolesGroupRepository;
import com.synectiks.procurement.repository.RolesRepository;
import com.synectiks.procurement.web.rest.errors.UniqueConstraintException;

@Service
public class RolesService {
	private static final Logger logger = LoggerFactory.getLogger(RolesService.class);

	@Autowired
	private RolesRepository rolesRepository;

	@Autowired
	private RolesGroupRepository rolesGroupRepository;

	@Autowired
	private RestTemplate restTemplate;

	@Transactional
	public Roles addRoles(ObjectNode obj) throws UniqueConstraintException {
		Roles roles = new Roles();
		if (obj.get("name") != null) {
			roles.setName(obj.get("name").asText());
		}
		
		try {
			List<Roles> roleList = rolesRepository.findAll(Example.of(roles));
	        if(roleList.size() > 0) {
	        	logger.error("Role already exists. Duplicate role not allowd");
	        	UniqueConstraintException ex = new UniqueConstraintException("Duplicate role not allowd");
	        	throw ex;
	        }
		}catch(Exception e) {
			logger.error("Exception in validating duplicate role. Exception: ",e);
        	throw e;
		}
        
		
		if (obj.get("description") != null) {
			roles.setDescription(obj.get("description").asText());
		}

		if (obj.get("status") != null) {
			roles.setStatus(obj.get("status").asText());
		}
	
		if (obj.get("user") != null) {
			roles.setCreatedBy(obj.get("user").asText());
			roles.setUpdatedBy(obj.get("user").asText());
		} else {
			roles.setCreatedBy(Constants.SYSTEM_ACCOUNT);
			roles.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
		}

		Instant now = Instant.now();
		roles.setCreatedOn(now);
		roles.setUpdatedOn(now);
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode objectNode = mapper.createObjectNode();
		objectNode.set("name", mapper.convertValue(roles.getName().toString(), JsonNode.class));
		objectNode.set("grp", mapper.convertValue(obj.get("isGroup").asBoolean(), JsonNode.class));
		objectNode.set("description", mapper.convertValue(roles.getDescription().toString(), JsonNode.class));
		Map<String, Object> map = new HashMap<String, Object>();
		ObjectNode obj3 = restTemplate.postForObject(Constants.SECURITY_SERVICE_URL, objectNode, ObjectNode.class, map);
		if (obj3 != null) {
			roles.setSecurityserviceId(obj3.get("id").asLong());
		}
		roles = rolesRepository.save(roles);
		logger.info("Role added successfully");

		if (roles != null) {
			RolesGroup rolesGroup = new RolesGroup();
			BeanUtils.copyProperties(roles, rolesGroup);
			rolesGroup.setRoles(roles);
			if (obj.get("isGroup") != null) {
				rolesGroup.setGroup(obj.get("isGroup").asBoolean());
			}

			rolesGroup = rolesGroupRepository.save(rolesGroup);
			roles.setGroup(rolesGroup.getGroup());
			logger.info("Roles group  added successfully");
		}
		logger.info("Role  added successfully" + roles.toString());
		return roles;

	}

	public Roles updateRoles(ObjectNode obj) throws UniqueConstraintException {
		Optional<Roles> ur = rolesRepository.findById(Long.parseLong(obj.get("id").asText()));
		if (!ur.isPresent()) {
			logger.error("Role not found");
			return null;
		}
		Roles roles = ur.get();

		try {
			Roles rol = new Roles();
			if (obj.get("name") != null) {
				rol.setName(obj.get("name").asText());
			}
			List<Roles> roleList = rolesRepository.findAll(Example.of(rol));
	        if(roleList.size() > 0) {
	        	for(Roles rl: roleList) {
	    			if(rl.getName().equalsIgnoreCase(obj.get("name").asText())) {
	    				logger.error("Duplicate role not allowd");
	    				UniqueConstraintException ex = new UniqueConstraintException("Duplicate role not allowd");
	    				throw ex;
	    			}
	        	}
	        }
	    }catch(Exception e) {
			logger.error("Exception in validating duplicate role. Exception: ",e);
        	throw e;
	    }
		
		if (obj.get("name") != null) {
			roles.setName(obj.get("name").asText());
		}

		if (obj.get("description") != null) {
			roles.setDescription(obj.get("description").asText());
		}

		if (obj.get("status") != null) {
			roles.setStatus(obj.get("status").asText());
		}

		if (obj.get("user") != null) {
			roles.setUpdatedBy(obj.get("user").asText());
		} else {
			roles.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
		}

		roles.setUpdatedOn(Instant.now());
		roles = rolesRepository.save(roles);
		logger.info("Role update successfully");
		if (roles != null) {
			RolesGroup rolesGroup = new RolesGroup();
			rolesGroup.setRoles(roles);
			rolesGroup = rolesGroupRepository.findOne(Example.of(rolesGroup)).get();
			if (obj.get("isGroup") != null) {
				rolesGroup.setGroup(obj.get("isGroup").asBoolean());
			}

			rolesGroup = rolesGroupRepository.save(rolesGroup);
			roles.setGroup(rolesGroup.getGroup());
			logger.info("Role group  update successfully");
		}
		return roles;
	}

	public List<Roles> searchRoles(@RequestParam Map<String, String> requestObj){
		Roles roles = new Roles();
		boolean isFilter = false;
		if (requestObj.get("id") != null) {
			roles.setId(Long.parseLong(requestObj.get("id")));
			isFilter = true;
		}

		if (requestObj.get("name") != null) {
			roles.setName(requestObj.get("name"));
			isFilter = true;
		}
		if (requestObj.get("description") != null) {
			roles.setDescription(requestObj.get("description"));
			isFilter = true;
		}
		if (requestObj.get("createdOn") != null) {
			Instant inst = Instant.parse(requestObj.get("createdOn"));
			roles.setCreatedOn(inst);
			isFilter = true;
		}

		if (requestObj.get("createdBy") != null) {
			roles.setCreatedBy(requestObj.get("createdBy"));
			isFilter = true;
		}
		if (requestObj.get("updatedOn") != null) {
			Instant inst = Instant.parse(requestObj.get("updatedOn"));
			roles.setUpdatedOn(inst);
			isFilter = true;
		}
		if (requestObj.get("updatedBy") != null) {
			roles.setUpdatedBy(requestObj.get("updatedBy"));
			isFilter = true;
		}
		List<Roles> list = null;
		if (isFilter) {
			list = this.rolesRepository.findAll(Example.of(roles), Sort.by(Direction.DESC, "id"));
		} else {
			list = this.rolesRepository.findAll(Sort.by(Direction.DESC, "id"));
		}
		for (Roles rol : list) {
			RolesGroup rolesGroup = new RolesGroup();
			rolesGroup.setRoles(roles);
			Optional<RolesGroup> org = rolesGroupRepository.findOne(Example.of(rolesGroup));
			if (org.isPresent()) {
				rol.setGroup(org.get().getGroup());
			}
		}
		logger.info("Role search completed. Total records: " + list.size());

		return list;
	}

	public Roles getRoles(Long id) {
		logger.info("Getting role by id: " + id);
		Optional<Roles> ovn = rolesRepository.findById(id);
		if (ovn.isPresent()) {
			Roles roles = ovn.get();
			logger.info("Role: " + ovn.get().toString());
			RolesGroup rolesGroup = new RolesGroup();
			rolesGroup.setRoles(roles);
			Optional<RolesGroup> org = rolesGroupRepository.findOne(Example.of(rolesGroup));
			if (org.isPresent()) {
				roles.setGroup(org.get().getGroup());
			}
			logger.info("Role found");
			return roles;
		}
		logger.warn("Role not found");
		return null;
	}

	public void deleteRoles(Long id) {
		Optional<Roles> oc = rolesRepository.findById(id);
		if (oc.isPresent()) {
			RolesGroup rolesGroup = new RolesGroup();
			rolesGroup.setRoles(oc.get());
			rolesGroupRepository.deleteAll(rolesGroupRepository.findAll(Example.of(rolesGroup)));
			rolesRepository.deleteById(id);
		}
	}

	public Roles getRolesByName(String name) {
		logger.info("Getting role by name: " + name);
		Roles role = new Roles();
		role.setName(name.trim());
		Optional<Roles> rul = rolesRepository.findOne(Example.of(role));
		if (rul.isPresent()) {
			role = rul.get();
			logger.info("Role found. Role: " + role);
			return role;
		}
		logger.warn("Role not found");
		return null;
	}
}
