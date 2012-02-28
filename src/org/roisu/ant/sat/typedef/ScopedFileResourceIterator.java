package org.roisu.ant.sat.typedef;

import java.io.File;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.tools.ant.types.resources.FileResource;
import org.roisu.ant.sat.typedef.ScopedFileList.ScopedFileName;

//@SuppressWarnings("rawtypes")
public class ScopedFileResourceIterator implements Iterator {
    private File basedir;
    private ScopedFileName[] files;
    private int pos = 0;

    /**
     * Construct a new FileResourceIterator.
     */
    public ScopedFileResourceIterator() {
    }

    /**
     * Construct a new FileResourceIterator relative to the specified
     * base directory.
     * @param f the base directory of this instance.
     */
    public ScopedFileResourceIterator(File f) {
        basedir = f;
    }

    /**
     * Construct a new FileResourceIterator over the specified filenames,
     * relative to the specified base directory.
     * @param f the base directory of this instance.
     * @param s the String[] of filenames.
     */
    public ScopedFileResourceIterator(File f, ScopedFileName[] s) {
        this(f);
        addFiles(s);
    }

    /**
     * Add an array of filenames to this FileResourceIterator.
     * @param s the filenames to add.
     */
    public void addFiles(ScopedFileName[] s) {
        int start = (files == null) ? 0 : files.length;
        ScopedFileName[] newfiles = new ScopedFileName[start + s.length];
        if (start > 0) {
            System.arraycopy(files, 0, newfiles, 0, start);
        }
        files = newfiles;
        System.arraycopy(s, 0, files, start, s.length);
    }

    /**
     * Find out whether this FileResourceIterator has more elements.
     * @return whether there are more Resources to iterate over.
     */
    public boolean hasNext() {
        return pos < files.length;
    }

    /**
     * Get the next element from this FileResourceIterator.
     * @return the next Object.
     */
    public Object next() {
        return nextResource();
    }

    /**
     * Not implemented.
     */
    public void remove() {
        throw new UnsupportedOperationException();
    }

    /**
     * Convenience method to return the next resource.
     * @return the next File.
     */
    public FileResource nextResource() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        ScopedFileResource sfr = new ScopedFileResource(basedir, files[pos].getName(), files[pos].getScope(), files[pos].getSrc());
        pos++;
        return sfr; 
        
    }

}
