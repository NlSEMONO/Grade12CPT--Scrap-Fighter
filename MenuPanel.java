import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;

public class MenuPanel extends JPanel implements ActionListener{ 
	//properties
	Timer thetimer = new Timer(1000/80,this);
	BufferedImage menuscreen = new BufferedImage(1280,720,BufferedImage.TYPE_INT_RGB);
	Graphics menudraw = menuscreen.getGraphics();
	BufferedImage cred;
	BufferedImage[] selectImg = new BufferedImage[3]; 
	
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
	
	Rectangle r = new Rectangle(300, 720-50, 50, 50);
	Rectangle dummy = new Rectangle(1280/4*3, 720-100, 50, 100);
	Rectangle[] atks = new Rectangle[2];
	phButt[] select = new phButt[4];
	
	int defY = 0;
	int defX = 0;
	boolean left = false;
	boolean atking = false;
	int up = 0;
	int selected = -1;
	int hovered = -1;
	
	int lastClick = -1;
	int ticksSince = 0;
	int atkTicks = 0;
	int atkCd = 0;
	
	phButt[] buttons = new phButt[9];
	
	//methods
	public void actionPerformed(ActionEvent evt){
		if (evt.getSource() == AllOutScrap.ssm){
			
		}
		if (evt.getSource() == thetimer){
			this.repaint();
		}
	}
	
	public void paintComponent(Graphics g){
		pXDtarg = (int)((mousePos.x-640)*1-cameraPos.x);
		pYDtarg = (int)((mousePos.y-360)*1-cameraPos.y);
		menudraw.setColor(Color.BLACK);
		pXDist = (int)(0.8*pXDist+0.2*pXDtarg);
		pYDist = (int)(0.8*pYDist+0.2*pYDtarg);
		menudraw.fillRect(0,0,1280,720);
		
		//background
		menudraw.setColor(new Color(237,237,237));
		menudraw.fillRect(-320-(int)(pXDist*0.5),-4600-(int)(pYDist*0.5),1920,5600);
		menudraw.fillRect(-7360-(int)(pXDist*0.5),-180-(int)(pYDist*0.5),16000,1080);
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
		
		//change menu buttons position
		buttons[0].setLocation(144-(int)(pXDist*0.1),420-(int)(pYDist*0.1));
		buttons[1].setLocation(656-(int)(pXDist*0.1),420-(int)(pYDist*0.1));
		buttons[2].setLocation(826-(int)(pXDist*0.1),576-(int)(pYDist*0.1));
		buttons[3].setLocation(485-(int)(pXDist*0.1),576-(int)(pYDist*0.1));
		buttons[4].setLocation(144-(int)(pXDist*0.1),576-(int)(pYDist*0.1));
			
		buttons[5].setLocation(30-(int)(pXDist*0.1),-172-(int)(pYDist*0.1));
		buttons[6].setLocation(555-(int)(pXDist*0.1),812-(int)(pYDist*0.1));
		buttons[7].setLocation(1438-(int)(pXDist*0.1),600-(int)(pYDist*0.1));
		buttons[8].setLocation(-328-(int)(pXDist*0.1),600-(int)(pYDist*0.1));
		
		r.x += defX;
		r.y+=defY;
		
		// wait for transition
		if (ticksSince>7) {
			// bottom middle button on main menu
			if (lastClick==3) {
				// dummy hitbox
				menudraw.setColor(Color.red);
				menudraw.fillRect(dummy.x, dummy.y, dummy.width, dummy.height);
				// training fighter hitbox
				menudraw.setColor(Color.black);
				menudraw.fillRect(r.x, r.y, r.width, r.height);
				menudraw.drawString("Q - High attack", 200, 50);
				menudraw.drawString("E - Low attack", 700, 50);
				if (atking) {
					if (atkTicks<10) {
						if (left) {
							menudraw.setColor(Color.blue);
							menudraw.fillRect(atks[up].x, atks[up].y, atks[up].width, atks[up].height);
						} else {
							menudraw.setColor(Color.blue);
							menudraw.fillRect(atks[up].x, atks[up].y, atks[up].width, atks[up].height);
						}
					} else if (atkTicks==10) {
						atkTicks=-1;
						atking = false;
						atkCd=40;
					}
					atkTicks++;
				} else if (atkCd>0) {
					atkCd--;
				}
			} 
			// bottom right on main menu
			else if (lastClick==2) {
				menudraw.drawImage(cred, 0, 0, null);
			} 
			
			// bottom left on main menu
			else if (lastClick==4) {
				menudraw.setColor(Color.black);
				menudraw.drawString("High scores (fastest round win in (s))", 400, 50);
				menudraw.drawString("Username", 50, 100);
				menudraw.drawString("Time (s)", 1280-250, 100);
			}
			
			// host game button & join game button
			else if (lastClick==0 || lastClick == 1) {
			 // draw character select buttons
				
				for (int i=0;i<4;i++) select[i].setLocation(100+(200*(i%2)), 100+(200*(i/2)));
				for (int i=0;i<4;i++) {
					if (select[i].changelook(mousePos, mouseDown, retriggerCatch)){
						cameraPos = select[i].pwp;
						retriggerCatch = true;
						selected = i;
						hovered = -1;
					} else if (!mouseDown) {
						retriggerCatch = false;
					} 
					
					if (select[i].contains(mousePos)&&!select[i].changelook(mousePos, mouseDown, retriggerCatch)) {
						hovered = i;
					}
					menudraw.drawImage(select[i].drawme,(int)select[i].getX(),(int)select[i].getY(),null);
				}
				if (selected!=-1) {
					menudraw.drawImage(selectImg[2], 100+(200*(selected%2)), 100+(200*(selected/2)), null);
				} 
				if (hovered!=-1) {
					menudraw.drawImage(selectImg[0], 100+(200*(hovered%2)), 100+(200*(hovered/2)), null);
				}
			} 
		}
		
		//draw menu buttons
		for (int i = 0; i<buttons.length; i++){
			if (buttons[i].changelook(mousePos, mouseDown, retriggerCatch)){
				cameraPos = buttons[i].pwp;
				retriggerCatch = true;
				lastClick = i;
				ticksSince = 0;
			} else if (!mouseDown) {
				retriggerCatch = false;
			}
			menudraw.drawImage(buttons[i].drawme,(int)buttons[i].getX(),(int)buttons[i].getY(),null);
		}
		g.drawImage(menuscreen,0,0,null);
		ticksSince++;
		if (ticksSince==Integer.MAX_VALUE)ticksSince=80;
	}
	
