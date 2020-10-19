package chess;

import boardgame.Board;
import boardgame.Piece;

/** =============== COMMENTS =============== **/
/* 1.1: We won't declare the setter 'setColor()' because we don't want the color of the piece
 * to be altered in any way. */
/** ======================================== **/

public class ChessPiece extends Piece {

	// Attributes
	private Color color;

	// Constructor
	public ChessPiece(Board board, Color color) {
		super(board);
		this.color = color;
	}

	// Getters e Setters
	public Color getColor() { // 1.1*
		return color;
	}
}
