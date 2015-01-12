package org.openmrs.module.inventory.web.controller.substoreItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Role;
import org.openmrs.api.context.Context;
import org.openmrs.module.hospitalcore.model.InventoryStore;
import org.openmrs.module.hospitalcore.util.PagingUtil;
import org.openmrs.module.inventory.InventoryService;
import org.openmrs.module.inventory.model.InventoryStoreItemPatient;
import org.openmrs.module.inventory.util.RequestUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller("IssueItemPatientListController")
@RequestMapping("/module/inventory/subStoreIssueItemPatientList.form")
public class IssueItemPatientListController {
	@RequestMapping(method = RequestMethod.GET)
	public String list( @RequestParam(value="pageSize",required=false)  Integer pageSize, 
            @RequestParam(value="currentPage",required=false)  Integer currentPage,
            @RequestParam(value="issueName",required=false)  String issueName,
            @RequestParam(value="fromDate",required=false)  String fromDate,
            @RequestParam(value="toDate",required=false)  String toDate,
            @RequestParam(value="receiptId",required=false)  Integer receiptId,
            Map<String, Object> model, HttpServletRequest request
	) {
		InventoryService inventoryService = (InventoryService) Context.getService(InventoryService.class);
	InventoryStore store = inventoryService.getStoreByCollectionRole(new ArrayList<Role>(Context.getAuthenticatedUser().getAllRoles()));
	
	/*if(store != null && store.getParent() != null && store.getIsItem() != 1){
		return "redirect:/module/inventory/subStoreIssueItemAccountList.form";
	}*/
	
	 int total = inventoryService.countStoreItemPatient(store.getId(), issueName, fromDate, toDate);
	 String temp = "";
		
		if(issueName != null){	
			if(StringUtils.isBlank(temp)){
				temp = "?issueName="+issueName;
			}else{
				temp +="&issueName="+issueName;
			}
	}
		if(!StringUtils.isBlank(fromDate)){	
			if(StringUtils.isBlank(temp)){
				temp = "?fromDate="+fromDate;
			}else{
				temp +="&fromDate="+fromDate;
			}
	}
		if(!StringUtils.isBlank(toDate)){	
			if(StringUtils.isBlank(temp)){
				temp = "?toDate="+toDate;
			}else{
				temp +="&toDate="+toDate;
			}
	}
	if(receiptId != null){	
			if(StringUtils.isBlank(temp)){
				temp = "?receiptId="+receiptId;
			}else{
				temp +="&receiptId="+receiptId;
			}
	}
		
		PagingUtil pagingUtil = new PagingUtil( RequestUtil.getCurrentLink(request)+temp , pageSize, currentPage, total );
		List<InventoryStoreItemPatient> listIssue = inventoryService.listStoreItemPatient(store.getId(),receiptId, issueName,fromDate, toDate, pagingUtil.getStartPos(), pagingUtil.getPageSize());
		model.put("issueName", issueName );
		model.put("receiptId", receiptId );
		model.put("toDate", toDate );
		model.put("fromDate", fromDate );
		model.put("pagingUtil", pagingUtil );
		model.put("listIssue", listIssue );
		model.put("store", store );
	 return "/module/inventory/substoreItem/subStoreIssueItemPatientList";
	 
	}
}
