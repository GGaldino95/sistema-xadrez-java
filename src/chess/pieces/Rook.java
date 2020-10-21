package chess.pieces;

import boardgame.Board;
import chess.ChessPiece;
import chess.Color;

/** =============== COMMENTS =============== **/
/* 1.1: We created a boolean matrix with the same dimensions of the board. */
/** ======================================== **/

public class Rook extends ChessPiece {
	
	// Constructor
	public Rook(Board board, Color color) {
		super(board, color);
	}

	// Methods
	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()]; // 1.1*
		return mat;
	}
	
	// toString
	@Override
	public String toString() {
		return "R";
	}
}
