package au.com.ibm.webapp.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public enum Role {

	ADMIN("ROLE_ADMIN", "Admin"), USER("ROLE_USER", "User"), ANONYMOUS("ROLE_ANONYMOUS", "Guest"), NOTAPPROVED("ROLE_NOTAPPROVED", "Pending");

	private final String value;
	private final String display;
	
	static final private Map<String,Role> ALIAS_MAP = new HashMap<String,Role>(); 
	static final private Map<String,Role> DISPLAY_MAP = new HashMap<String,Role>();
	static { 
	    for (Role l:Role.values()) { 
	      // ignoring the case by normalizing to uppercase
	      ALIAS_MAP.put(l.value,l);
	      DISPLAY_MAP.put(l.display, l);
	    } 
	  } 
	
	private Role(String v, String d) {
		value = v;
		display = d;
	}

	public String value() {
		return value;
	}

	public String display() {
		return display;
	}

	public static Role getEnum(String authority) {
		return ALIAS_MAP.get(authority);
	}

	public static String getDisplayValue(String authority) {
		Role role = getEnum(authority);
		return role != null ? role.display : null;
	}
	
	public static Set<String> getAllRolesList() {
		Set<String> rolesList = DISPLAY_MAP.keySet();
		return rolesList;
	}

	public static String getValue(String d) {
		Role role = DISPLAY_MAP.get(d);
		return role != null ? role.value : null;
	}

}
