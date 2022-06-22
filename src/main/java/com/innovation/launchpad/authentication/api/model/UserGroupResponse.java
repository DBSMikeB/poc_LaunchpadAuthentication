package com.innovation.launchpad.authentication.api.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class UserGroupResponse implements Serializable {

	private static final long serialVersionUID = 848734666181082569L;
	private Set<String> groups = new HashSet<>();
	
	public UserGroupResponse(String... groups) {
		for(String group : groups) {
			this.groups.add(group);
		}
	}
	
	public UserGroupResponse(Set<String> groups) {
		this.groups.addAll(groups);
	}
	
	public Set<String> getGroups() {
		return this.groups;
	}
	
	public void setGroups(Set<String> groups) {
		this.groups = groups;
	}
}
