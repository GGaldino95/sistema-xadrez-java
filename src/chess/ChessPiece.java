package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;

/** =============== COMMENTS =============== **/
/* 1.1: We won't declare the setter 'setColor()' because we don't want the color of the piece
 * to be altered in any way. */
/** ======================================== **/

public abstract class ChessPiece extends Piece {

	// Attributes
	private Color color;
	private int moveCount;
	

	// Constructor
	public ChessPiece(Board board, Color color) {
		super(board);
		this.color = color;
	}

	// Getters e Setters
	public Color getColor() { // 1.1*
		return color;
	}
	
	public int getMoveCount() {
		return moveCount;
	}
	
	public ChessPosition getChessPosition() {
		return ChessPosition.fromPosition(position);
	}
	
	// Methods
	protected boolean isThereOpponentPiece(Position position) {
		ChessPiece p = (ChessPiece)getBoard().piece(position); // Downcasting
		return p != null && p.getColor() != color;
	}
	
	public void increaseMoveCount() {
		moveCount++;
	}
	
	public void decreaseMoveCount() {
		moveCount--;
	}
}
