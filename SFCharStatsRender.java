//Importing the necessary java libraries
import java.io.*;
import java.lang.Math;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.awt.*;
import java.awt.image.*;

//This is a model type class that will load the character stats from the CSV file into a 2D array that can quickly be referenced
public class SFCharStatsRender{

	// Method to get a specific number of rows and columns from a text file.
	// Parameters specify how many rows and columns are created for the 2D array
	public String[][] CharStatsRender(String strFile, int intRow, int intCol){
		
		String strLine = ""; // Line variable to store read lines from text files
		
		//A BufferedReader that looks for a file with the specified name in the jar file
		BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(strFile)));
		
		//Creates a String type 2D array. Values will be parsed back into integers when they are called upon
		String[][] dataFile = new String [intRow][intCol];
		
		//For loops will load the data from the CSV file into the 2D array
		for(int currentrow = 0; currentrow < intRow+1; currentrow++){ // intRow + 1 because the header row needs to be ignored
			// any row except the header will be loaded into the array
			if (currentrow != 0){
				String strLineSplit[];
				// read line from CSV
				try{
					strLine = br.readLine();					
				}catch(IOException e){
					System.out.println("Error: Problem Reading From Character Stats CSV");
				}
				strLineSplit = strLine.split(","); // break the one line of input into an array (data separated by commas)
				
				for (int intC=0;intC<intCol;intC++) dataFile[currentrow-1][intC] = strLineSplit[intC]; // save data in the return variable (2D array)
			// reading and ignoring the header row
			}else{ 
				System.out.println("Loading Up Character Stats");
				try{
					strLine = br.readLine();					
				}catch(IOException e){
				}
			}
		
		}
		
		//Returns the 2D array
		return dataFile;
		
	}
	
	// Method to get names and times of high scorers inside winners.txt (IN FOLDER, NOT JAR) (THIS IS WHERE STUFF IS WRITTEN) 
	public ArrayList<String[]> highscores() {
		ArrayList<String[]> ret = new ArrayList<>(); // return variable
		try {
			BufferedReader br = new BufferedReader(new FileReader("winners.txt"));
			// read the first 30 lines for top 15 players's names and times and save them in return variable
			for (int intCount=0;intCount<30;intCount++) {
				String in = br.readLine();
				if (in!=null) {
					if (intCount%2==0) ret.add(new String[2]); // new string array per row added to arraylist
					ret.get(intCount/2)[intCount%2] = in; // set the appropriate index of the string array to the line being read
				}
				else break;
			}		
		// if winners.txt does not exist in the place the user saved the file, add the names of the legends who scored high when the game was in testing phase to the return variable
		} catch (FileNotFoundException e) {
			ret.add(new String[2]);
			ret.get(0)[0] = "Rick Astley";
			ret.get(0)[1] = "0.01";
			ret.add(new String[2]);
			ret.get(1)[0] = "NlSEMONO";
			ret.get(1)[1] = "5.00";
			ret.add(new String[2]);
			ret.get(2)[0] = "Derp2349";
			ret.get(2)[1] = "5.00";
			ret.add(new String[2]);
			ret.get(3)[0] = "VNolanV";
			ret.get(3)[1] = "5.00";
		} catch (IOException e) {
		}
		return ret;
	}
	
	public SFCharStatsRender(){
		
	}

}
