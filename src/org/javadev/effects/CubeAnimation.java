package org.javadev.effects;

import java.awt.image.PixelGrabber;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Graphics;
import java.awt.BasicStroke;
import javax.swing.JPanel;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Color;
import java.awt.Composite;
import java.awt.AlphaComposite;
import java.awt.Toolkit;
import java.awt.image.MemoryImageSource;
import java.awt.image.ColorModel;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import javax.swing.SwingUtilities;
import java.awt.Component;
/**
 * It is the cube animation class provides cube animation effect to the java panel.
 * @author Dmitry Markman
 * @author Luca Lutterotti
 * @author Sam Berlin
 */
public class CubeAnimation implements Animation
{
    SpecialPanel animationPanel;
    private AnimationListener listener;
    boolean direction;
    int animationDuration;
    
    @Override
    public void setDirection(final boolean direction) {
        this.direction = direction;
    }
    
    @Override
    public void setAnimationDuration(final int animationDuration) {
        this.animationDuration = ((animationDuration < 1000) ? 1000 : animationDuration);
    }
    
    @Override
    public Component animate(final Component toHide, final Component toShow, final AnimationListener listener) {
        this.listener = listener;
        this.animationPanel = new SpecialPanel(this, this.direction ? toHide : toShow, this.direction ? toShow : toHide);
        this.animationPanel.beginAngle = (this.direction ? 0 : 180);
        this.animationPanel.endAngle = (this.direction ? 180 : 0);
        this.animationPanel.needToStartThread = true;
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
                CubeAnimation.this.animationPanel = null;
                CubeAnimation.this.listener.animationFinished();
                CubeAnimation.access$1(CubeAnimation.this, null);
            }
        });
    }
    
    public CubeAnimation() {
        this.animationPanel = null;
        this.listener = null;
        this.direction = true;
        this.animationDuration = 2000;
    }
    /* synthetic */ 
    static void access$1(final CubeAnimation $0, final AnimationListener $1) {
        $0.listener = $1;
    }
    
    final class SpecialOp implements BufferedImageOp
    {
        CubeAnimation owner;
        double angle;
        double epsilon;
        double cosa;
        double sinea;
        boolean positiveDirection;
        int[] pixels;
        int[] destPixels;
        private Object kernelLock;
        BufferedImageOp convOp;
        BufferedImage dest1;
        
//        SpecialOp(final CubeAnimation this$0) {
//            this(this$0, 0.0);
//        }
//        SpecialOp(CubeAnimation c,final double ang){
//            this.owner=c;
//            this.angle=ang;
//        }
        SpecialOp(final double angle) {
            this.angle = 0.0;
            this.epsilon = 0.30000001192092896;
            this.cosa = Math.cos(3.141592653589793 * this.angle / 180.0);
            this.sinea = Math.sin(3.141592653589793 * this.epsilon * this.angle / 180.0);
            this.positiveDirection = false;
            this.pixels = null;
            this.destPixels = null;
            this.kernelLock = new Object();
            this.convOp = null;
            this.setAngle(angle);
            this.setConvOp(1, 20.0f, 1.0f);
        }
        
        public void setAngle(final double angle) {
            this.angle = angle;
            this.cosa = Math.cos(3.141592653589793 * angle / 180.0);
            this.sinea = Math.sin(3.141592653589793 * this.epsilon * angle / 180.0);
        }
        
        public void setPositiveDirection(final boolean positiveDirection) {
            this.positiveDirection = positiveDirection;
        }
        
        public void setPixels(final int[] pixels) {
            this.pixels = pixels;
            if (pixels == null) {
                this.destPixels = null;
            }
            else {
                this.destPixels = new int[pixels.length];
            }
        }
        
        @Override
        public synchronized BufferedImage filter(final BufferedImage src, BufferedImage dest) {
            if (dest == null && this.destPixels == null) {
                dest = this.createCompatibleDestImage(src, null);
            }
            final int w = src.getWidth();
            final int h = src.getHeight();
            final double kw = w * (1.0 - this.cosa);
            final double k1 = this.sinea / w;
            double xx = kw;
            for (int x = 0; x < w; ++x) {
                final int i = this.positiveDirection ? x : (w - x);
                final double k2 = 1.0 - k1 * i;
                final double k3 = (1.0 - k2) * h / 2.0;
                int currIndex = x;
                double yy = k3;
                for (int y = 0; y < h; ++y) {
                    final int px = (this.pixels == null) ? src.getRGB(x, y) : this.pixels[currIndex];
                    final int iyy = (int)yy;
                    final int ixx = (int)xx;
                    if (ixx >= 0 && ixx < w && iyy >= 0 && iyy < h) {
                        if (this.destPixels == null) {
                            dest.setRGB(ixx, iyy, px);
                        }
                        else {
                            this.destPixels[iyy * w + ixx] = px;
                        }
                    }
                    currIndex += w;
                    yy += k2;
                }
                xx += this.cosa;
            }
            if (this.destPixels != null && dest == null) {
                final MemoryImageSource source = new MemoryImageSource(w, h, this.destPixels, 0, w);
                final Image img = Toolkit.getDefaultToolkit().createImage(source);
                if (this.dest1 == null) {
                    this.dest1 = new BufferedImage(w, h, src.getType());
                }
                final Graphics2D g2d = this.dest1.createGraphics();
                final Composite oldComp = g2d.getComposite();
                final Color oldColor = g2d.getColor();
                g2d.setComposite(AlphaComposite.getInstance(1, 0.0f));
                g2d.setColor(Color.white);
                g2d.fillRect(0, 0, w, h);
                g2d.setComposite(oldComp);
                g2d.setColor(oldColor);
                g2d.drawImage(img, 0, 0, null);
                g2d.dispose();
                dest = this.dest1;
            }
            return dest;
        }
        
        @Override
        public Rectangle2D getBounds2D(final BufferedImage src) {
            return src.getRaster().getBounds();
        }
        
        @Override
        public BufferedImage createCompatibleDestImage(final BufferedImage src, ColorModel destCM) {
            if (destCM == null) {
                destCM = src.getColorModel();
            }
            final int w = src.getWidth();
            final int h = src.getHeight();
            return new BufferedImage(destCM, destCM.createCompatibleWritableRaster(w, h), destCM.isAlphaPremultiplied(), null);
        }
        
        @Override
        public Point2D getPoint2D(final Point2D srcPt, Point2D dstPt) {
            if (dstPt == null) {
                dstPt = new Point2D.Float();
            }
            dstPt.setLocation(srcPt);
            return dstPt;
        }
        
        @Override
        public RenderingHints getRenderingHints() {
            return null;
        }
        
        public void setConvOp(final int dimensionOrder, final float centralValue, final float sideValue) {
            float kernelCenter = centralValue;
            float kernelOuter = sideValue;
            final int dimension = 2 * dimensionOrder + 1;
            final int matrixSize = dimension * dimension;
            float summ = (matrixSize - 1) * sideValue + centralValue;
            if (summ == 0.0f) {
                kernelCenter = 1.0f;
                kernelOuter = 0.0f;
                summ = 1.0f;
            }
            final float[] values = new float[matrixSize];
            final int centralIndex = matrixSize / 2;
            for (int i = 0; i < matrixSize; ++i) {
                values[i] = ((i == centralIndex) ? (centralValue / summ) : (sideValue / summ));
            }
            synchronized (this.kernelLock) {
                final Kernel kernel = new Kernel(dimension, dimension, values);
                if (kernel != null) {
                    this.convOp = new ConvolveOp(kernel);
                }
            }
            // monitorexit(this.kernelLock)
        }
    }
    
    final class SpecialPanel extends JPanel
    {
        CubeAnimation owner;
        BasicStroke stroke2;
        BasicStroke stroke1;
        BufferedImage firstImage;
        BufferedImage secondImage;
        Component component1;
        Component component2;
        int[] firstPixels;
        int[] secondPixels;
        SpecialOp op;
        double angle;
        public double beginAngle;
        public double endAngle;
        double deltaAngle;
        float effectTime;
        long dt;
        int counter;
        long totalDrawTime;
        public boolean needToStartThread;
        private long startTime;
        
        SpecialPanel(final CubeAnimation owner, final BufferedImage firstImage, final BufferedImage secondImage) {
            this.stroke2 = new BasicStroke(2.0f);
            this.stroke1 = new BasicStroke(1.0f);
            this.angle = 0.0;
            this.beginAngle = 0.0;
            this.endAngle = 360.0;
            this.deltaAngle = 0.5;
            this.effectTime = 2000.0f;
            this.dt = Math.round(this.effectTime * this.deltaAngle / 180.0);
            this.counter = 0;
            this.totalDrawTime = 0L;
            this.needToStartThread = false;
            this.startTime = 0L;
            this.owner = owner;
            this.firstImage = firstImage;
            this.secondImage = secondImage;
            this.angle = this.beginAngle;
            this.op = new SpecialOp(this.angle);
            this.setOpaque(false);
        }
        
        SpecialPanel(final CubeAnimation owner, final Component component1, final Component component2) {
            this.stroke2 = new BasicStroke(2.0f);
            this.stroke1 = new BasicStroke(1.0f);
            this.angle = 0.0;
            this.beginAngle = 0.0;
            this.endAngle = 360.0;
            this.deltaAngle = 0.5;
            this.effectTime = 2000.0f;
            this.dt = Math.round(this.effectTime * this.deltaAngle / 180.0);
            this.counter = 0;
            this.totalDrawTime = 0L;
            this.needToStartThread = false;
            this.startTime = 0L;
            this.owner = owner;
            this.component1 = component1;
            this.component2 = component2;
            this.angle = this.beginAngle;
            this.op = new SpecialOp(this.angle);
            this.setOpaque(false);
            this.firstImage = this.createImageFromComponent(component1);
            this.firstPixels = this.createPixels(this.firstImage);
            this.secondImage = this.createImageFromComponent(component2);
            this.secondPixels = this.createPixels(this.secondImage);
        }
        
        public void setAnimationDuration(final int animationDuration) {
            this.effectTime = (float)((animationDuration < 1000) ? 1000 : animationDuration);
            this.dt = Math.round(this.effectTime * this.deltaAngle / 180.0);
        }
        
        void startThread(final double val1, final double val2) {
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
            final Runnable repaint = new Runnable() {
                @Override
                public void run() {
                    SpecialPanel.this.repaint();
                    SpecialPanel.this.getToolkit().sync();
                }
            };
            final Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    final double absDeltaAngle = Math.abs(SpecialPanel.this.deltaAngle);
                    final long initTime = System.currentTimeMillis();
                    while ((SpecialPanel.this.angle < SpecialPanel.this.endAngle - SpecialPanel.this.deltaAngle / 2.0 || SpecialPanel.this.deltaAngle <= 0.0) && (SpecialPanel.this.angle > SpecialPanel.this.endAngle - SpecialPanel.this.deltaAngle / 2.0 || SpecialPanel.this.deltaAngle >= 0.0)) {
                        if (SpecialPanel.this.angle >= 360.0) {
                            SpecialPanel.this.angle = 0.0;
                        }
                        try {
                            Thread.sleep(SpecialPanel.this.dt);
                            SpecialPanel.this.repaint();
                            SpecialPanel.this.getToolkit().sync();
                        }
                        catch (Throwable t) {}
                    }
                    SpecialPanel.this.angle = SpecialPanel.this.endAngle;
                    SpecialPanel.this.repaint();
//                    if (SpecialPanel.this.counter != 0) {
//                        System.out.println("total count " + SpecialPanel.this.counter + " time " + (System.currentTimeMillis() - initTime) + " average time " + SpecialPanel.this.totalDrawTime / SpecialPanel.this.counter);
//                    }
                    if (SpecialPanel.this.owner != null) {
                        SpecialPanel.this.owner.rotationFinished();
                    }
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
                this.angle = this.beginAngle;
                this.startTime = System.currentTimeMillis();
            }
            final long time = System.currentTimeMillis();
            final double oldAngle = this.angle;
            this.angle += this.deltaAngle * (time - this.startTime) / this.dt;
            if ((this.angle >= this.endAngle && this.deltaAngle > 0.0) || (this.angle <= this.endAngle && this.deltaAngle < 0.0)) {
                this.angle = this.endAngle;
            }
            this.startTime = time;
            if (this.firstImage == null || this.secondImage == null) {
                return;
            }
            final double needAngle = (180.0 - this.angle > 1.0 || this.angle > 1.0) ? this.angle : oldAngle;
            final Graphics2D g2d = (Graphics2D)g;
            final int ww = this.firstImage.getWidth();
            final int hh = this.firstImage.getHeight();
            final boolean needDirection = false;
            final double cosa = Math.cos(3.141592653589793 * needAngle / 180.0 / 2.0);
            this.op.setAngle(needAngle / 2.0);
            this.op.setPositiveDirection(false);
            final long beforeDraw = System.currentTimeMillis();
            this.op.setPixels(this.firstPixels);
            g2d.drawImage(this.firstImage, this.op, -(int)(ww * (1.0 - cosa)), 0);
            this.op.setPixels(this.secondPixels);
            this.op.setPositiveDirection(true);
            final double beta = 180.0 * Math.acos(1.0 - cosa) / 3.141592653589793;
            this.op.setAngle(beta);
            g2d.drawImage(this.secondImage, this.op, 0, 0);
            this.totalDrawTime += System.currentTimeMillis() - beforeDraw;
            ++this.counter;
            this.op.setPixels(null);
        }
        
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
        
        int[] createPixels(final BufferedImage bim) {
            int[] pixels = null;
            if (bim == null) {
                return pixels;
            }
            final int ww = bim.getWidth();
            final int hh = bim.getHeight();
            pixels = new int[ww * hh];
            final PixelGrabber pg = new PixelGrabber(bim, 0, 0, ww, hh, pixels, 0, ww);
            try {
                pg.grabPixels();
            }
            catch (InterruptedException ex) {
                return null;
            }
            if ((pg.getStatus() & 0x80) != 0x0) {
                return null;
            }
            return pixels;
        }
    }
}
