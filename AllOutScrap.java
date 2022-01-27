import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;
import java.util.ArrayList;

public class AllOutScrap implements ActionListener,WindowListener, KeyListener, MouseListener, MouseMotionListener{
	
	// Group of 3, one group of 2
	//
	//theframe.requestFocus(); might come in handy!
	//
	//
	
	//properties
	static String strSep = "6b9"; // separator for network messages (so messages with commas are fully shown)
	static JFrame theframe = new JFrame ("All Out Scrap!"); // display frame
	static MenuPanel themenu = new MenuPanel(); // panel for main menu
	static SuperSocketMaster ssm; // network object
	static GamePanel game = new GamePanel(); // panel for gameplay
	static int pCount = 0; // player count
	static SFCharStatsRender loader = new SFCharStatsRender(); // object to load data from text files
	static String[][] statistics; // character stats array
	static String[][] hbxes; // character hitboxes array
	static String[][] atkhbxes; // chracter attack hitboxes
	static boolean inGame = false; // is the player in game?
	static boolean[] locked = new boolean[2]; // players readied up? 0 - server readied? 1 - client readied?
	static int[] chars = new int[2]; // characters selected 0 - server character, 1 - client character
	static boolean blnS; // is server?
	static boolean connected = false; // is this connected to a network
	
	static ArrayList<String[]> highscores = new ArrayList<>(); // highscores of arraylist
	static int[] rgb = new int[3]; // rgb values for the health bar colour
	static String[] strUsers = new String[2]; // usernames
	
	//methods
	public void actionPerformed(ActionEvent evt){
	}
	
	public void windowActivated(WindowEvent evt){
		//don't need
	}
	
	public void windowDeactivated(WindowEvent evt){
		//don't need
	}
	
	public void windowClosed(WindowEvent evt){
		//don't need
	}
	
	public void windowClosing(WindowEvent evt){
		
	}
	
	public void windowDeiconified(WindowEvent evt){
		//don't need
	}
	
	public void windowIconified(WindowEvent evt){
		//don't need
	}
	
	public void windowOpened(WindowEvent evt){
		//don't need
	}
	
	// when key gets released
	public void keyReleased(KeyEvent evt){
		// if not ingame
		if (!inGame) {
			// if it's a movement key (a, s, or d), stop the movement
			if (evt.getKeyChar()=='a' || evt.getKeyChar()=='A') {
				themenu.defX = 0;
				themenu.left = true;
			} else if (evt.getKeyChar()=='s' || evt.getKeyChar()=='S') {
				themenu.duck = false;
			} else if (evt.getKeyChar()=='d' || evt.getKeyChar()=='D') {
				themenu.defX = 0;
				themenu.left = false;
			} 
		// in game is the same concept, but references the game panel instead and picks the right fighter's movement to stop
		} else {
			if (evt.getKeyChar()=='a' || evt.getKeyChar()=='A') {
				if (blnS) {
					game.defX = 0;
					game.left = true;
				} else {
					game.defX2 = 0;
				}
			} else if (evt.getKeyChar()=='s' || evt.getKeyChar()=='S') {
				if (blnS) {
					game.duck = false;
				} else  {
					game.duck2 = false;
				}
			} else if (evt.getKeyChar()=='d' || evt.getKeyChar()=='D') {
				if (blnS) {
					game.defX = 0;
					game.left = false;
				} else {
					game.defX2 = 0;
				}
			} 
		}
	}
	
