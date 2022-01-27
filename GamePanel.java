import javax.swing.*;
import java.awt.image.*;
import java.awt.*;
import java.io.*;
import javax.imageio.*;
import java.util.ArrayList;
import javax.swing.event.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;

public class GamePanel extends JPanel implements ActionListener{
	Timer time = new Timer(1000/80, this); // screen refresh timer
	ArrayList<String> winners = new ArrayList<>(); // names of round winners
	ArrayList<Double> times = new ArrayList<>(); // times of round winners
	Color[] col = new Color[3]; // health bar colour, energy bar colour, fully charged energy bar colour
	Color backCol = Color.white; // background colour
	Player phost = new Player("Keanu Reeves",25,150,75,5,100,50,0); // server player data
	Player pclient = new Player("Eggs",10,100,50,10,100,50,0); // client player data
	// server and client movement and attack variables
	int defX = 0, defX2 = 0; 
	int atkTicks = 0, atkCd = 0, up, up2, atkTicks2 = 0, atkCd2 = 0;
	double tme = 0, tme2 = 0;
	boolean atking = false, left = false, jump = false, duck = false;
	boolean atking2 = false, left2 = true, jump2 = false, duck2 = false;
	boolean ult = false, ult2 = false;
	int ultTicks = 0, ultTicks2 = 0;
	double vi = 29.6;
	double accel = -2.2;
	boolean kb = false;
	boolean kb2 = false;
	int jumpCd = 0, jumpCd2 = 0; 
	
	// round/game over variables
	int serieswin = 1;
	String winner;
	boolean done = false;
	Font rounds, winlbl;
	
	int[][] hbxH = new int[8][4]; // server player hitboxes; row: 0 = idle, 1 - high attack, 2 - low attack, 3 - ult
	int[][] hbxC = new int[8][4]; // client player hitboxes; row: 0 = idle, 1 - high attack, 2 - low attack, 3 - ult
	int[][] atkhbxH = new int[3][4]; // server attack hitboxes; 0 - high attack, 1 - low attack, 2 - ult
	int[][] atkhbxC = new int[3][4]; // client attack hitboxes; 0 - high attack, 1 - low attack, 2 - ult
	
	// what hitbox to use + what image to display for player fighters and attacks
	int currHbxH = 0;
	int currHbxC = 0;
	int currAtkH = 0;
	int currAtkC = 0;
	
	// timer ticks since the round started
	int startTicks = 0;
	
	Rectangle[] backs = new Rectangle[2]; // sprite image location
	Rectangle fighter = new Rectangle(300, 720-50, 50, 50); // server hitbox
	Rectangle dummy = new Rectangle(700, 720-50, 50, 50); // client hitbox
	Rectangle[] atks = new Rectangle[2]; // attack hitbox (server)
	Rectangle[] atks2 = new Rectangle[2]; // attack hitbox (client)
	
	// timer ticks since a chat action has occured
	int chatTicks = 0;
	
	// chat message box, scrollpane and textarea
	JTextArea chat = new JTextArea();
	JScrollPane scr = new JScrollPane(chat);
	JTextField mess = new JTextField();
	String chatText = "";
	
	// message separator for network message
	String strSep = AllOutScrap.strSep;
	
	// end screen image
	BufferedImage end;
	
	// all sprites in the .jar file
	BufferedImage[][] sprites = new BufferedImage[4][8];
	BufferedImage[][] sprites2 = new BufferedImage[4][8];
	
	// right facing sprites of both players
	BufferedImage[] pSprites;
	BufferedImage[] cSprites;
	
	// left facing sprites of both players
	BufferedImage[] pSprites2;
	BufferedImage[] cSprites2;

