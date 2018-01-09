
package com.krishagni.catissueplus.core.administrative.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainerPosition;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerSummary;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenListCriteria;
import com.krishagni.catissueplus.core.common.Pair;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface StorageContainerDao extends Dao<StorageContainer> {
	List<StorageContainer> getStorageContainers(StorageContainerListCriteria listCrit);

	StorageContainer getByName(String name);
	
	StorageContainer getByBarcode(String barcode);
	
	void delete(StorageContainerPosition position);

	Map<String, Object> getContainerIds(String key, Object value);

	Long getStorageContainersCount(StorageContainerListCriteria listCrit);

	List<String> getNonCompliantContainers(ContainerRestrictionsCriteria crit);

	List<String> getNonCompliantSpecimens(ContainerRestrictionsCriteria crit);

	int getSpecimensCount(Long containerId);

	Map<Long, Integer> getSpecimensCount(Collection<Long> containerIds);

	List<Specimen> getSpecimens(SpecimenListCriteria crit, boolean orderByLocation);

	Long getSpecimensCount(SpecimenListCriteria crit);

	Map<Long, Integer> getRootContainerSpecimensCount(Collection<Long> containerIds);

	Map<String, Integer> getSpecimensCountByType(Long containerId);

	StorageContainerSummary getAncestorsHierarchy(Long containerId);

	List<StorageContainerSummary> getChildContainers(StorageContainer container);

	List<StorageContainer> getDescendantContainers(StorageContainerListCriteria crit);

	int deleteReservedPositions(List<String> reservationIds);

	int deleteReservedPositionsOlderThan(Date expireTime);

	List<Long> getLeafContainerIds(Long containerId, int startAt, int maxContainers);

	Map<String, List<String>> getInaccessibleSpecimens(List<Long> containerIds, List<Pair<Long, Long>> siteCps, boolean useMrnSites, int firstN);

	Map<String, List<String>> getInvalidSpecimensForSite(List<Long> containerIds, Long siteId, int firstN);

	Map<Long, List<Long>> getDescendantContainerIds(Collection<Long> containerIds);

	List<StorageContainer> getShippedContainers(Collection<Long> containerIds);
}
	
