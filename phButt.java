import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;

public class phButt extends Rectangle {
	Point pwp;
	BufferedImage drawme = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
	Graphics2D d = (Graphics2D) drawme.getGraphics();
	BufferedImage[] imgRoster = new BufferedImage[3];
	//[0] - default appearance
	//[1] - appearance when hovered over
	//[2] - appearance when pressed
	
	/**changes appearance depending if the button is neutral if the mouse is hovering over the button, or if*/
	public boolean changelook (Point mousePos,boolean mouseDown,boolean retriggerCatch){
		d.clearRect(0,0,this.width,this.height);
		if (this.contains(mousePos)){
			d.drawImage(imgRoster[1],0,0,null);
			if (mouseDown && !retriggerCatch){
				d.drawImage(imgRoster[2],0,0,null);
				return true;
			}
		} else {
			d.drawImage(imgRoster[0],0,0,null);
		}
		return false;
	}
	
	public phButt (int intX,int intY,int intW, int intH, int pwpX, int pwpY, String imgdef, String imghov, String imgprs){
		super(intX,intY,intW,intH);
		this.pwp = new Point(pwpX, pwpY);
		try{
			this.imgRoster[0] = ImageIO.read(new File(imgdef));
			this.imgRoster[1] = ImageIO.read(new File(imghov));
			this.imgRoster[2] = ImageIO.read(new File(imgprs));
		} catch (IOException e){
			System.out.println("unable to load file");
		}
		d.setBackground(new Color(0,0,0,0));
	}
}
