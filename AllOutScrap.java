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
	static String strSep = "6b9";
	static JFrame theframe = new JFrame ("All Out Scrap!");
	static MenuPanel themenu = new MenuPanel();
	static SuperSocketMaster ssm;
	static GamePanel game = new GamePanel();
	static int pCount = 0;
	static SFCharStatsRender loader = new SFCharStatsRender();
	static String[][] statistics;
	static String[][] hbxes;
	static String[][] atkhbxes;
	static boolean inGame = false;
	static boolean[] locked = new boolean[2];
	static int[] chars = new int[2];
	static boolean blnS;
	static boolean connected = false;
	
	static ArrayList<String[]> highscores = new ArrayList<>();
	static boolean[] ready = new boolean[2];
	static int[] rgb = new int[3];
	static String[] strUsers = new String[2];
	
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
	
	public void keyReleased(KeyEvent evt){
		if (!inGame) {
			if (evt.getKeyChar()=='a' || evt.getKeyChar()=='A') {
				themenu.defX = 0;
				themenu.left = true;
			} else if (evt.getKeyChar()=='s' || evt.getKeyChar()=='S') {
				themenu.duck = false;
			} else if (evt.getKeyChar()=='d' || evt.getKeyChar()=='D') {
				themenu.defX = 0;
				themenu.left = false;
			} 
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
		if (!inGame) {
			
			if (evt.getKeyChar()=='z') {
				sortScores();
			}
			
			themenu.atking = false;
			if (evt.getKeyChar()=='q'||evt.getKeyChar()=='Q') {
				// high attack
				if (themenu.atkCd<1) themenu.atking = true;
				themenu.up = 0;
			} else if (evt.getKeyChar()=='e'||evt.getKeyChar()=='E') {
				// low attack
				if (themenu.atkCd<1) themenu.atking = true;
				themenu.up = 1;
			} else if (evt.getKeyChar()==' ') {
				// ult 
				
				
			} 
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
			if (themenu.left) {
				themenu.atks[0].x = themenu.r.x-40;
				themenu.atks[1].x = themenu.r.x-40;
			} else {
				themenu.atks[0].x = themenu.r.x+40;
				themenu.atks[1].x = themenu.r.x+40;
			}
			themenu.atks[0].y = themenu.r.y;
			themenu.atks[1].y = themenu.r.y+25;
			//comment
			if (themenu.atking&&themenu.atks[themenu.up].intersects(themenu.dummy)&&themenu.atkTicks==0&&themenu.atkCd<1) {
				System.out.println("OUCH!");
			}
		} else {
			game.atking = false;
			if (evt.getKeyChar()=='q'||evt.getKeyChar()=='Q') {
				// high attack
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
			} else if (evt.getKeyChar()=='e'||evt.getKeyChar()=='E') {
				// low attack
				if (blnS) {
					if (game.done) {
						game.done = false;
						if (game.phost.introunds==game.serieswin||game.pclient.introunds==game.serieswin) {
							toMenu();
							
							for (int intC=0;intC<game.times.size();intC++) {
								if (highscores.size()<15) {
									String[] add = {game.winners.get(intC), game.times.get(intC).toString()};
									highscores.add(add);
									sortScores();
								} else if (game.times.get(intC)>Double.parseDouble(highscores.get(highscores.size()-1)[1])) {
									highscores.remove(highscores.size()-1);
									String[] add = {game.winners.get(intC), game.times.get(intC).toString()};
									highscores.add(add);
									sortScores();
								} 
							}
							
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
							game.times.clear();
							game.winners.clear();
						} 
						else game.nextRound();
					} else {
						if (game.atkCd<1) game.atking = true;
						game.up = 1;
					}
				} else {
					if (game.atkCd2<1) {
						game.atking2 = true;
						ssm.sendText("attack"+strSep+game.up2);
					}
					game.up2 = 1; 
				} 
				
			} else if (evt.getKeyChar()==' ') {
				if (blnS) { 
					if (game.phost.intcenergy==50) game.ult = true;
				} else {
					if (game.pclient.intcenergy==50) game.ult2 = true;
				}
			}
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
			
			
			if (evt.getKeyCode()==10) {
				if (!game.mess.isVisible()) {
					game.chatTicks = 0;
					game.mess.setVisible(true);
					game.scr.setVisible(true);
					game.mess.requestFocus();
				}
			}
			
			
			if (game.atking&&game.atks[game.up].intersects(game.dummy)&&game.atkTicks==0&&game.atkCd<1) {
				System.out.println("OUCH!");
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
	
	public void mousePressed (MouseEvent evt){
		themenu.mouseDown = true;
	}
	
	public void mouseReleased (MouseEvent evt){
		themenu.mouseDown = false;
		themenu.retriggerCatch = false;
	}
	
	public void mouseMoved (MouseEvent evt){
		themenu.mousePos = new Point(evt.getX()-8, evt.getY()-30);
		
	}
	
	public void mouseDragged (MouseEvent evt){
		//probably won't need
	}
	
	public void toMenu() {
		ssm.sendText("toMenu");
		theframe.setContentPane(themenu);
		theframe.pack();
		themenu.lastClick = 5;
		themenu.cameraPos = new Point(0, 0);
		inGame = false;
		ssm = null;
	}
	
	public static void toGame() {
		theframe.requestFocus();
		// server tells client to start the game if characters are chosen and both players are readied up
		if (locked[1]&&locked[0]&&blnS&&chars[0]!=-1&&chars[1]!=-1) {
			theframe.setContentPane(game);
			theframe.pack();
			inGame = true;
			ssm.sendText("choose"+strSep+chars[0]);
			game.serieswin = themenu.theslider.getValue();
			ssm.sendText("startGame"+strSep+game.serieswin);
			
			// load character hitboxes into game
			System.out.println(chars[0]+" "+chars[1]);
			for (int i=0;i<8;i++) for (int j=0;j<4;j++) game.hbxH[i][j] = Integer.parseInt(hbxes[8*chars[0]+i][j]); 
			for (int i=0;i<8;i++) for (int j=0;j<4;j++) game.hbxC[i][j] = Integer.parseInt(hbxes[8*chars[1]+i][j]); 
			for (int i=0;i<3;i++) for (int j=0;j<4;j++) game.atkhbxH[i][j] = Integer.parseInt(atkhbxes[3*chars[0]+i][j]); 
			for (int i=0;i<3;i++) for (int j=0;j<4;j++) game.atkhbxC[i][j] = Integer.parseInt(atkhbxes[3*chars[1]+i][j]); 
		// server tells ppl to choose characters if both players are readied up
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
		} else if (!blnS) {
			chars[1] = themenu.selected;
			theframe.setContentPane(game);
			theframe.pack();
			inGame = true;
			// hitboxes temporary for client side
			for (int i=0;i<8;i++) for (int j=0;j<4;j++) game.hbxH[i][j] = Integer.parseInt(hbxes[8*chars[0]+i][j]); 
			for (int i=0;i<8;i++) for (int j=0;j<4;j++) game.hbxC[i][j] = Integer.parseInt(hbxes[8*chars[1]+i][j]); 
			for (int i=0;i<3;i++) for (int j=0;j<4;j++) game.atkhbxH[i][j] = Integer.parseInt(atkhbxes[3*chars[0]+i][j]); 
			for (int i=0;i<3;i++) for (int j=0;j<4;j++) game.atkhbxC[i][j] = Integer.parseInt(atkhbxes[3*chars[1]+i][j]); 
		}
		
		
		// save usernames in server client objects
		game.phost.strplayername = strUsers[0];
		game.pclient.strplayername = strUsers[1];
		
		// load character stats into player class
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
		
		System.out.println("yes"+" "+chars[0] +" "+chars[1]);
		game.pSprites = game.sprites[chars[0]];
		game.cSprites = game.sprites[chars[1]];
		
		game.pSprites2 = game.sprites2[chars[0]];
		game.cSprites2 = game.sprites2[chars[1]];
	}
	
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
		if (game.atking) ssm.sendText("attack"+strSep+game.up);
	}
	
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
	
	public static void makeServer() {
		ssm = new SuperSocketMaster(9112, new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] strMess = ssm.readText().split(strSep);
				if (strMess[0].equals("ready")) {
					locked[1] = true;
					toGame();
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
				} else if (strMess[0].equals("choose")) {
					chars[1] = Integer.parseInt(strMess[1]);
					themenu.otherselected = chars[1];
				} else if (strMess[0].equals("attack")) {
					game.atking2 = true;
					game.up2 = Integer.parseInt(strMess[1]);
				} else if (strMess[0].equals("chat")) {
					if (inGame) {
						game.chat.append(strMess[1]+": "+strMess[2]+"\n");
						game.scr.setVisible(true);
						game.chatTicks=0;
					} else {
						themenu.thetxtarea.append("\n"+strUsers[1]+": "+strMess[1]);
					}
				} else if (strMess[0].equals("connect")){ 
					if (!connected) {
						connected = true;
						strUsers[1] = strMess[1];
						strUsers[0] = themenu.usernamefield.getText();
						ssm.sendText("connect"+strSep+strUsers[0]);
					} else  {
						ssm.sendText("disconnect"+strSep+strMess[1]);
					}
					
				} else if (strMess[0].equals("disconnect")) {
					connected = false;
				} 
			}
		});
		ssm.connect();
	}
	
	public static void makeClient(String strHost) {
		ssm = new SuperSocketMaster(strHost, 9112, new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] strMess = ssm.readText().split(strSep);
				if (strMess[0].equals("startGame")) {
					toGame();
					game.serieswin = Integer.parseInt(strMess[1]);
				} else if (strMess[0].equals("disconnect")&&strMess[1].equals(strUsers[1])) {
					ssm = null;
					themenu.usernamefield.setEnabled(true);
					themenu.thetxtarea.append("\n"+"That lobby is already full. Please try joining another lobby.");
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
				} else if (strMess[0].equals("choose")) {
					chars[0] = Integer.parseInt(strMess[1]);
					themenu.otherselected = chars[0];
				} else if (strMess[0].equals("attack")) {
					game.up = Integer.parseInt(strMess[1]);
					game.atking = true;
				}  else if (strMess[0].equals("chat")) {
					if (inGame) {
						game.chat.append(strMess[1]+": "+strMess[2]+"\n");
						game.scr.setVisible(true);
						game.chatTicks=0;
					}else {
						themenu.thetxtarea.append("\n"+strUsers[0]+": "+strMess[1]);
					}
				} else if (strMess[0].equals("knockback")) {
					int type = Integer.parseInt(strMess[1]);
					if (type==0)game.kb = true;
					else game.kb2 = true;
				} else if (strMess[0].equals("roundEnd")) {
					game.winner = strMess[1];
					game.done = true;
					game.phost.introunds = Integer.parseInt(strMess[2]);
					game.pclient.introunds = Integer.parseInt(strMess[3]);
				} else if (strMess[0].equals("nextRound")) {
					game.done = false;
				} else if (strMess[0].equals("gameEnd")) {
					game.done = true;
					game.winner = strMess[1];
					game.phost.introunds = Integer.parseInt(strMess[2]);
					game.pclient.introunds = Integer.parseInt(strMess[3]);
				} else if (strMess[0].equals("toMenu")) {
					theframe.setContentPane(themenu);
					theframe.pack();
					themenu.lastClick = 5;
					themenu.cameraPos = new Point(0, 0);
					inGame = false;
					ssm = null;
				} else if (strMess[0].equals("connect")){ 
					strUsers[0] = strMess[1];
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
		ssm.connect();
	} 
	
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
		theframe.addMouseMotionListener(this);
		theframe.addMouseListener(this);
		theframe.addKeyListener(this);
		theframe.addWindowListener(this);
		themenu.setPreferredSize(new Dimension(1280,720));
		game.setPreferredSize(new Dimension(1280,720));
		theframe.setContentPane(themenu);
		theframe.pack();
		theframe.setDefaultCloseOperation(3);
		theframe.setResizable(false);
		theframe.setVisible(true);
		statistics = loader.CharStatsRender("Basic Character Stats - Sheet1.csv", 4, 4);
		hbxes = loader.CharStatsRender("Main Body Hitbox Stats - Sheet1.csv", 32, 4);
		atkhbxes = loader.CharStatsRender("High, Low and Ult Hitbox Stats - Sheet1.csv", 12, 4);
		highscores = loader.highscores();
		rgb[0] = 255;
	}
	
	//main method
	public static void main(String[] args){
		new AllOutScrap();
	}
}
