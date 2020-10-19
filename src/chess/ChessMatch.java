package chess;

import boardgame.Board;

/** =============== COMMENTS =============== **/
/* 1.1: The Class that needs to know the dimensions of the 'Board' is 'ChessMatch'. */

/* 1.2: 'getPieces' is a ChessPiece[][] type instead of Piece[][] (located on 'Board' class)
 * because we're on the Chess layer, and the Interface will only be able to read the
 * ChessPiece[][] type. (Layer-programming) */
/** ======================================== **/

public class ChessMatch {

	private Board board;
	
	public ChessMatch() { // 1.1*
		board = new Board(8, 8);
	}
	
	public ChessPiece[][] getPieces() { // 1.2*
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i, j);
			}
		}
		
		return mat;
	}
	
}