	public void keyPressed(KeyEvent evt){
		// not in game = reference tutorial screen
		if (!inGame) {
			// attacks go off if they are not on cooldown
			themenu.atking = false;
			// high attack
			if (evt.getKeyChar()=='q'||evt.getKeyChar()=='Q') {
				if (themenu.atkCd<1) themenu.atking = true;
				themenu.up = 0;
			// low attack
			} else if (evt.getKeyChar()=='e'||evt.getKeyChar()=='E') {
				if (themenu.atkCd<1) themenu.atking = true;
				themenu.up = 1;
			// ultimate
			} else if (evt.getKeyChar()==' ') {
				themenu.ult = true;
			} 
			
			// movement keys (w = jump, s = duck, a = left, d = right)
			if (evt.getKeyChar()=='w' || evt.getKeyChar()=='W') {
				if (!themenu.duck) themenu.jump = true;
			} else if (evt.getKeyChar()=='a' || evt.getKeyChar()=='A') {
				themenu.defX = -5;
				themenu.left = true;
			} else if (evt.getKeyChar()=='s' || evt.getKeyChar()=='S') {
				themenu.duck = true;
			} else if (evt.getKeyChar()=='d' || evt.getKeyChar()=='D') {
				themenu.defX = 5;
				themenu.left = false;
			} 
			
			// if the character is facing left attack leftwards, otherwise, attack rightwards
			if (themenu.left) {
				themenu.atks[0].x = themenu.r.x-40;
				themenu.atks[1].x = themenu.r.x-40;
			} else {
				themenu.atks[0].x = themenu.r.x+40;
				themenu.atks[1].x = themenu.r.x+40;
			}
			
			// elevation for up attack vs down attack
			themenu.atks[0].y = themenu.r.y;
			themenu.atks[1].y = themenu.r.y+25;
		// ingame
		} else {
			// assume not attacking by default
			game.atking = false;
			
			// attacks go off for the corresponding fighter (based on who is server player) and if not on cooldown
			// high attack
			if (evt.getKeyChar()=='q'||evt.getKeyChar()=='Q') {
				if (blnS) {
					if (game.atkCd<1) game.atking = true;
					game.up = 0;
				} else {
					if (game.atkCd2<1) {
						game.atking2 = true;
						ssm.sendText("attack"+strSep+game.up2);
					}
					game.up2 = 0; 
				}
			// low attack/next round
			} else if (evt.getKeyChar()=='e'||evt.getKeyChar()=='E') {
				// server side can decide whether or not to move to the next round
				if (blnS) {
					// if the round is over
					if (game.done) {
						game.done = false;
						// if a game winner has been decided:
						if (game.phost.introunds==game.serieswin||game.pclient.introunds==game.serieswin) {
							toMenu();
							
							// add game times to high scores if applicable
							for (int intC=0;intC<game.times.size();intC++) {
								// if there are less than 15 high scores, add and sort
								if (highscores.size()<15) {
									String[] add = {game.winners.get(intC), game.times.get(intC).toString()};
									highscores.add(add);
									sortScores();
								// otherwise, see if lowest high score is beaten out, and sort
								} else if (game.times.get(intC)>Double.parseDouble(highscores.get(highscores.size()-1)[1])) {
									highscores.remove(highscores.size()-1);
									String[] add = {game.winners.get(intC), game.times.get(intC).toString()};
									highscores.add(add);
									sortScores();
								} 
							}
							
							// write new high scores to high scores file
							try {
								PrintWriter file = new PrintWriter(new FileWriter("winners.txt"));
								
								for (int intCount = 0; intCount<highscores.size(); intCount++) {
									file.println(highscores.get(intCount)[0]);
									file.println(highscores.get(intCount)[1]);
								}
								
								file.close();
								
								System.out.println("successfully wrote to file");
							} catch (IOException e){
								System.out.println(e.toString());
							} 
							
							// clear high score winner and times lists
							game.times.clear();
							game.winners.clear();
						} 
						// if the a game winner has not been decided, move to the next round
						else game.nextRound();
					// if the round is not over, low attack for server fighter
					} else {
						if (game.atkCd<1) game.atking = true;
						game.up = 1;
					}
				// if not server, make client fighter low attack
				} else {
					if (game.atkCd2<1) {
						game.atking2 = true;
						ssm.sendText("attack"+strSep+game.up2);
					}
					game.up2 = 1; 
				} 
			// ultimate attack
			} else if (evt.getKeyChar()==' ') {
				// make the correct charcater ult if they have enough energy
				if (blnS) { 
					if (game.phost.intcenergy==50) game.ult = true;
				} else {
					if (game.pclient.intcenergy==50) game.ult2 = true;
				}
			}
			
			// movement (w = jump, s = jump, a = left, d = right) for the corresponding fighter based on server/client
			if (evt.getKeyChar()=='w' || evt.getKeyChar()=='W') {
					if (blnS) {
						if (game.jumpCd<1 && !game.duck) game.jump = true;
					} else {
						if (game.jumpCd2<1 && !game.duck2) game.jump2 = true;
					}
			} 
			if (evt.getKeyChar()=='a' || evt.getKeyChar()=='A') {
				if (blnS) {
					game.defX = -5;
					game.left = true;
				} else {
					game.defX2 = -5;
					game.left2 = true;
				}
			} 
			if (evt.getKeyChar()=='s' || evt.getKeyChar()=='S') {
				if (blnS) {
					game.duck = true;
				} else {
					game.duck2 = true;
				}
			} 
			if (evt.getKeyChar()=='d' || evt.getKeyChar()=='D') {
				if (blnS){
					game.defX = 5;
					game.left = false;
				} else {
					game.defX2 = 5;
					game.left2 = false;
				}
			} 
			
			// enter key opens the chat and message box
			if (evt.getKeyCode()==10) {
				if (!game.mess.isVisible()) {
					game.chatTicks = 0;
					game.mess.setVisible(true);
					game.scr.setVisible(true);
					game.mess.requestFocus();
				}
			}
			
			game.repaint();
		}
	}
	