	public void paintComponent(Graphics g) {
		// background
		g.setColor(backCol);
		g.fillRect(0, 0, 1280, 720);
		
		// score
		g.setColor(Color.black);
		g.setFont(rounds);
		g.drawString(phost.introunds+"-"+pclient.introunds, 600, 30);
		
		// hitbox variables
		currHbxH = 0;
		currHbxC = 0;
		
		// SERVER MOVEMENTS
		// each action uses a different hitbox/image stored in arrays, based on fighter selected
		if (ult) {
			currHbxH = 3;
			currAtkH = 2;
		} else if (atking) { // no attack = idle, even if moving
			if (up==0) {
				currHbxH = 1;
				currAtkH = 0;
			} else {
				currHbxH = 2;
				currAtkH = 1;
			}
		} else if (duck) {
			currHbxH = 5;
		} else if (jump) {
			currHbxH = 4;
		}
		
		// CLIENT MOVEMENTS (lines 104-119 for client)
		// each action uses a different hitbox/image stored in arrays, based on fighter selected
		if (ult2) {
			currHbxC = 3;
			currAtkC = 2;
		} else if (atking2) { // no attack = idle, even if moving
			if (up2==0) {
				currHbxC = 1;
				currAtkC = 0;
			} else {	
				currHbxC = 2;
				currAtkC = 1;
			}
		} else if (duck2){
			currHbxC = 5;
		} else if (jump2) {
			currHbxC = 4;
		}
				
		// change player hitbox based on what players are doing
		fighter.width = hbxH[currHbxH][0];
		fighter.height = hbxH[currHbxH][1];
		dummy.width = hbxC[currHbxC][0];
		dummy.height = hbxC[currHbxC][1];
		
		// make the fighter and dummy's images touch the ground
		backs[0].y = 720-backs[0].height;
		backs[1].y = 720-backs[1].height; 
		
		// change x movement variables to the correct magnitude based on fighter speed
		if (defX!=0) defX = defX > 0 ? phost.intpspeed : phost.intpspeed*-1; 
		if (defX2!=0) defX2 = defX2 > 0 ? pclient.intpspeed : pclient.intpspeed*-1; 
		
		// x axis movement
		if (backs[0].x+defX>=0&&backs[0].x+defX<=1280-256){
			backs[0].x += defX;
		}
		if (backs[1].x+defX2>=0&&backs[1].x+defX2<=1280-256){
			backs[1].x += defX2;
		}
		
		// jumping (server)
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
		
		// jumping (client)
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
		
		// change hitbox x and y based on distance from top left corner stored in an array, and based on what direction the fighters are facing
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
		
		// hp and energy bars
		g.setColor(col[0]);
		g.fillRect(0, 0,(int)(400*(phost.intchealth*1.0/phost.intphealth)), 50);
		g.fillRect(1280-(int)(400*(pclient.intchealth*1.0/pclient.intphealth)), 0, 400, 50);
		if (phost.intcenergy==50) g.setColor(col[2]);
		else g.setColor(col[1]);
		g.fillRect(0, 50, (int)(5*phost.intcenergy), 25);
		if (pclient.intcenergy==50) g.setColor(col[2]);
		else g.setColor(col[1]);
		g.fillRect(1280-(int)(5*pclient.intcenergy), 50, 300, 25);
		
		/* drawing hitboxes for debugging commented out
		// enemy 
		g.setColor(Color.red);
		g.fillRect(dummy.x, dummy.y, dummy.width, dummy.height);
		
		// training fighter hitbox
		g.setColor(Color.black);
		g.fillRect(fighter.x, fighter.y, fighter.width, fighter.height);
		*/
		
		// attack hitboxes for server and client based on data from an array, which is from a text file
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
		
		// attack animation (server)
		if (atking) {
			if (atkTicks==0) {
				if (atks[up].intersects(dummy)&&AllOutScrap.blnS) {
					// damage, knockback and energy gain 
					kb = true;
					if (!done) pclient.intchealth -= phost.intpattack;
					if (phost.intcenergy!=50) phost.intcenergy += 5;
				}
			}
			if (atkTicks<10) {
				if (kb) {
					backs[1].x = fighter.x > dummy.x ? Math.max(0, backs[1].x-(1000/pclient.intpweight/10)) : Math.min(backs[1].x+(1000/pclient.intpweight/10), 1280-256);
					currHbxC = 6;
					if (AllOutScrap.ssm!=null) AllOutScrap.ssm.sendText("knockback"+AllOutScrap.strSep+"0");
					left2 = fighter.x > dummy.x ? false : true;
				}
				// g.setColor(Color.blue);
				// g.fillRect(atks[up].x, atks[up].y, atks[up].width, atks[up].height);
			} else if (atkTicks==10) {
				atkTicks=-1;
				atking = false;
				kb = false;
				atkCd=40;
			}
			atkTicks++;
		} else if (atkCd>0) {
			atkCd--;
		}
		
		// attack animation (client)
		if (atking2) {
			if (atkTicks2==0) {
				if (atks2[up2].intersects(fighter)&&AllOutScrap.blnS) {
					// damage, knockback and energy gain 
					kb2 = true;
					if (!done) phost.intchealth -= pclient.intpattack;
					if (pclient.intcenergy!=50) pclient.intcenergy += 5;
				}
			}
			if (atkTicks2<10) {
				if (kb2) {
					backs[0].x = fighter.x < dummy.x ? Math.max(0, backs[0].x-(1000/phost.intpweight/10)) : Math.min(backs[0].x+(1000/phost.intpweight/10), 1280-256);
					currHbxH = 6;
					AllOutScrap.ssm.sendText("knockback"+AllOutScrap.strSep+"1");
					left = fighter.x < dummy.x ? false : true;
				}
				// g.setColor(Color.blue);
				// g.fillRect(atks2[up2].x, atks2[up2].y, atks2[up2].width, atks2[up2].height);
			} else if (atkTicks2==10) {
				atkTicks2=-1;
				atking2 = false;
				kb2 = false;
				atkCd2=40;
			}
			atkTicks2++;
		} else if (atkCd2>0) {
			atkCd2--;
		}
		
		// ultimate animation (server)
		if (ult) {
			if (ultTicks==0) {
				if (atks[1].intersects(dummy)&&AllOutScrap.blnS) {
					// damage and knockback
					if (!done) pclient.intchealth -= phost.intpattack*3;
					kb = true;
				}
				phost.intcenergy = 0;
			} 
			// show attack on screen
			if (ultTicks<40) {
				if (kb) {
					backs[1].x = fighter.x > dummy.x ? Math.max(0, backs[1].x-(3000/pclient.intpweight/40)) : Math.min(backs[1].x+(3000/pclient.intpweight/40), 1280-256);
					currHbxC = 6;
					AllOutScrap.ssm.sendText("knockback"+AllOutScrap.strSep+"0");
					left2 = fighter.x > dummy.x ? false : true;
				}
				// g.setColor(Color.blue);
				// g.fillRect(atks[1].x, atks[1].y, atks[1].width, atks[1].height);
			} else if (ultTicks==40) {
				ultTicks = -1;
				ult = false;
				kb = false;
			}
			ultTicks++;
		}
		
		// ultimate animation (client)
		if (ult2) {
			if (ultTicks2==0) {
				if (atks2[1].intersects(fighter)&&AllOutScrap.blnS) {
					// damage and knockback
					if (!done) phost.intchealth -= pclient.intpattack*3;
					kb = true;
				}
				pclient.intcenergy = 0;
			} 
			// show attack on screen
			if (ultTicks2<40) {
				if (kb2) {
					backs[0].x = fighter.x < dummy.x ? Math.max(0, backs[0].x-(3000/phost.intpweight/40)) : Math.min(backs[0].x+(3000/phost.intpweight/40), 1280-256);
					currHbxH = 6;
					AllOutScrap.ssm.sendText("knockback"+AllOutScrap.strSep+"1");
					left = fighter.x < dummy.x ? false : true;
				}
				// g.setColor(Color.blue);
				// g.fillRect(atks2[1].x, atks2[1].y, atks2[1].width, atks2[1].height);
			} else if (ultTicks2==40) {
				ultTicks2 = -1;
				ult2 = false;
				kb2 = false;
			}
			ultTicks2++;
		}
		
		// server and client update each other on what their fighters are doing and the new coordinates of the fighters
		if (AllOutScrap.blnS&&AllOutScrap.ssm!=null) AllOutScrap.sendUpdate(); // the server also sends out health and energy data to the client
		else if (!AllOutScrap.blnS&&AllOutScrap.ssm!=null) AllOutScrap.move();
		
		// if an attack just went off, check if the game is over (ie. someone's hp <= 0)
		if ((atking||atking2||ult||ult2)&&AllOutScrap.blnS&&!done) {
			// check winner
			if (phost.intchealth<=0||pclient.intchealth<=0) {
				// make the appropriate person the winner
				if (phost.intchealth < pclient.intchealth) {
					winner = pclient.strplayername;
					pclient.introunds++;
					currHbxH = 7;
				} else {
					winner = phost.strplayername;
					phost.introunds++;
					currHbxC = 7;
				}
				// add winner name and time to the arraylists
				winners.add(winner);
				times.add(time.getDelay()*1.0*startTicks/1000.0);
				
				// update client so it knows to show the round/game end screen
				AllOutScrap.sendUpdate();
				if (phost.introunds==serieswin||pclient.introunds==serieswin) AllOutScrap.ssm.sendText("gameEnd"+strSep+winner+strSep+phost.introunds+strSep+pclient.introunds);
				else AllOutScrap.ssm.sendText("roundEnd"+strSep+winner+strSep+phost.introunds+strSep+pclient.introunds);
				
				// set game to done
				done = true;
			}
		} 
		
		// if round/game is done show the end screen
		if (done) {
			g.setColor(Color.white);
			g.setFont(winlbl);
			String toScreen = AllOutScrap.blnS ? winner+" has won the round! Press e to start next round." : winner+" has won the round! Waiting for server to start next round.";
			// slightly modify the text printed on screen if the game is over
			if (phost.introunds==serieswin||pclient.introunds==serieswin) {
				toScreen = AllOutScrap.blnS ? winner+" has won the game! Press e to return to main menu." : winner+" has won the game! Waiting for server to close the lobby.";
			}
			// make the appropriate person KO'ed
			if (phost.intchealth < pclient.intchealth) {
				currHbxH = 7;
			} else {
				currHbxC = 7;
			}
			
			// draw the sprite that corresponds to what the player is doing (the variable has been set to corpse)
			if (left) {
				if (pSprites2[currHbxH].getWidth()>256) g.drawImage(pSprites2[currHbxH], backs[0].x-256, backs[0].y, null);
				else g.drawImage(pSprites2[currHbxH], backs[0].x, backs[0].y, null);
			} else {
				g.drawImage(pSprites[currHbxH], backs[0].x, backs[0].y, null);
			}
			
			if (left2) {
				if (cSprites2[currHbxC].getWidth()>256) g.drawImage(cSprites2[currHbxC], backs[1].x-256, backs[1].y, null);
				else g.drawImage(cSprites2[currHbxC], backs[1].x, backs[1].y, null);
			} else {
				 g.drawImage(cSprites[currHbxC], backs[1].x, backs[1].y, null);
			}
			
			g.drawImage(end, 0, 0, null);
			g.drawString(toScreen, 350, 350);
		} else {
			// non-corpse sprite drawing is done here (attacking fighters are deprioritized when drawing)
			if (!atking&&!ult) {
				if (left) {
					if (pSprites2[currHbxH].getWidth()>256) g.drawImage(pSprites2[currHbxH], backs[0].x-256, backs[0].y, null);
					else g.drawImage(pSprites2[currHbxH], backs[0].x, backs[0].y, null);
					
				} else {
					g.drawImage(pSprites[currHbxH], backs[0].x, backs[0].y, null);
				}
			}
			
			if (!atking2&&!ult2) {
				if (left2) {
					if (cSprites2[currHbxC].getWidth()>256) g.drawImage(cSprites2[currHbxC], backs[1].x-256, backs[1].y, null);
					else g.drawImage(cSprites2[currHbxC], backs[1].x, backs[1].y, null);
				} else {
					g.drawImage(cSprites[currHbxC], backs[1].x, backs[1].y, null);
				}
			}
			
			if (atking||ult) {
				if (left) {
					if (pSprites2[currHbxH].getWidth()>256) g.drawImage(pSprites2[currHbxH], backs[0].x-256, backs[0].y, null);
					else g.drawImage(pSprites2[currHbxH], backs[0].x, backs[0].y, null);
					
				} else {
					g.drawImage(pSprites[currHbxH], backs[0].x, backs[0].y, null);
				}
			}
			
			if (atking2||ult2) {
				if (left2) {
					if (cSprites2[currHbxC].getWidth()>256) g.drawImage(cSprites2[currHbxC], backs[1].x-256, backs[1].y, null);
					else g.drawImage(cSprites2[currHbxC], backs[1].x, backs[1].y, null);
				} else {
					g.drawImage(cSprites[currHbxC], backs[1].x, backs[1].y, null);
				}
			}
		}
		
	}
	
