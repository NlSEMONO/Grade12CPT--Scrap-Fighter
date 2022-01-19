//Importing the necessary java libraries
import java.io.*;
import java.lang.Math;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.util.Random;
import java.util.Scanner;
import java.awt.*;
import java.awt.image.*;

//This is a model type class that will load the character stats from the CSV file into a 2D array that can quickly be referenced
public class SFCharStatsRender{

	//Variables that specifies how many rows and columns are created for the 2D array
	public String[][] CharStatsRender(String strFile, int intRow, int intCol){
		
		String strLine = "";
		
		//A BufferedReader that is 
		BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(strFile)));
		
		//Creates a String type 2D array. Values will be parsed back into integers when they are called upon
		String[][] dataFile = new String [intRow][intCol];
		
		//Forloops will load the data from the CSV file into the 2D array
		for(int currentrow = 0; currentrow < intRow+1; currentrow++){
			if (currentrow != 0){
				String strLineSplit[];
				try{
					strLine = br.readLine();					
				}catch(IOException e){
					System.out.println("Error: Problem Reading From Character Stats CSV");
				}
				strLineSplit = strLine.split(",");
				
				for (int intC=0;intC<intCol;intC++) dataFile[currentrow-1][intC] = strLineSplit[intC];
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
	
	public SFCharStatsRender(){
		
	}

}
