package com.krishagni.catissueplus.core.de.repository.impl;

import java.util.List;

import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.de.domain.QueryFolder;
import com.krishagni.catissueplus.core.de.repository.QueryFolderDao;

public class QueryFolderDaoImpl extends AbstractDao<QueryFolder> implements QueryFolderDao {

	private static final String FQN = QueryFolder.class.getName();
	
	private static final String GET_QUERY_FOLDERS_BY_USER = FQN + ".getQueryFoldersByUser";
	
	private static final String GET_FOLDER_BY_NAME = FQN + ".getQueryFolderByName";

	private static final String SHARED_WITH_USER = FQN + ".sharedWithUser";

	@Override
	public Class<QueryFolder> getType() {
		return QueryFolder.class;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<QueryFolder> getUserFolders(Long userId) { 
		return getCurrentSession().getNamedQuery(GET_QUERY_FOLDERS_BY_USER)
			.setLong("userId", userId)
			.list();
	}

	@Override
	public QueryFolder getQueryFolder(Long folderId) {
		return getById(folderId);
	}
	
	@Override
	public void deleteFolder(QueryFolder folder) {
		getCurrentSession().delete(folder);
	}

	@Override
	public boolean isFolderSharedWithUser(Long folderId, Long userId) {
		Long sharedFolderId = (Long) getCurrentSession().getNamedQuery(SHARED_WITH_USER)
			.setParameter("folderId", folderId)
			.setParameter("userId", userId)
			.setMaxResults(1)
			.uniqueResult();
		return folderId.equals(sharedFolderId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public QueryFolder getByName(String name) { 
		List<QueryFolder> folders = getCurrentSession().getNamedQuery(GET_FOLDER_BY_NAME)
			.setString("name", name)
			.list();
		return folders.isEmpty() ? null : folders.iterator().next();
	}
}