import java.util.Random;
import java.util.Scanner;

public class Board {
	private int boardSize;
	public String[][] board;
	private int difficultyLevel;
	public String winner;
	private Boolean gameOver = false;
	public Boolean humanFirst = false;
	public int winRow = -1;
	public int winColumn = -1;
	public int totalCellsPlayed = 0;
	public int totalCells = boardSize * boardSize;
	public int mostCount = 0;
	public int moveToWin = 0;
	public int maxRow = 0;
	public int maxColumn = 0;
	
	public int getBoardSize() {
		return boardSize;
	}

	public Boolean getGameStatus() {
		return gameOver;
	}
	
	public void setBoardSize(int boardSize) {
		this.boardSize = boardSize;
	}
	
	public enum gameDifficulty {
		easy, medium, hard;
	}
	
	
	public int getDifficultyLevel() {
		return difficultyLevel;
	}

	public void setDifficultyLevel(int difficultyLevel) {
		this.difficultyLevel = difficultyLevel;
	}
	
	
	//set board to blank
	public void BoardInit() {
		int size = getBoardSize();
		board = new String[size][size];
		
		for (int row = 0; row < size; row++) {
			for (int column = 0; column < size; column++) {
				board[row][column] = " ";
			}
		}
	}
	
	public void checkTurn() {
		int difficulty = getDifficultyLevel();
		if (difficulty == 1) {
			//gameDifficulty gameDiff = gameDifficulty.easy;
			humanFirst = true;
		}
		else {
			Random rand = new Random();
			int turn = rand.nextInt(10) + 1;
			
			if (turn % 2 == 0) {
				humanFirst = true;
			}
		}
	}
		
	public void humanPlay() {
		int boardSize = getBoardSize();
		Boolean validInput = false;
		int row = 0, column = 0;
		while (!validInput) {
			System.out.println("Pick your move by inputing the cell row ranging between 1 and " + boardSize + " that has not been picked");
			try {
				Scanner rowInput = new Scanner(System.in);		
				row = Integer.parseInt(rowInput.nextLine());
				
				System.out.println("Pick your move by inputing the cell column ranging between 1 and " + boardSize + " that has not been picked");
				column = Integer.parseInt(rowInput.nextLine());
				if (row > boardSize || column > boardSize) {
					validInput = false;
					System.out.println("Please input a valid cell number ranging between 1 and " + boardSize);
				}
				else {
					if (board[row - 1][column - 1] != " ") {
						System.out.println("Cell is already occupied. Please pick another cell");
						validInput = false;
					}
					
					else {
						validInput = true;
					}
				}
				
				//rowInput.close();
			}
			catch (NumberFormatException ex){
				System.out.println("Please enter a valid number ranging between 1 and " + boardSize);
			}	
			
			}	
			if (board[row - 1][column - 1] == " ") {
				board[row - 1][column - 1] = "X";
				totalCellsPlayed++;
			}
		
	}
	
	public void computerPlay(gameDifficulty difficulty) {
		System.out.println("Here's the computer move: \n");
		if (difficulty == gameDifficulty.easy) {
			easyMove();
		}
		
		else if (difficulty == gameDifficulty.medium) {
			String robotSymbol = "O";
			boolean mediumSuccessful = mediumMove(robotSymbol);
			
			if (mediumSuccessful == false && winRow != -1 && winColumn != -1) {
				board[winRow][winColumn] = "O";
				winRow = -1;
				winColumn = -1;
				totalCellsPlayed++;
				checkWinner(gameDifficulty.medium, "O");
			}
			
			else if (mediumSuccessful == false) {
				easyMove();
			}
		}
		
		else if (difficulty == gameDifficulty.hard) {
			hardMove();
		}
		
		
		//humanFirst = true;
	}
	
	public void easyMove() {
		Random rand = new Random();
		boolean moveSuccessful = false;
		
		while (!moveSuccessful) {
			int column = rand.nextInt(boardSize);
			int row = rand.nextInt(boardSize);
			
			if (board[row][column] == " ") {
				board[row][column] = "O";
				totalCellsPlayed++;
				moveSuccessful = true;
			}
			checkWinner(gameDifficulty.easy, "O");
		}
		
		checkWinner(gameDifficulty.easy, "O");
		
	}
	
	public boolean mediumMove(String symbol) {
		boolean moveSuccessful = false;
		moveSuccessful = checkWinner(gameDifficulty.medium, symbol);
		
		return moveSuccessful;
	}
	
