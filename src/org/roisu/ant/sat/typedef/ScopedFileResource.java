package org.roisu.ant.sat.typedef;

import java.io.File;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.resources.FileResource;

public class ScopedFileResource extends FileResource {
    private String scope = Scope.ALL;
    private String sources = null;

	public ScopedFileResource() {
	    super();
    }

	public ScopedFileResource(File b, String name) {
	    super(b, name);
    }
	
	public ScopedFileResource(File b, String name, String scope, String sources) {
	    super(b, name);
	    this.scope = scope.toLowerCase();
	    this.sources = sources;
    }

	public ScopedFileResource(File f) {
	    super(f);
    }

	public ScopedFileResource(Project p, String s) {
	    super(p, s);
    }

	public String getScope() {
    	return scope;
    }

	public void setScope(String scope) {
    	this.scope = scope.toLowerCase();
    }

	public String getSources() {
    	return sources;
    }

	public void setSources(String sources) {
    	this.sources = sources;
    }

}
