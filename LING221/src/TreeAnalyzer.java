import java.util.*;
public class TreeAnalyzer {
	static double closeToOne = 0.95;
	
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
			
			/*if (i==word.length()-1 && n.word==true) //extra condition of if stem-last letter is a word
			{
				return true;
			}*/
			
			
		}
		/*if (n.word==false) //if stem + e is a word, then pass condition 1
		{
			if (n.getChild('e')!=null)
			{
				//System.out.println("e test:" + word);
				return n.getChild('e').word;
			}
		}*/
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
	 * helper method that gets rid of morphemes with negative scores
	 * also delete morpheme "e" since that is an unwanted suffix from the rule stem+e is rule, since this incorrectly adds "e" as suffix
	 */
	public void deleteNegative(LinkedList<Morpheme> list)
	{
		for (Morpheme  m: list)
		{
			//System.out.println(m.morpheme);
			if (m.point<0 || m.morpheme.equals("e")){ 
				//if (m.morpheme.equals(("s")))System.out.println("SSS");
				list.remove(m);
				deleteNegative(list); //prevent concurrent modification of list being iterated through
				return;
			}
			
		}
	}
	
	
	/*
	 * return a list of possible morphemes as suffix (use forward tree)
	 */
	public LinkedList<Morpheme> suffix(LinkedList<String> inputs)
	{
		boolean valid;
		boolean removeFirstSuffix;
		Morpheme newsuffix;
		LinkedList<Morpheme> list = new LinkedList<Morpheme>();
		for (String s: inputs) //for all the strings read in
		{
			int length = s.length();
			for (int i = length-1; i>=2;i--) //continuously increase size of possible "suffix" to test
			{
				removeFirstSuffix = false;
				valid = false;
				String suffix = s.substring(i, length);
				String rest = s.substring(0,i);
				//if (suffix.equals(("s")))System.out.println("S");
				if (suffix.charAt(0)==s.charAt(i-1)) //hop + ping, "ing" here is the suffix
				{
					//System.out.println(rest + suffix);
					removeFirstSuffix = true;
					//suffix = suffix.substring(1, suffix.length());
				}
				if (removeFirstSuffix) newsuffix = findMorpheme(list,suffix.substring(1, suffix.length())); //remove letter
				else newsuffix = findMorpheme(list,suffix);
				
				//if (suffix.equals(("s")))System.out.println(wordExist(fr,rest) + " "+ rest);
				if (wordExist(fr,rest)) //if condition 1 satisfied
				{
				//	if (suffix.equals(("s")))System.out.println(aroundOne(fr,rest) + " "+ rest);
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
					//System.out.println(suffix + ":" + rest);
					newsuffix.point = newsuffix.point+19;
					newsuffix.retrievedWords.add(s);
				}
				else //if not
				{
					newsuffix.point = newsuffix.point-1;
				}
				
				if (!newsuffix.contains(s)) newsuffix.words.add(s); 
				else continue;
				
			}
		}
		deleteNegative(list);
		pruning(list);
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
					newprefix.retrievedWords.add(s);
				}
				else //if not
				{
					newprefix.point = newprefix.point-1;
				}
				//newprefix.words.add(s);
				if (!newprefix.contains(s)) newprefix.words.add(s); 
				else continue;
			}
		}
		deleteNegative(list);
		pruning(list);
		return list;
	}
	
	/*
	 * helper pruning method
	 */
	private boolean pruned(LinkedList<Morpheme> list, Morpheme composite)
	{
		int pruned = 0;
		String temp = new String(composite.morpheme);
		for (Morpheme m: list)
		{
			if (temp.contains(m.morpheme) && m.point>=composite.point && !m.morpheme.equals(composite.morpheme))
			{
				temp.replace(m.morpheme, ""); //prune away a party
				pruned++;
			}
		}
		//System.out.println(pruned);
		return pruned==2;
	}
	
	/*
	 * prune away some morphemes
	 */
	public void pruning(LinkedList<Morpheme> list)
	{
		for (Morpheme m: list)
		{
			//System.out.println(m.morpheme);
			if (pruned(list,m))
			{	
				System.out.println("pruned");
				list.remove(m);
				pruning(list);
				return;
			}
		}
		
		return;
	}
}
