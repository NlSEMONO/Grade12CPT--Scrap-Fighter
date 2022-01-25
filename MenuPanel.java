import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;

public class MenuPanel extends JPanel implements ActionListener, ChangeListener{ 
	//properties
	Timer thetimer = new Timer(1000/80,this);
	BufferedImage menuscreen = new BufferedImage(1280,720,BufferedImage.TYPE_INT_RGB);
	Graphics menudraw = menuscreen.getGraphics();
	BufferedImage cred;
	BufferedImage helpscreenbg;
	BufferedImage[] selectImg = new BufferedImage[3]; 
	
	JTextField usernamefield = new JTextField("Username Goes Here");
	
	JTextField ipfield = new JTextField("127.0.0.1");
	JTextField portfield = new JTextField("9112");
	JTextField chatfield = new JTextField("Chat Text Goes Here");
	JSlider theslider = new JSlider (SwingConstants.HORIZONTAL,1,10,5);
	JLabel thelabel = new JLabel("Rounds");
	JTextArea thetxtarea = new JTextArea("Chat Area!!!");
	JScrollPane thescroll = new JScrollPane(thetxtarea);
	
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
	
	phButt[] buttons = new phButt[15];
	
	double tme = 0;
	double vi = 29.6;
	double accel = -2.2;
	boolean jump = false, duck = false;
	boolean deced = false;
	int jumpCd = 0;
	
	int add = 50;
	
	int numrounds;
	String ipaddress;
	JColorChooser thecolchooser = new JColorChooser();
	
	//methods
	public void actionPerformed(ActionEvent evt){
		if (evt.getSource() == thetimer){
			this.repaint();
		}
	}
	
