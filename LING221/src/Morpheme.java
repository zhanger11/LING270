import java.util.*;
public class Morpheme {
	String morpheme;
	int point;
	LinkedList<String> words;
	LinkedList<String> retrievedWords;
	public Morpheme(String s)
	{
		morpheme = s;
		point = 0;
		words = new LinkedList<String>();
		retrievedWords = new LinkedList<String>();
	}
	
	public boolean retrievedContains (String s)
	{
		for (String word: retrievedWords)
		{
			if (word.equals(s)) return true;
		}
		return false;
	}
	
	public boolean contains (String s)
	{
		for (String word: words)
		{
			if (word.equals(s)) return true;
		}
		return false;
	}
}
