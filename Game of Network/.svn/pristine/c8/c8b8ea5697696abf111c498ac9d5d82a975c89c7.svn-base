import java.util.Enumeration;
import java.util.Random;
import java.util.Vector;

public class ElielComputerPlayer extends Player
{
	Enumeration player1Rows;
	Vector rowValues = new Vector();
	
	public enum player2Rows {B, C, D, E, F, G};
	private final int MAX = 8;
	ElielBoard boardGrid;
	int[][] validMoves;
	
	public ElielComputerPlayer(int playerNum)
	{
		super(playerNum);
		boardGrid = new ElielBoard();
		validMoves = new int[MAX][MAX];
		rowValues.add('A');
		rowValues.add('B');
		rowValues.add('C');
		rowValues.add('D');
		rowValues.add('E');
		rowValues.add('F');
		rowValues.add('G');
		rowValues.add('H');
		player1Rows = rowValues.elements();
	}

	@Override
	public Move getMove()
	{
		char row = 'A';
		int col = 0;
		Random r = new Random();
		Move move = null;

		do
		{
			if (playerNum == 1)
			{
				String possibleRows = "ABCDEFGH";
				row = (char) player1Rows.nextElement();
				//row = possibleRows.charAt(r.nextInt(possibleRows.length()));
				col = 2 + (int) (Math.random() * ((7 - 2) + 1));
				move = new Move(row, col);
			} 
			if(playerNum == 2)
			{
				String possibleRows = "BCDEFG";
				//row = (char) player1Rows.
				row = possibleRows.charAt(r.nextInt(possibleRows.length()));
				col = 1 + (int) (Math.random() * ((8 - 1) + 1));
				move = new Move(row, col);
			}

		} while(boardGrid.checkValidMove(playerNum, move) != null);
		

		return move;
	}

	@Override
	public void OpponentMove(Move m)
	{
		if(boardGrid.checkValidMove(playerNum, m) != null)
		{
			return;
		}
		else
		{
			boardGrid.placePiece(m, playerNum);
		}		
	}

	public void printBoard()
	{
		boardGrid.printBoard();
	}

}
