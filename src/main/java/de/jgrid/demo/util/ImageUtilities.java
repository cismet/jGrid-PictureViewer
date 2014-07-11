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
package com.guigarage.jgrid.demo.util;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class ImageUtilities {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   width   DOCUMENT ME!
     * @param   height  DOCUMENT ME!
     * @param   alpha   DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static BufferedImage createCompatibleImage(final int width, final int height, final boolean alpha) {
        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;

        final String bool = System.getProperty("java.awt.headless");

        if (!GraphicsEnvironment.isHeadless() && !Boolean.parseBoolean(bool)) {
            final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            try {
                // Determine the type of transparency of the new buffered image
                int transparency = Transparency.OPAQUE;
                if (alpha) {
                    transparency = Transparency.TRANSLUCENT;
                }

                // Create the buffered image
                final GraphicsDevice gs = ge.getDefaultScreenDevice();
                final GraphicsConfiguration gc = gs.getDefaultConfiguration();
                bimage = gc.createCompatibleImage(width, height, transparency);
            } catch (HeadlessException e) {
                // The system does not have a screen
            }
        }
        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            if (alpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(width, height, type);
        }
        return bimage;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   image  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static BufferedImage createCompatibleImage(final BufferedImage image) {
        final BufferedImage ret = createCompatibleImage(image.getWidth(), image.getHeight(), true);
        final Graphics g = ret.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return ret;
    }

    /**
     * Progressive bilinear scaling: for any downscale size, scale iteratively by halves using BILINEAR filtering until
     * the proper size is reached.
     *
     * @param   inputImage  DOCUMENT ME!
     * @param   width       DOCUMENT ME!
     * @param   height      DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static BufferedImage getOptimalScalingImage(final BufferedImage inputImage,
            final int width,
            final int height) {
        int currentWidth = inputImage.getWidth();
        int currentHeigth = inputImage.getHeight();
        BufferedImage currentImage = inputImage;
        int deltaX = currentWidth - width;
        int deltaY = currentHeigth - height;
        int nextPow2X = currentWidth >> 1;
        int nextPow2Y = currentHeigth >> 1;
        while ((currentWidth > 1) && (currentHeigth > 1)) {
            if ((deltaX <= nextPow2X) || (deltaY <= nextPow2Y)) {
                if ((currentWidth != width) || (currentHeigth != height)) {
                    final BufferedImage tmpImage = createCompatibleImage(width, height, true);
                    final Graphics g = tmpImage.getGraphics();
                    ((Graphics2D)g).setRenderingHint(
                        RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    g.drawImage(currentImage, 0, 0, tmpImage.getWidth(), tmpImage.getHeight(), null);
                    currentImage = tmpImage;
                }
                return currentImage;
            } else {
                final BufferedImage tmpImage = createCompatibleImage(currentWidth >> 1, currentHeigth >> 1, true);
                final Graphics g = tmpImage.getGraphics();
                ((Graphics2D)g).setRenderingHint(
                    RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                g.drawImage(currentImage, 0, 0, tmpImage.getWidth(), tmpImage.getHeight(), null);
                currentImage = tmpImage;
                currentWidth = currentImage.getWidth();
                currentHeigth = currentImage.getHeight();
                deltaX = currentWidth - width;
                deltaY = currentHeigth - height;
                nextPow2X = currentWidth >> 1;
                nextPow2Y = currentHeigth >> 1;
            }
        }
        return currentImage;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   width  DOCUMENT ME!
     * @param   i      DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static BufferedImage resizeToNewWidth(final int width, final BufferedImage i) {
        final int newHeigth = (i.getHeight() * width) / i.getWidth();
        return getOptimalScalingImage(i, width, newHeigth);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   heigth  DOCUMENT ME!
     * @param   i       DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static BufferedImage resizeToNewHeigth(final int heigth, final BufferedImage i) {
        final int newWidth = (i.getWidth() * heigth) / i.getHeight();
        return getOptimalScalingImage(i, newWidth, heigth);
    }
}
