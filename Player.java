//Importing the necessary java libraries
import java.awt.*;

//This is a class that will hold various values that are related to the 
public class Player{

	//Properties
	String strplayername; 	//Variable for player's name
	int intpattack;			//Variable for character's attack damage
	int intphealth;			//Variable for character's max health
	int intpweight;			//Variable for character's weight 
	int intpspeed;			//Variable for character's speed
	int intchealth;			//Variable for character's current health
	int intcenergy;			//Variable for character's current energy
	int introunds;			//Variable for number of rounds the player has won
	
	
	//Methods
	
	//Constrcutor
	public Player(String strpname, int intpa, int intph, int intpw, int intps, int intch, int intce, int intr){
		
		//Here the Player constructs a Player object using the character's stats that the player has chosen and also the player's currently health and energy levels.
		this.strplayername = strpname;
		this.intpattack = intpa;
		this.intphealth = intph;
		this.intpweight = intpw;
		this.intpspeed = intps;
		this.intchealth = intch;
		this.intcenergy = intce;
		this.introunds = intr;
		
	}

}
