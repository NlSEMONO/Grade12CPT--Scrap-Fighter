import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;

public class MenuPanel extends JPanel implements ActionListener{ 
	//properties
	Timer thetimer = new Timer(1000/60,this);
	BufferedImage menuscreen = new BufferedImage(1280,720,BufferedImage.TYPE_INT_RGB);
	Graphics menudraw = menuscreen.getGraphics();
	
	//combine mouse x and y into one point
	Point mousePos = new Point(640,360);
	int pXDtarg = 0;
	int pYDtarg = 0;
	int pXDist = 0;
	int pYDist = 0;
	//combine camera x and y into one point
	Point cameraPos = new Point(0,0);
	boolean mouseDown = false;
	boolean retriggerCatch = false;
	boolean hostMode = false;
	
	phButt[] buttons = new phButt[7];
	
	//methods
	public void actionPerformed(ActionEvent evt){
		if (evt.getSource() == AllOutScrap.ssm){
			
		}
		if (evt.getSource() == thetimer){
			this.repaint();
		}
	}
	
	public void paintComponent(Graphics g){
		pXDtarg = (int)((mousePos.x-640)*0.5-cameraPos.x);
		pYDtarg = (int)((mousePos.y-360)*0.5-cameraPos.y);
		menudraw.setColor(Color.BLACK);
		pXDist = (int)(0.8*pXDist+0.2*pXDtarg);
		pYDist = (int)(0.8*pYDist+0.2*pYDtarg);
		menudraw.fillRect(0,0,1280,720);
		
		//background
		menudraw.setColor(new Color(237,237,237));
		menudraw.fillRect(-320-(int)(pXDist*0.5),-4600-(int)(pYDist*0.5),1920,5600);
		menudraw.fillRect(-4600-(int)(pXDist*0.5),-180-(int)(pYDist*0.5),16000,1080);
		menudraw.setColor(new Color(255,255,255));
		menudraw.fillOval(320-(int)(pXDist*0.5),50-(int)(pYDist*0.5),640,640);
		
		//ground layers
		menudraw.setColor(new Color(202,202,202));
		menudraw.fillRect(-256-(int)(pXDist*0.4),446-(int)(pYDist*0.4),1792,300);
		menudraw.setColor(new Color(185,185,185));
		menudraw.fillRect(-192-(int)(pXDist*0.3),533-(int)(pYDist*0.3),1664,300);
		menudraw.setColor(new Color(170,170,170));
		menudraw.fillRect(-128-(int)(pXDist*0.2),576-(int)(pYDist*0.2),1536,300);
		menudraw.setColor(new Color(140,140,140));
		menudraw.fillRect(-96-(int)(pXDist*0.15),605-(int)(pYDist*0.15),1472,1357);
		
		//clouds
		menudraw.setColor(new Color(245,245,245));
		menudraw.fillRect(-192-(int)(pXDist*0.3),-2000-(int)(pYDist*0.3),1664,1900);
		menudraw.fillRect(-192-(int)(pXDist*0.3),-2600-(int)(pYDist*0.3),1664,300);
		menudraw.setColor(new Color(250,250,250));
		menudraw.fillRect(-128-(int)(pXDist*0.2),-1000-(int)(pYDist*0.2),1536,900);
		menudraw.setColor(new Color(255,255,255));
		menudraw.fillRect(-96-(int)(pXDist*0.15),-500-(int)(pYDist*0.15),1472,400);
		
		menudraw.setColor(new Color(5,5,14));
		menudraw.fillRect(-69-(int)(pXDist*0.1),400-(int)(pYDist*0.1),10,150);
		
		//change menu buttons position
		for (int i = 0; i<4; i++){
			buttons[i].setLocation(144+(i%2*512)-(int)(pXDist*0.1),420+156*(i/2)-(int)(pYDist*0.1));
		}
		
		buttons[4].setLocation(30-(int)(pXDist*0.1),-172-(int)(pYDist*0.1));
		buttons[5].setLocation(555-(int)(pXDist*0.1),812-(int)(pYDist*0.1));
		buttons[6].setLocation(1438-(int)(pXDist*0.1),600-(int)(pYDist*0.1));
		
		//draw menu buttons
		for (int i = 0; i<buttons.length; i++){
			if (buttons[i].changelook(mousePos, mouseDown, retriggerCatch)){
				cameraPos = buttons[i].pwp;
				retriggerCatch = true;
			} else if (!mouseDown) {
				retriggerCatch = false;
			}
			menudraw.drawImage(buttons[i].drawme,(int)buttons[i].getX(),(int)buttons[i].getY(),null);
		}
		g.drawImage(menuscreen,0,0,null);
	}
	
	//constructor
	public MenuPanel(){
		super();
		thetimer.start();
		menudraw.setFont(new Font("Small Fonts",Font.PLAIN,30));
		
		//switch to menu buttons
		buttons[0] = new phButt(0,0,480,130,0,7920,"b1def.png","b1hov.png","b1prs.png");
		buttons[1] = new phButt(0,0,480,130,0,7920,"b1def.png","b1hov.png","b1prs.png");
		buttons[2] = new phButt(0,0,480,86,0,-7920,"b1def.png","b1hov.png","b1prs.png");
		buttons[3] = new phButt(0,0,480,86,-14080,0,"b1def.png","b1hov.png","b1prs.png");
		
		//return to menu buttons
		buttons[4] = new phButt(0,0,170,90,0,0,"b1def.png","b1hov.png","b1prs.png");
		buttons[5] = new phButt(0,0,170,90,0,0,"b1def.png","b1hov.png","b1prs.png");
		buttons[6] = new phButt(0,0,170,90,0,0,"b1def.png","b1hov.png","b1prs.png");
	}
}
