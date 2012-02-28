package org.roisu.ant.sat.typedef;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.DataType;
import org.apache.tools.ant.types.FileList;
import org.apache.tools.ant.types.Reference;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.FileResourceIterator;

public class ScopedFileList extends DataType implements ResourceCollection {
	private Vector<ScopedFileName> files = new Vector<ScopedFileName>();
    private File dir;
    
    
    /**
     * The default constructor.
     *
     */
    public ScopedFileList() {
        super();
    }

    /**
     * A copy constructor.
     *
     * @param filelist a <code>FileList</code> value
     */
    protected ScopedFileList(ScopedFileList filelist) {
        this.dir       = filelist.dir;
        this.files = filelist.files;
        setProject(filelist.getProject());
    }

	
	/**
     * Inner class corresponding to the &lt;file&gt; nested element.
     */
    public static class ScopedFileName {
    	/** The default scope is the all scope. */
        private String scope = Scope.ALL;
        private String src = null;
        private String name;

        /**
         * The name attribute of the file element.
         *
         * @param name the name of a file to add to the file list.
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return the name of the file for this element.
         */
        public String getName() {
            return name;
        }

    	/** */
        public void setSrc(String src) {
            this.src = src;
        }

        /** */
        public String getSrc() {
            return src;
        }

        /** */
        public void setScope(String scope) {
            this.scope = scope;
        }

        /** */
        public String getScope() {
            return scope;
        }
    }

    /** */
    public List<ScopedFileName> getScopedFiles() {
    	if (isReference()) {
    		if(getRefid().getReferencedObject() instanceof ScopedFileList)
    			return ((ScopedFileList) getRefid().getReferencedObject()).getScopedFiles();
    	}
        return this.files;
    }

    /**
     * Makes this instance in effect a reference to another FileList
     * instance.
     *
     * <p>You must not set another attribute or nest elements inside
     * this element if you make it a reference.</p>
     * @param r the reference to another filelist.
     * @exception BuildException if an error occurs.
     */
    public void setRefid(Reference r) throws BuildException {
        if ((dir != null) || (files.size() != 0)) {
            throw tooManyAttributes();
        }
        super.setRefid(r);
    }

    /**
     * Set the dir attribute.
     *
     * @param dir the directory this filelist is relative to.
     * @exception BuildException if an error occurs
     */
    public void setDir(File dir) throws BuildException {
        checkAttributesAllowed();
        this.dir = dir;
    }

    /**
     * @param p the current project
     * @return the directory attribute
     */
    public File getDir(Project p) {
        if (isReference()) {
            return getRef(p).getDir(p);
        }
        return dir;
    }

    /**
     * Set the filenames attribute.
     *
     * @param filenames a string contains filenames, separated by , or
     *        by whitespace.
     */
    public void setFiles(String filenames) {
        checkAttributesAllowed();
        if (filenames != null && filenames.length() > 0) {
            StringTokenizer tok = new StringTokenizer(
                filenames, ", \t\n\r\f", false);
            while (tok.hasMoreTokens()) {
            	ScopedFileName sf = new ScopedFileName();
            	sf.setName(tok.nextToken());
            	this.files.addElement(sf);
            }
        }
    }

    /**
     * Returns the list of files represented by this FileList.
     * @param p the current project
     * @return the list of files represented by this FileList.
     */
    public String[] getFiles(Project p) {
        if (isReference()) {
            return getRef(p).getFiles(p);
        }

        if (dir == null) {
            throw new BuildException("No directory specified for filelist.");
        }

        if (files.size() == 0) {
            throw new BuildException("No files specified for filelist.");
        }

        // TODO : Convert to String
        String[] result = new String[files.size()];
        files.copyInto(result);
        return result;
    }

    /**
     * Performs the check for circular references and returns the
     * referenced FileList.
     * @param p the current project
     * @return the FileList represented by a referenced filelist.
     */
    protected FileList getRef(Project p) {
        return (FileList) getCheckedRef(p);
    }

    /**
     * Add a nested &lt;file&gt; nested element.
     *
     * @param name a configured file element with a name.
     * @since Ant 1.6.2
     */
    public void addConfiguredFile(ScopedFileName name) {
        if (name.getName() == null) {
            throw new BuildException(
                "No name specified in nested file element");
        }
        files.addElement(name);
    }

    private List<String> getFileNames() {
    	List<String> filenames = new ArrayList<String>(files.size());
    	for(ScopedFileName sf : files) {
    		filenames.add(sf.getName());
    	}
    	return filenames;
    }
    
    /**
     * Fulfill the ResourceCollection contract.
     * @return an Iterator of Resources.
     * @since Ant 1.7
     */
    public Iterator iterator() {
    	log("ScopedFileList.iterator()");
        if (isReference()) {
            return ((FileList) getRef(getProject())).iterator();
        }
        return new ScopedFileResourceIterator(dir, files.toArray(new ScopedFileName[files.size()]));
    }

    /**
     * Fulfill the ResourceCollection contract.
     * @return number of elements as int.
     * @since Ant 1.7
     */
    public int size() {
    	log("ScopedFileList.size()");
        if (isReference()) {
            return ((FileList) getRef(getProject())).size();
        }
        return files.size();
    }

    /**
     * Always returns true.
     * @return true indicating that all elements will be FileResources.
     * @since Ant 1.7
     */
    public boolean isFilesystemOnly() {
        return true;
    }
}
