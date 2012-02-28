package org.roisu.ant.sat.typedef;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tools.ant.BuildException;

/**
 * Utility class to operate on scopes.
 */
public class Scope {
	
	public static final String ALL = "all";
	public static final String SYS_PROPERTY = "org.roisu.ant.sat.scopes";
	public static final String DEFAULT_SCOPES = "all,unit|runtime,compile";
	private static final Map<String,Integer> scopes = new HashMap<String,Integer>();
	
	static {
		initializeScopes(null);
	}

	public static synchronized void initializeScopes(String scopesStr) {
		if(scopesStr == null) {
			scopesStr = DEFAULT_SCOPES;
		}
		// Construct the map of scopes
		scopes.clear();
		int i = 0;
		for(String s : scopesStr.split(",")) {
			for(String ss : s.split("\\|")) {
				scopes.put(ss.toLowerCase(), Integer.valueOf(i));
			}
			i++; 
		}
		// Check that first scope is all
		if((scopes.get(ALL) == null) || (scopes.get(ALL).intValue() != 0)) {
			throw new RuntimeException("Invalid list of scopes");
		}
	}
	
	public static boolean isInScope(String scope, String targetScope) throws BuildException {
		int iScope = scopes.get(scope).intValue();
		int iTargetScope = scopes.get(targetScope).intValue();
		
		if((iScope == 0) || (iTargetScope == 0))
			return true;
		
		return iScope >= iTargetScope;
	}
	
	public static boolean isValidScope(String scope) {
		return scopes.containsKey(scope);
	}
}
