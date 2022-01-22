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
	BufferedImage helpscreenbg;
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
	int atkTicks = 0;
	int atkCd = 0;
	
	phButt[] buttons = new phButt[9];
	
	//methods
	public void actionPerformed(ActionEvent evt){
		if (evt.getSource() == thetimer){
			this.repaint();
		}
	}	
	
	public void paintComponent(Graphics g){
		pXDtarg = (int)((mousePos.x-640)*0.1-cameraPos.x);
		pYDtarg = (int)((mousePos.y-360)*0.1-cameraPos.y);
		menudraw.setColor(Color.BLACK);
		pXDist = (int)(0.8*pXDist+0.2*pXDtarg);
		pYDist = (int)(0.8*pYDist+0.2*pYDtarg);
		menudraw.fillRect(0,0,1280,720);
		
		//backgrounds
		menudraw.setColor(new Color(237,237,237));
		menudraw.fillRect(0-pXDist,0-pYDist,1280,720);
		menudraw.setColor(new Color(202,202,202));
		menudraw.fillRect(0-pXDist,-828-pYDist,1280,720);
		menudraw.setColor(new Color(185,185,185));
		menudraw.fillRect(1472-pXDist,0-pYDist,1280,720);
		menudraw.setColor(new Color(170,170,170));
		menudraw.fillRect(0-pXDist,-828-pYDist,1280,720);
		menudraw.setColor(new Color(140,140,140));
		menudraw.fillRect(-1472-pXDist,0-pYDist,1280,720);
		
		//change menu buttons position
		buttons[0].setLocation(144-(int)(pXDist*1.5),420-(int)(pYDist*1.5));
		buttons[1].setLocation(656-(int)(pXDist*1.5),420-(int)(pYDist*1.5));
		buttons[2].setLocation(826-(int)(pXDist*1.5),576-(int)(pYDist*1.5));
		buttons[3].setLocation(485-(int)(pXDist*1.5),576-(int)(pYDist*1.5));
		buttons[4].setLocation(144-(int)(pXDist*1.5),576-(int)(pYDist*1.5));
			
		buttons[5].setLocation(30-(int)(pXDist*1.5),-622-(int)(pYDist*1.5));
		buttons[6].setLocation(555-(int)(pXDist*1.5),1262-(int)(pYDist*1.5));
		buttons[7].setLocation(2238-(int)(pXDist*1.5),600-(int)(pYDist*1.5));
		buttons[8].setLocation(-1128-(int)(pXDist*1.5),600-(int)(pYDist*1.5));
		
		r.x = Math.min(Math.max(r.x + defX,50),1230);
		r.y = Math.min(Math.max(r.y + defY,50),620);
		
		// bottom middle button on main menu
		if (lastClick==3) {
			// dummy hitbox
			menudraw.drawImage(helpscreenbg, 1472-pXDist, 0-pYDist, null);
			menudraw.setColor(Color.red);
			menudraw.fillRect(dummy.x+1472-pXDist, dummy.y-pYDist, dummy.width, dummy.height);
			// training fighter hitbox
			menudraw.setColor(Color.black);
			menudraw.fillRect(r.x+1472-pXDist, r.y-pYDist, r.width, r.height);
			if (atking) {
				if (atkTicks<10) {
					if (left) {
						menudraw.setColor(Color.blue);
						menudraw.fillRect(atks[up].x+1472-pXDist, atks[up].y-pYDist, atks[up].width, atks[up].height);
					} else {
						menudraw.setColor(Color.blue);
						menudraw.fillRect(atks[up].x+1472-pXDist, atks[up].y-pYDist, atks[up].width, atks[up].height);
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
			menudraw.drawImage(cred, -pXDist, 828-pYDist, null);
		} 
		
		// bottom left on main menu
		else if (lastClick==4) {
			menudraw.setColor(Color.black);
			menudraw.drawString("High scores (fastest round win in (s))", -1440-(int)(pXDist*1.25), 50-(int)(pYDist*1.25));
			menudraw.drawString("Username", -1790-(int)(pXDist*1.25), 100-(int)(pYDist*1.25));
			menudraw.drawString("Time (s)", -810-(int)(pXDist*1.25), 100-(int)(pYDist*1.25));
		}
		
		// host game button & join game button
		else if (lastClick==0 || lastClick == 1) {
		 // draw character select buttons
			for (int i=0;i<4;i++) select[i].setLocation(100+(200*(i%2))-(int)(pXDist*1.25), -935+(200*(i/2)-(int)(pYDist*1.25)));
			for (int i=0;i<4;i++) {
				if (select[i].changelook(mousePos, mouseDown, retriggerCatch)){
					cameraPos = select[i].pwp;
					retriggerCatch = true;
					
					// find which button was clicked
					selected = selected == i ? -1 : i;
					hovered = -1;
				} else if (!mouseDown) {
					retriggerCatch = false;
				} 
				
				// check if button is being hovered over
				if (select[i].contains(mousePos)&&!select[i].changelook(mousePos, mouseDown, retriggerCatch)) {
					hovered = i;
				} else if (hovered!=-1) {
					if (!select[hovered].contains(mousePos)) hovered = -1;
				}
				menudraw.drawImage(select[i].drawme,(int)select[i].getX(),(int)select[i].getY(),null);
			}
			
			// if a selection/hover has been made, draw the appropriate image
			if (selected!=-1) {
				menudraw.drawImage(selectImg[2], 100+(200*(selected%2))-(int)(pXDist*1.25), -935+(200*(selected/2))-(int)(pYDist*1.25), null);
			} 
			if (hovered!=-1) {
				menudraw.drawImage(selectImg[0], 100+(200*(hovered%2))-(int)(pXDist*1.25), -935+(200*(hovered/2))-(int)(pYDist*1.25), null);
			}
			
			AllOutScrap.blnS = lastClick==0 ? true : false;
			if (AllOutScrap.ssm==null) {
				if (AllOutScrap.blnS) AllOutScrap.makeServer();
				else AllOutScrap.makeClient("localhost"); 
			} 
		} else if (lastClick>=5&&lastClick<=8) {
			AllOutScrap.ssm = null;
		}
		
		//draw menu buttons
		for (int i = 0; i<buttons.length; i++){
			if (buttons[i].changelook(mousePos, mouseDown, retriggerCatch)){
				cameraPos = buttons[i].pwp;
				retriggerCatch = true;
				lastClick = i;
			} else if (!mouseDown) {
				retriggerCatch = false;
			}
			menudraw.drawImage(buttons[i].drawme,(int)buttons[i].getX(),(int)buttons[i].getY(),null);
		}
		g.drawImage(menuscreen,0,0,null);
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
		buttons[0] = new phButt(0,0,480,130,0,828,"b1def.png","b1hov.png","b1prs.png");
		buttons[1] = new phButt(0,0,480,130,0,828,"b1def.png","b1hov.png","b1prs.png");
		buttons[2] = new phButt(0,0,310,86,0,-828,"b1def.png","b1hov.png","b1prs.png");
		buttons[3] = new phButt(0,0,310,86,-1472,0,"b1def.png","b1hov.png","b1prs.png");
		buttons[4] = new phButt(0,0,310,86,1472,0,"b1def.png","b1hov.png","b1prs.png");
		
		//return to menu buttons
		buttons[5] = new phButt(0,0,170,90,0,0,"b1def.png","b1hov.png","b1prs.png");
		buttons[6] = new phButt(0,0,170,90,0,0,"b1def.png","b1hov.png","b1prs.png");
		buttons[7] = new phButt(0,0,170,90,0,0,"b1def.png","b1hov.png","b1prs.png");
		buttons[8] = new phButt(0,0,170,90,0,0,"b1def.png","b1hov.png","b1prs.png");
		
		// character select buttons
		for (int i=0;i<4;i++) {
			select[i] = new phButt(0, 0, 200, 200,0,828,"f"+i+"def.jpg","f"+i+"def.jpg","f"+i+"def.jpg");
		}
		
		// high atk
		atks[0] = new Rectangle(r.x+10, r.y, 50, 25);
		// low atk
		atks[1] = new Rectangle(r.x+10, r.y+25, 50, 25);
		
		cred = img("ScrapFighter-CreditsImage.png");
		helpscreenbg = img("SFHelpScreenBG.jpg");	
		// hover, other player's selection, your selection
		selectImg[0] = img("hover.png");
		selectImg[1] = img("otherselect.png");
		selectImg[2] = img("select.png");
		
	}
}
