package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolSummary;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.CpWorkflowConfig;
import com.krishagni.catissueplus.core.biospecimen.services.impl.CpWorkflowTxnCache;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.de.events.ExtensionDetail;

@JsonFilter("withoutId")
@JsonInclude(Include.NON_NULL)
public class CollectionProtocolDetail extends CollectionProtocolSummary {
	private List<UserSummary> coordinators;
	
	private List<CollectionProtocolSiteDetail> cpSites;

	private Boolean consentsWaived;

	private String irbId;

	private Long anticipatedParticipantsCount;

	private String sopDocumentUrl;

	private String sopDocumentName;

	private Boolean storeSprEnabled;

	private Boolean extractSprText;

	private String descriptionUrl;

	private String specimenLabelFmt;

	private String derivativeLabelFmt;

	private String aliquotLabelFmt;

	private String specimenBarcodeFmt;
	
	private String visitNameFmt;
	
	private Boolean manualPpidEnabled;
	
	private Boolean manualVisitNameEnabled;
	
	private Boolean manualSpecLabelEnabled;

	private Boolean bulkPartRegEnabled;

	private Boolean barcodingEnabled;

	private Boolean closeParentSpecimens;

	private String containerSelectionStrategy;

	private Boolean aliquotsInSameContainer;

	private String visitCollectionMode;

	private String visitNamePrintMode;

	private Integer visitNamePrintCopies;
	
	private String spmnLabelPrePrintMode;
	
	private List<CpSpecimenLabelPrintSettingDetail> spmnLabelPrintSettings;

	private List<DistributionProtocolSummary> distributionProtocols;

	private Long catalogId;

	private String activityStatus;

	private ExtensionDetail extensionDetail;

	private String aliquotLabelFmtToUse;

	private String specimenBarcodeFmtToUse;

	//
	// mostly used for export and import of CP
	// 
	private List<ConsentTierDetail> consents;
	
	private List<CollectionProtocolEventDetail> events;

	private Map<String, CpWorkflowCfgDetail.WorkflowDetail> workflows;

	public List<UserSummary> getCoordinators() {
		return coordinators;
	}

	public void setCoordinators(List<UserSummary> coordinators) {
		this.coordinators = coordinators;
	}

	public List<CollectionProtocolSiteDetail> getCpSites() {
		return cpSites;
	}

	public void setCpSites(List<CollectionProtocolSiteDetail> cpSites) {
		this.cpSites = cpSites;
	}

	public Boolean getConsentsWaived() {
		return consentsWaived;
	}

	public void setConsentsWaived(Boolean consentsWaived) {
		this.consentsWaived = consentsWaived;
	}

	public String getIrbId() {
		return irbId;
	}

	public void setIrbId(String irbId) {
		this.irbId = irbId;
	}

	public Long getAnticipatedParticipantsCount() {
		return anticipatedParticipantsCount;
	}

	public void setAnticipatedParticipantsCount(Long anticipatedParticipantsCount) {
		this.anticipatedParticipantsCount = anticipatedParticipantsCount;
	}

	public String getSopDocumentUrl() {
		return sopDocumentUrl;
	}

	public void setSopDocumentUrl(String sopDocumentUrl) {
		this.sopDocumentUrl = sopDocumentUrl;
	}

	public String getSopDocumentName() {
		return sopDocumentName;
	}

	public void setSopDocumentName(String sopDocumentName) {
		this.sopDocumentName = sopDocumentName;
	}

	public Boolean getStoreSprEnabled() {
		return storeSprEnabled;
	}

	public void setStoreSprEnabled(Boolean storeSprEnabled) {
		this.storeSprEnabled = storeSprEnabled;
	}

	public Boolean getExtractSprText() {
		return extractSprText;
	}

	public void setExtractSprText(Boolean extractSprText) {
		this.extractSprText = extractSprText;
	}

	public String getDescriptionUrl() {
		return descriptionUrl;
	}

	public void setDescriptionUrl(String descriptionUrl) {
		this.descriptionUrl = descriptionUrl;
	}

	public String getSpecimenLabelFmt() {
		return specimenLabelFmt;
	}

	public void setSpecimenLabelFmt(String specimenLabelFmt) {
		this.specimenLabelFmt = specimenLabelFmt;
	}

	public String getDerivativeLabelFmt() {
		return derivativeLabelFmt;
	}

	public void setDerivativeLabelFmt(String derivativeLabelFmt) {
		this.derivativeLabelFmt = derivativeLabelFmt;
	}

	public String getSpecimenBarcodeFmt() {
		return specimenBarcodeFmt;
	}

	public void setSpecimenBarcodeFmt(String specimenBarcodeFmt) {
		this.specimenBarcodeFmt = specimenBarcodeFmt;
	}