	public BufferedImage img(String fileName) {
		// jar ver only
		try {
			return ImageIO.read(getClass().getResourceAsStream(fileName));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	
	
	//constructor
	public MenuPanel(){
		super();
		thetimer.start();
		menudraw.setFont(new Font("Small Fonts",Font.PLAIN,30));
		
		//switch to menu buttons
		buttons[0] = new phButt(0,0,480,130,0,7920,"b1def.png","b1hov.png","b1prs.png");
		buttons[1] = new phButt(0,0,480,130,0,7920,"b1def.png","b1hov.png","b1prs.png");
		buttons[2] = new phButt(0,0,310,86,0,-7920,"b1def.png","b1hov.png","b1prs.png");
		buttons[3] = new phButt(0,0,310,86,-14080,0,"b1def.png","b1hov.png","b1prs.png");
		buttons[4] = new phButt(0,0,310,86,14080,0,"b1def.png","b1hov.png","b1prs.png");
		
		//return to menu buttons
		buttons[5] = new phButt(0,0,170,90,0,0,"b1def.png","b1hov.png","b1prs.png");
		buttons[6] = new phButt(0,0,170,90,0,0,"b1def.png","b1hov.png","b1prs.png");
		buttons[7] = new phButt(0,0,170,90,0,0,"b1def.png","b1hov.png","b1prs.png");
		buttons[8] = new phButt(0,0,170,90,0,0,"b1def.png","b1hov.png","b1prs.png");
		
		// character select buttons
		for (int i=0;i<4;i++) {
			select[i] = new phButt(0, 0, 200, 200,0,7920,"f"+i+"def.jpg","f"+i+"def.jpg","f"+i+"def.jpg");
		}
		
		// high atk
		atks[0] = new Rectangle(r.x+10, r.y, 50, 25);
		// low atk
		atks[1] = new Rectangle(r.x+10, r.y+25, 50, 25);
		
		cred = img("ScrapFighter-CreditsImage.png");
		
		// hover, other player's selection, your selection
		selectImg[0] = img("hover.png");
		selectImg[1] = img("otherselect.png");
		selectImg[2] = img("select.png");
		
	}
}
