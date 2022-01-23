import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;

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
			if (evt.getKeyChar()=='w' || evt.getKeyChar()=='W') {
				themenu.defY = 0;
			} else if (evt.getKeyChar()=='a' || evt.getKeyChar()=='A') {
				themenu.defX = 0;
				themenu.left = true;
			} else if (evt.getKeyChar()=='s' || evt.getKeyChar()=='S') {
				themenu.defY = 0;
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
					ssm.sendText("move"+strSep+"-1");
					game.defX2 = 0;
				}
			} else if (evt.getKeyChar()=='s' || evt.getKeyChar()=='S') {
				if (blnS) {
					game.duck = false;
				} else  {
					ssm.sendText("move"+strSep+"-3");
					game.duck2 = false;
				}
			} else if (evt.getKeyChar()=='d' || evt.getKeyChar()=='D') {
				if (blnS) {
					game.defX = 0;
					game.left = false;
				} else {
					ssm.sendText("move"+strSep+"-4");
					game.defX2 = 0;
				}
			} 
		}
	}
	
	public void keyPressed(KeyEvent evt){
		if (!inGame) {
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
				
				// readies you up for now
				if (!blnS) {
					if (themenu.selected!=-1) {
						ssm.sendText("choose"+strSep+themenu.selected);
						ssm.sendText("ready");
						chars[1] = themenu.selected;
					}
				} else {
					if (themenu.selected!=-1) {
						locked[0] = true;
						chars[0] = themenu.selected;
						ssm.sendText("choose"+strSep+themenu.selected);
						System.out.println(locked[0] +" "+locked[1]);
						this.toGame();
					}
				}
			} 
			if (evt.getKeyChar()=='w' || evt.getKeyChar()=='W') {
				themenu.defY = -5;
			} else if (evt.getKeyChar()=='a' || evt.getKeyChar()=='A') {
				themenu.defX = -5;
				themenu.left = true;
			} else if (evt.getKeyChar()=='s' || evt.getKeyChar()=='S') {
				themenu.defY = 5;
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
					if (game.atkCd<1) game.atking = true;
					game.up = 1;
				} else {
					if (game.atkCd2<1) {
						game.atking2 = true;
						ssm.sendText("attack"+strSep+game.up2);
					}
					game.up2 = 1; 
				} 
				
			} else if (evt.getKeyChar()==' ') {
				// ult 
				
			} 
			if (evt.getKeyChar()=='w' || evt.getKeyChar()=='W') {
				if (blnS) game.jump = true;
				else {
					ssm.sendText("move"+strSep+"2");
					game.jump2 = true;
				}
			} else if (evt.getKeyChar()=='a' || evt.getKeyChar()=='A') {
				if (blnS) {
					game.defX = -5;
					game.left = true;
				} else {
					ssm.sendText("move"+strSep+"1");
					game.defX2 = -5;
					game.left2 = true;
				}
			} else if (evt.getKeyChar()=='s' || evt.getKeyChar()=='S') {
				if (blnS) {
					game.duck = true;
				} else {
					game.duck2 = true;
					ssm.sendText("move"+strSep+"3");
				}
			} else if (evt.getKeyChar()=='d' || evt.getKeyChar()=='D') {
				if (blnS){
					game.defX = 5;
					game.left = false;
				} else {
					ssm.sendText("move"+strSep+"4");
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
	
	public static void toGame() {
		if (locked[1]&&locked[0]&&blnS) {
			theframe.setContentPane(game);
			theframe.pack();
			inGame = true;
			ssm.sendText("choose"+strSep+chars[0]);
			ssm.sendText("startGame");
			
			// load character hitboxes into game
			System.out.println(chars[0]+" "+chars[1]);
			for (int i=0;i<4;i++) for (int j=0;j<4;j++) game.hbxH[i][j] = Integer.parseInt(hbxes[4*chars[0]+i][j]); 
			for (int i=0;i<4;i++) for (int j=0;j<4;j++) game.hbxC[i][j] = Integer.parseInt(hbxes[4*chars[1]+i][j]); 
			for (int i=0;i<3;i++) for (int j=0;j<4;j++) game.atkhbxH[i][j] = Integer.parseInt(atkhbxes[3*chars[0]+i][j]); 
			for (int i=0;i<3;i++) for (int j=0;j<4;j++) game.atkhbxC[i][j] = Integer.parseInt(atkhbxes[3*chars[1]+i][j]); 
			
			// load character stats into player class
			game.phost.intpattack = Integer.parseInt(statistics[chars[0]][0]);
			game.phost.intphealth = Integer.parseInt(statistics[chars[0]][1]);
			game.phost.intpweight = Integer.parseInt(statistics[chars[0]][2]);
			game.phost.intpspeed = Integer.parseInt(statistics[chars[0]][3]);
			
			game.pclient.intpattack = Integer.parseInt(statistics[chars[1]][0]);
			game.pclient.intphealth = Integer.parseInt(statistics[chars[1]][1]);
			game.pclient.intpweight = Integer.parseInt(statistics[chars[1]][2]);
			game.pclient.intpspeed = Integer.parseInt(statistics[chars[1]][3]);
			
		} else if (!blnS) {
			theframe.setContentPane(game);
			theframe.pack();
			inGame = true;
			for (int i=0;i<4;i++) for (int j=0;j<4;j++) game.hbxH[i][j] = Integer.parseInt(hbxes[4*chars[0]+i][j]); 
			for (int i=0;i<4;i++) for (int j=0;j<4;j++) game.hbxC[i][j] = Integer.parseInt(hbxes[4*chars[1]+i][j]); 
			for (int i=0;i<3;i++) for (int j=0;j<4;j++) game.atkhbxH[i][j] = Integer.parseInt(atkhbxes[3*chars[0]+i][j]); 
			for (int i=0;i<3;i++) for (int j=0;j<4;j++) game.atkhbxC[i][j] = Integer.parseInt(atkhbxes[3*chars[1]+i][j]); 
		}
	}
	
	public static void sendUpdate() {
		int left = game.left ? 1 : 0;
		int jump = game.jump ? 1 : 0;
		int duck = game.duck ? 1 : 0;
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
					strSep+duck);
		if (game.atking) ssm.sendText("attack"+strSep+game.up);
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
					int function = Integer.parseInt(strMess[1]);
					if (function==1) {
						game.defX2 = -5;
						game.left2 = true;
					} else if (function==2) {
						game.jump2 = true;
					} else if (function==3) {
						game.duck2 = true;
					} else if (function==4) {
						game.defX2 = 5;
						game.left2 = false;
					} else if (function==-1||function==-4) {
						game.defX2 = 0;
					} else if (function==-3) {
						game.duck2 = false;
					}
				} else if (strMess[0].equals("choose")) {
					chars[1] = Integer.parseInt(strMess[1]);
				} else if (strMess[0].equals("disconnect")) {
					
				} else if (strMess[0].equals("attack")) {
					game.atking2 = true;
					game.up2 = Integer.parseInt(strMess[1]);
				} else if (strMess[0].equals("chat")) {
					if (inGame) {
						game.chat.append(strMess[1]+": "+strMess[2]+"\n");
						game.scr.setVisible(true);
						game.chatTicks=0;
					}
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
				} else if (strMess[0].equals("choose")) {
					System.out.println(strMess[1]);
					chars[0] = Integer.parseInt(strMess[1]);
				} else if (strMess[0].equals("attack")) {
					game.up = Integer.parseInt(strMess[1]);
					game.atking = true;
				}  else if (strMess[0].equals("chat")) {
					if (inGame) {
						game.chat.append(strMess[1]+": "+strMess[2]+"\n");
						game.scr.setVisible(true);
						game.chatTicks=0;
					}
				}
			}
		});
		ssm.connect();
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
		hbxes = loader.CharStatsRender("Main Body Hitbox Stats - Sheet1.csv", 16, 4);
		atkhbxes = loader.CharStatsRender("High, Low and Ult Hitbox Stats - Sheet1.csv", 12, 4);
	}
	
	//main method
	public static void main(String[] args){
		new AllOutScrap();
	}
}
