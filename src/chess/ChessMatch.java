package chess;

import boardgame.Board;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

/** =============== COMMENTS =============== **/
/* 1.1: The Class that needs to know the dimensions of the 'Board' is 'ChessMatch'. */

/* 1.2: 'getPieces' is a ChessPiece[][] type instead of Piece[][] (located on 'Board' class)
 * because we're on the Chess layer, and the Interface will only be able to read the
 * ChessPiece[][] type. (Layer-programming) */
/** ======================================== **/

public class ChessMatch {

	// Attributes
	private Board board;
	
	// Constructor
	public ChessMatch() { // 1.1*
		board = new Board(8, 8);
		initialSetup();
	}
	
	// Methods
	public ChessPiece[][] getPieces() { // 1.2*
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i, j);
			}
		}
		
		return mat;
	}
	
	private void initialSetup() {
		board.placePiece(new Rook(board, Color.WHITE), new Position(2, 1));
		board.placePiece(new King(board, Color.BLACK), new Position(0, 4));
		board.placePiece(new King(board, Color.WHITE), new Position(7, 4));
	}
}
