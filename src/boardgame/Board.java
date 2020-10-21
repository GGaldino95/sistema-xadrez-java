package boardgame;

/** =============== COMMENTS =============== **/
/* 1.1: We won't declare the setter 'setRows()' because we don't want the number of rows to be 
 * altered in any way. */

/* 1.2: We won't declare the setter 'setColumns()' because we don't want the number of columns to be 
 * altered in any way. */
/** ======================================== **/

public class Board {

	// Attributes
	private int rows;
	private int columns;
	private Piece[][] pieces;
	
	// Constructor
	public Board(int rows, int columns) {
		if (rows < 1 || columns < 1)
			throw new BoardException("Error creating board: there must be at least 1 row and 1 column");
		
		this.rows = rows;
		this.columns = columns;
		pieces = new Piece[rows][columns];
	}

	// Getters e Setters
	public int getRows() { // 1.1*
		return rows;
	}

	public int getColumns() { // 1.2*
		return columns;
	}
	
	// Piece Methods
	public Piece piece(int row, int column) {
		if (!positionExists(row, column))
			throw new BoardException("Position not on the board.");
		
		return pieces[row][column];
	}
	
	public Piece piece(Position position) { // Overload
		if (!positionExists(position))
			throw new BoardException("Position not on the board.");
		
		return pieces[position.getRow()][position.getColumn()];
	}
	
	public void placePiece(Piece piece, Position position) {
		if (thereIsAPiece(position))
			throw new BoardException("There is already a piece on position " + position);
		
		pieces[position.getRow()][position.getColumn()] = piece;
		piece.position = position;
	}
	
	public Piece removePiece(Position position) {
		if (!positionExists(position))
			throw new BoardException("Position not on the board.");
		
		if (piece(position) == null)
			return null;
		
		Piece aux = piece(position);
		aux.position = null;
		pieces[position.getRow()][position.getColumn()] = null;
		return aux;
	}
	
	public boolean thereIsAPiece(Position position) {
		if (!positionExists(position))
			throw new BoardException("Position not on the board.");
		
		return piece(position) != null;
	}
	
	// Position Methods
	private boolean positionExists(int row, int column) {
		return (row >= 0 && row < rows) && (column >= 0 && column < columns);
	}
	
	public boolean positionExists(Position position) { // Overload
		return positionExists(position.getRow(), position.getColumn());
	}
	
	
}