	public void stateChanged(ChangeEvent evt){
		if (evt.getSource() == theslider){
			int intValue = theslider.getValue();
			System.out.println(intValue);
			thelabel.setText(""+intValue);
			numrounds = intValue;
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
			
		buttons[5].setLocation(50-(int)(pXDist*1.5),-622-(int)(pYDist*1.5));
		buttons[6].setLocation(555-(int)(pXDist*1.5),1262-(int)(pYDist*1.5));
		buttons[7].setLocation(2238-(int)(pXDist*1.5),600-(int)(pYDist*1.5));
		buttons[8].setLocation(-1128-(int)(pXDist*1.5),600-(int)(pYDist*1.5));
		
		buttons[9].setLocation(971-(int)(pXDist*1.5),-622-(int)(pYDist*1.5)); 	//Ready Up Button
		buttons[10].setLocation(655-(int)(pXDist*1.5),-622-(int)(pYDist*1.5)); 	//Colour Button
		buttons[11].setLocation(10000-(int)(pXDist*1.5),-10000-(int)(pYDist*1.5));	//croom Button
		buttons[12].setLocation(10000-(int)(pXDist*1.5),-10000-(int)(pYDist*1.5));	//jroom Button
		buttons[13].setLocation(10000-(int)(pXDist*1.5),-10000-(int)(pYDist*1.5));	//lroom Button
		buttons[14].setLocation(1184-(int)(pXDist*1.5),-662-(int)(pYDist*1.5));	//Send Button
		
		ipfield.setLocation(250-(int)(pXDist*1.5),-682-(int)(pYDist*1.5));
		chatfield.setLocation(971-(int)(pXDist*1.5),-662-(int)(pYDist*1.5));
		usernamefield.setLocation(250-(int)(pXDist*1.5),-772-(int)(pYDist*1.5));
		theslider.setLocation(655-(int)(pXDist*1.5),-682-(int)(pYDist*1.5));
		thelabel.setLocation(855-(int)(pXDist*1.5),-682-(int)(pYDist*1.5));
		thescroll.setLocation(971-(int)(pXDist*1.5),-1195-(int)(pYDist*1.5));
		
		r.x = Math.min(Math.max(r.x + defX,50),1230);
		r.y = Math.min(Math.max(r.y + defY,50),620);
		
		// bottom middle button on main menu
		if (lastClick==3) {
			AllOutScrap.theframe.requestFocus();
			
			if (jump) {
				r.y = (720-r.height)-((int)((vi*tme)+(accel*tme*tme)));
				if (r.y-r.height > 720) {
					r.y = 720-r.height;
					jump = false;
					tme = -0.5;
					jumpCd = 30;
				}
				tme+=0.5;
			}else if (jumpCd>0) {
				jumpCd--;
			}
			
			// duck
			if (duck&&!deced) {
				r.height /= 2;
				r.y = 720+r.height;
				deced = true;
				add = 75;
			} else if (!duck&&deced) {
				deced = false;
				r.height*=2;
				r.y = 720-r.height;
				add = 50;
			}
			
			// dummy hitbox
			menudraw.drawImage(helpscreenbg, 1472-pXDist, 0-pYDist, null);
			menudraw.setColor(Color.red);
			menudraw.fillRect(dummy.x+1472-pXDist, dummy.y-pYDist, dummy.width, dummy.height);
			// training fighter hitbox
			menudraw.setColor(Color.black);
			menudraw.fillRect(r.x+1472-pXDist, r.y-pYDist + add, r.width, r.height);
			
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
			for (int i=0;i<4;i++) select[i].setLocation(50+(200*(i%2))-(int)(pXDist*1.25), -985+(200*(i/2)-(int)(pYDist*1.25)));
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
				menudraw.drawImage(selectImg[2], 50+(200*(selected%2))-(int)(pXDist*1.25), -985+(200*(selected/2))-(int)(pYDist*1.25), null);
			} 
			if (hovered!=-1) {
				menudraw.drawImage(selectImg[0], 50+(200*(hovered%2))-(int)(pXDist*1.25), -985+(200*(hovered/2))-(int)(pYDist*1.25), null);
			}
			
			AllOutScrap.blnS = lastClick==0 ? true : false;
			if (AllOutScrap.ssm==null) {
				if (AllOutScrap.blnS) AllOutScrap.makeServer();
				else AllOutScrap.makeClient("localhost"); 
			} 
			
			ipfield.setEditable(true);
			ipfield.setVisible(true);
			chatfield.setEditable(true);
			chatfield.setVisible(true);
			usernamefield.setEditable(true);
			usernamefield.setVisible(true);
			theslider.setVisible(true);
			
			if (AllOutScrap.blnS == true){
				buttons[11].setLocation(250-(int)(pXDist*1.5),-622-(int)(pYDist*1.5));	//croom Button
			} else{ 
				buttons[12].setLocation(250-(int)(pXDist*1.5),-622-(int)(pYDist*1.5));	//jroom Button
				buttons[13].setLocation(426-(int)(pXDist*1.5),-622-(int)(pYDist*1.5));	//lroom Button
			}
			
			
		} else if (lastClick>=5&&lastClick<=8) {
			AllOutScrap.ssm = null;
			ipfield.setEditable(false);
			ipfield.setVisible(false);
			chatfield.setEditable(false);
			chatfield.setVisible(false);
			usernamefield.setEditable(false);
			usernamefield.setVisible(false);
			theslider.setVisible(false);
			buttons[11].setLocation(10000-(int)(pXDist*1.5),-10000-(int)(pYDist*1.5));	//croom Button
			buttons[12].setLocation(10000-(int)(pXDist*1.5),-10000-(int)(pYDist*1.5));	//jroom Button
			buttons[13].setLocation(10000-(int)(pXDist*1.5),-10000-(int)(pYDist*1.5));	//lroom Button
			AllOutScrap.theframe.requestFocus();
			
		} else if (lastClick == 14){
			lastClick = AllOutScrap.blnS ? 0 : 1;
			thetxtarea.append("\n" + usernamefield.getText() + ": " + chatfield.getText());
		} else if (lastClick == 9){
			lastClick = AllOutScrap.blnS ? 0 : 1;
			if (AllOutScrap.blnS == true){
				AllOutScrap.ready[0] = true;
			}else{
				AllOutScrap.ready[1] = true;
			}
		} else if (lastClick == 10){
			Color thecolor = thecolchooser.showDialog(themenucol, "Colour Chooser", new Color(0,0,0));
			AllOutScrap.rgb[0] = thecolor.getRed());
			AllOutScrap.rgb[1] = thecolor.getGreen());
			AllOutScrap.rgb[2] = thecolor.getBlue());
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
		
		setLayout(null);
		setPreferredSize(new Dimension(1280,720));
		
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
		
		buttons[9] = new phButt(0,0,284,90,0,828,"b1def.png","b1hov.png","b1prs.png"); 	//readyupbutton
		buttons[10] = new phButt(0,0,266,90,0,828,"b1def.png","b1hov.png","b1prs.png"); 	//colorbutton
		
		buttons[11] = new phButt(0,0,355,90,0,828,"b1def.png","b1hov.png","b1prs.png"); 	//croombutton
		buttons[12] = new phButt(0,0,177,90,0,828,"b1def.png","b1hov.png","b1prs.png"); 	//jroombutton
		buttons[13] = new phButt(0,0,176,90,0,828,"b1def.png","b1hov.png","b1prs.png");	//lroombutton 

		buttons[14] = new phButt(0,0,71,30,0,828,"b1def.png","b1hov.png","b1prs.png");	//sendbutton 
	
		
		ipfield.setSize(355,50);
		//ipfield.setHorizontalAlignment(JTextField.CENTER);
		ipfield.setEditable(false);
		ipfield.setVisible(false);
		add(ipfield);
		
		
		chatfield.setSize(213,30);
		//portfield.setHorizontalAlignment(JTextField.CENTER);
		chatfield.setEditable(false);
		chatfield.setVisible(false);
		add(chatfield);
		
		usernamefield.setSize(671,80);
		//usernamefield.setHorizontalAlignment(JTextField.CENTER);
		Font font1 = new Font("SansSerif", Font.PLAIN, 30);
		usernamefield.setFont(font1);
		usernamefield.setEditable(false);
		usernamefield.setVisible(false);
		add(usernamefield);
		
		theslider.setSize(200,50);
		theslider.setVisible(false);
		theslider.addChangeListener(this);
		add(theslider);
		
		thelabel.setSize(66, 50);
		thelabel.setHorizontalAlignment(JTextField.CENTER);
		add(thelabel);
		
		thetxtarea.setEditable(false);
		thescroll.setSize(284,523);
		add(thescroll);
		//multi-line
		
		AllOutScrap.theframe.requestFocus();
		
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
