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
public class SFHitStatsBoxRender{

	public String[][] HitBoxStatsRender(){
		
		String strLine = "";
		
		//A BufferedReader that is 
		BufferedReader br = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("CharacterStats.csv")));
		
		//Variables that specifies how many rows and columns are created for the 2D array
		int numofrow = 4;
		int numofcolumn = 4;
		int row = 0;
		int column = 0;
		
		//Creates a String type 2D array. Values will be parsed back into integers when they are called upon
		String dataFile [][];
		dataFile = new String [numofrow][numofcolumn];
		
		//Forloops will load the data from the CSV file into the 2D array
		for(int currentrow = 0; currentrow < numofrow+1; currentrow++){
			if (currentrow != 0){
				String strLineSplit[];
				try{
					strLine = br.readLine();					
				}catch(IOException e){
					System.out.println("Error: Problem Reading From Character Stats CSV");
				}
				strLineSplit = strLine.split(",");
				
				dataFile[currentrow-1] = strLineSplit;
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
	
	public SFHitStatsBoxRender(){
		
	}

}
