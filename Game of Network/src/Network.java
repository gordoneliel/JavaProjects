import java.util.Scanner;

public class Network
{
	private int numPlayers;

	public Network(int numPlayers)
	{
		this.numPlayers = numPlayers;
	}

	public void playNetwork(Player player1, Player player2)
	{
		while (true)
		{
			Move player1move1 = player1.getMove();
			player2.OpponentMove(player1move1);

			
			player1.printBoard();
			
			Move player2move1 = player2.getMove();
			player1.OpponentMove(player2move1);
			

			player2.printBoard();

		}

	}

	public static void main(String[] args)
	{
		Scanner scanner = SingletonScanner.getInstance();
		Network network = new Network(-1);

		while (network.numPlayers == -1)
		{
			System.out.println("Enter number of players << 0, 1, or 2 >>: ");
			network.numPlayers = scanner.nextInt();

			ElielComputerPlayer comPlayer1;
			ElielComputerPlayer comPlayer2;
			HumanPlayer humanPlayer1;
			HumanPlayer humanPlayer2;

			switch (network.numPlayers)
			{
			case 0:
				comPlayer1 = new ElielComputerPlayer(1);
				comPlayer2 = new ElielComputerPlayer(2);
				network.playNetwork(comPlayer1, comPlayer2);
				break;
			case 1:
				humanPlayer1 = new HumanPlayer(1);
				comPlayer1 = new ElielComputerPlayer(2);
				network.playNetwork(humanPlayer1, comPlayer1);
				break;
			case 2:
				humanPlayer1 = new HumanPlayer(1);
				humanPlayer2 = new HumanPlayer(2);
				network.playNetwork(humanPlayer1, humanPlayer2);
				break;
			default:
				System.out.println("Please enter a valid number of players << 0, 1 or 2 >>");
			}
		}
	}
}
