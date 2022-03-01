package com.synectiks.procurement.business.service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.synectiks.procurement.config.Constants;
import com.synectiks.procurement.domain.Roles;
import com.synectiks.procurement.domain.Rules;
import com.synectiks.procurement.repository.RulesRepository;
import com.synectiks.procurement.web.rest.errors.UniqueConstraintException;

@Service
public class RulesService {
	
	private static final Logger logger = LoggerFactory.getLogger(RulesService.class);
	
	@Autowired
	private RolesService rolesService;

	@Autowired
	private RulesRepository rulesRepository;

	public Rules addRules(ObjectNode obj) throws UniqueConstraintException{
		Rules rules = new Rules();

		if (obj.get("name").asText() != null) {
			rules.setName(obj.get("name").asText());
			List<Rules> ruleList = rulesRepository.findAll(Example.of(rules));
			if(ruleList.size() > 0) {
				logger.error("Rule already exists. Duplicate rule not allowd");
	        	UniqueConstraintException ex = new UniqueConstraintException("Duplicate rule not allowd");
	        	throw ex;
			}
		} else {
			logger.error("Rule could not be added. Rule missing");
			return null;
		}
		
//		if (obj.get("roleId") != null && obj.get("name") != null) {
//			Roles rol = rolesService.getRoles(obj.get("roleId").asLong());
//			if (rol != null) {	
//				rules.setRoles(rol);
//				try {
//					logger.debug("Checking for duplicate rule for given role : "+rol.getName());
//					List<Rules> ruleList = rulesRepository.findAll(Example.of(rules));
//					if (ruleList.size() > 0) {
//						logger.error("Rule already exists. Duplicate rule not allowed for role:" +rol.getName());
//						UniqueConstraintException ex = new UniqueConstraintException("Rule already exists. Duplicate rule not allowed for role:" +rol.getName());
//						throw ex;
//					}
//				} catch (Exception e) {
//					logger.error("Exception in validating duplicate rule. Exception: ", e);
//					throw e;
//				}
//			}
//		}
		
		if (obj.get("description") != null) {
			rules.setDescription(obj.get("description").asText());
		}
		if (obj.get("status") != null) {
			rules.setStatus(obj.get("status").asText());
		}
		if (obj.get("rule") != null) {
			rules.setRule(obj.get("rule").toString());
		}

		if (obj.get("user") != null) {
			rules.setCreatedBy(obj.get("user").asText());
			rules.setUpdatedBy(obj.get("user").asText());
		} else {
			rules.setCreatedBy(Constants.SYSTEM_ACCOUNT);
			rules.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
		}
		
		Instant now = Instant.now();
		rules.setCreatedOn(now);
		rules.setUpdatedOn(now);
		
		rules = rulesRepository.save(rules);
		logger.info("Adding rule completed successfully. Rule: " + rules.toString());
		return rules;
	}

	public Rules updateRules(ObjectNode obj) throws UniqueConstraintException, JSONException  {
		
		Optional<Rules> oRule = rulesRepository.findById(Long.parseLong(obj.get("ruleId").asText()));
		if (!oRule.isPresent()) {
			logger.error("Rule not found");
			return null;
		}
		
		Rules rules = oRule.get();
		
		
			if (obj.get("roleName") != null && (!rules.getRoles().getName().equalsIgnoreCase(obj.get("roleName").asText()))) {
				Map<String, String> requestObj = new HashMap<>();
				requestObj.put("name", obj.get("roleName").asText());
				List<Roles> roleList = rolesService.searchRoles(requestObj);
				if(roleList.size() > 0) {
					for(Roles rl: roleList) {
						if(rl.getName().equalsIgnoreCase((rules.getName()))) {
							logger.error("Rule already exists. Duplicate rule not allowed for role:" +rl.getName());
							UniqueConstraintException ex = new UniqueConstraintException("Rule already exists. Duplicate rule not allowed for role:" +rl.getName());
							throw ex;
						}
					}
				}
				
			}



		if (obj.get("name") != null) {
			rules.setName(obj.get("name").asText().toUpperCase());
		}
		
		Optional<Rules> optional = rulesRepository.findOne(Example.of(rules));
		if (optional.isPresent()) {
			return null;
		}
		if (obj.get("description") != null) {
			rules.setDescription(obj.get("description").asText());
		}

		if (obj.get("status") != null) {
			rules.setStatus(obj.get("status").asText());
		}
		if (obj.get("rule") != null) {
			rules.setRule(obj.get("rule").asText());
		}

		if (obj.get("user") != null) {
			rules.setUpdatedBy(obj.get("user").asText());
		} else {
			rules.setUpdatedBy(Constants.SYSTEM_ACCOUNT);
		}
		
		rules.setUpdatedOn(Instant.now());
		rules = rulesRepository.save(rules);
		logger.info("Updateing rule completed successfully. Rule: " + rules.toString());
		
		return rules;
	}

