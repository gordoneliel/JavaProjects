public class CheckBoard
{
	private final static int MAX = 8;
	private static int[][] visited = new int[MAX][MAX];
	static int clusterCount = 0;

	/**
	 * Checks for a valid move in a Network Board
	 * If a piece has already been placed there, placing a piece at that location
	 * would form a cluster of 3 or more pieces, the location is in
	 * opponent's goal, the location is in corner, move would create network
	 * for both players
	 */
	public static boolean validMove(int playerNum, Move move, int[][] boardGrid)
	{
		int row = move.toRow - 'A';
		int col = move.toCol - 1;
		System.out.println("Row: " + row + " Col: " + col);
		System.out.println("Board In aValid: " + boardGrid[row][col]);
		System.out.println("ClusterCount: " + clusterCount);
		
		
		if (boardGrid[row][col] != 0)
		{
			System.out.println("Another player's piece is in that position.");
			return false;
		}
		
		if (!checkMove(row, col, playerNum, move, boardGrid)) return false;
		
		// check if in other players goal
		if (playerNum == 1)
		{
			if (move.toCol == 1 || move.toCol == MAX)
			{
				System.out.println("Sorry, your piece lands in the opponents goal.");
				return false;
			}
		} 
		else
		{
			if (move.toRow == 'A' || move.toRow == 'H')
			{
				System.out.println("Sorry, your piece lands in the opponents goal.");
				return false;
			}
		}
		// check if corner case
		if (move.toCol == 1 || move.toCol == MAX)
		{
			if (move.toRow == 'A' || move.toRow == 'H')
			{
				System.out.println("Sorry your piece lands the corner of the board.");
				return false;
			}
		}
//		for (int i = 0; i < visited.length; i++)
//		{
//			for (int j = 0; j < visited.length; j++)
//			{
//				visited[i][j] = 0;
//			}
//		}

		clusterCount = 0;
		// Check for clustering
		if(isCluster(row, col, playerNum, move, boardGrid) >= 3)			
		{
			System.out.println("Move Creates a cluster");
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if a player has placed a piece
	 * @param row
	 * @param col
	 * @param playerNum
	 * @param move
	 * @param boardGrid
	 * @return
	 */
	private static boolean checkMove(int row, int col, int playerNum, Move move, int[][] boardGrid)
	{
		if ((move.moveType != Move.MoveType.PLACE_PIECE))
		{
			if (boardGrid[row][col] != playerNum)
			{
				System.out.println();
				return false;
			}
		}
		return true;
		
	}
	
	/**
	 * Checks if a move creates a cluster of 3
	 * @return
	 */
	public static int isCluster(int row, int col, int playerNum, Move move, int[][] boardGrid)
	{		
		// Base Case
		if(row == 0 || col == 0  || row == MAX || col == MAX)
		{
			return clusterCount;
		}
		
		System.out.println("BoardGrid of: " + "( " + row +", " + col + " ): "+ boardGrid[row][col] + " Player:" + playerNum);
		if(visited[row][col] != 1 && boardGrid[row][col] == playerNum)
		{
			System.out.println("here");
			clusterCount++;
			visited[row][col] = 1;
			isCluster(row - 1, col - 1, playerNum, move, boardGrid);//
			isCluster(row - 1, col, playerNum, move, boardGrid);//
			isCluster(row - 1, col + 1, playerNum, move, boardGrid);//
			
			isCluster(row, col - 1, playerNum, move, boardGrid);//
			isCluster(row, col + 1, playerNum, move, boardGrid);//
			
			isCluster(row + 1, col, playerNum, move, boardGrid);
			isCluster(row + 1, col - 1, playerNum, move, boardGrid);
			isCluster(row + 1, col + 1, playerNum, move, boardGrid);
		}
		
		return clusterCount;
		
		
//		for (int i = row - 1; i < row + 1 && i < MAX; i++)
//		{
//			for (int j = col - 1; j < col + 1 && j < MAX; j++)
//			{
//				if(i == - 1)
//				{
//					i = 1;
//				}
//				if(j == -1)
//				{
//					j = 1;
//				}
//				clusterCount++;
//				System.out.println(playerNum + " " + boardGrid[i][j]);
//				if(playerNum == 1 && boardGrid[i][j] == 1)
//				{
//
//					clusterCount += helper(i, j, boardGrid);
//				}
//				if(playerNum == 2 && boardGrid[i][j] == 2)
//				{
//					clusterCount += helper(i, j, boardGrid);
//				}
//				
//			}
//		}
//		
//		return clusterCount;
		
//		System.out.println("ClusterCount: " + clusterCount);
//		if(clusterCount >= 3)
//		{
//			return true;
//		}
//		else
//		{
//			return false;
//		}
		
		// check row before, row and row after - same for colmn 
		// - check for edge cases
		//  count neighbors
//		return true;
	}
	
	private static int helper(int row, int col, int[][] boardGrid)
	{
		int count = 0;
		for (int i = row - 1; i < row + 1 && i < MAX; i++)
		{
			for (int j = col - 1; j < col + 1 && j < MAX; j++)
			{
				if(i == - 1)
				{
					i = 1;
				}
				if(j == -1)
				{
					j = 1;
				}
				if(boardGrid[i][j] == 1 && visited[i][j] != 1)
				{
					visited[i][j] = 1;
					count++;
				}
				if(boardGrid[i][j] == 2 && visited[i][j] != 1)
				{
					visited[i][j] = 1;
					count++;
				}
			}
		}
		return count;
	}
	/**
	 * Checks if a player has made a valid network
	 * @return
	 */
	public static boolean isNetwork()
	{
		for (int rowitr = -1; rowitr <= 1; rowitr++)
		{
			for (int colitr = -1; colitr <= 1; colitr++)
			{
				if (rowitr != 0 || colitr != 0)
				{
					//
//					while (en.hasMoreElements())
//					{
//						type type = (type) en.nextElement();	
//					}
				}
			}
		}
		return true;
	}
}
