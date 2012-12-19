import java.util.*;

public class Letter {
	char letter;
	int frequency;
	
	//declare a hashMap that hashes correlates between a following letter "c" and the # times "c" will follow this character
	HashMap<Character,Integer> followingLetter;
	
	//constructor
	public Letter (char letter)
	{
		this.letter = letter;
		frequency = 0;
		followingLetter = new HashMap<Character, Integer>();
	}
	
	public HashMap<Character,Integer> getMap()
	{
		return followingLetter;
	}
	
	public void followedBy(char c)
	{
		Character newChar = new Character(c);
		int followFrequency = 1;
		//if already encountered before, take old frequency and increment
		if (followingLetter.containsKey(newChar))
		{
			followFrequency = followingLetter.get(newChar).intValue()+1;
		}
		
		//this should override the existing entry if it existed, thus taking care of both existing case, and new case
		followingLetter.put(newChar, new Integer(followFrequency));
		
	}
	
	/*
	 * get the probability that this current character is followed by character "c"
	 * 
	 * returns: probability, or -1 if this is never followed by "c"
	 */
	public double getProbability(char c)
	{
		if (!followingLetter.containsKey(new Character(c)))
		{
			return -1.0;
		}
		return (double) followingLetter.get(new Character(c)).intValue()/ (double) frequency;
	}
	
	/*
	 * return: char that this object is representing
	 */
	public char getLetter()
	{
		return this.letter;
	}
	
	/*
	 * return: number of times this char has appeared
	 */
	public int getFrequency()
	{
		return this.frequency;
	}
	
	/*
	 * sets the number of times this char has appeared
	 */
	public void setFrequency(int f)
	{
		this.frequency = f;
	}
	
}
