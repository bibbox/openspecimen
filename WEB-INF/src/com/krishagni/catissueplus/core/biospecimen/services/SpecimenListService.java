package com.krishagni.catissueplus.core.biospecimen.services;

import java.io.OutputStream;
import java.util.List;
import java.util.function.BiConsumer;

import com.krishagni.catissueplus.core.biospecimen.events.ShareSpecimenListOp;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenInfo;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenListDetail;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenListSummary;
import com.krishagni.catissueplus.core.biospecimen.events.UpdateListSpecimensOp;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenListCriteria;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenListsCriteria;
import com.krishagni.catissueplus.core.common.events.EntityQueryCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.de.events.QueryDataExportResult;

import edu.common.dynamicextensions.query.QueryResultData;

public interface SpecimenListService {
	ResponseEvent<List<SpecimenListSummary>> getSpecimenLists(RequestEvent<SpecimenListsCriteria> req);
	
	ResponseEvent<Long> getSpecimenListsCount(RequestEvent<SpecimenListsCriteria> req);
	
	ResponseEvent<SpecimenListDetail> getSpecimenList(RequestEvent<EntityQueryCriteria> req);
	
	ResponseEvent<SpecimenListDetail> createSpecimenList(RequestEvent<SpecimenListDetail> req);
	
	ResponseEvent<SpecimenListDetail> updateSpecimenList(RequestEvent<SpecimenListDetail> req);

	ResponseEvent<SpecimenListDetail> patchSpecimenList(RequestEvent<SpecimenListDetail> req);

	ResponseEvent<List<SpecimenInfo>> getListSpecimens(RequestEvent<SpecimenListCriteria> req);

	ResponseEvent<Integer> getListSpecimensCount(RequestEvent<SpecimenListCriteria> req);

	ResponseEvent<List<SpecimenInfo>> getListSpecimensSortedByRel(RequestEvent<EntityQueryCriteria> req);

	ResponseEvent<Integer>  updateListSpecimens(RequestEvent<UpdateListSpecimensOp> req);
	
	ResponseEvent<List<UserSummary>> shareSpecimenList(RequestEvent<ShareSpecimenListOp> req);
	
	ResponseEvent<SpecimenListDetail> deleteSpecimenList(RequestEvent<Long> req);

	ResponseEvent<Boolean> addChildSpecimens(RequestEvent<Long> req);

	ResponseEvent<QueryDataExportResult> exportSpecimenList(RequestEvent<SpecimenListCriteria> req);

	//
	// Used for internal consumption purpose.
	//
	QueryDataExportResult exportSpecimenList(SpecimenListCriteria crit, BiConsumer<QueryResultData, OutputStream> rptConsumer);

	boolean toggleStarredSpecimenList(Long listId, boolean starred);
}
