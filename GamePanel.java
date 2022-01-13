import javax.swing.*;
import java.awt.image.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import javax.swing.event.*;
import java.awt.event.*;

public class GamePanel extends JPanel implements ActionListener{
	Timer time = new Timer(1000/100, this);
	ArrayList<Double> times = new ArrayList<>();
	Color[] col = new Color[2];
	Color backCol = Color.white;
	Player phost = new Player("Keanu Reeves",25,150,75,5,100,50,0);
	Player pclient = new Player("Eggs",10,100,50,10,100,50,0);
	int p1HP = 29;
	int p2HP = 15;
	int p1En = 15;
	int p2En = 75;
	int maxEn = 100;
	
	public void paintComponent(Graphics g) {
				
		// background
		g.setColor(backCol);
		g.fillRect(0, 0, 1280, 720);
		
		// hp and energy
		g.setColor(col[0]);
		g.fillRect(0, 0,(int)(400*phost.intchealth/phost.intphealth), 50);
		g.fillRect(1280-(int)(400*pclient.intchealth/pclient.intphealth), 0, 400, 50);
		
		g.setColor(col[1]);
		g.fillRect(0, 50, (int)(5*phost.intcenergy), 25);
		g.fillRect(1280-(int)(5*pclient.intcenergy), 50, 300, 25);
		
		
	}
	
	public void loadData(String[][] data) {
		
	}
	
	public GamePanel() {
		super();
		col[0] = Color.red;
		col[1] = Color.blue;
		time.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==time) this.repaint();
		
	}
	
	
}
