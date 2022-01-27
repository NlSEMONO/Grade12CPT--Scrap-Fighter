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
	
	JTextField ipfield = new JTextField("localhost");
	JTextField portfield = new JTextField("9112");
	JTextField chatfield = new JTextField("Chat Text Goes Here");
	JSlider theslider = new JSlider (SwingConstants.HORIZONTAL,1,9,3);
	JLabel thelabel = new JLabel("Wins: "+theslider.getValue());
	JTextArea thetxtarea = new JTextArea("Chat Area!!!");
	JScrollPane thescroll = new JScrollPane(thetxtarea);
	JButton randomComponent = new JButton("don't click me");
		
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
	JLabel[][] highs = new JLabel[15][2];
	
	int defY = 0;
	int defX = 0;
	boolean left = false;
	boolean atking = false;
	int up = 0;
	int selected = -1;
	int hovered = -1;
	int otherselected = -1;
		
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
			thelabel.setText("Wins: "+intValue);
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
		
		r.x = Math.min(Math.max(r.x + defX,r.width),1280-r.width);
		r.y = Math.min(Math.max(r.y + defY,r.height),720-r.height);
		
		// bottom middle button on main menu
		if (lastClick==3) {
			AllOutScrap.theframe.requestFocus();
			
			if (jump) {
				r.y = (720-r.height)-((int)((vi*tme)+(accel*tme*tme)));
				if (r.y > 720-r.height) {
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
				r.y += r.height;
				deced = true;
			} else if (!duck&&deced) {
				deced = false;
				r.height*=2;
				r.y -= r.height/2;
			}
			
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
		// bottom right on main menu (credits screen)
		else if (lastClick==2) {
			menudraw.drawImage(cred, -pXDist, 828-pYDist, null);
		} 
		
		// bottom left on main menu (high scores)
		else if (lastClick==4) {
			menudraw.setColor(Color.black);
			menudraw.drawString("High scores (fastest round win in (s))", -1440-(int)(pXDist*1.25), 50-(int)(pYDist*1.25));
			menudraw.drawString("Username", -1790-(int)(pXDist*1.25), 100-(int)(pYDist*1.25));
			menudraw.drawString("Time (s)", -810-(int)(pXDist*1.25), 100-(int)(pYDist*1.25));
			
			// JLabels for high scorers and their times
			for (int intC=0;intC<AllOutScrap.highscores.size();intC++) {
				menudraw.drawString(AllOutScrap.highscores.get(intC)[0], -1790-(int)(pXDist*1.25), 100+intC*30+40-(int)(pYDist*1.25));
				menudraw.drawString(AllOutScrap.highscores.get(intC)[1], -810-(int)(pXDist*1.25), 100+intC*30+40-(int)(pYDist*1.25));
				
			}
		}
		
		// host game button & join game button
		else if (lastClick==0 || lastClick == 1) {
		 // draw character select buttons
			for (int i=0;i<4;i++) select[i].setLocation(50+(200*(i%2))-(int)(pXDist*1.25), -985+(200*(i/2)-(int)(pYDist*1.25)));
			for (int i=0;i<4;i++) {
				if (select[i].changelook(mousePos, mouseDown, retriggerCatch)){
					cameraPos = select[i].pwp;
					retriggerCatch = true;
					
					// find which button was clicked, if the character was already selected or the opponent selected it, reset the selection
					if (AllOutScrap.ssm!=null) {
						if (AllOutScrap.connected) {
							selected = (selected == i || (otherselected == i && otherselected!=-1)) ? -1 : i;
							hovered = -1;
							if (AllOutScrap.blnS) {
								System.out.println(selected);
								AllOutScrap.chars[0] = selected;
							} else {
								AllOutScrap.chars[1] = selected;
							}
							AllOutScrap.ssm.sendText("choose"+AllOutScrap.strSep+selected);
						} else {
							thetxtarea.append("\nWait for another player to join your room before selecting a character.");
						}
						
					} else  {
						thetxtarea.append("\nJoin/create a room before selecting a character");
					}
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
			
			// show what opponent chose
			if (otherselected!=-1) menudraw.drawImage(selectImg[1], 50+(200*(otherselected%2))-(int)(pXDist*1.25), -985+(200*(otherselected/2))-(int)(pYDist*1.25), null);
			
			AllOutScrap.blnS = lastClick==0 ? true : false;
			
			ipfield.setEditable(true);
			if (!AllOutScrap.blnS) ipfield.setVisible(true);
			chatfield.setEditable(true);
			chatfield.setVisible(true);
			usernamefield.setEditable(true);
			usernamefield.setVisible(true);
			if (AllOutScrap.blnS) theslider.setVisible(true);
			if (!AllOutScrap.blnS) thelabel.setVisible(false);
			
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
			AllOutScrap.ssm.sendText("chat"+AllOutScrap.strSep+chatfield.getText());
		// ready up if server/client is connected
		} else if (lastClick == 9&&AllOutScrap.ssm!=null){
			lastClick = AllOutScrap.blnS ? 0 : 1;
			if (AllOutScrap.blnS == true){
				AllOutScrap.locked[0] = true;
				AllOutScrap.toGame();
			}else{
				AllOutScrap.locked[1] = true;
				AllOutScrap.ssm.sendText("ready");
			}
		// color chooser for healthbar
		} else if (lastClick == 10){
			lastClick = AllOutScrap.blnS ? 0 : 1;
			Color thecolor = thecolchooser.showDialog(null, "Colour Chooser", new Color(255,0,0));
			AllOutScrap.rgb[0] = thecolor.getRed();
			AllOutScrap.rgb[1] = thecolor.getGreen();
			AllOutScrap.rgb[2] = thecolor.getBlue();
		// create room button
		} else if (lastClick==11) {
			lastClick = 0;
			AllOutScrap.makeServer();
			usernamefield.setEnabled(false);
		// join room button
		} else if (lastClick==12) {
			lastClick = 1;
			AllOutScrap.makeClient(ipfield.getText());
			AllOutScrap.ssm.sendText("connect"+AllOutScrap.strSep+usernamefield.getText());
			usernamefield.setEnabled(false);
			ipfield.setEnabled(false);
			AllOutScrap.connected = true;
			AllOutScrap.strUsers[1] = usernamefield.getText();
		// leave room button
		} else if (lastClick==13&&AllOutScrap.ssm!=null) {
			lastClick = 1;
			AllOutScrap.ssm.sendText("disconnect");
			usernamefield.setEnabled(true);
			usernamefield.setEnabled(true);
			AllOutScrap.ssm = null;
			AllOutScrap.connected = false;
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
		buttons[0] = new phButt(0,0,480,130,0,828,"ServerButtondef.png","ServerButtonhov.png","ServerButtonprs.png");		//serverbutton
		buttons[1] = new phButt(0,0,480,130,0,828,"ClientButtondef.png","ClientButtonhov.png","ClientButtonprs.png");		//clientbutton
		buttons[2] = new phButt(0,0,310,86,0,-828,"CreditsButtondef.png","CreditsButtonhov.png","CreditsButtonprs.png");		//creditsbutton
		buttons[3] = new phButt(0,0,310,86,-1472,0,"TutorialButtondef.png","TutorialButtonhov.png","TutorialButtonprs.png");	//tutorialbutton
		buttons[4] = new phButt(0,0,310,86,1472,0,"HighscoresButtondef.png","HighscoresButtonhov.png","HighscoresButtonprs.png");		//highscoresbutton
		
		//return to menu buttons
		buttons[5] = new phButt(0,0,170,90,0,0,"BackButtondef.png","BackButtonhov.png","BackButtonprs.png");
		buttons[6] = new phButt(0,0,170,90,0,0,"BackButtondef.png","BackButtonhov.png","BackButtonprs.png");
		buttons[7] = new phButt(0,0,170,90,0,0,"BackButtondef.png","BackButtonhov.png","BackButtonprs.png");
		buttons[8] = new phButt(0,0,170,90,0,0,"BackButtondef.png","BackButtonhov.png","BackButtonprs.png");
		
		// character select buttons
		for (int i=0;i<4;i++) {
			select[i] = new phButt(0, 0, 200, 200,0,828,"f"+i+"def.jpg","f"+i+"def.jpg","f"+i+"def.jpg");
		}
		
		buttons[9] = new phButt(0,0,284,90,0,828,"ReadyButtondef.png","ReadyButtonhov.png","ReadyButtonprs.png"); 		//readyupbutton
		buttons[10] = new phButt(0,0,266,90,0,828,"UXColourButtondef.png","UXColourButtonhov.png","UXColourButtonprs.png"); 	//colorbutton
		buttons[11] = new phButt(0,0,355,90,0,828,"CreateButtondef.png","CreateButtonhov.png","CreateButtonprs.png"); 	//croombutton
		buttons[12] = new phButt(0,0,177,90,0,828,"JoinButtondef.png","JoinButtonhov.png","JoinButtonprs.png"); 	//jroombutton
		buttons[13] = new phButt(0,0,176,90,0,828,"LeaveButtondef.png","LeaveButtonhov.png","LeaveButtonprs.png");		//lroombutton
		buttons[14] = new phButt(0,0,71,30,0,828,"SendButtondef.png","SendButtonhov.png","SendButtonprs.png");		//sendbutton 
	
		
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
		
		Font font1 = new Font("SansSerif", Font.PLAIN, 30), font2;
		
		try {
			font1 = Font.createFont(Font.PLAIN, this.getClass().getResourceAsStream("open-sans.regular.ttf"));
			font2 = font1.deriveFont(Font.PLAIN, 30);
			usernamefield.setFont(font2);
		} catch (Exception e) {
			font2 = new Font("SansSerif", Font.PLAIN, 30);
		}
		usernamefield.setFont(font2);
		
		usernamefield.setEditable(false);
		usernamefield.setVisible(false);
		add(usernamefield);
		
		theslider.setSize(200,50);
		theslider.setVisible(false);
		theslider.addChangeListener(this);
		add(theslider);
		
		try {
			font2 = font1.deriveFont(Font.PLAIN, 15);
		} catch (Exception e) {
			font2 = new Font("SansSerif", Font.PLAIN, 15);
		}
		
		thelabel.setFont(font2);
		
		thelabel.setSize(66, 50);
		thelabel.setHorizontalAlignment(JTextField.CENTER);
		add(thelabel);
		
		thetxtarea.setEditable(false);
		thescroll.setSize(284,523);
		add(thescroll);
		//multi-line
		
		
		try {
			font2 = font1.deriveFont(Font.PLAIN, 20);
		} catch (Exception e) {
			font2 = new Font("SansSerif", Font.PLAIN, 20);
		}
		
		for (int intC=0;intC<15;intC++) {
			for (int intC2=0;intC2<2;intC2++) {
				highs[intC][intC2] = new JLabel();
				if (intC2==1) highs[intC][intC2].setSize(100, 30);
				else highs[intC][intC2].setSize(500, 30);
				highs[intC][intC2].setFont(font2);
			}
		}
		
		AllOutScrap.theframe.requestFocus();
		
		//high atk
		atks[0] = new Rectangle(0,0, 50, 25);
		//low atk
		atks[1] = new Rectangle(0,0, 50, 25);
		
		cred = img("ScrapFighter-CreditsImage.png");
		helpscreenbg = img("SFHelpScreenBG.jpg");	
		// hover, other player's selection, your selection
		selectImg[0] = img("hover.png");
		selectImg[1] = img("otherselect.png");
		selectImg[2] = img("select.png");
		
		randomComponent.setVisible(false);
		add(randomComponent);
	}
}
