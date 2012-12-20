import java.util.*;
public class TreeAnalyzer {
	static double closeToOne = 0.90;
	
	TreeRoot fr;
	TreeRoot br;
	public TreeAnalyzer(TreeRoot forward, TreeRoot backward)
	{
		fr = forward;
		br = backward;
	}
	
	/*
	 * return found Morpheme, if not creates a new one, adds to list, then returns it
	 */
	public Morpheme findMorpheme(LinkedList<Morpheme> list, String s)
	{
		//System.out.println("HERE:"+s);
		Morpheme returnM = null;
		for (Morpheme m: list)
		{
			if (m.morpheme.equals(s))
			{
				//System.out.println("here BOOBIES");
				returnM = m;
				return returnM;
			}
		}
		returnM = new Morpheme(s);
		list.add(returnM);
		return returnM;
	}
	
	/*
	 * condition #1
	 */
	public boolean wordExist(TreeRoot root, String word)
	{
		//System.out.println(word);
		Node n = null;
		for (int i = 0; i < word.length();i++)
		{
			if (n==null)
			{
				n = root.getChild(word.charAt(i));
			}
			else
			{
				n = n.getChild(word.charAt(i));
			}
		}
		//System.out.println(n.word);
		return n.word;
	}
	/*
	 * condition #2
	 */
	public boolean aroundOne(TreeRoot root, String word)
	{
		Node n = null;
		for (int i = 0; i < word.length()-1;i++)
		{
			if (n==null)
			{
				n = root.getChild(word.charAt(i));
			}
			else
			{
				n = n.getChild(word.charAt(i));
			}
		}
		if (((double)n.getChild(word.charAt(word.length()-1)).getCount())/((double)n.getCount()) > closeToOne) //if > threshold
				return true;
		return false;
	}
	
	/*
	 * condition #3
	 */
	public boolean lessThanOne(TreeRoot root, String word, char c)
	{
		Node n = null;
		for (int i = 0; i < word.length();i++)
		{
			if (n==null)
			{
				n = root.getChild(word.charAt(i));
			}
			else
			{
				n = n.getChild(word.charAt(i));
			}
		}
		if (((double)n.getChild(c).getCount())/((double)n.getCount()) < 1.0) 
				return true;
		return false;
	}
	
	/*
	 * return a list of possible morphemes as suffix (use forward tree)
	 */
	public LinkedList<Morpheme> suffix(LinkedList<String> inputs)
	{
		boolean valid;
		Morpheme newsuffix;
		LinkedList<Morpheme> list = new LinkedList<Morpheme>();
		for (String s: inputs) //for all the strings read in
		{
			int length = s.length();
			for (int i = length-1; i>=2;i--) //continuously increase size of possible "suffix" to test
			{
				valid = false;
				String suffix = s.substring(i, length);
				String rest = s.substring(0,i);
				newsuffix = findMorpheme(list,suffix);
				if (wordExist(fr,rest)) //if condition 1 satisfied
				{
					if (aroundOne(fr,rest)) //if condition 2 satisfied
					{
						if (lessThanOne(fr,rest,suffix.charAt(0))) //if condition 3 satisfied
						{
							valid = true;
						}
					}
				}
				if (valid) //if this "suffix" meets all requirements
				{
					newsuffix.point = newsuffix.point+19;
				}
				else //if not
				{
					newsuffix.point = newsuffix.point-1;
				}
				
			}
		}
		
		return list;
	}
	
	/*
	 * return a list of possible morphemes as prefix (use backward tree)
	 */
	public LinkedList<Morpheme> prefix(LinkedList<String> inputs)
	{
		boolean valid;
		Morpheme newprefix;
		LinkedList<Morpheme> list = new LinkedList<Morpheme>();
		for (String s: inputs) //for all the strings read in
		{
			int length = s.length();
			for (int i = length-1; i>=2;i--) //continuously increase size of possible "prefix" to test
			{
				valid = false;
				String prefix = s.substring(i, length);
				String rest = s.substring(0,i);
				newprefix = findMorpheme(list,prefix);
				if (wordExist(br,rest)) //if condition 1 satisfied
				{
					if (aroundOne(br,rest)) //if condition 2 satisfied
					{
						if (lessThanOne(br,rest,prefix.charAt(0))) //if condition 3 satisfied
						{
							valid = true;
						}
					}
				}
				if (valid) //if this "prefix" meets all requirements
				{
					newprefix.point = newprefix.point+19;
				}
				else //if not
				{
					newprefix.point = newprefix.point-1;
				}
				
			}
		}
		
		return list;
	}
}
