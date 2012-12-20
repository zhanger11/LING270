import java.util.*;
public class TreeAnalyzer {
	TreeRoot fr;
	TreeRoot br;
	public TreeAnalyzer(TreeRoot forward, TreeRoot backward)
	{
		fr = forward;
		br = backward;
	}
	
	/*
	 * return a list of possible morphemes as suffix (use forward tree)
	 */
	public LinkedList<Morpheme> suffix(LinkedList<String> inputs)
	{
		LinkedList<Morpheme> list = new LinkedList<Morpheme>();
		return null;
	}
	
	/*
	 * return a list of possible morphenes as prefix (use backward tree)
	 */
	public LinkedList<Morpheme> prefix(LinkedList<String> inputs)
	{
		LinkedList<Morpheme> list = new LinkedList<Morpheme>();
		return null;
	}
}
