import java.util.*;
public class Morpheme {
	String morpheme;
	int point;
	LinkedList<String> words;
	public Morpheme(String s)
	{
		morpheme = s;
		point = 0;
		words = new LinkedList<String>();
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
