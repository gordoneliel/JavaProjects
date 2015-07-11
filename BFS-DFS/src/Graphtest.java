class Graphtest
{

	public static final int GRAPH_SIZE = 5;
	public static final double EDGE_PERCENT = 0.3;

	public static void DFS(Graph G, int Parent[], int startVertex,
			boolean Visited[])
	{
		// Fill me in!
		// Recursion is your friend here!
		Edge tmp;
		Visited[startVertex] = true;

		for (tmp = G.edges[startVertex]; tmp != null; tmp = tmp.next)
		{
			if (!Visited[tmp.neighbor])
			{
				Parent[tmp.neighbor] = startVertex;
				DFS(G, Parent, tmp.neighbor, Visited);
			}
		}
	}

	public static void printString()
	{
		System.out.println("\n");
	}

	public static void PrintPath(int Parent[], int endvertex)
	{
		// Fill me in!
		// Recursion is your friend here!
		// You may want to use a helper function, just to get the final
		// end-of-line to print out correctly.
		// As always, see me if you have any questions!

		if (Parent[endvertex] == -1)
		{
			System.out.print(endvertex);
			// printString();
			return;
		}

		PrintPath(Parent, Parent[endvertex]);
		System.out.print(endvertex);
	}

	public static void BFS(Graph G, int Parent[], int startVertex,
			boolean Visited[])
	{
		// Fill me in!
		// Recursion, not so much your friend
		// Feel free to use the provided Queue code (but be sure to submit
		// all code necessary for this file to compile and run!)

		Edge tmp;
		int nextV;
		ListQueue Q = new ListQueue();

		Q.enqueue(new Integer(startVertex));
		while (!Q.empty())
		{
			nextV = ((Integer) Q.dequeue()).intValue();
			if (!Visited[nextV])
			{
				Visited[nextV] = true;
				for (tmp = G.edges[nextV]; tmp != null; tmp = tmp.next)
				{
					Q.enqueue(new Integer(tmp.neighbor));
					Parent[tmp.neighbor] = startVertex;
				}
			}
		}
	}

	public static void main(String args[])
	{
		boolean Visited[] = new boolean[GRAPH_SIZE];
		int Parent[] = new int[GRAPH_SIZE];
		Graph G = new Graph(GRAPH_SIZE);
		int i;
		for (i = 0; i < G.numVertex; i++)
		{
			Visited[i] = false;
			Parent[i] = -1;
		}
		G.randomize(EDGE_PERCENT);
		G.print();
		BFS(G, Parent, 0, Visited);
		System.out.println("----------------");
		System.out.println("BFS:");
		System.out.println("----------------");
		for (i = 0; i < G.numVertex; i++)
		{
			System.out.println("Path from 0 to " + i + ":");
			PrintPath(Parent, i);
		}
		for (i = 0; i < G.numVertex; i++)
		{
			Visited[i] = false;
			Parent[i] = -1;
		}
		DFS(G, Parent, 0, Visited);
		System.out.println("----------------");
		System.out.println("DFS:");
		System.out.println("----------------");

		for (i = 0; i < G.numVertex; i++)
		{
			System.out.println("Path from 0 to " + i + ":");
			PrintPath(Parent, i);
		}
	}
}