	public String getAliquotLabelFmt() {
		return aliquotLabelFmt;
	}

	public void setAliquotLabelFmt(String aliquotLabelFmt) {
		this.aliquotLabelFmt = aliquotLabelFmt;
	}

	public String getVisitNameFmt() {
		return visitNameFmt;
	}

	public void setVisitNameFmt(String visitNameFmt) {
		this.visitNameFmt = visitNameFmt;
	}

	public Boolean getManualPpidEnabled() {
		return manualPpidEnabled;
	}

	public void setManualPpidEnabled(Boolean manualPpidEnabled) {
		this.manualPpidEnabled = manualPpidEnabled;
	}

	public Boolean getManualVisitNameEnabled() {
		return manualVisitNameEnabled;
	}

	public void setManualVisitNameEnabled(Boolean manualVisitNameEnabled) {
		this.manualVisitNameEnabled = manualVisitNameEnabled;
	}

	public Boolean getManualSpecLabelEnabled() {
		return manualSpecLabelEnabled;
	}

	public void setManualSpecLabelEnabled(Boolean manualSpecLabelEnabled) {
		this.manualSpecLabelEnabled = manualSpecLabelEnabled;
	}

	public Boolean getBulkPartRegEnabled() {
		return bulkPartRegEnabled;
	}

	public void setBulkPartRegEnabled(Boolean bulkPartRegEnabled) {
		this.bulkPartRegEnabled = bulkPartRegEnabled;
	}

	public Boolean getBarcodingEnabled() {
		return barcodingEnabled;
	}

	public void setBarcodingEnabled(Boolean barcodingEnabled) {
		this.barcodingEnabled = barcodingEnabled;
	}

	public Boolean getCloseParentSpecimens() {
		return closeParentSpecimens;
	}

	public void setCloseParentSpecimens(Boolean closeParentSpecimens) {
		this.closeParentSpecimens = closeParentSpecimens;
	}

	public String getContainerSelectionStrategy() {
		return containerSelectionStrategy;
	}

	public void setContainerSelectionStrategy(String containerSelectionStrategy) {
		this.containerSelectionStrategy = containerSelectionStrategy;
	}

	public Boolean getAliquotsInSameContainer() {
		return aliquotsInSameContainer;
	}

	public void setAliquotsInSameContainer(Boolean aliquotsInSameContainer) {
		this.aliquotsInSameContainer = aliquotsInSameContainer;
	}

	public String getVisitCollectionMode() {
		return visitCollectionMode;
	}

	public void setVisitCollectionMode(String visitCollectionMode) {
		this.visitCollectionMode = visitCollectionMode;
	}

	public String getVisitNamePrintMode() {
		return visitNamePrintMode;
	}

	public void setVisitNamePrintMode(String visitNamePrintMode) {
		this.visitNamePrintMode = visitNamePrintMode;
	}

	public Integer getVisitNamePrintCopies() {
		return visitNamePrintCopies;
	}

	public void setVisitNamePrintCopies(Integer visitNamePrintCopies) {
		this.visitNamePrintCopies = visitNamePrintCopies;
	}

	public String getSpmnLabelPrePrintMode() {
		return spmnLabelPrePrintMode;
	}

	public void setSpmnLabelPrePrintMode(String spmnLabelPrePrintMode) {
		this.spmnLabelPrePrintMode = spmnLabelPrePrintMode;
	}

	public List<CpSpecimenLabelPrintSettingDetail> getSpmnLabelPrintSettings() {
		return spmnLabelPrintSettings;
	}

	public void setSpmnLabelPrintSettings(List<CpSpecimenLabelPrintSettingDetail> spmnLabelPrintSettings) {
		this.spmnLabelPrintSettings = spmnLabelPrintSettings;
	}

	public List<DistributionProtocolSummary> getDistributionProtocols() {
		return distributionProtocols;
	}

	public void setDistributionProtocols(List<DistributionProtocolSummary> distributionProtocols) {
		this.distributionProtocols = distributionProtocols;
	}

