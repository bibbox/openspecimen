package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum DistributionOrderErrorCode implements ErrorCode {
	NOT_FOUND,
	
	DP_REQUIRED,
	
	DUP_NAME,
	
	NAME_REQUIRED, 
	
	INVALID_STATUS, 
	
	INVALID_CREATION_DATE,
	
	INVALID_EXECUTION_DATE,

	ITEM_QTY_REQ,

	ITEM_INVALID_QTY,

	ITEM_STATUS_REQ,
	
	RPT_TMPL_NOT_CONFIGURED,
	
	ALREADY_EXECUTED,

	DUPLICATE_SPECIMENS,
	
	INVALID_SPECIMEN_STATUS,
	
	NO_SPECIMENS_TO_DIST,

	NO_SPMNS_IN_LIST,

	INVALID_DP_FOR_REQ,

	INVALID_SPECIMENS_FOR_DP,

	SPMN_RESV_FOR_OTH_DPS,
	
	SPECIMEN_DOES_NOT_EXIST,

	CLOSED_SPECIMENS,

	SPECIMEN_NOT_IN_REQ,
	
	INVALID_REQUESTER_RECV_SITE_INST,

	CANT_UPDATE_EXEC_ORDER,

	CANT_DELETE_EXEC_ORDER,

	REQUESTER_REQ,

	REQUESTER_NOT_FOUND,

	DISTRIBUTOR_NOT_FOUND,

	STATUS_REQ,

	NOT_DISTRIBUTED,

	RETURN_QTY_REQ,

	INVALID_RETURN_QUANTITY,

	SPECIMEN_ALREADY_RETURNED,

	RETURN_DATE_REQ,

	INVALID_RETURN_DATE,

	RETURNED_BY_REQ,

	SPMN_NOT_FOUND,

	NON_CONSENTING_SPECIMENS,

	INVALID_COST,

	SPMNS_DENIED,

	NO_SPMNS_RESV_FOR_DP;

	@Override
	public String code() {
		return "DIST_ORDER_" + this.name();
	}

}
