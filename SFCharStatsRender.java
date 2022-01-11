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

public class SFCharStatsRender{

	public String[][] CharStatsRender(){
		
		String strLine = "";
		BufferedReader br= new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("CharacterStats.csv")));
		
		int numofrow = 5;
		int numofcolumn = 4;
		int row = 0;
		int column = 0;
		
		
		
		String dataFile [][];
		dataFile = new String [numofrow][numofcolumn];
		
		
		
		for(int currentrow = 0; currentrow < 4; currentrow++){
			for (int currentcolumn = 0; currentcolumn < 3; currentcolumn++){
				if (currentrow != 0){
					String strLineSplit[];
					try{
						strLine = br.readLine();					
					}catch(IOException e){
						System.out.println("Error: Problem Reading From Character Stats CSV");
					}
					strLineSplit = strLine.split(",");
					dataFile[currentrow][currentcolumn] = strLineSplit[currentcolumn];
				}else{
					System.out.println("Loading Up Character Stats");
				}
			}
		
		}	
		
		return dataFile;
		
	}
	
	public SFCharStatsRender(){
		
	}

}