	public void keyTyped(KeyEvent evt){
		//don't need
	}
	
	public void mouseExited(MouseEvent evt) {
		//don't need
	}
	
	public void mouseEntered (MouseEvent evt){
		//don't need
	}
	
	public void mouseClicked (MouseEvent evt){
		//don't need
	}
	
	// tell menu that mouse is clicked on click
	public void mousePressed (MouseEvent evt){
		themenu.mouseDown = true;
	}
	
	// tell menu that mouse is released, and reset the "function has already been executed" variable on release
	public void mouseReleased (MouseEvent evt){
		themenu.mouseDown = false;
		themenu.retriggerCatch = false;
	}
	
	// change camera position based on mouse movement
	public void mouseMoved (MouseEvent evt){
		themenu.mousePos = new Point(evt.getX()-8, evt.getY()-30);
		
	}
	
	public void mouseDragged (MouseEvent evt){
		//probably won't need
	}
	
	// switch to menu panel, resetting all network related variables
	public void toMenu() {
		ssm.sendText("toMenu");
		theframe.setContentPane(themenu);
		theframe.pack();
		themenu.lastClick = 5;
		themenu.cameraPos = new Point(0, 0);
		inGame = false;
		ssm = null;
		connected = false;
	}
	
	// switch to game panel
	public static void toGame() {
		theframe.requestFocus();
		// server tells client to start the game if characters are chosen and both players are readied up, switches to game panel and loads hitboxes
		if (locked[1]&&locked[0]&&blnS&&chars[0]!=-1&&chars[1]!=-1) {
			theframe.setContentPane(game);
			theframe.pack();
			inGame = true;
			ssm.sendText("choose"+strSep+chars[0]);
			game.serieswin = themenu.theslider.getValue();
			ssm.sendText("startGame"+strSep+game.serieswin);
			
			// load character hitboxes into game
			for (int i=0;i<8;i++) for (int j=0;j<4;j++) game.hbxH[i][j] = Integer.parseInt(hbxes[8*chars[0]+i][j]); 
			for (int i=0;i<8;i++) for (int j=0;j<4;j++) game.hbxC[i][j] = Integer.parseInt(hbxes[8*chars[1]+i][j]); 
			for (int i=0;i<3;i++) for (int j=0;j<4;j++) game.atkhbxH[i][j] = Integer.parseInt(atkhbxes[3*chars[0]+i][j]); 
			for (int i=0;i<3;i++) for (int j=0;j<4;j++) game.atkhbxC[i][j] = Integer.parseInt(atkhbxes[3*chars[1]+i][j]); 
		// server tells people to choose characters if both players are readied up, 1 of 2 players have not selected characters, and unreadies them
		} else if (blnS&&(chars[0]==-1||chars[1]==-1)&&(locked[0]&&locked[1])) {
			String send = "missing"+strSep;
			themenu.thetxtarea.append("\n The following players must select a character:");
			if (chars[0]==-1){
				send+="0";
				locked[0] = false;
				themenu.thetxtarea.append("\n"+strUsers[0]);
			} 
			if (chars[1]==-1){
				send+=strSep+"1";
				locked[1] = false;
				themenu.thetxtarea.append("\n"+strUsers[1]);
			}
			ssm.sendText(send);
		// if not server (ie. client redirected to this method via toGame command in ssm), switch to game panel
		} else if (!blnS) {
			chars[1] = themenu.selected;
			theframe.setContentPane(game);
			theframe.pack();
			inGame = true;
			// load character hitboxes 
			for (int i=0;i<8;i++) for (int j=0;j<4;j++) game.hbxH[i][j] = Integer.parseInt(hbxes[8*chars[0]+i][j]); 
			for (int i=0;i<8;i++) for (int j=0;j<4;j++) game.hbxC[i][j] = Integer.parseInt(hbxes[8*chars[1]+i][j]); 
			for (int i=0;i<3;i++) for (int j=0;j<4;j++) game.atkhbxH[i][j] = Integer.parseInt(atkhbxes[3*chars[0]+i][j]); 
			for (int i=0;i<3;i++) for (int j=0;j<4;j++) game.atkhbxC[i][j] = Integer.parseInt(atkhbxes[3*chars[1]+i][j]); 
		}
		
		// save usernames in server client player objects
		game.phost.strplayername = strUsers[0];
		game.pclient.strplayername = strUsers[1];
		
		// load character stats into player objects (based on what fighter was chosen)
		game.phost.intpattack = Integer.parseInt(statistics[chars[0]][0]);
		game.phost.intphealth = Integer.parseInt(statistics[chars[0]][1]);
		game.phost.intpweight = Integer.parseInt(statistics[chars[0]][2]);
		game.phost.intpspeed = Integer.parseInt(statistics[chars[0]][3]);
		
		game.pclient.intpattack = Integer.parseInt(statistics[chars[1]][0]);
		game.pclient.intphealth = Integer.parseInt(statistics[chars[1]][1]);
		game.pclient.intpweight = Integer.parseInt(statistics[chars[1]][2]);
		game.pclient.intpspeed = Integer.parseInt(statistics[chars[1]][3]);
		
		game.phost.intchealth = game.phost.intphealth;
		game.pclient.intchealth = game.pclient.intphealth;
		game.phost.intcenergy = 0;
		game.pclient.intcenergy = 0;
		
		// change healthbar color 
		game.col[0] = new Color(rgb[0], rgb[1], rgb[2]);
		
		// use load server and client sprite arrays based on what characters were chosen (both left and right sprite arrays for server/client)
		game.pSprites = game.sprites[chars[0]];
		game.cSprites = game.sprites[chars[1]];
		game.pSprites2 = game.sprites2[chars[0]];
		game.cSprites2 = game.sprites2[chars[1]];
		
		// make chat visible at start of game
		game.chatTicks = 0;
	}
	