	// on switch to next round, reset all values to default values
	public void nextRound() {
		phost.intcenergy = 0;
		pclient.intcenergy = 0;
		
		phost.intchealth = phost.intphealth;
		pclient.intchealth = pclient.intphealth;
		
		backs[0].x = 0;
		backs[1].x = 1280-256;
		left2 = true;
		
		AllOutScrap.sendUpdate();
		AllOutScrap.ssm.sendText("nextRound");
		startTicks = 0;
	}
	
	// method to look for an image with file name fileName and return the image version
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
		// panel defaults + inherit JPanel methods/properties
		super();
		setLayout(null);
		setPreferredSize(new Dimension(1280, 720));
		
		// set health bar colour, energy bar colour and energy full colour
		col[0] = Color.red;
		col[1] = Color.blue;
		col[2] = Color.green;
		time.start(); // start timer
		
		// image location initialization
		backs[0] = new Rectangle(0, 720-256, 256, 256);
		backs[1] = new Rectangle(1280-256, 720-256, 256, 256);
		
		// attack hitbox initialization (server and client)
		atks[0] = new Rectangle(0, 0, 0, 0);
		atks[1] = new Rectangle(0, 0, 0, 0);
		atks2[0] = new Rectangle(0, 0, 0, 0);
		atks2[1] = new Rectangle(0, 0, 0, 0);
		
