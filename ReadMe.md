### Description
This Java Animating Card Layout is not my own property but is not available anywhere around to use, so have shared over
github in the hope that it will become useful for new learners and tweakers who wishes to use some cool but light weight animation on their swing based application.

This less known java animation class were the dreamchild of Dmitry Markman, Luca Lutterotti, and Sam Berlin. This open source extension to Java's standard CardLayout manager uses animated transitions to replace a card's components with another card's components. Once uploaded onto java.net as project but later as the java.net site gone down the project as well the file gone out of the net that made impossible new comers to try this small library as an animation library for a small java project and application development.

### How to Build:
Simply download as zip or by git clone command then open using netbeans, then clean and build to get the jar file of the library.
Double click the generated jar file to run the demo application to check if the library is built perfectly or not.


### USES AND MINIMUM REQUIREMENT
Requires atleast java 5 or later.

This class library provides APIs to achive six type of animations.

1) AnimationCardLayout:
   The main entry point of the all effect collection of this class library.
   provides
	* different methods to instantiate
	* getter method to get animation referenece
	* setter method to set animation
	* setter method to set the animation duration (minimum 500ms)

   * Instantiate by calling,
   AnimatingCardLayout animationcardLayout=new AnimatingCardLayout();
   This will provide an instance of AnimatingCardLayout class without any
   animation effect.

   With associated animation effect.
   AnimatingCardLayout animationCardLayout=new AnimatingCardLayout(Animation animation)
	
   Methods:
	A. public AnimatingCardLayout() for creating an instance without any animation.
	B. public AnimatingCardLayout(Animation animation) for creating an instance with animation effect.
	C. public Animation getAnination() to get the associated animation effect of the animatingCardLayout class.

   public void setAnimationDuration(int duration) to set the time of animation delays in milliseconds minimum is 500ms.
       

1) Cube Animation: 
   Simply call the animation class with the provided constructor or the entry point.
	Instanciate by calling,
	CubeAnimation cubeanimation=new CubeAnimation();
	
	then add it to the AnimatingCardLayout by using the provided
	setAnimation method of the AnimatingCardLayout class
	
	Although other methods are made available by the animation class but not neccessary.

2) Dashboard Animation: 
   Simply call the animation class with the provided constructor or the entry point.
	Instanciate by calling,
	DashboardAnimation dashboardanimation=new DashboardAnimation();
	
	then add it to the AnimatingCardLayout by using the provided
	setAnimation method of the AnimatingCardLayout class
	
	Although other methods are made available by the animation class but not neccessary.

3) Fade Animation: 
   Simply call the animation class with the provided constructor or the entry point.
	Instanciate by calling,
	FadeAnimation fadeanimation=new FadeAnimation();
	
	then add it to the AnimatingCardLayout by using the provided
	setAnimation method of the AnimatingCardLayout class
	
	Although other methods are made available by the animation class but not neccessary.

4) Iris Animation: 
   Simply call the animation class with the provided constructor or the entry point.
	Instanciate by calling,
	IrisAnimation irisanimation=new IrisAnimation();
	
	then add it to the AnimatingCardLayout by using the provided
	setAnimation method of the AnimatingCardLayout class
	
	Although other methods are made available by the animation class but not neccessary.
5) Cube Animation: 
   Simply call the animation class with the provided constructor or the entry point.
	Instanciate by calling,
	RadialAnimation radialanimation=new RadialAnimation();
	
	then add it to the AnimatingCardLayout by using the provided
	setAnimation method of the AnimatingCardLayout class
	
	Although other methods are made available by the animation class but not neccessary.

6) Slide Animation: 
   Simply call the animation class with the provided constructor or the entry point.
	Instanciate by calling,
	SlideAnimation slideanimation=new SlideAnimation();
	
	then add it to the AnimatingCardLayout by using the provided
	setAnimation method of the AnimatingCardLayout class
	
	Although other methods are made available by the animation class but not neccessary.


Here is the example for quick implementation:
```
import java.awt.event.ActionEvent;
import javax.swing.SwingUtilities;

/**
 * @author Bristi Roy
 */
public class QuickDemo extends javax.swing.JFrame{
    javax.swing.JPanel Apanel;
    javax.swing.JPanel panel1;
    javax.swing.JPanel panel2;
    javax.swing.JButton btn1;
    javax.swing.JButton btn2;
    org.javadev.layout.AnimatingCardLayout ac;
	
	
    public QuickDemo(){
        Apanel=new javax.swing.JPanel();
        panel1=new javax.swing.JPanel(new java.awt.FlowLayout(1));
        panel2=new javax.swing.JPanel(new java.awt.FlowLayout(1));
        btn1=new javax.swing.JButton("Next");
        btn2=new javax.swing.JButton("Back");
        panel1.setBackground(new java.awt.Color(123,123,123));
        panel2.setBackground(new java.awt.Color(232,205,202));
        panel1.add(btn1);
        panel2.add(btn2);
        btn1.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btn1action(e);
            }
        });
        btn2.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btn2action(e);
            }
        });
         
        //instanciating animatingcardLayout
        ac=new org.javadev.AnimatingCardLayout(new org.javadev.effects.FadeAnimation());
        Apanel.setLayout(ac);
        Apanel.add(panel1,"panel1");
        Apanel.add(panel2,"panel2");
        ac.show(this.Apanel, "panel1");
        this.add(Apanel);
        this.setSize(400,400);
        this.setVisible(true);
        this.setDefaultCloseOperation(3);
        this.setResizable(false);
        this.setTitle("Quick Demo");
	}
    private void btn1action(ActionEvent e){
        this.ac.show(this.Apanel, "panel2");
    }
    private void btn2action(ActionEvent e){
         this.ac.show(this.Apanel, "panel1");
    }
        
    public static void main(String[] args){
         SwingUtilities.invokeLater(new Runnable(){
             @Override
             public void run(){
                 new QuickDemo();
             }
         });
     }
}
```

Take a look at the demo application to get the idea of the implementation of the animation on any swing application.

### License
AnimatingCardLayout is subject to the Berkeley Software Distribution (BSD) license.
