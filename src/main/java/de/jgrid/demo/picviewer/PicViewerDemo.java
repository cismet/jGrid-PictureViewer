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
import com.guigarage.jgrid.demo.util.CoolProgressBarUI;
import com.guigarage.jgrid.demo.util.ImageUtilities;
import com.guigarage.jgrid.demo.util.UrlLoader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.io.IOException;

import java.net.URL;

import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class PicViewerDemo extends JFrame {

    //~ Static fields/initializers ---------------------------------------------

    private static final long serialVersionUID = 1L;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new PicViewerDemo object.
     */
    public PicViewerDemo() {
        setTitle("PictureViewer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        final DefaultListModel model = new DefaultListModel();

        final JFrame loadFrame = new JFrame("Loading Demo");
        loadFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final JProgressBar bar = new JProgressBar();
        bar.setForeground(Color.ORANGE);
        bar.setUI(new CoolProgressBarUI());
        loadFrame.getContentPane().setLayout(
            new FlowLayout(SwingUtilities.CENTER, 20, 20));
        loadFrame.getContentPane().add(bar);
        loadFrame.setResizable(false);
        loadFrame.pack();
        loadFrame.setLocationRelativeTo(null);
        loadFrame.setBackground(Color.DARK_GRAY);
        loadFrame.setVisible(true);

        final JGrid grid = new JGrid(model);
        grid.getCellRendererManager().setDefaultRenderer(new PicViewerRenderer());
        grid.setFixedCellDimension(160);
        grid.addMouseMotionListener(new MouseAdapter() {

                int lastIndex = -1;

                @Override
                public void mouseMoved(final MouseEvent e) {
                    if (lastIndex >= 0) {
                        final Object o = grid.getModel().getElementAt(lastIndex);
                        if (o instanceof PicViewerObject) {
                            final Rectangle r = grid.getCellBounds(lastIndex);
                            if ((r != null) && !r.contains(e.getPoint())) {
                                ((PicViewerObject)o).setMarker(false);
                                grid.repaint(r);
                            }
                        }
                    }

                    final int index = grid.getCellAt(e.getPoint());
                    if (index >= 0) {
                        final Object o = grid.getModel().getElementAt(index);
                        if (o instanceof PicViewerObject) {
                            final Rectangle r = grid.getCellBounds(index);
                            if (r != null) {
                                ((PicViewerObject)o).setFraction(((float)e.getPoint().x - (float)r.x)
                                            / (float)r.width);
                                ((PicViewerObject)o).setMarker(true);
                                lastIndex = index;
                                grid.repaint(r);
                            }
                        }
                    }
                }
            });

        final JScrollPane scrollPane = new JScrollPane(grid);
        scrollPane.setBorder(null);

        getContentPane().add(scrollPane, BorderLayout.CENTER);

        final JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout());

        final JSlider slider = new JSlider(64, 512, grid.getFixedCellDimension());
        slider.setValue(grid.getFixedCellDimension());
        slider.putClientProperty("JComponent.sizeVariant", "small");
        slider.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(final ChangeEvent e) {
                    grid.setFixedCellDimension(slider.getValue());
                }
            });

        final JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new FlowLayout());
        try {
            sliderPanel.add(new JLabel(
                    new ImageIcon(
                        ImageIO.read(UrlLoader.getInstance().load(
                                "/de/jgrid/demo/picviewer/dim_small.png")))));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        sliderPanel.add(slider);
        try {
            sliderPanel.add(new JLabel(
                    new ImageIcon(
                        ImageIO.read(UrlLoader.getInstance().load(
                                "/de/jgrid/demo/picviewer/dim_large.png")))));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        controlPanel.add(sliderPanel, BorderLayout.EAST);
        getContentPane().add(controlPanel, BorderLayout.SOUTH);
        getContentPane().setBackground(grid.getBackground());
        setSize(800, 600);
        setLocationRelativeTo(null);

        final SwingWorker<Void, PicViewerObject> worker = new SwingWorker<Void, PicViewerObject>() {

                @Override
                protected Void doInBackground() throws Exception {
                    setProgress(0);
                    publish(generateViewObject("beach"));
                    setProgress((int)(100.0f / 6.0f));
                    publish(generateViewObject("desert"));
                    setProgress((int)(100.0f / 6.0f) * 2);
                    publish(generateViewObject("water"));
                    setProgress((int)(100.0f / 6.0f) * 3);
                    publish(generateViewObject("farm"));
                    setProgress((int)(100.0f / 6.0f) * 4);
                    publish(generateViewObject("nature"));
                    setProgress((int)(100.0f / 6.0f) * 5);
                    publish(generateViewObject("travel"));
                    setProgress(100);
                    return null;
                }

                private PicViewerObject generateViewObject(final String folder) {
                    final PicViewerObject obj = new PicViewerObject();
                    for (int i = 1; i <= 10; i++) {
                        try {
                            obj.addImage(ImageUtilities.createCompatibleImage(
                                    ImageIO.read(
                                        new URL(
                                            "http://guigarage.com/downloads/viewerpics/"
                                                    + folder
                                                    + "/"
                                                    + i
                                                    + ".png"))));
                        } catch (Exception exception) {
                            exception.printStackTrace();
                        }
                    }
                    return obj;
                }

                @Override
                protected void process(final List<PicViewerObject> chunks) {
                    bar.setValue(getProgress());
                    for (final Iterator<PicViewerObject> iterator = chunks.iterator(); iterator.hasNext();) {
                        model.addElement(iterator.next());
                    }
                }

                @Override
                protected void done() {
                    final int size = model.getSize();
                    for (int j = 0; j < 3; j++) {
                        for (int i = 0; i < size; i++) {
                            final PicViewerObject o = (PicViewerObject)((PicViewerObject)model.get(i)).clone();
                            o.setFraction((float)(Math.random()));
                            model.addElement(o);
                        }
                    }
                    loadFrame.setVisible(false);
                    setVisible(true);
                }
            };
        worker.execute();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  args  DOCUMENT ME!
     */
    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    try {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    new PicViewerDemo();
                }
            });
    }
}
