package org.javadev.effects;

import java.awt.image.ColorModel;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Shape;
import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Component;
/**
 * It is the Iris animation class provides iris kind animation effect to the java panel.
 * @author Dmitry Markman
 * @author Luca Lutterotti
 * @author Sam Berlin
 */
public class IrisAnimation implements Animation
{
    SpecialPanel animationPanel;
    private AnimationListener listener;
    boolean direction;
    int animationDuration;
    /**
     * 
     * @param direction 
     */
    @Override
    public void setDirection(final boolean direction) {
        this.direction = direction;
    }
    /**
     * 
     * @param animationDuration 
     */
    @Override
    public void setAnimationDuration(final int animationDuration) {
        this.animationDuration = ((animationDuration < 1000) ? 1000 : animationDuration);
    }
    /**
     * 
     * @param toHide
     * @param toShow
     * @param listener
     * @return 
     */
    @Override
    public Component animate(final Component toHide, final Component toShow, final AnimationListener listener) {
        this.listener = listener;
        this.animationPanel = new SpecialPanel(this, toHide, toShow);
        this.animationPanel.needToStartThread = true;
        this.animationPanel.beginAngle = 0.0f;
        this.animationPanel.endAngle = 180.0f;
        this.animationPanel.setAnimationDuration(this.animationDuration);
        return this.animationPanel;
    }
    
    public Component getAnimationPanel() {
        return this.animationPanel;
    }
    
