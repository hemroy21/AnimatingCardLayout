package org.javadev.effects;

import java.awt.Component;
/**
 * Defines Interface for all animation classes
 * @author Dmitry Markman, Luca Lutterotti, and Sam Berlin
 */
public interface Animation
{
    Component animate(final Component p0, final Component p1, final AnimationListener p2);
    
    void setDirection(final boolean p0);
    
    void setAnimationDuration(final int p0);
}
