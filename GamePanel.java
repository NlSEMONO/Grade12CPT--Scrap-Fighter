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
	int defX = 0, defY = 0;
	int atkTicks = 0, atkCd = 0, up;
	double tme = 0;
	boolean atking = false, left = false, jump = false, duck = false, deced = false;
	Rectangle fighter = new Rectangle(300, 720-50, 50, 50);
	Rectangle dummy = new Rectangle(700, 720-50, 50, 50);
	Rectangle[] atks = new Rectangle[2];
	double vi = 200-phost.intpweight;
	double accel = -350.5/2;
	
	public void paintComponent(Graphics g) {
		// x axis movement
		fighter.x += defX;
		
		// jumping
		if (jump) {
			fighter.y = (720-fighter.height)-Math.min((int)(((vi*tme)+(accel*tme*tme))*1.6), 50);
			if (fighter.y+fighter.height > 720) {
				fighter.y = 720-fighter.height;
				jump = false;
				tme = -0.02;
			}
			tme+=0.02;
		}
		
		// duck
		if (duck&&!deced) {
			deced = true;
			fighter.height = fighter.height/2;
			fighter.y = 720-((720-fighter.y)/2);
		} else if (!duck&&deced) {
			deced = false;
			fighter.y = 720-((720-fighter.y)*2);
			fighter.height = fighter.height*2;
		}
		
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
		
		// enemy hitbox
		g.setColor(Color.red);
		g.fillRect(dummy.x, dummy.y, dummy.width, dummy.height);
		
		// training fighter hitbox
		g.setColor(Color.black);
		g.fillRect(fighter.x, fighter.y, fighter.width, fighter.height);
		
		if (atking) {
			System.out.println(atks[up].x + " " + atks[up].y + " " +atks[up].width+""+ atks[up].height);
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
	}
	
	public void loadData(String[][] data) {
		
	}
	
	public GamePanel() {
		super();
		col[0] = Color.red;
		col[1] = Color.blue;
		time.start();
		// high atk
		atks[0] = new Rectangle(fighter.x+10, fighter.y, 50, 25);
		// low atk
		atks[1] = new Rectangle(fighter.x+10, fighter.y+25, 50, 25);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==time) {
			this.repaint();
			
		}
		
	}
	
	
}