    void rotationFinished() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                IrisAnimation.this.animationPanel = null;
                IrisAnimation.this.listener.animationFinished();
                IrisAnimation.access$1(IrisAnimation.this, null);
            }
        });
    }
    /**
     * Default Constructor
     */
    public IrisAnimation() {
        this.animationPanel = null;
        this.listener = null;
        this.direction = true;
        this.animationDuration = 2000;
    }
    /* synthetic */ 
    static void access$1(final IrisAnimation $0, final AnimationListener $1) {
        $0.listener = $1;
    }
    /**
     * Default internal class to handle the effects
     */
    class SpecialPanel extends JPanel
    {
        IrisAnimation owner;
        BufferedImage firstImage;
        BufferedImage secondImage;
        Component component1;
        Component component2;
        float angle;
        public float beginAngle;
        public float endAngle;
        float deltaAngle;
        float effectTime;
        long dt;
        int counter;
        long totalDrawTime;
        public boolean needToStartThread;
        /**
         * 
         * @param owner
         * @param firstImage
         * @param secondImage 
         */
        SpecialPanel(final IrisAnimation owner, final BufferedImage firstImage, final BufferedImage secondImage) {
            this.angle = 0.0f;
            this.beginAngle = 0.0f;
            this.endAngle = 360.0f;
            this.deltaAngle = 0.5f;
            this.effectTime = 2000.0f;
            this.dt = Math.round(this.effectTime * this.deltaAngle / 180.0f);
            this.counter = 0;
            this.totalDrawTime = 0L;
            this.needToStartThread = false;
            this.owner = owner;
            this.firstImage = firstImage;
            this.secondImage = secondImage;
            this.angle = this.beginAngle;
            this.setOpaque(false);
        }
        /**
         * 
         * @param owner
         * @param component1
         * @param component2 
         */
        SpecialPanel(final IrisAnimation owner, final Component component1, final Component component2) {
            this.angle = 0.0f;
            this.beginAngle = 0.0f;
            this.endAngle = 360.0f;
            this.deltaAngle = 0.5f;
            this.effectTime = 2000.0f;
            this.dt = Math.round(this.effectTime * this.deltaAngle / 180.0f);
            this.counter = 0;
            this.totalDrawTime = 0L;
            this.needToStartThread = false;
            this.owner = owner;
            this.component1 = component1;
            this.component2 = component2;
            this.angle = this.beginAngle;
            this.setOpaque(false);
        }
        /**
         * 
         * @param animationDuration 
         */
        public void setAnimationDuration(final int animationDuration) {
            this.effectTime = (float)((animationDuration < 1000) ? 1000 : animationDuration);
            this.dt = Math.round(this.effectTime * this.deltaAngle / 180.0f);
        }
        /**
         * 
         * @param val1
         * @param val2 
         */
        void startThread(final float val1, final float val2) {
            this.counter = 0;
            this.totalDrawTime = 0L;
            this.beginAngle = val1;
            this.endAngle = val2;
            if (this.endAngle < this.beginAngle) {
                this.deltaAngle = -Math.abs(this.deltaAngle);
            }
            else {
                this.deltaAngle = Math.abs(this.deltaAngle);
            }
            this.angle = this.beginAngle;
//Uncomment when you believe it will be usable
//            final Runnable repaint = new Runnable() {
//                public void run() {
//                    SpecialPanel.this.repaint();
//                    SpecialPanel.this.getToolkit().sync();
//                }
//            };
            final Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    final float absDeltaAngle = Math.abs(SpecialPanel.this.deltaAngle);
                    long startTime = System.currentTimeMillis();
                    final long initTime = System.currentTimeMillis();
                    while (true) {
                        final long time = System.currentTimeMillis();
                        final SpecialPanel this$1 = SpecialPanel.this;
                        this$1.angle += SpecialPanel.this.deltaAngle * (time - startTime) / SpecialPanel.this.dt;
                        startTime = time;
                        if ((SpecialPanel.this.angle >= SpecialPanel.this.endAngle - SpecialPanel.this.deltaAngle / 2.0f && SpecialPanel.this.deltaAngle > 0.0f) || (SpecialPanel.this.angle <= SpecialPanel.this.endAngle - SpecialPanel.this.deltaAngle / 2.0f && SpecialPanel.this.deltaAngle < 0.0f)) {
                            break;
                        }
                        if (SpecialPanel.this.angle >= 360.0f) {
                            SpecialPanel.this.angle = 0.0f;
                        }
                        try {
                            Thread.sleep(SpecialPanel.this.dt);
                            SpecialPanel.this.repaint();
                            SpecialPanel.this.getToolkit().sync();
                        }
                        catch (Throwable t) {}
                    }
                    SpecialPanel.this.angle = SpecialPanel.this.endAngle;
                    if (Math.abs(SpecialPanel.this.angle - 360.0f) < absDeltaAngle / 2.0f) {
                        SpecialPanel.this.angle = 0.0f;
                    }
                    if (Math.abs(SpecialPanel.this.angle - 180.0f) < absDeltaAngle / 2.0f) {
                        SpecialPanel.this.angle = 180.0f;
                    }
                    SpecialPanel.this.repaint();
//                    if (SpecialPanel.this.counter != 0) {
//                        System.out.println("total count " + SpecialPanel.this.counter + " time " + (System.currentTimeMillis() - initTime) + " average time " + SpecialPanel.this.totalDrawTime / SpecialPanel.this.counter);
//                    }
                    if (SpecialPanel.this.owner != null) {
                        SpecialPanel.this.owner.rotationFinished();
                    }
                    synchronized (SpecialPanel.this) {
                        if (SpecialPanel.this.component1 != null) {
                            SpecialPanel.this.firstImage = null;
                        }
                        if (SpecialPanel.this.component2 != null) {
                            SpecialPanel.this.secondImage = null;
                        }
                    }
                    // monitorexit(this.this$1)
                }
            });
            t.start();
        }
        @Override
        public void update(final Graphics g) {
            this.paint(g);
        }
        @Override
        public synchronized void paint(final Graphics g) {
            if (this.needToStartThread) {
                this.totalDrawTime = 0L;
                this.counter = 0;
                this.needToStartThread = false;
                this.startThread(this.beginAngle, this.endAngle);
                if (this.firstImage == null) {
                    this.firstImage = this.createImageFromComponent(this.component1);
                }
                if (this.secondImage == null) {
                    this.secondImage = this.createImageFromComponent(this.component2);
                }
            }
            if (this.firstImage == null || this.secondImage == null) {
                return;
            }
            final Graphics2D g2d = (Graphics2D)g;
//Implemented but commented for future ideas
//            final int ww = this.firstImage.getWidth();
//            final int hh = this.firstImage.getHeight();
//            final BufferedImage currImage = null;
            //final int[] currPixels = null;
            final int w = this.firstImage.getWidth();
            final int h = this.firstImage.getHeight();
            int cw = (int)(w * this.angle / 180.0f);
            int ch = (int)(h * this.angle / 180.0f);
            if (cw < 0) {
                cw = 0;
            }
            if (cw > w) {
                cw = w;
            }
            if (ch < 0) {
                ch = 0;
            }
            if (ch > h) {
                ch = h;
            }
            final long beforeDraw = System.currentTimeMillis();
            g2d.drawImage(this.firstImage, null, 0, 0);
            final Shape oldClip = g2d.getClip();
            g2d.setClip(new Rectangle(w / 2 - cw / 2, h / 2 - ch / 2, cw, ch));
            g2d.drawImage(this.secondImage, null, 0, 0);
            g2d.setClip(oldClip);
            this.totalDrawTime += System.currentTimeMillis() - beforeDraw;
            ++this.counter;
        }
        /**
         * 
         * @param comp
         * @return 
         */
        BufferedImage createImageFromComponent(final Component comp) {
            BufferedImage retImage = null;
            if (comp == null) {
                return retImage;
            }
            try {
                final GraphicsEnvironment genv = GraphicsEnvironment.getLocalGraphicsEnvironment();
                final GraphicsDevice gd = genv.getDefaultScreenDevice();
                final GraphicsConfiguration gc = gd.getDefaultConfiguration();
                final ColorModel cm = gc.getColorModel();
                final boolean hasAlpha = cm.hasAlpha();
                final int cw = comp.getSize().width;
                final int ch = comp.getSize().height;
                if (hasAlpha) {
                    retImage = gc.createCompatibleImage(cw, ch);
                }
                else {
                    retImage = new BufferedImage(cw, ch, 2);
                }
                if (retImage == null) {
                    return retImage;
                }
                final Graphics og = retImage.getGraphics();
                comp.paint(og);
                og.dispose();
            }
            catch (Throwable t) {}
            return retImage;
        }
    }
}
