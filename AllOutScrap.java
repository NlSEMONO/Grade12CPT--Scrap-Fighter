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
	
	static JFrame theframe = new JFrame ("All Out Scrap!");
	static MenuPanel themenu = new MenuPanel();
	static SuperSocketMaster ssm;
	static GamePanel game = new GamePanel();
	static int pCount = 0;
	static String strSep = "6b9";
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
				game.defX = 0;
				game.left = true;
			} else if (evt.getKeyChar()=='s' || evt.getKeyChar()=='S') {
				game.duck = false;
			} else if (evt.getKeyChar()=='d' || evt.getKeyChar()=='D') {
				game.defX = 0;
				game.left = false;
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
			
			if (themenu.atking&&themenu.atks[themenu.up].intersects(themenu.dummy)&&themenu.atkTicks==0&&themenu.atkCd<1) {
				System.out.println("OUCH!");
			}
		} else {
			game.atking = false;
			if (evt.getKeyChar()=='q'||evt.getKeyChar()=='Q') {
				// high attack
				if (game.atkCd<1) game.atking = true;
				game.up = 0;
			} else if (evt.getKeyChar()=='e'||evt.getKeyChar()=='E') {
				// low attack
				if (game.atkCd<1) game.atking = true;
				game.up = 1;
			} else if (evt.getKeyChar()==' ') {
				// ult 
				
			} 
			if (evt.getKeyChar()=='w' || evt.getKeyChar()=='W') {
				game.jump = true;
			} else if (evt.getKeyChar()=='a' || evt.getKeyChar()=='A') {
				game.defX = -5;
				game.left = true;
			} else if (evt.getKeyChar()=='s' || evt.getKeyChar()=='S') {
				game.duck = true;
			} else if (evt.getKeyChar()=='d' || evt.getKeyChar()=='D') {
				game.defX = 5;
				game.left = false;
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
			ssm.sendText("startGame");
			
			// load character hitboxes into game
			System.out.println(chars[0]+" "+chars[1]);
			for (int i=0;i<4;i++) for (int j=0;j<4;j++) game.hbxH[i][j] = Integer.parseInt(hbxes[4*chars[0]+i][j]); 
			for (int i=0;i<4;i++) for (int j=0;j<4;j++) game.hbxC[i][j] = Integer.parseInt(hbxes[4*chars[1]+i][j]); 
			for (int i=0;i<3;i++) for (int j=0;j<4;j++) game.atkhbxH[i][j] = Integer.parseInt(atkhbxes[3*chars[0]+i][j]); 
			for (int i=0;i<3;i++) for (int j=0;j<4;j++) game.atkhbxC[i][j] = Integer.parseInt(atkhbxes[3*chars[1]+i][j]); 
			
		} else if (!blnS) {
			theframe.setContentPane(game);
			theframe.pack();
			inGame = true;
		}
	}
	
	public static void makeServer() {
		ssm = new SuperSocketMaster(9112, new ActionListener () {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] strMess = ssm.readText().split(strSep);
				if (strMess[0].equals("ready")) {
					locked[1] = true;
					toGame();
				} else if (strMess[0].equals("attack")) {
					
				} else if (strMess[0].equals("connect")) {
					
				} else if (strMess[0].equals("sendName")) {
					
				} else if (strMess[0].equals("chat")) {
					
				} else if (strMess[0].equals("choose")) {
					if (blnS) chars[1] = Integer.parseInt(strMess[1]);
				} else if (strMess[0].equals("disconnect")) {
					
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
