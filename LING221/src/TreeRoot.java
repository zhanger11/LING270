import java.util.*;

public class TreeRoot{
	LinkedList<Node> children;

	public TreeRoot()
	{
		children = new LinkedList<Node>();
	}
	
	/*
	 * goes through each node of the tree and print each node out, uses breadth first search
	 */
	public void treeTravel()
	{
		LinkedList<Node> q= new LinkedList<Node>();
		for (Node n: children) //add all initial elements to this new list
		{
			q.add(n);
		}
		
		while (!q.isEmpty())
		{
			Node n= q.pop();
			System.out.print(n.getChar());
			System.out.println(n.getCount());
			for (Node child: n.children)
			{
				q.add(child);
			}
		}
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