	public void hardMove() {
		boolean successfulMove = false;
		successfulMove = mediumMove("O");
		boolean madeAMove = false;
		//check if we have an empty cell that we can win
		//while (madeAMove == false) {
		if (successfulMove == false) {
			successfulMove = mediumMove("X");		//see if x can win so we can block it
			if (successfulMove == false && winRow != -1 && winColumn != -1) {
				if (board[winRow][winColumn] == " ") {
					board[winRow][winColumn] = "O";
					winRow = -1;
					winColumn = -1;
					totalCellsPlayed++;
					checkWinner(gameDifficulty.hard, "O");
					//madeAMove = true;
				}
			}
			else if (successfulMove == false) {		//if x can't win and we can't block them, pick the cell with most
				if (moveToWin > 0) {				//symbols and play from there
					if (moveToWin == 1) {
						pursuingRow();
						//madeAMove = true;
					}
					
					else if (moveToWin == 2) {
						pursuingColumn();
						//madeAMove = true;
					}
					
					else if (moveToWin == 3) {
						pursuingUpDiagonal();
						//madeAMove = true;
					}
					
					else if (moveToWin == 4){
						pursuingDownDiagonal();
						//madeAMove = true;
					}
					
				}
				
				else {
					easyMove();
					//madeAMove = true;
				}
			//}
			
		}
		}
		
		
	}
	
	public void pursuingRow() {
		for (int column = 0; column < boardSize; column++) {
			if (board[maxRow][column] == " ") {
				board[maxRow][column] = "O";
				totalCellsPlayed++;
				break;
			}
		}
	}
	
	public void pursuingColumn() {
		for (int row = 0; row < boardSize; row++) {
			if (board[row][maxColumn] == " ") {
				board[row][maxColumn] = "O";
				totalCellsPlayed++;
				break;
			}
		}
	}
	
	public void pursuingUpDiagonal() {
		for (int row = 0; row < boardSize; row++) {
			if (board[row][row] == " ") {
				board[row][row] = "O";
				totalCellsPlayed++;
				break;
			}
		}
	}
	
	public void pursuingDownDiagonal() {
		int column = 0;
		for (int row = boardSize - 1; row >= 0; row--) {
			if (board[column][row] == " ") {
				board[column][row] = "O";
				totalCellsPlayed++;
				break;
			}
		}
	}
	public boolean checkWinner(gameDifficulty difficulty, String symbol) {
		String row = "row", column = "column", upDiagonal = "upDiagonal", downDiagonal = "downDiagonal";
		boolean winnerRow = checkPosition(symbol, row);
		boolean winnerCol = checkPosition(symbol, column);
		boolean winnerDiagUp = checkPosition(symbol, upDiagonal);
		boolean winnerDiagDown = checkPosition(symbol, downDiagonal);
		
		if (winnerRow == true || winnerCol == true || winnerDiagUp || winnerDiagDown) {
			return true;
		}
		
		else {
			return false;
		}
	}
	
	public boolean checkPosition(String symbol, String placeCheck) {
		int winningRow = 0;
		int winningCol = 0;
		int counter = 0;
		int emptyPosition = 0;
		//int tempCount = 0;
		if (placeCheck == "row" || placeCheck == "column") {
			firstLoop:
			for (int row = 0; row < boardSize; row++) {
				counter = 0;
				emptyPosition = 0;
				
				for (int col = 0; col < boardSize; col++) {
					//row check
					if (placeCheck == "row") {
						if (board[row][col] == symbol) {
							counter++;
							if (counter == boardSize) {
								break firstLoop;
							}
						}
						else if (board[row][col] == " ") {
							emptyPosition++;
							winningRow = row;
							winningCol = col;
							
							if (emptyPosition > 1) {
								break;
							}
						}
						
						else {
							break;
						}
						
						if (counter > mostCount) {
							moveToWin = 1;
							maxRow = row;
						}
						mostCount = Math.max(mostCount, counter);
						
						
					}
					//column check
					if (placeCheck == "column") {
						if (board[col][row] == symbol) {
							counter++;
						}
						else if (board[col][row] == " ") {
							emptyPosition++;
							winningRow = row;
							winningCol = col;
							
							if (emptyPosition > 1) {
								break;
							}
						}
						
						else {
							break;
						}
						
						//keeping track of the row/column/diagonal w the most mark
						if (counter > mostCount) {
							moveToWin = 2;
							maxColumn = col;
						}
						mostCount = Math.max(mostCount, counter);
					}
					}
					
					markWinning(counter, emptyPosition, winningRow, winningCol, placeCheck);
					
					}
				}
				
				else {
					if (placeCheck == "downDiagonal") {
						secondLoop:
						for (int row = 0; row < boardSize; row++) {
							if (board[row][row] == symbol) {
								counter++;
								if (counter == boardSize) {
									break secondLoop;
								}
							}
							else if (board[row][row] == " ") {
								emptyPosition++;
								winningRow = row;
								winningCol = row;
								
								if (emptyPosition > 1) {
									break;
								}
							}
							
							else {
								break;
							}
							
							
						}
					//keeping track of the row/column/diagonal w the most mark
					if (counter > mostCount) {
						moveToWin = 3;
					}
					mostCount = Math.max(mostCount, counter);
					markWinning(counter, emptyPosition, winningRow, winningCol, placeCheck); 

					}
					
					else {
						int col = 0;
						thirdLoop:
						for (int row = boardSize - 1; row >= 0; row--) {
							if (board[col][row] == symbol) {
								counter++;
								if (counter == boardSize) {
									break thirdLoop;
								}
							}
							else if (board[col][row] == " ") {
								emptyPosition++;
								winningRow = row;
								winningCol = col;
								
								if (emptyPosition > 1) {
									break;
								}
							}
							
							else {
								break;
							}
							col++;
						}
						
						//keeping track of the row/column/diagonal w the most mark
						if (counter > mostCount) {
							moveToWin = 4;
						}
						mostCount = Math.max(mostCount, counter);
						markWinning(counter, emptyPosition, winningRow, winningCol, placeCheck);

					}
			}
			
				if (counter == boardSize && symbol == "X") {
					winner = "Human";
					return true;
				}
				
				if (counter == boardSize && symbol == "O") {
					winner = "Computer";
					return true;
				}
			
		return false;
		
	}
	
