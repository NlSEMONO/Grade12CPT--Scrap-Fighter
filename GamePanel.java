import javax.swing.*;
import java.awt.image.*;
import java.awt.*;
import java.io.*;
import javax.imageio.*;
import java.util.ArrayList;
import javax.swing.event.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements ActionListener{
	Timer time = new Timer(1000/80, this);
	ArrayList<String> winners = new ArrayList<>();
	ArrayList<Double> times = new ArrayList<>();
	Color[] col = new Color[3];
	Color backCol = Color.white;
	Player phost = new Player("Keanu Reeves",25,150,75,5,100,50,0);
	Player pclient = new Player("Eggs",10,100,50,10,100,50,0);
	int defX = 0, defX2 = 0;
	int atkTicks = 0, atkCd = 0, up, up2, atkTicks2 = 0, atkCd2 = 0;
	double tme = 0, tme2 = 0;
	boolean atking = false, left = false, jump = false, duck = false;
	boolean atking2 = false, left2 = true, jump2 = false, duck2 = false;
	boolean ult = false, ult2 = false;
	int ultTicks = 0, ultTicks2 = 0;
	int serieswin = 1;
	String winner;
	boolean done = false;
	Font rounds, winlbl;
	
	int[][] hbxH = new int[4][4]; // server player hitboxes; row: 0 = idle, 1 - high attack, 2 - low attack, 3 - ult
	int[][] hbxC = new int[4][4]; // client player hitboxes; row: 0 = idle, 1 - high attack, 2 - low attack, 3 - ult
	int[][] atkhbxH = new int[3][4]; // server attack hitboxes; 0 - high attack, 1 - low attack, 2 - ult
	int[][] atkhbxC = new int[3][4]; // client attack hitboxes; 0 - high attack, 1 - low attack, 2 - ult
	
	int currHbxH = 0;
	int currHbxC = 0;
	int currAtkH = 0;
	int currAtkC = 0;
	
	int jumpCd = 0, jumpCd2 = 0, startTicks = 0;
	
	// Rectangle ultRect = new Rectangle(0, 0, 0, 0), ultRect2 = new Rectangle(0, 0, 0, 0);
	Rectangle[] backs = new Rectangle[2];
	Rectangle fighter = new Rectangle(300, 720-50, 50, 50);
	Rectangle dummy = new Rectangle(700, 720-50, 50, 50); 
	Rectangle[] atks = new Rectangle[2];
	Rectangle[] atks2 = new Rectangle[2];
	double vi = 29.6;
	double accel = -2.2;
	
	int chatTicks = 0;
	
	JTextArea chat = new JTextArea();
	JScrollPane scr = new JScrollPane(chat);
	JTextField mess = new JTextField();
	
	String chatText = "";
	String strSep = AllOutScrap.strSep;
	
	BufferedImage end;
	
	public void paintComponent(Graphics g) {
		// background
		g.setColor(backCol);
		g.fillRect(0, 0, 1280, 720);
		
		// score
		g.setColor(Color.black);
		g.setFont(rounds);
		g.drawString(phost.introunds+"-"+pclient.introunds, 600, 30);
		
		// decide which hitbox to use
		if (ult) {
			currHbxH = 3;
			currAtkH = 2;
		} else if (!atking) { // no attack = idle, even if moving
			currHbxH = 0;
		} else {
			if (up==0) {
				currHbxH = 1;
				currAtkH = 0;
			} else {
				currHbxH = 2;
				currAtkH = 1;
			}
		} 
		
		if (ult2) {
			currHbxC = 3;
			currAtkC = 2;
		} else if (!atking2) { // no attack = idle, even if moving
			currHbxC = 0;
		} else {
			if (up2==0) {
				currHbxC = 1;
				currAtkC = 0;
			} else {	
				currHbxC = 2;
				currAtkC = 1;
			}
		} 
				
		// change hitbox based on what player is doing
		fighter.width = hbxH[currHbxH][0];
		fighter.height = hbxH[currHbxH][1];
		
		dummy.width = hbxC[currHbxC][0];
		dummy.height = hbxC[currHbxC][1];
		
		// make the fighter and dummy touch the floor based on new height
		backs[0].y = 720-backs[0].height;
		backs[1].y = 720-backs[1].height; 
		
		if (defX!=0) defX = defX > 0 ? phost.intpspeed : phost.intpspeed*-1; 
		if (defX2!=0) defX2 = defX2 > 0 ? pclient.intpspeed : pclient.intpspeed*-1; 
		
		// x axis movement
		if (backs[0].x+defX>=0&&backs[0].x+defX<=1280-256){
			backs[0].x += defX;
		}
		if (backs[1].x+defX2>=0&&backs[1].x+defX2<=1280-256){
			backs[1].x += defX2;
		}
		
		
		// jumping
		if (jump) {
			backs[0].y = (720-backs[0].height)-((int)((vi*tme)+(accel*tme*tme)));
			if (backs[0].y+backs[0].height > 720) {
				backs[0].y = 720-backs[0].height;
				jump = false;
				tme = -0.5;
				jumpCd = 30;
			}
			tme+=0.5;
		}else if (jumpCd>0) {
			jumpCd--;
		}
		
		if (jump2) {
			backs[1].y = (720-backs[1].height)-((int)((vi*tme2)+(accel*tme2*tme2)));
			if (backs[1].y+backs[1].height > 720) {
				backs[1].y = 720-backs[1].height;
				jump2 = false;
				tme2 = -0.5;
				jumpCd2 = 30;
			}
			tme2+=0.5;
		} else if (jumpCd2>0) {
			jumpCd2--;
		}
		
		g.setColor(Color.green);
		g.fillRect(backs[0].x, backs[0].y, 256, 256);
		g.fillRect(backs[1].x, backs[1].y, 256, 256);
		
		// change hitbox x and y based on where the images are
		if (left) {
			fighter.x = backs[0].x+backs[0].width-hbxH[currHbxH][2]-fighter.width;
			
		} else {
			fighter.x = backs[0].x+hbxH[currHbxH][2];
		}
		
		if (left2) {
			dummy.x = backs[1].x+backs[1].width-hbxC[currHbxC][2]-dummy.width;
		} else {
			dummy.x = backs[1].x+hbxC[currHbxC][2];
		}
			
		
		fighter.y = backs[0].y+hbxH[currHbxH][3];
		dummy.y = backs[1].y+hbxC[currHbxC][3];
		
		// duck
		if (duck) {
			fighter.height /= 2;
			fighter.y = 720-fighter.height;
		} 
		
		if (duck2) {
			dummy.height /= 2;
			dummy.y = 720-dummy.height;
		} 
		
		// hp and energy
		g.setColor(col[0]);
		g.fillRect(0, 0,(int)(400*(phost.intchealth*1.0/phost.intphealth)), 50);
		g.fillRect(1280-(int)(400*(pclient.intchealth*1.0/pclient.intphealth)), 0, 400, 50);
		
		if (phost.intcenergy==50) g.setColor(col[2]);
		else g.setColor(col[1]);
		g.fillRect(0, 50, (int)(5*phost.intcenergy), 25);
		
		if (pclient.intcenergy==50) g.setColor(col[2]);
		else g.setColor(col[1]);
		g.fillRect(1280-(int)(5*pclient.intcenergy), 50, 300, 25);
		
		// enemy hitbox
		g.setColor(Color.red);
		g.fillRect(dummy.x, dummy.y, dummy.width, dummy.height);
		
		// training fighter hitbox
		g.setColor(Color.black);
		g.fillRect(fighter.x, fighter.y, fighter.width, fighter.height);
		
		// attack hitboxes
		atks[0].width = atkhbxH[currAtkH][0];
		atks[0].height = atkhbxH[currAtkH][1];
		atks[1].width = atkhbxH[currAtkH][0];
		atks[1].height = atkhbxH[currAtkH][1];
		atks[0].y = backs[0].y+atkhbxH[currAtkH][3];
		atks[1].y = backs[0].y+atkhbxH[currAtkH][3];
		
		atks2[0].width = atkhbxC[currAtkC][0];
		atks2[0].height = atkhbxC[currAtkC][1];
		atks2[1].width = atkhbxC[currAtkC][0];
		atks2[1].height = atkhbxC[currAtkC][1];
		atks2[0].y = backs[1].y+atkhbxC[currAtkC][3];
		atks2[1].y = backs[1].y+atkhbxC[currAtkC][3];
		
		if (!left) {
			atks[0].x = backs[0].x+atkhbxH[currAtkH][2];
			atks[1].x = backs[0].x+atkhbxH[currAtkH][2];
		} else {
			atks[0].x = backs[0].x+backs[0].width-atkhbxH[currAtkH][2]-atks[0].width;
			atks[1].x = backs[0].x+backs[0].width-atkhbxH[currAtkH][2]-atks[1].width;
		}
		
		if (left2) {
			atks2[0].x = backs[1].x+backs[1].width-atkhbxC[currAtkC][2]-atks2[0].width;
			atks2[1].x = backs[1].x+backs[1].width-atkhbxC[currAtkC][2]-atks2[1].width;
		} else {
			atks2[0].x = backs[1].x+atkhbxC[currAtkC][2];
			atks2[1].x = backs[1].x+atkhbxC[currAtkC][2];
		}
		
		if (atking) {
			if (atkTicks==0) {
				if (atks[up].intersects(dummy)&&AllOutScrap.blnS) {
					backs[1].x = fighter.x > dummy.x ? Math.max(0, backs[1].x-(1000/pclient.intpweight)) : Math.min(backs[1].x+(1000/pclient.intpweight), 1280-256);
					if (!done) pclient.intchealth -= phost.intpattack;
					if (phost.intcenergy!=50) phost.intcenergy += 5;
					AllOutScrap.ssm.sendText("knockback"+AllOutScrap.strSep+"0");
				}
			}
			if (atkTicks<10) {
				g.setColor(Color.blue);
				g.fillRect(atks[up].x, atks[up].y, atks[up].width, atks[up].height);
			} else if (atkTicks==10) {
				atkTicks=-1;
				atking = false;
				atkCd=40;
			}
			atkTicks++;
		} else if (atkCd>0) {
			atkCd--;
		}
		
		if (atking2) {
			if (atkTicks2==0) {
				if (atks2[up2].intersects(fighter)&&AllOutScrap.blnS) {
					backs[0].x = fighter.x < dummy.x ? Math.max(0, backs[0].x-(1000/phost.intpweight)) : Math.min(backs[0].x+(1000/phost.intpweight), 1280-256);
					if (!done) phost.intchealth -= pclient.intpattack;
					if (pclient.intcenergy!=50) pclient.intcenergy += 5;
				}
			}
			if (atkTicks2<10) {
				g.setColor(Color.blue);
				g.fillRect(atks2[up2].x, atks2[up2].y, atks2[up2].width, atks2[up2].height);
			} else if (atkTicks2==10) {
				atkTicks2=-1;
				atking2 = false;
				atkCd2=40;
			}
			atkTicks2++;
		} else if (atkCd2>0) {
			atkCd2--;
		}
		
		if (ult) {
			if (ultTicks==0) {
				if (atks[1].intersects(dummy)&&AllOutScrap.blnS) {
					phost.intcenergy = 0;
					if (!done) pclient.intchealth -= phost.intpattack*3;
					backs[1].x = fighter.x > dummy.x ? Math.max(0, backs[1].x-(3000/pclient.intpweight)) : Math.min(backs[1].x+(3000/pclient.intpweight), 1280-256);
					AllOutScrap.ssm.sendText("knockback"+AllOutScrap.strSep+"1");
				}
			} 
			// show attack on screen
			if (ultTicks<40) {
				g.setColor(Color.blue);
				g.fillRect(atks[1].x, atks[1].y, atks[1].width, atks[1].height);
			} else if (ultTicks==40) {
				ultTicks = -1;
				ult = false;
			}
			ultTicks++;
		}
		
		if (ult2) {
			if (ultTicks2==0) {
				if (atks2[1].intersects(fighter)&&AllOutScrap.blnS) {
					pclient.intcenergy = 0;
					if (!done) phost.intchealth -= pclient.intpattack*3;
					backs[0].x = fighter.x < dummy.x ? Math.max(0, backs[0].x-(3000/phost.intpweight)) : Math.min(backs[0].x+(3000/phost.intpweight), 1280-256);
				}
			} 
			// show attack on screen
			if (ultTicks2<40) {
				g.setColor(Color.blue);
				g.fillRect(atks2[1].x, atks2[1].y, atks2[1].width, atks2[1].height);
			} else if (ultTicks2==40) {
				ultTicks2 = -1;
				ult2 = false;
			}
			ultTicks2++;
		}
		
		if (AllOutScrap.blnS&&AllOutScrap.ssm!=null) AllOutScrap.sendUpdate();
		else AllOutScrap.move();
		
		if ((atking||atking2||ult||ult2)&&AllOutScrap.blnS&&!done) {
			if (phost.intchealth<=0||pclient.intchealth<=0) {
				if (phost.intchealth < pclient.intchealth) {
					winner = pclient.strplayername;
					pclient.introunds++;
				} else {
					winner = phost.strplayername;
					phost.introunds++;
					
				}
				winners.add(winner);
				times.add(time.getDelay()*1.0*startTicks/1000.0);
				
				AllOutScrap.sendUpdate();
				if (phost.introunds==serieswin||pclient.introunds==serieswin) AllOutScrap.ssm.sendText("gameEnd"+strSep+winner+strSep+phost.introunds+strSep+pclient.introunds);
				else AllOutScrap.ssm.sendText("roundEnd"+strSep+winner+strSep+phost.introunds+strSep+pclient.introunds);
				done = true;
			}
		} 
		if (done) {
			g.drawImage(end, 0, 0, null);
			g.setColor(Color.white);
			g.setFont(winlbl);
			String toScreen = AllOutScrap.blnS ? winner+" has won the round! Press e to start next round." : winner+" has won the round! Waiting for server to start next round.";
			if (phost.introunds==serieswin||pclient.introunds==serieswin) {
				toScreen = AllOutScrap.blnS ? winner+" has won the game! Press e to return to main menu." : winner+" has won the game! Waiting for server to close the lobby.";
			}
			g.drawString(toScreen, 350, 350);
		}
	}
	
	public void loadData(String[][] data) {
		
	}
	
	public void nextRound() {
		phost.intcenergy = 0;
		pclient.intcenergy = 0;
		
		phost.intchealth = phost.intphealth;
		pclient.intchealth = pclient.intphealth;
		
		backs[0].x = 0;
		backs[1].x = 1280-256;
		
		AllOutScrap.sendUpdate();
		AllOutScrap.ssm.sendText("nextRound");
		startTicks = 0;
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
	
	public GamePanel() {
		super();
		setLayout(null);
		setPreferredSize(new Dimension(1280, 720));
		col[0] = Color.red;
		col[1] = Color.blue;
		col[2] = Color.green;
		time.start();
		backs[0] = new Rectangle(0, 720-256, 256, 256);
		backs[1] = new Rectangle(1280-256, 720-256, 256, 256);
		atks[0] = new Rectangle(0, 0, 0, 0);
		atks[1] = new Rectangle(0, 0, 0, 0);
		atks2[0] = new Rectangle(0, 0, 0, 0);
		atks2[1] = new Rectangle(0, 0, 0, 0);
		
		scr.setLocation(0, 100);
		scr.setSize(300, 300);
		add(scr);
		
		mess.setLocation(0, 400);
		mess.setSize(300, 50);
		add(mess);
		
		mess.addActionListener(this); 
		
		chat.setEditable(false);
		chat.setLineWrap(true);
		scr.setVisible(false);
		mess.setVisible(false);
		
		end = img("blur.png");
		
		try {
			rounds = Font.createFont(Font.PLAIN, this.getClass().getResourceAsStream("open-sans.regular.ttf"));
			rounds = rounds.deriveFont(Font.PLAIN, 40);
			winlbl = Font.createFont(Font.PLAIN, this.getClass().getResourceAsStream("open-sans.regular.ttf"));
			winlbl = winlbl.deriveFont(Font.PLAIN, 20);
		} catch (FontFormatException e){
		} catch (IOException e){
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==time) {
			this.repaint();
			if (!mess.isVisible()) chatTicks++;
			// chat stays for 5 seconds if you don't type
			if (chatTicks*time.getDelay()>=5000) scr.setVisible(false);
			startTicks++;
		} else if (e.getSource()==mess&&!mess.getText().equals("")) {
			chatTicks = 0;
			chatText = AllOutScrap.blnS ? phost.strplayername : pclient.strplayername;
			chat.append(chatText+": "+mess.getText()+"\n");
			mess.setVisible(false);
			AllOutScrap.theframe.requestFocus();
			AllOutScrap.ssm.sendText("chat"+strSep+chatText+strSep+mess.getText());
			mess.setText("");
		}
		
	}
	
	
}
