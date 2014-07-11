/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * Created on Jan 22, 2011
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2011 Hendrik Ebbers
 */
package de.jgrid.demo.picviewer;

import java.awt.image.BufferedImage;

import java.util.ArrayList;
import java.util.List;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class PicViewerObject implements Cloneable {

    //~ Instance fields --------------------------------------------------------

    private List<BufferedImage> images;

    private float fraction;

    private int index;

    private boolean marker;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new PicViewerObject object.
     */
    public PicViewerObject() {
        images = new ArrayList<BufferedImage>();
        setFraction(0.0f);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected Object clone() {
        final PicViewerObject clone = new PicViewerObject();
        for (final BufferedImage image : images) {
            clone.addImage(image);
        }
        clone.setFraction(getFraction());
        return clone;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  image  DOCUMENT ME!
     */
    public void addImage(final BufferedImage image) {
        images.add(image);
        setFraction(getFraction());
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public BufferedImage getImage() {
        return images.get(index);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  marker  DOCUMENT ME!
     */
    public void setMarker(final boolean marker) {
        this.marker = marker;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isMarker() {
        return marker;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getIndex() {
        return index;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Object getValueForFraction() {
        return getImage();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getFraction() {
        return fraction;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  fraction  DOCUMENT ME!
     */
    public void setFraction(final float fraction) {
        this.fraction = Math.max(0.0f, Math.min(1.0f, fraction));
        this.index = (int)(this.fraction * (float)(images.size()));
    }
}
