//Importing the necessary java libraries
import java.awt.*;

/**
 * <h1> Player </h1>
 * Player is a model class that holds values related to each player's fighter during gameplay <p>
 * @author Francis Madarang
 * @version 2.0
 * @since 2022-1-11
 **/
//This is a class that will hold various values that are related to the player
public class Player{

	//Properties
	/** The player's username (display name) 
	 */
	public String strplayername; 	//Variable for player's name
	/** The player's fighter's attack stat */
	public int intpattack;			//Variable for character's attack damage
	/** The player's fighter's maximum health stat */ 
	public int intphealth;			//Variable for character's max health
	/** The player's fighter's weight stat */ 
	public int intpweight;			//Variable for character's weight 
	/** The player's fighter's speed stat */ 
	public int intpspeed;			//Variable for character's speed
	/** The player's fighter's current health stat */
	public int intchealth;			//Variable for character's current health
	/** The player's fighter's current energy stat */
	public int intcenergy;			//Variable for character's current energy
	/** The player's number of rounds won <p> */
	public int introunds;			//Variable for number of rounds the player has won
	
	
	//Methods
	
	//Constrcutor
	/**
	 * Create a Player object with attack, maximum health, weight, speed, current health, current energy, rounds won and username values <p>
	 * @param strpname Player's username (display name)
	 * @param intpa Player's fighter's attack
	 * @param intph Player's fighter's maximum health
	 * @param intpw Player's fighter's weight
	 * @param intps Player's fighter's speed
	 * @param intch Player's fighter's current health
	 * @param intce Player's fighter's current energy
	 * @param intr Rounds the player has won
	 */
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
