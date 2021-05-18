/*
 * This Demo application is for showing different effects of the class Library
 * of AnimatingCardLayout class that was once developed by Dmitry Markman, Luca Lutterotti, and Sam Berlin
 * and uploaded onto javaopensource site but later when the site stopped working, this class library went
 * vanished from internet for new developers who needs to use some simple animation library on their swing
 * application. This class library is a perfect library for giving some nice effect without using loads of
 * classes.
 * Feel free to use this demo application and due to nature of Open Source feel free to use the library.
 */
package org.javadev.test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.javadev.AnimatingCardLayout;
import org.javadev.effects.CubeAnimation;
import org.javadev.effects.DashboardAnimation;
import org.javadev.effects.FadeAnimation;
import org.javadev.effects.IrisAnimation;
import org.javadev.effects.RadialAnimation;
import org.javadev.effects.SlideAnimation;

/**
 * Demo Application to showcase the effect of the animation card layout class library.
 * Thanks to 
 * @author Dmitry Markman,
 * @author Luca Lutterotti and
 * @author Sam Berlin
 */
public class CardLayoutAnimationDemo extends JFrame{
    private final JButton btn1=new JButton("Show Picture");
    private final JButton btn2=new JButton("Show Table");
    protected AnimatingCardLayout ac;
    private JRadioButton Cube;
    private JRadioButton Dashboard;
    private JRadioButton Fade_in_out;
    private JRadioButton Iris;
    private JRadioButton Radial;
    private JRadioButton Slide;
    protected JPanel cards;
    /**
     * Default Constructor
     */
    public CardLayoutAnimationDemo(){
        cards=new JPanel();
        ac=new AnimatingCardLayout(new FadeAnimation());
        ac.setAnimationDuration(1500);
        cards.setLayout(ac);
        JPanel tablePanel=createTableScreen();
        cards.add(tablePanel,"cardTable");
        JPanel imgPanel=createImageScreen();
        cards.add(imgPanel, "cardImage");
        //needed to give some memory for animation and effect
        tablePanel.setDoubleBuffered(true);
        imgPanel.setDoubleBuffered(true);
        cards.setDoubleBuffered(true);
        ac.show(cards, "cardTable");
        this.add(cards);
    }
    
