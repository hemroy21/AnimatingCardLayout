package org.javadev;

import java.awt.Container;
import java.util.ArrayList;
import java.util.List;
import java.awt.Component;
import org.javadev.effects.Animation;
import org.javadev.effects.AnimationListener;
import java.awt.CardLayout;
import java.util.Objects;
/**
 * Custom Card Layout extended from java swing CardLayout to implement some
 * animation effect.
 * @author Dmitry Markman
 * @author Luca Lutterotti
 * @author Sam Berlin
 */
public class AnimatingCardLayout extends CardLayout implements AnimationListener
{
    private Animation animation;
    protected int animationDuration;
    private Component animationPanel;
    private boolean isAnimating;
    private Component toShow;
    protected List comps;
    
    public AnimatingCardLayout() {
        this.animation = null;
        this.animationDuration = 2000;
        this.animationPanel = null;
        this.isAnimating = false;
        this.toShow = null;
        this.comps = new ArrayList();
    }
    
    public AnimatingCardLayout(final Animation anim) {
        this.animation = null;
        this.animationDuration = 2000;
        this.animationPanel = null;
        this.isAnimating = false;
        this.toShow = null;
        this.comps = new ArrayList();
        this.animation = anim;
    }
    
    @Override
    public void addLayoutComponent(final String name, final Component comp) {
        final Tuple tuple = new Tuple(name, comp);
        final int idx = this.comps.indexOf(tuple);
        if (idx == -1) {
            this.comps.add(tuple);
        }
        else {
            final Tuple existing = (Tuple)this.comps.get(idx);
            existing.comp = comp;
        }
        super.addLayoutComponent(name, comp);
    }
    
    @Override
    public void removeLayoutComponent(final Component comp) {
        for (int i = 0; i < this.comps.size(); ++i) {
            final Tuple next = (Tuple)this.comps.get(i);
            if (next.comp == comp) {
                this.comps.remove(i);
            }
        }
        super.removeLayoutComponent(comp);
    }
    
    @Override
    public void first(final Container parent) {
        synchronized (parent.getTreeLock()) {
            this.animate(parent, parent.getComponent(0), false);
        }
        // monitorexit(parent.getTreeLock())
    }
    
    @Override
    public void next(final Container parent) {
        synchronized (parent.getTreeLock()) {
            final int ncomponents = parent.getComponentCount();
            final int nextCard = (this.getCurrent(parent) + 1) % ncomponents;
            this.animate(parent, parent.getComponent(nextCard), true);
        }
        // monitorexit(parent.getTreeLock())
    }
    
    @Override
    public void previous(final Container parent) {
        synchronized (parent.getTreeLock()) {
            final int ncomponents = parent.getComponentCount();
            final int currentCard = this.getCurrent(parent);
            final int nextCard = (currentCard > 0) ? (currentCard - 1) : (ncomponents - 1);
            this.animate(parent, parent.getComponent(nextCard), false);
        }
        // monitorexit(parent.getTreeLock())
    }
    
    @Override
    public void last(final Container parent) {
        synchronized (parent.getTreeLock()) {
            final int ncomponents = parent.getComponentCount();
            this.animate(parent, parent.getComponent(ncomponents - 1), true);
        }
        // monitorexit(parent.getTreeLock())
    }
    
    @Override
    public void show(final Container parent, final String name) {
        synchronized (parent.getTreeLock()) {
            final int idx = this.indexOf(name);
            if (idx != -1) {
                this.animate(parent, parent.getComponent(idx), idx >= this.getCurrent(parent));
            }
        }
        // monitorexit(parent.getTreeLock())
    }
    
    @Override
    public void animationFinished() {
        final Container parent = this.animationPanel.getParent();
        final Component next = this.toShow;
        this.isAnimating = false;
        this.toShow = null;
        parent.remove(this.animationPanel);
        this.animationPanel = null;
        super.show(parent, this.nameOf(next));
    }
    
    private void animate(final Container parent, final Component toGoTo, final boolean direction) {
        if (this.isAnimating) {
            throw new IllegalStateException("already animating");
        }
        final Component current = this.current(parent);
        if (current == this.animationPanel) {
            return;
        }
        final String nextName = this.nameOf(toGoTo);
        final String curName = this.nameOf(current);
        if (this.animation == null || current == toGoTo || current == null || curName == null) {
            super.show(parent, nextName);
        }
        else {
            this.isAnimating = true;
            this.toShow = toGoTo;
            this.animation.setDirection(direction);
            this.animation.setAnimationDuration(this.animationDuration);
            parent.add(this.animationPanel = this.animation.animate(current, this.toShow, this), "__animation__");
            this.animationPanel.setVisible(true);
            current.setVisible(false);
            parent.validate();
        }
    }
    
    private int indexOf(final String name) {
        for (int i = 0; i < this.comps.size(); ++i) {
            final Tuple next = (Tuple)this.comps.get(i);
            if (next.name.equals(name)) {
                return i;
            }
        }
        return -1;
    }
    
    private String nameOf(final Component comp) {
        for (int i = 0; i < this.comps.size(); ++i) {
            final Tuple next =(Tuple) this.comps.get(i);
            if (next.comp == comp) {
                return next.name;
            }
        }
        return null;
    }
    
    private Component current(final Container parent) {
        for (int size = parent.getComponentCount(), i = 0; i < size; ++i) {
            final Component next = parent.getComponent(i);
            if (next.isVisible()) {
                return next;
            }
        }
        return null;
    }
    
    private int getCurrent(final Container parent) {
        for (int size = parent.getComponentCount(), i = 0; i < size; ++i) {
            if (parent.getComponent(i).isVisible()) {
                return i;
            }
        }
        return -1;
    }
    
    public Animation getAnimation() {
        return this.animation;
    }
    
    public void setAnimation(final Animation animation) {
        this.animation = animation;
    }
    
    public void setAnimationDuration(final int animationDuration) {
        this.animationDuration = ((animationDuration < 500) ? 500 : animationDuration);
    }
    
    protected static class Tuple
    {
        String name;
        Component comp;
        
        public Tuple(final String name, final Component comp) {
            this.name = name;
            this.comp = comp;
        }
        
        @Override
        public boolean equals(final Object o) {
            return o == this || (o instanceof Tuple && this.name.equals(((Tuple)o).name));
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 71 * hash + Objects.hashCode(this.name);
            return hash;
        }
    }
}
