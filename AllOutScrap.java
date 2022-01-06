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
	static JPanel otherpanel = new JPanel();
	
	//methods
	public void actionPerformed(ActionEvent evt){
		if (evt.getSource() == ssm){
			
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
		
	}
	
	public void keyPressed(KeyEvent evt){
		
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
	
	//constructor
	public AllOutScrap(){
		theframe.addMouseMotionListener(this);
		theframe.addMouseListener(this);
		theframe.addKeyListener(this);
		theframe.addWindowListener(this);
		themenu.setPreferredSize(new Dimension(1280,720));
		otherpanel.setPreferredSize(new Dimension(1280,720));
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
