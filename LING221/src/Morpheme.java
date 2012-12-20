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
}
