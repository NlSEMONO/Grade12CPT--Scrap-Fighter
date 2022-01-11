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
	int[][] stats = new int[2][4];
	int p1HP = 29;
	int p2HP = 15;
	int p1En = 15;
	int p2En = 75;
	int maxEn = 100;
	
	public void paintComponent(Graphics g) {
		// hardcode testing, del later
		stats[0][1] = 30;
		stats[1][1] = 40;
		
		double HPer1 = p1HP*1.0/stats[0][1];
		double HPer2 = p2HP*1.0/stats[1][1];
		double EPer1 = p1En*1.0/maxEn;
		double EPer2 = p2En*1.0/maxEn;
		
		// background
		g.setColor(backCol);
		g.fillRect(0, 0, 1280, 720);
		
		// hp and energy
		g.setColor(col[0]);
		g.fillRect(0, 0,(int) (400*HPer1), 50);
		g.fillRect(1280-(int)(400*HPer2), 0, 400, 50);
		
		g.setColor(col[1]);
		g.fillRect(0, 50, (int)(300*EPer1), 25);
		g.fillRect(1280-(int)(300*EPer2), 50, 300, 25);
		
		
	}
	
	public void loadData(String[][] data, int p1, int p2) {
		for (int intC=0;intC<4;intC++) {
			stats[0][intC] = Integer.parseInt(data[p1][intC]);
		}
		for (int intC=0;intC<4;intC++) {
			stats[1][intC] = Integer.parseInt(data[p2][intC]);
		}
	}
	
	public GamePanel() {
		col[0] = Color.red;
		col[1] = Color.blue;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==time) this.repaint();
		
	}
	
	
}
