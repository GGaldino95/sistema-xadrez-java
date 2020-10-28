package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

/** =============== COMMENTS =============== **/
/* 1.1: We created a boolean matrix with the same dimensions of the board. */
/** ======================================== **/

public class Pawn extends ChessPiece {

	// Constructor
	public Pawn(Board board, Color color) {
		super(board, color);
	}

	// Methods
	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()]; // 1.1*
		Position p = new Position(0, 0); // aux

		if (getColor() == Color.WHITE) { // WHITE PIECE
			// North 1 block
			p.setValues(position.getRow() - 1, position.getColumn());
			if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p))
				mat[p.getRow()][p.getColumn()] = true;

			// North 2 blocks
			p.setValues(position.getRow() - 2, position.getColumn());
			Position p2 = new Position(position.getRow() - 1, position.getColumn());
			if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p) && getBoard().positionExists(p2)
					&& !getBoard().thereIsAPiece(p2) && getMoveCount() == 0)
				mat[p.getRow()][p.getColumn()] = true;

			// Capture enemy on Northwest
			p.setValues(position.getRow() - 1, position.getColumn() - 1);
			if (getBoard().positionExists(p) && isThereOpponentPiece(p))
				mat[p.getRow()][p.getColumn()] = true;

			// Capture enemy on Northeast
			p.setValues(position.getRow() - 1, position.getColumn() + 1);
			if (getBoard().positionExists(p) && isThereOpponentPiece(p))
				mat[p.getRow()][p.getColumn()] = true;

		} else { // BLACK PIECE
			// South 1 block
			p.setValues(position.getRow() + 1, position.getColumn());
			if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p))
				mat[p.getRow()][p.getColumn()] = true;

			// South 2 blocks
			p.setValues(position.getRow() + 2, position.getColumn());
			Position p2 = new Position(position.getRow() + 1, position.getColumn());
			if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p) && getBoard().positionExists(p2)
					&& !getBoard().thereIsAPiece(p2) && getMoveCount() == 0)
				mat[p.getRow()][p.getColumn()] = true;

			// Capture enemy on Southwest
			p.setValues(position.getRow() + 1, position.getColumn() - 1);
			if (getBoard().positionExists(p) && isThereOpponentPiece(p))
				mat[p.getRow()][p.getColumn()] = true;

			// Capture enemy on Southeast
			p.setValues(position.getRow() + 1, position.getColumn() + 1);
			if (getBoard().positionExists(p) && isThereOpponentPiece(p))
				mat[p.getRow()][p.getColumn()] = true;

		}

		return mat;
	}

	// toString
	@Override
	public String toString() {
		return "P";
	}
}
