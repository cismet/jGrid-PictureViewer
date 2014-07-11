/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package com.guigarage.jgrid.demo.util;

import java.io.IOException;

import java.net.URL;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class UrlLoader {

    //~ Static fields/initializers ---------------------------------------------

    private static UrlLoader instance;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new UrlLoader object.
     */
    private UrlLoader() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static UrlLoader getInstance() {
        if (instance == null) {
            instance = new UrlLoader();
        }
        return instance;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   path  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    public URL load(final String path) throws IOException {
        URL url = ClassLoader.getSystemClassLoader().getResource(path);
        if (url == null) {
            // Fuer den Gebrauch mit Jars
            url = this.getClass().getResource(path);
        }
        if (url == null) {
            url = ClassLoader.getSystemResource(path);
        }
        if (url == null) {
            throw new IOException("URL für " + path + " konnte nicht erstellt werden!");
        }
        return url;
    }
}
