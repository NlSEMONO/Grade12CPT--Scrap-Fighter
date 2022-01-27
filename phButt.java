import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;

/**
 * <h1>phButt </h1>
 * This is a custom button class that has 3 appearances: hovered over, clicked on, or nothing done to it at all
 * @author Francis Madarang
 * @version 1.0
 * @since 2022-1-5
 **/

public class phButt extends Rectangle {
	/** The coordinates that the button redirects the camera to after being clicked*/
	public Point pwp;
	/** Image that the button image is drawn onto*/
	public BufferedImage drawme = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
	/** Graphics object of drawme (used to draw the button image onto the drawme image)*/
	public Graphics2D d = (Graphics2D) drawme.getGraphics();
	
	/** Array of images the button can appear in. 0 - default (no hover, no click), 1 - hovered over, 2 - clicked*/
	public BufferedImage[] imgRoster = new BufferedImage[3];
	//[0] - default appearance
	//[1] - appearance when hovered over
	//[2] - appearance when pressed
	
	/**
	 * This method checks if the button is being hovered over or being clicked, and changes the button's appearance accordingly
	 * @param mousePos The point of the cursor
	 * @param mouseDown Whether or not the mouse is being clicked
	 * @param retriggerCatch Whether the button's clicked function has already been triggered
	 * @return Whether or not the button is being clicked
	 * */
	public boolean changelook (Point mousePos,boolean mouseDown,boolean retriggerCatch){
		d.clearRect(0,0,this.width,this.height); // clear the buffer
		// if hovered over, draw the hovered over appearance
		if (this.contains(mousePos)){ 
			d.drawImage(imgRoster[1],0,0,null);
			// if clicked and the button's function has not already been triggered, draw clicked appearance
			if (mouseDown && !retriggerCatch){
				d.drawImage(imgRoster[2],0,0,null);
				return true;
			}
		// if mouse isn't on the button, make the appearance default appearance
		} else {
			d.drawImage(imgRoster[0],0,0,null);
		}
		// hovered returns false
		return false;
	}
	
	/**
	 * This method looks for an image INSIDE the .jar file with the requested file name and returns the image
	 * @param fileName The file name of the requested image
	 * @return The image version of the requested file name
	 */
	public BufferedImage img(String fileName) {
		// jar ver only
		try {
			// return the image version of the requested file name
			return ImageIO.read(getClass().getResourceAsStream(fileName));
		} catch (IOException e) {
			System.out.println("bruh moment");
		}
		return null;
	}
	
	/**
	 * Creates a phButt object with coordinates for it's location on a buffer, length, width, point that the button changes the screen's location to, and file names for the 3 appearances of the button
	 * @param intX X-coordinate of button on the buffer
	 * @param intY Y-coordinate of button on the buffer
	 * @param intW Width of the button
	 * @param intH Height of the button
	 * @param pwpX X-coordinate of the camera redirect
	 * @param pwpY Y-coordinate of the camera redirect
	 * @param imgdef File name of default appearance of the button 
	 * @param imghov File name of the hovered over appearance of the button
	 * @param imgprs File name of the clicked appearance of the button
	 */
	public phButt (int intX,int intY,int intW, int intH, int pwpX, int pwpY, String imgdef, String imghov, String imgprs){
		// inherit rectangle's methods and properties
		super(intX,intY,intW,intH);
		
		// set camera position and 3 appearances
		this.pwp = new Point(pwpX, pwpY);
		this.imgRoster[0] = img(imgdef);
		this.imgRoster[1] = img(imghov);
		this.imgRoster[2] = img(imgprs);
		
		d.setBackground(new Color(0,0,0,0));
	}
}
