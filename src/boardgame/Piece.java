package boardgame;

/** =============== COMMENTS =============== **/
/* 1.1: We don't have the Position parameter in this constructor because the position of the 
 * newly-created Piece will be null, meaning that the Piece isn't placed on the board yet. */

/* 1.2: You don't need to do that line if you don't want to. Java sets the 'position' as null 
 * by default. */

/* 1.3: We won't declare the setter 'setBoard()' because we don't want the board to be altered
 * in any way. */

/* 1.4: We will set 'getBoard' as 'protected' so that only subClasses or Classes on the same 
 * package can access it. */

/* 1.5: This is a 'Hook Method', where a concrete method calls a possible implementation from
 * a concrete subClass through the abstract method implementation of the superClass. */

/* 1.6: We're using 'mat.length()' on the 'for' condition because we'll assume that the matrix 
 * is a square. */
/** ======================================== **/

public abstract class Piece {

	// Attributes
	protected Position position;
	private Board board;
	
	// Constructor
	public Piece(Board board) { // 1.1*
		this.board = board;
		position = null; // 1.2*
	}

	// Getters and Setters
	protected Board getBoard() { // 1.3*, 1.4*
		return board;
	}
	
	// Methods
	public abstract boolean[][] possibleMoves();
	
	public boolean possibleMove(Position position) { // 1.5*
		return possibleMoves()[position.getRow()][position.getColumn()]; 
	}
	
	public boolean isThereAnyPossibleMove() { // 1.5*
		boolean[][] mat = possibleMoves();
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat.length; j++) { // 1.6*
				if (mat[i][j]) 
					return true;
			}
		}
		
		return false;
	}
	
}