    /**
     * Panel to show table and a button
     * @return 
     */
    private JPanel createTableScreen() {
        ButtonGroup btngrp=new ButtonGroup();
        JLabel label=new JLabel("Animation:");
        Cube = new JRadioButton("Cube");
        Dashboard= new JRadioButton("Dashboard");
        Fade_in_out= new JRadioButton("FadeinNout");
        Iris= new JRadioButton("Iris");
        Radial= new JRadioButton("Radial");
        Slide= new JRadioButton("Slide");
        JPanel ap=new JPanel(new BorderLayout());
        JPanel buttonPanel=new JPanel(new FlowLayout(1));
        JPanel radioPanel=new JPanel(new FlowLayout(1));
        //default setting is fade animation
        Fade_in_out.setSelected(true);
        btngrp.add(Cube);
        btngrp.add(Dashboard);
        btngrp.add(Fade_in_out);
        btngrp.add(Iris);
        btngrp.add(Radial);
        btngrp.add(Slide);
        radioPanel.add(label);
        radioPanel.add(Cube);
        radioPanel.add(Dashboard);
        radioPanel.add(Fade_in_out);
        radioPanel.add(Iris);
        radioPanel.add(Radial);
        radioPanel.add(Slide);
        //adding listeners
        Cube.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                cubebtnchanged(e);
            }
            
        });
        Dashboard.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                dashbtnchanged(e);
            }
            
        });
        Fade_in_out.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                fadebtnchanged(e);
            }
            
        });
        Iris.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                irisbtnchanged(e);
            }
            
        });
        Radial.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                radialbtnchanged(e);
            }
            
        });
        Slide.addChangeListener(new ChangeListener(){

            @Override
            public void stateChanged(ChangeEvent e) {
                slidebtnchanged(e);
            }
            
        });
        ap.add(radioPanel,"North");
        //Table rows and columns
        int nCol=8,nRows=30;
        Object[][] tableData;
        Object[] ColumnNames=new String[nCol];
        //naming each Columns
        for (int i = 0; i < ColumnNames.length; ++i) {
                ColumnNames[i] = "Column " + i;
        }
        //filling table data
        tableData = new String[nRows][nCol];
            for (int j = 0; j < nRows; ++j) {
                for (int k = 0; k < nCol; ++k) {
                    tableData[j][k] = "Row " + j + " Column " + k;
                }
            }
        final JTable table = new JTable(tableData, ColumnNames);
        table.setAutoResizeMode(0);
        final JScrollPane sp = new JScrollPane(table);
        sp.setHorizontalScrollBarPolicy(32);
        sp.setVerticalScrollBarPolicy(22);
        ap.add(sp, "Center");
        ap.setSize(this.getSize());
        btn1.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                btn1Pressed(e);
            }
        });
        buttonPanel.add(btn1);
        ap.add(buttonPanel,"South");
        return ap;
    }
    /**
     * Panel to show the image with button
     * @return 
     */
    private JPanel createImageScreen() {
     JPanel p=new JPanel();
        p.setLayout(new FlowLayout(1));
        p.setBackground(new Color(223,134,143));
        JLabel lbl=new JLabel();
        lbl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/javadev/resource/anImage.jpg")));
        p.add(lbl,"Center");
        btn2.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
               btn2Pressed(e);
            }
        });
        p.add(btn2,"South");
        
        return p;
    }
    /**All Events**/
    private void cubebtnchanged(ChangeEvent e){
        if(Cube.isSelected()){
            AnimatingCardLayout a=(AnimatingCardLayout)this.cards.getLayout();
            a.setAnimation(new CubeAnimation());
        }
    }
    private void dashbtnchanged(ChangeEvent e){
        if(Dashboard.isSelected()){
            AnimatingCardLayout a=(AnimatingCardLayout)this.cards.getLayout();
            a.setAnimation(new DashboardAnimation());
        }
    }
    private void fadebtnchanged(ChangeEvent e){
        if(Fade_in_out.isSelected()){
            AnimatingCardLayout a=(AnimatingCardLayout)this.cards.getLayout();
            a.setAnimation(new FadeAnimation());
        }
    }
    /**
     * 
     * @param e 
     */
    private void irisbtnchanged(ChangeEvent e){
       if(Iris.isSelected()){
           AnimatingCardLayout a=(AnimatingCardLayout)this.cards.getLayout(); 
           a.setAnimation(new IrisAnimation());
        } 
    }
    /**
     * 
     * @param e 
     */
    private void radialbtnchanged(ChangeEvent e){
        if(Radial.isSelected()){
            AnimatingCardLayout a=(AnimatingCardLayout)this.cards.getLayout();
            a.setAnimation(new RadialAnimation());
        }
    }
    /**
     * 
     * @param e 
     */
    private void slidebtnchanged(ChangeEvent e){
        if(Slide.isSelected()){
            AnimatingCardLayout a=(AnimatingCardLayout)this.cards.getLayout();
            a.setAnimation(new SlideAnimation());
        }
    }
    /**
     * Button Pressed Event for button 1
     * @param e 
     */
    private void btn1Pressed(ActionEvent e){
        ac.show(cards, "cardImage");
    }
    /**
     * Button pressed event of the button 2
     * @param e 
     */
    private void btn2Pressed(ActionEvent e){
        ac.show(cards, "cardTable");
    }
    
    /**
     * Main Calling function to call this demo application
     * @param args 
     */
    public static void main(String args[]){
        try {
            javax.swing.UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (
                ClassNotFoundException |
                InstantiationException |
                IllegalAccessException |
                UnsupportedLookAndFeelException ex
                )
        {
            Logger.getLogger(CardLayoutAnimationDemo.class.getName()).log(Level.SEVERE, null, ex);
        }
        EventQueue.invokeLater(new Runnable(){
            @Override
            public void run(){
                Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
                CardLayoutAnimationDemo c = new CardLayoutAnimationDemo();
                c.setTitle("Animation Demo");
                c.setSize(500, 400);
                c.setDefaultCloseOperation(3);
                c.setVisible(true);
                c.setResizable(false);
                //Some quick location setting for this demo application he he :)
                c.setLocation((int)(dim.getWidth()-500)/2,(int)(dim.getHeight()-400)/2);
            }
        });
    }
}
