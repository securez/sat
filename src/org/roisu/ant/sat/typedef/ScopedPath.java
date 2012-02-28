package org.roisu.ant.sat.typedef;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.tools.ant.types.Resource;
import org.apache.tools.ant.types.ResourceCollection;
import org.apache.tools.ant.types.resources.Union;


/**
 * The Class ScopedPath.
 */
@SuppressWarnings("rawtypes")
public class ScopedPath extends Union {
	
	/** The scope. */
	private String scope = Scope.ALL;

	/**
	 * Instantiates a new scoped path.
	 */
	public ScopedPath() {
	    super();
    }

	/**
	 * Instantiates a new scoped path.
	 *
	 * @param rc the rc
	 */
	public ScopedPath(ResourceCollection rc) {
	    super(rc);
    }
	
    /**
     * Sets the scope.
     *
     * @param name the new scope
     */
    public void setScope(String name) {
    	log("ScopedPath.setScope('" + name + "')");
        this.scope = name.toLowerCase();
    }

    /**
     * Gets the scope.
     *
     * @return the scope
     */
    public String getScope() {
    	return this.scope;
    }

    /* (non-Javadoc)
     * @see org.apache.tools.ant.types.resources.Union#getCollection(boolean)
     */
    @SuppressWarnings("unchecked")
    @Override
    protected Collection getCollection(boolean asString) {
    	log("ScopedPath.getCollection("+ asString + ")");
        List rc = getResourceCollections();
        if (rc.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        //preserve order-encountered using a list; enforce set logic manually:
        // (LinkedHashSet better, but JDK 1.4+)
        ArrayList union = new ArrayList(rc.size() * 2);
        // Use a set as list.contains() can be expensive for lots of resources
        Set set = new HashSet(rc.size() * 2);
        for (Iterator rcIter = rc.iterator(); rcIter.hasNext();) {
            for (Iterator r = nextRC(rcIter).iterator(); r.hasNext();) {
                Object o = r.next();
                
                // Check for scoped resource
                if((o instanceof ScopedFileResource) && (!this.scope.equals(Scope.ALL))) {
                	ScopedFileResource sfr = (ScopedFileResource) o;
                	// TODO : Add scope logic
                	if(Scope.isInScope(sfr.getScope(), scope)) {
                		if (asString) {
    	                    o = o.toString();
    	                }
    	                if (set.add(o)) {
    	                    union.add(o);
    	                }
                	}
                } else {
	                if (asString) {
	                    o = o.toString();
	                }
	                if (set.add(o)) {
	                    union.add(o);
	                }
                }
            }
        }
        return union;
    }

    private static ResourceCollection nextRC(Iterator i) {
        return (ResourceCollection) i.next();
    }

}
