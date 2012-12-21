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
	
	static LinkedList<Morpheme> actualPrefix = new LinkedList<Morpheme>();
	static LinkedList<Morpheme> actualSuffix = new LinkedList<Morpheme>();
	
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
		int state = 0; //0= expecting prefix or suffix morpheme symbol (+/-), 1 = reading in actual morpheme, 2 = reading in words
		int list = 0; //0 = prefix, 1 = suffix, 2 = no morpheme
		Morpheme m = null;
		try {
			br = new BufferedReader(new FileReader(filename));
			String line;
			//for each word read
			while((line = br.readLine())!=null)
			{
				
				if (state==1) //add morpheme answer to proper list, and fix which list to add answer to 
				{
					if (list == 0) actualPrefix.add(m =new Morpheme(new StringBuffer(line).reverse().toString()));
					else if (list==1) actualSuffix.add(m =new Morpheme(line));
					state = 2;
				}
				
				else if (state==2)
				{
					if (line.equals("+") ||line.equals("_")||line.equals("-")) //new morpheme
					{
						state = 0;
					}
					else //normal inputs
					{
						//if (list!=2) m.words.add(line); //adding word to answer key morpheme
						if (list==0) m.words.add(new StringBuffer(line).reverse().toString());
						else if (list==1) m.words.add(line);	
						if (uniqueWord(line)) //normal adding of words
						{
							words.add(line);
							forwardList.add(line);
							//backwardList.add(line);
							parseStatistics(line);
							backwardList.add(new StringBuffer(line).reverse().toString());
						}
					}
				}
				if (state==0) //prefix or suffix?
				{
					if (line.equals("+")) list = 0;
					else if (line.equals("-")) list = 1;
					else list = 2;
					
					state = 1;
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
	
	private static Morpheme contains (LinkedList<Morpheme> list, String s)
	{
		Morpheme m = null;
		for (Morpheme morpheme: list)
		{
			if (morpheme.morpheme.equals(s)) 
			{
				m = morpheme;
				break;
			}
		}
		return m;
	}
	
	private static int union (LinkedList<Morpheme> answer, LinkedList<Morpheme> derived)
	{
		int similar = 0;
		Morpheme found=null;
		for (Morpheme a: answer)
		{
			if ((found=contains(derived,a.morpheme))!=null)
			{
				for (String s: a.words)
				{
					if (found.contains(s)) similar++;
				}
			}
		}
		return similar;
	}
	
	private static int totalSize(LinkedList<Morpheme> list)
	{
		int size = 0;
		for (Morpheme m: list)
		{
			size = size + m.words.size();
		}
		return size;
	}
	
	private static void printList(LinkedList<Morpheme> list, String label, PrintWriter out, boolean reverse, LinkedList<Morpheme> answer)
	{
		out.println(label);
		out.println("_________________");
		//print out prefix
		if (reverse)
		{
			for (Morpheme m: list)
			{
				Morpheme a = contains(answer,m.morpheme);
				out.print(new StringBuffer(m.morpheme).reverse().toString() + ":");
				out.println(m.point);
				out.print("RETRIVED: ");
				for (String s: m.words)
				{
					out.print(new StringBuffer(s).reverse().toString()+",");
				}
				out.println();
				out.print("RELEVANT: ");
				if (a!=null)
				{
					for (String s: a.words)
					{
						out.print(new StringBuffer(s).reverse().toString()+ ",");
					}
				}
				out.println();
				
				/*out.print(new StringBuffer(m.morpheme).reverse().toString() + ":");
				out.print(m.point+" (");
				for (String s: m.words)
				{
					out.print(new StringBuffer(s).reverse().toString()+",");
				}
				out.println(")");*/
			}
		}
		else
		{
			for (Morpheme m: list)
			{
				Morpheme a = contains(answer,m.morpheme);
				out.print(m.morpheme + ":");
				out.println(m.point);
				out.print("RETRIVED: ");
				for (String s: m.words)
				{
					out.print(s+",");
				}
				out.println();
				out.print("RELEVANT: ");
				if (a!=null)
				{
					for (String s: a.words)
					{
						out.print(s+ ",");
					}
				}
				out.println();
			}
		}
		
		//out.println();
	}
	
	public static void main(String[] args)
	{
		filename = args[0];
		TreeAnalyzer t = new TreeAnalyzer(forward,backward);
		readFile();
		prefix = t.prefix(backwardList);
		suffix = t.suffix(forwardList);
		int unionP = union(actualPrefix,prefix); 
		int unionS = union(actualSuffix,suffix);
		try { //output to file
			PrintWriter out = new PrintWriter(new FileWriter("outputfile.txt"));
			
			printList(prefix,"PREFIX",out,true, actualPrefix);
			out.print("PRECISION: ");
			out.println((double)((double)unionP/(double)totalSize(prefix)));
			out.print("RECALL: ");
			out.println((double)((double)unionP/(double)totalSize(actualPrefix)));
			
			
			out.println();
			printList(suffix,"SUFFIX",out,false, actualSuffix);
			out.print("PRECISION: ");
			out.println((double)((double)unionS/(double)totalSize(suffix)));
			out.print("RECALL: ");
			out.println((double)((double)unionS/(double)totalSize(actualSuffix)));
			
			out.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		
	}
	
}
