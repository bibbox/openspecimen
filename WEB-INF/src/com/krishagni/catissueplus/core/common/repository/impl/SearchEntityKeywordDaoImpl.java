package com.krishagni.catissueplus.core.common.repository.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.query.Query;

import com.krishagni.catissueplus.core.common.domain.SearchEntityKeyword;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.common.repository.SearchEntityKeywordDao;

public class SearchEntityKeywordDaoImpl extends AbstractDao<SearchEntityKeyword> implements SearchEntityKeywordDao {

	@Override
	public Class<SearchEntityKeyword> getType() {
		return SearchEntityKeyword.class;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<SearchEntityKeyword> getKeywords(String entity, Long entityId, String key) {
		return (List<SearchEntityKeyword>) getCurrentSession().getNamedQuery(GET_KEYWORDS)
			.setParameter("entity", entity)
			.setParameter("entityId", entityId)
			.setParameter("key", key)
			.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<SearchEntityKeyword> getMatches(String entity, String searchTerm, int maxResults) {
		String sql = getCurrentSession().getNamedQuery(GET_MATCHES).getQueryString();
		if (entity != null) {
			sql = String.format(sql, " r.short_name = :entity and ");
		} else {
			sql = String.format(sql, "");
		}

		Query query = getCurrentSession().createSQLQuery(sql);
		if (entity != null) {
			query.setParameter("entity", entity);
		}

		List<Object[]> rows = query.setParameter("value", searchTerm + "%")
			.setMaxResults(maxResults <= 0 ? 100 : maxResults)
			.list();

		List<SearchEntityKeyword> result = new ArrayList<>();
		for (Object[] row : rows) {
			int idx = 0;

			SearchEntityKeyword keyword = new SearchEntityKeyword();
			keyword.setId(((Number) row[idx++]).longValue());
			keyword.setEntity((String) row[idx++]);
			keyword.setEntityId(((Number) row[idx++]).longValue());
			keyword.setKey((String) row[idx++]);
			keyword.setValue((String) row[idx++]);
			keyword.setStatus(((Number) row[idx++]).intValue());
			result.add(keyword);
		}

		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<String> getMatchingEntities(String entity, String searchTerm) {
		String sql = getCurrentSession().getNamedQuery(GET_MATCHING_ENTITIES).getQueryString();
		if (entity != null) {
			sql = String.format(
				sql,
				" inner join os_search_entity_ranks r1 on r1.entity = k.entity ",
				" r1.short_name = :entity and "
			);
		} else {
			sql = String.format(sql, "", "");
		}

		Query query = getCurrentSession().createSQLQuery(sql);
		if (entity != null) {
			query.setParameter("entity", entity);
		}

		return (List<String>) query.setParameter("value", searchTerm + "%").list();
	}

	private static final String FQN = SearchEntityKeyword.class.getName();

	private static final String GET_KEYWORDS = FQN + ".getKeywords";

	private static final String GET_MATCHES = FQN + ".getMatches";

	private static final String GET_MATCHING_ENTITIES = FQN + ".getMatchingEntities";
}
