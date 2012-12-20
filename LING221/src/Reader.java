import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;

public class Reader {
	static String filename = "data.txt";
	static LinkedList<String> words = new LinkedList<String>();
	static LinkedList<String> forwardList = new LinkedList<String>();
	static LinkedList<String> backwardList = new LinkedList<String>();
	static TreeRoot forward = new TreeRoot();
	static TreeRoot backward = new TreeRoot();	
	//reader
	static BufferedReader br;
	
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
				words.add(line);
				forwardList.add(line);
				backwardList.add(line);
				parseStatistics(line);
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
		}
	}
	
	public static void main(String[] args)
	{
		readFile();
		forward.treeTravel();
		
	}
	
}