	//this function is to mark the winningPosition when iterating through the board
	public void markWinning(int symbolCounter, int emptyPosition, int row, int column, String placeCheck) {
		if (symbolCounter == boardSize - 1 && emptyPosition == 1) {
			if (placeCheck == "row") {
				winRow = row;
				winColumn = column;
			}
			
			else if (placeCheck == "column") {
				winRow = column;
				winColumn = row;
			}
			
			else if (placeCheck == "downDiagonal") {
				winRow = row;
				winColumn = column;
			}
			
			else {
				winRow = column;
				winColumn = row;
			}
		}
	}
	
	public void announceWinner() {
		if (winner == "Human") {
			System.out.println("Congratulations you won!");
			System.exit(0);
		}
		
		else {
			System.out.println("You're dead!");
			System.exit(0);
		}
		
	}
	
	public void tieChecker() {
		if (totalCellsPlayed == (boardSize * boardSize)) {
			System.out.println("There's a tie, no one won");
			System.exit(0);
		}
		
	}
	
	public void play() {
		checkTurn();
		gameDifficulty difficulty = null;
		String humanSymbol = "X";
		String computerSymbol = "O";
		//boolean computerPlay = false;
		//int totalCells = (int) Math.pow(boardSize, 2);
		if (gameOver == false) {
			if (difficultyLevel == 1) {
				difficulty = gameDifficulty.easy;
			}
			if (difficultyLevel == 2) {
				difficulty = gameDifficulty.medium;
			}
			
			else if (difficultyLevel == 3) {
				difficulty = gameDifficulty.hard;
			}
		}
		
		while (gameOver == false) {
			if (humanFirst) {
				//computerPlay = true;
				humanPlay();
				printBoard();
				boolean isWin = checkWinner(difficulty, humanSymbol);
				if (isWin == true) {
					gameOver = true;
					announceWinner();
				}
				tieChecker();
				
				if (gameOver == false) {
					computerPlay(difficulty);
					printBoard();
					
					boolean computerWin = checkWinner(difficulty, computerSymbol);
					if (computerWin == true) {
						gameOver = true;
						announceWinner();
					}
					tieChecker();
					//computerPlay = false;
				}
				
			}
			
			else {
				computerPlay(difficulty);
				printBoard();
				//tieChecker();

				boolean computerWin = checkWinner(difficulty, computerSymbol);
				if (computerWin == true) {
					gameOver = true;
					announceWinner();
				}
				tieChecker();
				if (gameOver == false) {
					humanPlay();
					printBoard();
					tieChecker();
					boolean isWin = checkWinner(difficulty, humanSymbol);
					if (isWin == true) {
						gameOver = true;
						announceWinner();
					}
					tieChecker();
				}
			}
		}
		
	}
	
	public void printBoard() {
		System.out.print("  ");

		for (int i = 1; i <= boardSize; i++) {
			System.out.print(i + " ");
		}
		System.out.println();


		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (j == 0 && i == 0) {
					System.out.print(" ");
					for (int k = 0; k < boardSize; k++) {
						System.out.print("+-");
					}
					System.out.println("+");
				}

				if (j == 0) {
					System.out.print(i + 1);
				}
				
				System.out.print("|" + board[i][j]);

			}
			System.out.println("|");
			System.out.print(" ");
			for (int k = 0; k < boardSize; k++) {
				System.out.print("+-");
			}
			System.out.println("+");

		
	}
	}

	
	
}