	// update method for servers (did not want to call this ssm.sendText(...) multiple times) sends player coordinates, health, energy, facing left, ducking, jumping, and ulting for server fighter
	public static void sendUpdate() {
		int left = game.left ? 1 : 0;
		int jump = game.jump ? 1 : 0;
		int duck = game.duck ? 1 : 0;
		int ult = game.ult ? 1 : 0;
		ssm.sendText("update"+ 
					strSep+game.backs[0].x+ 
					strSep+game.backs[0].y+
					strSep+game.backs[1].x+
					strSep+game.backs[1].y+
					strSep+game.phost.intchealth+
					strSep+game.phost.intcenergy+
					strSep+game.pclient.intchealth+
					strSep+game.pclient.intcenergy+
					strSep+left+
					strSep+jump+
					strSep+duck+
					strSep+ult);
		// attack command specifies what type of attack is being recieved
		if (game.atking) ssm.sendText("attack"+strSep+game.up);
	}
	
	// move command for clients (created for same reason as sendUpdate()) sends client coordinates, jumping, ducking, facing left, and ulting for client fighter 
	public static void move() {
		int left = game.left2 ? 1 : 0;
		int jump = game.jump2 ? 1 : 0;
		int duck = game.duck2 ? 1 : 0;
		int ult = game.ult2 ? 1 : 0;
		ssm.sendText("move"+
					strSep+game.backs[1].x+
					strSep+game.backs[1].y+
					strSep+left+
					strSep+jump+
					strSep+duck+
					strSep+ult);
	}
	
