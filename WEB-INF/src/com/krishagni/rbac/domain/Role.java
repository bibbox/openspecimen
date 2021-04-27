package com.krishagni.rbac.domain;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.envers.Audited;
import org.hibernate.proxy.HibernateProxyHelper;

import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;
import com.krishagni.catissueplus.core.common.CollectionUpdater;

@Audited
public class Role extends BaseEntity {
	private String name;

	private String description;
	
	private Set<RoleAccessControl> acl = new HashSet<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<RoleAccessControl> getAcl() {
		return acl;
	}

	public void setAcl(Set<RoleAccessControl> acl) {
		this.acl = acl;
	}

	public void updateRole(Role other) {
		setName(other.getName());
		setDescription(other.getDescription());
		updateAcl(other);
	}

	private void updateAcl(Role other) {
		CollectionUpdater.update(getAcl(), other.getAcl());
		for (RoleAccessControl rac : getAcl()) {
			rac.setRole(this);
		}
	}
}
