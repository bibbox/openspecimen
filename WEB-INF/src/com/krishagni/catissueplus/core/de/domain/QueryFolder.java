package com.krishagni.catissueplus.core.de.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.UserGroup;
import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;
import com.krishagni.catissueplus.core.de.repository.DaoFactory;

@Configurable
public class QueryFolder extends BaseEntity {
	private String name;

	private User owner;
	
	private Boolean sharedWithAll;

	private Set<User> sharedWith = new HashSet<>();

	private Set<UserGroup> sharedWithGroups = new HashSet<>();

	private Set<SavedQuery> savedQueries = new HashSet<>();

	@Autowired
	private DaoFactory daoFactory;

	public static String getEntityName() {
		return "query_folder";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean isSharedWithAll() {
		return sharedWithAll != null && sharedWithAll.equals(true);
	}

	public void setSharedWithAll(Boolean sharedWithAll) {
		this.sharedWithAll = sharedWithAll;
	}
	
	public Set<User> getSharedWith() {
		return sharedWith;
	}

	public void setSharedWith(Set<User> sharedWith) {
		this.sharedWith = sharedWith;
	}

	public Set<UserGroup> getSharedWithGroups() {
		return sharedWithGroups;
	}

	public void setSharedWithGroups(Set<UserGroup> sharedWithGroups) {
		this.sharedWithGroups = sharedWithGroups;
	}

	public Set<User> getAllSharedUsers() {
		Set<User> users = new HashSet<>(getSharedWith());
		getSharedWithGroups().forEach(g -> users.addAll(g.getUsers()));
		return users;
	}

	public Set<SavedQuery> getSavedQueries() {
		return savedQueries;
	}

	public void setSavedQueries(Set<SavedQuery> savedQueries) {
		this.savedQueries = savedQueries;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public void addQueries(List<SavedQuery> queries) {
		savedQueries.addAll(queries);
	}
	
	public void updateQueries(List<SavedQuery> queries) {
		savedQueries.retainAll(queries);

		for (SavedQuery query : queries) {
			if (!savedQueries.contains(query)) {
				savedQueries.add(query);
			}
		}
	}
	
	public void removeQueries(List<SavedQuery> queries) {
		savedQueries.removeAll(queries);
	}
	
	public void removeQueriesById(List<Long> queryIds) {
		Iterator<SavedQuery> iterator = savedQueries.iterator();
		while (iterator.hasNext()) {
			SavedQuery query = iterator.next();
			if (queryIds.contains(query.getId())) {
				iterator.remove();
			}
		}		
	}

	public Collection<User> addSharedUsers(Collection<User> users) {
		Set<User> addedUsers = new HashSet<>(users);
		addedUsers.removeAll(sharedWith);
		sharedWith.addAll(addedUsers);
		return addedUsers;
	}
	
	public void removeSharedUsers(Collection<User> users) {
		sharedWith.removeAll(users);
	}
	
	public Collection<User> updateSharedUsers(Collection<User> users) {
		Set<User> newUsers = new HashSet<>(users);
		newUsers.removeAll(sharedWith);
		
		sharedWith.retainAll(users);
		sharedWith.addAll(newUsers);
		return newUsers;
	}

	public Collection<UserGroup> addSharedGroups(Collection<UserGroup> groups) {
		Set<UserGroup> addedGrps = new HashSet<>(groups);
		addedGrps.removeAll(getSharedWithGroups());
		getSharedWithGroups().addAll(addedGrps);
		return addedGrps;
	}

	public void removeSharedGroups(Collection<UserGroup> groups) {
		getSharedWithGroups().removeAll(groups);
	}

	public Collection<UserGroup> updateSharedGroups(Collection<UserGroup> groups) {
		Set<UserGroup> newGroups = new HashSet<>(groups);
		newGroups.removeAll(getSharedWithGroups());

		getSharedWithGroups().retainAll(groups);
		getSharedWithGroups().addAll(groups);
		return newGroups;
	}
	
	public boolean canUserAccess(Long userId) {
		if (owner != null && userId.equals(owner.getId())) {
			return true;
		}
		
		if (Boolean.TRUE.equals(sharedWithAll)) {
			return true;
		}

		return daoFactory.getQueryFolderDao().isFolderSharedWithUser(getId(), userId);
	}
		
	public void update(QueryFolder folder) {
		setName(folder.getName());
		setSavedQueries(folder.getSavedQueries());
		setSharedWithAll(folder.isSharedWithAll());
		if (folder.isSharedWithAll()) {
			sharedWith.clear();
			sharedWithGroups.clear();
		} else {
			setSharedWith(folder.getSharedWith());
			setSharedWithGroups(folder.getSharedWithGroups());
		}		
	}
}
