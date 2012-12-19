import java.util.*;

public class Node {
	LinkedList<Node> children;
	char character;
	int count;
	
	public Node (char c)
	{
		character = c;
		children = new LinkedList<Node>();
		count = 1;
	}
	
	public void incrementCount()
	{
		count = count+1;
	}
	
	public int getCount()
	{
		return count;
	}
	
	public char getChar()
	{
		return character;
	}
	
	/*
	 * returns the node that contains the character c, null if otherwise
	 */
	public Node getChild(char c)
	{
		Object[] child = children.toArray();
		for (int i = 0; i< child.length;i++)
		{
			if (((Node)child[i]).getChar()==c) return (Node)child[i];
		}
		
		return null;
	}
	
	public Node addChild(char c)
	{
		Node n = getChild(c);
		if (n==null) //if new
		{
			Node newNode = new Node(c);
			this.children.addLast(newNode);
			return newNode;
		}
		//else just increment that node
		n.incrementCount();
		return n;
	}
	
}