	// method to make ssm server object from a static context (this is called in main menu)
	public static void makeServer() {
		// make ssm object with it's own ActionListener
		ssm = new SuperSocketMaster(9112, new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				// split message by the static separator variable
				String[] strMess = ssm.readText().split(strSep);
				// ready command = make client readied up and check if both players are readied up
				if (strMess[0].equals("ready")) {
					locked[1] = true;
					toGame();
				// move changes client fighter's coordinates and determines what it is doing
				} else if (strMess[0].equals("move")) {
					int[] data = new int[strMess.length];
					for (int i=1;i<strMess.length;i++){
						data[i] = Integer.parseInt(strMess[i]);
					}
					game.backs[1].x = data[1];
					game.backs[1].y = data[2];
					game.left2 = data[3]==0 ? false : true;
					game.duck2 = data[5]==0 ? false : true;
					game.jump2 = data[4]==0 ? false : true;
					game.ult2 = data[6]==0 ? false: true;
					if (!game.jump2) game.tme2 = 0;
				// choose specifies what character the client has chosen
				} else if (strMess[0].equals("choose")) {
					chars[1] = Integer.parseInt(strMess[1]);
					themenu.otherselected = chars[1];
				// attack sets client's fighter to attacking and determines high vs low attack
				} else if (strMess[0].equals("attack")) {
					game.atking2 = true;
					game.up2 = Integer.parseInt(strMess[1]);
				// chat command appends the client's message to the chat area of the panel
				} else if (strMess[0].equals("chat")) {
					if (inGame) {
						// if in game, will reset the counter for the chat dissappearing
						game.chat.append(strMess[1]+": "+strMess[2]+"\n");
						game.scr.setVisible(true);
						game.chatTicks=0;
					} else {
						themenu.thetxtarea.append("\n"+strUsers[1]+": "+strMess[1]);
					}
				// connect saves the client's username and tells it the server's username
				} else if (strMess[0].equals("connect")){ 
					if (!connected) {
						connected = true;
						strUsers[1] = strMess[1];
						strUsers[0] = themenu.usernamefield.getText();
						ssm.sendText("connect"+strSep+strUsers[0]);
					} else  {
						ssm.sendText("disconnect"+strSep+strMess[1]);
					}
				// disconnect will make reset the connection status of the server and remove the disconnected client's selection for a character
				} else if (strMess[0].equals("disconnect")) {
					connected = false;
					locked[1] = false;
					themenu.otherselected = -1;
				} 
			}
		});
		// open the network 
		ssm.connect();
	}
	
	// method to make ssm client object from a static context (this is called in main menu)
	public static void makeClient(String strHost) {
		// make ssm object with it's own ActionListener
		ssm = new SuperSocketMaster(strHost, 9112, new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				// split message by the static separator variable
				String[] strMess = ssm.readText().split(strSep);
				// start game calls the swap to game panel and sets the rounds to win variable in game
				if (strMess[0].equals("startGame")) {
					toGame();
					game.serieswin = Integer.parseInt(strMess[1]);
				// if disconnect is called and the username of the client matches the one sent in the server, the player will be forced off the lobby
				} else if (strMess[0].equals("disconnect")&&strMess[1].equals(strUsers[1])) {
					ssm.disconnect();
					ssm = null;
					themenu.usernamefield.setEnabled(true);
					themenu.thetxtarea.append("\n"+"That lobby is already full. Please try joining another lobby.");
				// update will define what the server's fighter is doing, what the health and energy levels are after calculations and the server's coordinates
				} else if (strMess[0].equals("update")) {
					int[] data = new int[strMess.length];
					for (int i=1;i<strMess.length;i++){
						data[i] = Integer.parseInt(strMess[i]);
					}
					
					game.jump = data[10]==0 ? false : true;
					game.backs[0].x = data[1];
					game.phost.intchealth = data[5];
					game.phost.intcenergy = data[6];
					game.pclient.intchealth = data[7];
					game.pclient.intcenergy = data[8];
					game.left = data[9]==0 ? false : true;
					game.duck = data[11]==0 ? false : true;
					game.ult = data[12]==0 ? false: true;
					if (!game.jump) game.tme = 0;
				// choose defines what fighter the server has chosen
				} else if (strMess[0].equals("choose")) {
					chars[0] = Integer.parseInt(strMess[1]);
					themenu.otherselected = chars[0];
				// attack tells the client what type of basic attack the server used (high or low)
				} else if (strMess[0].equals("attack")) {
					game.up = Integer.parseInt(strMess[1]);
					game.atking = true;
				// chat prints the server's message on the appropriate panel
				}  else if (strMess[0].equals("chat")) {
					if (inGame) {
						game.chat.append(strMess[1]+": "+strMess[2]+"\n");
						game.scr.setVisible(true);
						// reset the dissappear counter for the chat
						game.chatTicks=0;
					}else {
						themenu.thetxtarea.append("\n"+strUsers[0]+": "+strMess[1]);
					}
				// knockback tells the client which player is being knocked back
				} else if (strMess[0].equals("knockback")) {
					int type = Integer.parseInt(strMess[1]);
					if (type==0)game.kb = true;
					else game.kb2 = true;
				// round end tells the client that the round has ended
				} else if (strMess[0].equals("roundEnd")) {
					game.winner = strMess[1];
					game.done = true;
					game.phost.introunds = Integer.parseInt(strMess[2]);
					game.pclient.introunds = Integer.parseInt(strMess[3]);
				// nextRound tells the client to move on to the next round (and resets client variables to defaults)
				} else if (strMess[0].equals("nextRound")) {
					game.done = false;
					game.backs[1].x = 1280-256;
					game.left2 = true;
				// game end tells the client that the game is over (ie. rounds to win has been reached)
				} else if (strMess[0].equals("gameEnd")) {
					game.done = true;
					game.winner = strMess[1];
					game.phost.introunds = Integer.parseInt(strMess[2]);
					game.pclient.introunds = Integer.parseInt(strMess[3]);
				// to menu swaps the client to the menu and disconnects them from the network
				} else if (strMess[0].equals("toMenu")) {
					theframe.setContentPane(themenu);
					theframe.pack();
					themenu.lastClick = 5;
					themenu.cameraPos = new Point(0, 0);
					inGame = false;
					ssm.disconnect();
					ssm = null;
				// connect what the server's username is after the client connects
				} else if (strMess[0].equals("connect")){ 
					strUsers[0] = strMess[1];
				// missing tells the client who has not selected their character despite readying up
				} else if (strMess[0].equals("missing")) {
					themenu.thetxtarea.append("\n The following players must select a character:");
					if (strMess.length<3) {
						themenu.thetxtarea.append("\n"+strUsers[Integer.parseInt(strMess[1])]);
						locked[Integer.parseInt(strMess[1])] = false;
					} else {
						themenu.thetxtarea.append("\n"+strUsers[Integer.parseInt(strMess[1])]);
						themenu.thetxtarea.append("\n"+strUsers[Integer.parseInt(strMess[2])]);
						locked[Integer.parseInt(strMess[1])] = false;
						locked[Integer.parseInt(strMess[2])] = false;
					}
				}
			}
		});
		// connect to the network
		ssm.connect();
	} 
	
	// bubble sorting the arraylist of high scores
	public void sortScores() {
		for (int intC=0;intC<highscores.size();intC++) {
			for (int intC2=0;intC2<highscores.size()-1;intC2++){
				double left = Double.parseDouble(highscores.get(intC2)[1]), right = Double.parseDouble(highscores.get(intC2+1)[1]);
				if (left>right) {
					String[] temp = highscores.get(intC2);
					highscores.remove(intC2);
					highscores.add(intC2+1, temp);
				}
			}
		}
	}
	
	//constructor
	public AllOutScrap(){
		// add listeners for mouse motion, mouse click, window entering/exiting, 
		theframe.addMouseMotionListener(this);
		theframe.addMouseListener(this);
		theframe.addKeyListener(this);
		theframe.addWindowListener(this);
		
		// set size for panels, make main menu panel the default menu when the program starts
		themenu.setPreferredSize(new Dimension(1280,720));
		game.setPreferredSize(new Dimension(1280,720));
		theframe.setContentPane(themenu);
		
		// set frame default settings
		theframe.pack();
		theframe.setDefaultCloseOperation(3);
		theframe.setResizable(false);
		theframe.setVisible(true);
		
		// load the data arrays/arraylist
		statistics = loader.CharStatsRender("Basic Character Stats - Sheet1.csv", 4, 4);
		hbxes = loader.CharStatsRender("Main Body Hitbox Stats - Sheet1.csv", 32, 4);
		atkhbxes = loader.CharStatsRender("High, Low and Ult Hitbox Stats - Sheet1.csv", 12, 4);
		highscores = loader.highscores();
		
		// make defualt health bar colour red
		rgb[0] = 255;
	}
	
	//main method
	public static void main(String[] args){
		new AllOutScrap();
	}
}
