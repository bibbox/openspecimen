
package com.krishagni.catissueplus.core.common.events;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.common.util.Utility;

@JsonFilter("withoutId")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserSummary implements Serializable {
	
	private static final long serialVersionUID = -8113791999197573026L;

	private Long id;

	private String type;

	private String firstName;

	private String lastName;

	private String loginName;
	
	private String domain;
	
	private String emailAddress;

	private Long instituteId;

	private String instituteName;

	private String primarySite;

	private Boolean admin;
	
	private Boolean instituteAdmin;
	
	private Boolean manageForms;

	private int cpCount;

	private Date creationDate;

	private String activityStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public Long getInstituteId() {
		return instituteId;
	}

	public void setInstituteId(Long instituteId) {
		this.instituteId = instituteId;
	}

	public String getInstituteName() {
		return instituteName;
	}

	public void setInstituteName(String instituteName) {
		this.instituteName = instituteName;
	}

	public String getPrimarySite() {
		return primarySite;
	}

	public void setPrimarySite(String primarySite) {
		this.primarySite = primarySite;
	}

	public Boolean getAdmin() {
		return admin;
	}

	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}
	
	public Boolean getInstituteAdmin() {
		return instituteAdmin;
	}
	
	public void setInstituteAdmin(Boolean instituteAdmin) {
		this.instituteAdmin = instituteAdmin;
	}

	public Boolean getManageForms() {
		return manageForms;
	}

	public void setManageForms(Boolean manageForms) {
		this.manageForms = manageForms;
	}

	public int getCpCount() {
		return cpCount;
	}

	public void setCpCount(int cpCount) {
		this.cpCount = cpCount;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public String formattedName() {
		StringBuilder name = new StringBuilder();
		if (StringUtils.isNotBlank(firstName)) {
			name.append(firstName);
		}

		if (StringUtils.isNotBlank(lastName)) {
			if (name.length() > 0) {
				name.append(" ");
			}

			name.append(lastName);
		}

		return name.toString();
	}

	public static UserSummary from(User user) {
		UserSummary result = new UserSummary();
		result.setId(user.getId());
		result.setType(user.getType().name());
		result.setFirstName(user.getFirstName());
		result.setLastName(user.getLastName());
		result.setLoginName(user.getLoginName());
		result.setDomain(user.getAuthDomain().getName());
		result.setEmailAddress(user.getEmailAddress());
		result.setAdmin(user.isAdmin());
		result.setInstituteAdmin(user.isInstituteAdmin());
		result.setCreationDate(user.getCreationDate());
		result.setManageForms(user.getManageForms());
		result.setActivityStatus(user.getActivityStatus());

		if (user.getInstitute() != null) {
			result.setInstituteId(user.getInstitute().getId());
			result.setInstituteName(user.getInstitute().getName());
		}

		if (user.getPrimarySite() != null) {
			result.setPrimarySite(user.getPrimarySite().getName());
		}

		return result;
	}
	
	public static List<UserSummary> from(Collection<User> users) {
		return Utility.nullSafeStream(users).map(UserSummary::from).collect(Collectors.toList());
	}
}
