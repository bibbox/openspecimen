package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.administrative.domain.UserGroup;
import com.krishagni.catissueplus.core.administrative.repository.UserGroupDao;
import com.krishagni.catissueplus.core.administrative.repository.UserGroupListCriteria;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class UserGroupDaoImpl extends AbstractDao<UserGroup> implements UserGroupDao {

	public Class<UserGroup> getType() {
		return UserGroup.class;
	}

	@Override
	public List<UserGroup> getGroups(UserGroupListCriteria crit) {
		Criteria query = getCurrentSession().createCriteria(UserGroup.class, "g")
			.createAlias("g.institute", "institute")
			.setFirstResult(0)
			.setMaxResults(crit.maxResults())
			.addOrder(Order.asc("g.name"));

		if (StringUtils.isNotBlank(crit.query())) {
			query.add(Restrictions.ilike("g.name", crit.query(), MatchMode.ANYWHERE));
		}

		if (StringUtils.isNotBlank(crit.institute())) {
			query.add(Restrictions.eq("institute.name", crit.institute()));
		}

		return query.list();
	}

	@Override
	public Long getGroupsCount(UserGroupListCriteria crit) {
		Criteria query = getCurrentSession().createCriteria(UserGroup.class, "g")
			.createAlias("g.institute", "institute");

		if (StringUtils.isNotBlank(crit.query())) {
			query.add(Restrictions.ilike("g.name", crit.query(), MatchMode.ANYWHERE));
		}

		if (StringUtils.isNotBlank(crit.institute())) {
			query.add(Restrictions.eq("institute.name", crit.institute()));
		}

		return ((Number) (query.setProjection(Projections.rowCount()).uniqueResult())).longValue();
	}

	@Override
	public Map<Long, Integer> getGroupUsersCount(Collection<Long> groupIds) {
		if (CollectionUtils.isEmpty(groupIds)) {
			return Collections.emptyMap();
		}

		List<Object[]> rows = getCurrentSession().getNamedQuery(GET_USERS_COUNT)
			.setParameter("groupIds", groupIds)
			.list();

		Map<Long, Integer> result = new HashMap<>();
		for (Object[] row : rows) {
			result.put((Long) row[0], (Integer) row[1]);
		}

		return result;
	}

	@Override
	public UserGroup getByName(String name) {
		return (UserGroup) getCurrentSession().getNamedQuery(GET_BY_NAME)
			.setParameter("name", name)
			.uniqueResult();
	}

	private static final String FQN = UserGroup.class.getName();

	private static final String GET_BY_NAME = FQN + ".getByName";

	private static final String GET_USERS_COUNT = FQN + ".getUsersCount";
}
