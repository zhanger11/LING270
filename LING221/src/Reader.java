import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Reader {
	static String filename;
	static LinkedList<String> words = new LinkedList<String>();
	static LinkedList<String> forwardList = new LinkedList<String>();
	static LinkedList<String> backwardList = new LinkedList<String>();
	static TreeRoot forward = new TreeRoot();
	static TreeRoot backward = new TreeRoot();
	static LinkedList<Morpheme> prefix;
	static LinkedList<Morpheme> suffix;
	//reader
	static BufferedReader br;
	
	public static boolean uniqueWord(String s)
	{
		for (String word: words)
		{
			if (word.equals((s))) 
			{
				return false;
			}
		}
		return true;
	}
	
	/*
	 * reads basic reading the file and creating a linkedlist out of all of the words, and parse them for frequency statistics
	 */
	public static void readFile()
	{
		try {
			br = new BufferedReader(new FileReader(filename));
			String line;
			//for each word read
			while((line = br.readLine())!=null)
			{
				if (uniqueWord(line))
				{
					words.add(line);
					forwardList.add(line);
					//backwardList.add(line);
					parseStatistics(line);
					backwardList.add(new StringBuffer(line).reverse().toString());
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	/*
	 * Does statistics update for each string
	 */
	public static void parseStatistics(String s)
	{
		int length = s.length();
		Node currentF = null;
		Node currentB = null;
		//loop through the string's characters
		for (int i = 0;i<length;i++)
		{ 
				if (currentF==null && currentB==null) //if we are still at the root
				{
					currentF=forward.addChild(s.charAt(i));
					currentB=backward.addChild(s.charAt(s.length()-1-i));
				}
				else
				{
					currentF = currentF.addChild(s.charAt(i));
					currentB = currentB.addChild(s.charAt(s.length()-1-i));
				}
				
				if (i==length-1)
				{
					currentF.isWord();
					currentB.isWord();
				}
		}
	}
	
	public static void main(String[] args)
	{
		filename = args[0];
		TreeAnalyzer t = new TreeAnalyzer(forward,backward);
		readFile();
		prefix = t.prefix(backwardList);
		suffix = t.suffix(forwardList);
		
		try { //output to file
			PrintWriter out = new PrintWriter(new FileWriter("outputfile.txt"));
			out.println("PREFIX");
			out.println("_________________");
			//print out prefix
			for (Morpheme m: prefix)
			{
				out.print(new StringBuffer(m.morpheme).reverse().toString() + ":");
				out.print(m.point+" (");
				for (String s: m.words)
				{
					out.print(s+",");
				}
				out.println(")");
			}
			out.println();
			out.println("SUFFIX");
			out.println("_________________");
			for (Morpheme m: suffix)
			{
				out.print(m.morpheme + ":");
				out.print(m.point+" (");
				for (String s: m.words)
				{
					out.print(s+",");
				}
				out.println(")");
			}

			out.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		
	}
	
}