	public List<Rules> searchRules(@RequestParam Map<String, String> requestObj) {
		Rules rules = new Rules();
		boolean isFilter = false;
		if (requestObj.get("id") != null) {
			rules.setId(Long.parseLong(requestObj.get("id")));
			isFilter = true;
		}
		if (requestObj.get("name") != null) {
			rules.setName(requestObj.get("name"));
			isFilter = true;
		}
		if (requestObj.get("rule") != null) {
			rules.setRule(requestObj.get("rule"));
			isFilter = true;
		}
		if (requestObj.get("description") != null) {
			rules.setDescription(requestObj.get("description"));
			isFilter = true;
		}
		if (requestObj.get("createdOn") != null) {
			Instant inst = Instant.parse(requestObj.get("createdOn"));
			rules.setCreatedOn(inst);
			isFilter = true;
		}

		if (requestObj.get("createdBy") != null) {
			rules.setCreatedBy(requestObj.get("createdBy"));
			isFilter = true;
		}
		if (requestObj.get("updatedOn") != null) {
			Instant inst = Instant.parse(requestObj.get("updatedOn"));
			rules.setUpdatedOn(inst);
			isFilter = true;
		}
		if (requestObj.get("updatedBy") != null) {
			rules.setUpdatedBy(requestObj.get("updatedBy"));
			isFilter = true;
		}
		List<Rules> list = null;
		if (isFilter) {
			list = this.rulesRepository.findAll(Example.of(rules), Sort.by(Direction.DESC, "id"));
		} else {
			list = this.rulesRepository.findAll(Sort.by(Direction.DESC, "id"));
		}

		logger.info("Rule search completed. Total records: " + list.size());

		return list;
	}

	public Rules getRules(Long id) {
		logger.info("Getting rules by id: " + id);
		Optional<Rules> rul = rulesRepository.findById(id);
		if (rul.isPresent()) {
			Rules rules = rul.get();
			logger.info("Rules: " + rul.get().toString());
			return rules;
		}
		logger.warn("Rules not found");
		return null;
	}
	
	public Rules getRulesByName(String name) {
		logger.info("Getting rules by name: " + name);
		Map<String, String> map = new HashMap<>();
		map.put("name", name);
		List<Rules> rules = searchRules(map);
		if(rules.size() > 0) {
			return rules.get(0);
		}
		return null;
	}


//	public Rules getRulesByName(String name) {
//		logger.info("Getting rules by name: " + name);
//		Rules rules = new Rules();
//		rules.setName(name.toUpperCase());
//		Optional<Rules> rul = rulesRepository.findOne(Example.of(rules));
//		if (rul.isPresent()) {
//			rules = rul.get();
//			logger.info("Rules: " + rules);
//			return rules;
//		}
//		logger.warn("Name not found");
//		return null;
//	}
	
	public List<Rules> getRulesByRole(Roles role) {
		logger.info("Getting rules by role: " + role.getName());
		Rules rules = new Rules();
		rules.setRoles(role);
		return rulesRepository.findAll(Example.of(rules));
	}
	
	public Rules getRulesByRoleAndRuleName(Roles role, String ruleName) {
		logger.info("Getting rules by role: " + role.getName()+" and rule name : "+ruleName);
		Rules rules = new Rules();
		rules.setName(ruleName);
		rules.setRoles(role);
		return rulesRepository.findOne(Example.of(rules)).orElse(null);
	}

//	
	public void deleteRules(Long id) {
		rulesRepository.deleteById(id);
		logger.info("Rule deleted successfully");
	}

}
