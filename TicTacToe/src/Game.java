import java.util.Scanner;

public class Game {
	public static void main(String[] args) {
		Board boardGame = new Board();

			System.out.println("Please enter difficulty level: easy, medium, or hard? \n 1 for easy, 2 for medium, 3 for hard");
			int boardSize;
			int difficulty;
			Scanner input = new Scanner(System.in);
			difficulty = input.nextInt();
			
			while (difficulty > 3 || difficulty < 0) {
				System.out.println("Not a valid difficulty");
				System.out.println("Please enter difficulty level: easy, medium, or hard? \n 1 for easy, 2 for medium, 3 for hard");
				difficulty = input.nextInt();
			}
			
			//Board boardGame = new Board();
			boardGame.setDifficultyLevel(difficulty);
			
			System.out.println("Please enter the board size from 1 to 10: \n");
			boardSize = input.nextInt();
			
			boardGame.setBoardSize(boardSize);
			boardGame.BoardInit();
			boardGame.printBoard();
			
			//System.out.println("Please input the row that you want to play");
			
			boardGame.play();
			input.close();
						
	}
}