	public Long getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(Long catalogId) {
		this.catalogId = catalogId;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public ExtensionDetail getExtensionDetail() {
		return extensionDetail;
	}

	public void setExtensionDetail(ExtensionDetail extensionDetail) {
		this.extensionDetail = extensionDetail;
	}

	public String getAliquotLabelFmtToUse() {
		return aliquotLabelFmtToUse;
	}

	public void setAliquotLabelFmtToUse(String aliquotLabelFmtToUse) {
		this.aliquotLabelFmtToUse = aliquotLabelFmtToUse;
	}

	public String getSpecimenBarcodeFmtToUse() {
		return specimenBarcodeFmtToUse;
	}

	public void setSpecimenBarcodeFmtToUse(String specimenBarcodeFmtToUse) {
		this.specimenBarcodeFmtToUse = specimenBarcodeFmtToUse;
	}

	public List<ConsentTierDetail> getConsents() {
		return consents;
	}

	public void setConsents(List<ConsentTierDetail> consents) {
		this.consents = consents;
	}

	public List<CollectionProtocolEventDetail> getEvents() {
		return events;
	}

	public void setEvents(List<CollectionProtocolEventDetail> events) {
		this.events = events;
	}

	public Map<String, CpWorkflowCfgDetail.WorkflowDetail> getWorkflows() {
		return workflows;
	}

	public void setWorkflows(Map<String, CpWorkflowCfgDetail.WorkflowDetail> workflows) {
		this.workflows = workflows;
	}

	public static CollectionProtocolDetail from(CollectionProtocol cp) {
		return from(cp, false);
	}
	
	public static CollectionProtocolDetail from(CollectionProtocol cp, boolean fullObject) {
		CollectionProtocolDetail result = new CollectionProtocolDetail();
		CollectionProtocolSummary.copy(cp, result);
		result.setCoordinators(UserSummary.from(cp.getCoordinators()));

		result.setConsentsWaived(cp.isConsentsWaived());
		result.setIrbId(cp.getIrbIdentifier());
		result.setAnticipatedParticipantsCount(cp.getEnrollment());
		result.setSopDocumentUrl(cp.getSopDocumentUrl());
		result.setSopDocumentName(cp.getSopDocumentName());
		result.setStoreSprEnabled(cp.getStoreSprEnabled());
		result.setExtractSprText(cp.getExtractSprText());
		result.setDescriptionUrl(cp.getDescriptionURL());
		result.setSpecimenLabelFmt(cp.getSpecimenLabelFormat());
		result.setDerivativeLabelFmt(cp.getDerivativeLabelFormat());
		result.setAliquotLabelFmt(cp.getAliquotLabelFormat());
		result.setSpecimenBarcodeFmt(cp.getSpecimenBarcodeFormat());
		result.setAliquotLabelFmtToUse(cp.getAliquotLabelFormatToUse());
		result.setSpecimenBarcodeFmtToUse(cp.getSpecimenBarcodeFormatToUse());
		result.setVisitNameFmt(cp.getVisitNameFormat());
		result.setManualPpidEnabled(cp.isManualPpidEnabled());
		result.setManualVisitNameEnabled(cp.isManualVisitNameEnabled());
		result.setManualSpecLabelEnabled(cp.isManualSpecLabelEnabled());
		result.setBulkPartRegEnabled(cp.isBulkPartRegEnabled());
		result.setSpecimenCentric(cp.isSpecimenCentric());
		result.setBarcodingEnabled(cp.isBarcodingEnabled());
		result.setCloseParentSpecimens(cp.isCloseParentSpecimens());
		result.setContainerSelectionStrategy(cp.getContainerSelectionStrategy());
		result.setAliquotsInSameContainer(cp.getAliquotsInSameContainer());
		result.setVisitCollectionMode(cp.getVisitCollectionMode().name());
		result.setVisitNamePrintMode(cp.getVisitNamePrintMode().name());
		result.setVisitNamePrintCopies(cp.getVisitNamePrintCopies());
		result.setSpmnLabelPrePrintMode(cp.getSpmnLabelPrePrintMode().name());
		result.setSpmnLabelPrintSettings(CpSpecimenLabelPrintSettingDetail.from(cp.getSpmnLabelPrintSettings()));
		result.setDistributionProtocols(DistributionProtocolSummary.from(cp.getDistributionProtocols()));
		result.setCatalogId(cp.getCatalogId());
		result.setActivityStatus(cp.getActivityStatus());
		result.setCpSites(CollectionProtocolSiteDetail.from(cp.getSites()));
		result.setExtensionDetail(ExtensionDetail.from(cp.getExtension()));

		if (fullObject) {
			result.setConsents(ConsentTierDetail.from(cp.getConsentTier()));
			result.setEvents(CollectionProtocolEventDetail.from(cp.getOrderedCpeList(), true));

			CpWorkflowConfig config = CpWorkflowTxnCache.getInstance().getWorkflows(cp.getId());
			if (config.getCp() != null) {
				result.setWorkflows(CpWorkflowCfgDetail.from(config).getWorkflows());
			}
		}
		
		return result;
	}

	public static List<CollectionProtocolDetail> from(Collection<CollectionProtocol> cps) {
		return cps.stream().map(CollectionProtocolDetail::from).collect(Collectors.toList());
	}
}
