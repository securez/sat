package org.roisu.ant.sat;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.roisu.ant.sat.typedef.Scope;

/**
 * The Class ScopeTask.
 */
public class ScopeTask extends Task {
	protected String value;
	
	/* (non-Javadoc)
	 * @see org.apache.tools.ant.Task#execute()
	 */
	@Override
    public void execute() throws BuildException {
		// TODO : Set the scopes
		log("Scopes value : " + this.value);
		Scope.initializeScopes(this.value);
    }

	public String getValue() {
    	return value;
    }

	public void setValue(String value) {
    	this.value = value;
    }
	
}
