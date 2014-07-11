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

import com.guigarage.jgrid.JGrid;
import com.guigarage.jgrid.demo.util.ImageUtilities;
import com.guigarage.jgrid.renderer.GridCellRenderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class PicViewerRenderer extends JComponent implements GridCellRenderer {

    //~ Static fields/initializers ---------------------------------------------

    private static final long serialVersionUID = 1L;

    //~ Instance fields --------------------------------------------------------

    private BufferedImage image;

    private boolean paintMarker = false;

    private float markerFraction;

    //~ Methods ----------------------------------------------------------------

    @Override
    public Component getGridCellRendererComponent(final JGrid grid,
            final Object value,
            final int index,
            final boolean isSelected,
            final boolean cellHasFocus) {
        image = null;
        paintMarker = false;
        if (value instanceof PicViewerObject) {
            image = ((PicViewerObject)value).getImage();
            markerFraction = ((PicViewerObject)value).getFraction();
            paintMarker = ((PicViewerObject)value).isMarker();
        }
        return this;
    }

    @Override
    protected void paintComponent(final Graphics g) {
        final Graphics2D g2 = (Graphics2D)g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (image != null) {
            int width = image.getWidth();
            int height = image.getHeight();

            final float widthFactor = (float)getWidth() / (float)image.getWidth();
            final float heightFactor = (float)getHeight() / (float)image.getHeight();
            if (widthFactor > heightFactor) {
                width = (int)((float)image.getWidth() * widthFactor);
                height = (int)((float)image.getHeight() * widthFactor);
            } else {
                width = (int)((float)image.getWidth() * heightFactor);
                height = (int)((float)image.getHeight() * heightFactor);
            }
            g2.drawImage(ImageUtilities.getOptimalScalingImage(image, width, height),
                (getWidth() - width)
                        / 2,
                (getHeight() - height)
                        / 2,
                null);

            if (paintMarker) {
                final int x = (int)(getWidth() * markerFraction);
                g2.setStroke(new BasicStroke(3.5f));
                g2.setColor(new Color(50, 50, 50));
                g2.drawLine(x, 0, x, getHeight());

                g2.setStroke(new BasicStroke(2.5f));
                g2.setColor(new Color(248, 211, 80));
                g2.drawLine(x, 0, x, getHeight());
            }
            g2.dispose();
        }
    }

    @Override
    public void validate() {
    }

    @Override
    public void invalidate() {
    }

    @Override
    public void repaint() {
    }

    @Override
    public void revalidate() {
    }

    @Override
    public void repaint(final long tm, final int x, final int y, final int width, final int height) {
    }

    @Override
    public void repaint(final Rectangle r) {
    }

    @Override
    public void firePropertyChange(final String propertyName, final byte oldValue, final byte newValue) {
    }

    @Override
    public void firePropertyChange(final String propertyName, final char oldValue, final char newValue) {
    }

    @Override
    public void firePropertyChange(final String propertyName, final short oldValue, final short newValue) {
    }

    @Override
    public void firePropertyChange(final String propertyName, final int oldValue, final int newValue) {
    }

    @Override
    public void firePropertyChange(final String propertyName, final long oldValue, final long newValue) {
    }

    @Override
    public void firePropertyChange(final String propertyName, final float oldValue, final float newValue) {
    }

    @Override
    public void firePropertyChange(final String propertyName, final double oldValue, final double newValue) {
    }

    @Override
    public void firePropertyChange(final String propertyName, final boolean oldValue, final boolean newValue) {
    }

    @Override
    protected void firePropertyChange(final String propertyName, final Object oldValue, final Object newValue) {
    }
}