		// chat settings
		scr.setLocation(0, 100);
		scr.setSize(300, 300);
		add(scr);
		mess.setLocation(0, 400);
		mess.setSize(300, 50);
		add(mess);
		chat.setEditable(false);
		chat.setLineWrap(true);
		scr.setVisible(false);
		mess.setVisible(false);
		
		// add listener to message box
		mess.addActionListener(this); 
		
		// end screen dimmer
		end = img("blur.png");
		
		// get font from font file inside the jar file 
		try {
			rounds = Font.createFont(Font.PLAIN, this.getClass().getResourceAsStream("open-sans.regular.ttf"));
			rounds = rounds.deriveFont(Font.PLAIN, 40);
			winlbl = Font.createFont(Font.PLAIN, this.getClass().getResourceAsStream("open-sans.regular.ttf"));
			winlbl = winlbl.deriveFont(Font.PLAIN, 20);
		} catch (FontFormatException e){
		} catch (IOException e){
		}
		
		// call loadimage methods to load the left and right sprite arrays for all characters
		loadImages("moby", 0);
		loadImages("scorp", 2);
		loadImages("luz", 1);
		loadImages("amelia", 3);
	}
	
	// method to load sprite images from inside the .jar file to the sprite array
	public void loadImages(String strPrefix, int intRow) {
		for (int intC=0;intC<8;intC++) {
			// right facing sprites
			sprites[intRow][intC] = img(strPrefix+intC+".png");
			// reflect right facing sprites to make them left facing
			sprites2[intRow][intC] = sprites[intRow][intC];
			AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
			tx.translate(-sprites2[intRow][intC].getWidth(null), 0);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			sprites2[intRow][intC] = op.filter(sprites2[intRow][intC], null);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==time) {
			this.repaint();
			// add to ticks to track round speed, game speed and ticks since last chat activity
			if (!mess.isVisible()) chatTicks++;
			// chat stays for 5 seconds if you don't type
			if (chatTicks*time.getDelay()>=5000) scr.setVisible(false);
			startTicks++;
		// if the message box recieves a signal, append it to the chat, clear the message box and make it invisible (so the user has to press enter again)
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
