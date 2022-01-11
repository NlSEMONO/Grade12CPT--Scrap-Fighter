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
	static String strSep = "*K(";
	
	//methods
	public void actionPerformed(ActionEvent evt){
		if (evt.getSource() == ssm){
			String[] strMess = ssm.readText().split(strSep);
			if (strMess[0].equals("ready")) {
				
			} else if (strMess[0].equals("attack")) {
				
			} else if (strMess[0].equals("connect")) {
				
			} else if (strMess[0].equals("move")) {
				
			} else if (strMess[0].equals("update")) {
				
			} else if (strMess[0].equals("gameEnd")) {
				
			} else if (strMess[0].equals("rejectName")) {
				
			} else if (strMess[0].equals("sendName")) {
				
			} else if (strMess[0].equals("chat")) {
				
			} else if (strMess[0].equals("choose")) {
				
			} else if (strMess[0].equals("disconnect")) {
				
			}
		}
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
	}
	
	public void keyPressed(KeyEvent evt){
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
		theframe.setContentPane(game);
		theframe.pack();
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
	}
	
	//main method
	public static void main(String[] args){
		new AllOutScrap();
	}
}
