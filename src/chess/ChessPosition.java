package chess;

import boardgame.Position;

/** =============== COMMENTS =============== **/
/* 1.1: We won't declare the setter 'setRow()' because we don't want the row coordinate to be 
 * altered in any way. */

/* 1.2: We won't declare the setter 'setColumn()' because we don't want the column coordinate to be 
 * altered in any way. */

/* 1.3: We use the empty String "" to force the compiler to concatenate the row and column. */
/** ======================================== **/

public class ChessPosition {
	
	// Attributes
	private char column;
	private int row;
	
	// Constructor
	public ChessPosition(char column, int row) {
		if ((column < 'a' || column > 'h') || (row < 1 || row > 8))
			throw new ChessException("Error instantiating ChessPosition. Valid values are from A1 to H8.");
		
		this.column = column;
		this.row = row;
	}

	// Getters and Setters
	public char getColumn() { // 1.1*
		return column;
	}

	public int getRow() { // 1.2*
		return row;
	}
	
	// Methods
	protected Position toPosition() {
		return new Position(8 - row, column - 'a');
	}
	
	protected static ChessPosition fromPosition(Position position) {
		return new ChessPosition((char)('a' + position.getColumn()), 8 - position.getRow()); 
	}
	
	// toString
	@Override
	public String toString() {
		return "" + column + row; // 1.3*
	}
}
