package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;

/** =============== COMMENTS =============== **/
/* 1.1: The Class that needs to know the dimensions of the 'Board' is 'ChessMatch'. */

/* 1.2: We won't declare the setter 'setTurn()' because we don't want the the turn to be 
 * altered in any way. */

/* 1.3: We won't declare the setter 'setCurrentPlayer()' because we don't want the current 
 * player to be altered in any way. */

/* 1.4: 'getPieces' is a ChessPiece[][] type instead of Piece[][] (located on 'Board' class)
 * because we're on the Chess layer, and the Interface will only be able to read the
 * ChessPiece[][] type. (Layer-programming) */

/* 1.5: 'this' is to make a self-reference to the ChessMatch class, since 'King' and 'Pawn'
 * now have a 'ChessMatch' argument on the respective constructors. */
/** ======================================== **/

public class ChessMatch {

	// Attributes
	private int turn;
	private Color currentPlayer;
	private Board board;
	private boolean check;
	private boolean checkmate;
	private ChessPiece enPassantVulnerable;
	private ChessPiece promoted;

	// Lists
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();

	// Constructor
	public ChessMatch() { // 1.1*
		board = new Board(8, 8);
		turn = 1;
		currentPlayer = Color.WHITE;
		initialSetup();
	}

	// Getters and Setters
	public int getTurn() { // 1.2*
		return turn;
	}

	public Color getCurrentPlayer() { // 1.3*
		return currentPlayer;
	}

	public boolean getCheck() {
		return check;
	}

	public boolean getCheckMate() {
		return checkmate;
	}

	public ChessPiece getEnPassantVulnerable() {
		return enPassantVulnerable;
	}

	public ChessPiece getPromoted() {
		return promoted;
	}
	
	// Methods
	public ChessPiece[][] getPieces() { // 1.4*
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i = 0; i < board.getRows(); i++) {
			for (int j = 0; j < board.getColumns(); j++) {
				mat[i][j] = (ChessPiece) board.piece(i, j);
			}
		}

		return mat;
	}

	public boolean[][] possibleMoves(ChessPosition sourcePosition) {
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();
	}

	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);
		validateTargetPosition(source, target);
		Piece capturedPiece = makeMove(source, target);

		if (testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessException("You can't put yourself in check");
		}

		ChessPiece movedPiece = (ChessPiece) board.piece(target);

		// Promotion (Special move)
		promoted = null;
		if (movedPiece instanceof Pawn) {
			if ((movedPiece.getColor() == Color.WHITE && target.getRow() == 0) || (movedPiece.getColor() == Color.BLACK && target.getRow() == 7)) {
				promoted = (ChessPiece)board.piece(target); // Downcasting
				promoted = replacePromotedPiece("Q");
			}
		}
		
		check = (testCheck(opponent(currentPlayer))) ? true : false;

		if (testCheckmate(opponent(currentPlayer))) {
			checkmate = true;
		} else {
			nextTurn();
		}

		// En Passant (Special move)
		if (movedPiece instanceof Pawn && (target.getRow() == source.getRow() - 2 || target.getRow() == source.getRow() + 2)) {
			enPassantVulnerable = movedPiece;
		} else {
			enPassantVulnerable = null;
		}

		return (ChessPiece) capturedPiece; // Downcasting
	}

	public ChessPiece replacePromotedPiece(String type) {
		if (promoted == null) 
			throw new IllegalStateException("There is no piece to be promoted");
			
		if (!type.equals("B") && !type.equals("N") && !type.equals("R") && !type.equals("Q"))
			return promoted;
		
		Position pos = promoted.getChessPosition().toPosition();
		Piece p = board.removePiece(pos);		
		piecesOnTheBoard.remove(p);
		
		ChessPiece newPiece = newPiece(type, promoted.getColor());
		board.placePiece(newPiece, pos);
		piecesOnTheBoard.add(newPiece);
		
		return newPiece;
	}
	
	private ChessPiece newPiece(String type, Color color) {
		if (type.equalsIgnoreCase("B")) return new Bishop(board, color);
		if (type.equalsIgnoreCase("N")) return new Knight(board, color);
		if (type.equalsIgnoreCase("Q")) return new Queen(board, color);
		
		return new Rook(board, color);
	}
	
	private Piece makeMove(Position source, Position target) {
		ChessPiece p = (ChessPiece) board.removePiece(source); // Downcasting
		p.increaseMoveCount();
		Piece captured = board.removePiece(target);
		board.placePiece(p, target);

		if (captured != null) {
			piecesOnTheBoard.remove(captured);
			capturedPieces.add(captured);
		}

		// Castling (Special Move) King-side Rook
		if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
			Position sourceR = new Position(source.getRow(), source.getColumn() + 3);
			Position targetR = new Position(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece) board.removePiece(sourceR);
			board.placePiece(rook, targetR);
			rook.increaseMoveCount();
		}

		// Castling (Special Move) Queen-side Rook
		if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
			Position sourceR = new Position(source.getRow(), source.getColumn() - 4);
			Position targetR = new Position(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece) board.removePiece(sourceR);
			board.placePiece(rook, targetR);
			rook.increaseMoveCount();
		}

		// En Passant (Special Move)
		if (p instanceof Pawn) {
			if (source.getColumn() != target.getColumn() && captured == null) {
				Position pawnPosition;
				if (p.getColor() == Color.WHITE) {
					pawnPosition = new Position(target.getRow() + 1, target.getColumn());
				} else {
					pawnPosition = new Position(target.getRow() - 1, target.getColumn());
				}
				
				captured = board.removePiece(pawnPosition);
				capturedPieces.add(captured);
				piecesOnTheBoard.remove(captured);
			}
		}

		return captured;
	}

	private void undoMove(Position source, Position target, Piece capturedPiece) {
		ChessPiece p = (ChessPiece) board.removePiece(target); // Downcasting
		p.decreaseMoveCount();
		board.placePiece(p, source);

		if (capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}

		// Castling (Special Move) King-side Rook
		if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
			Position sourceR = new Position(source.getRow(), source.getColumn() + 3);
			Position targetR = new Position(source.getRow(), source.getColumn() + 1);
			ChessPiece rook = (ChessPiece) board.removePiece(targetR);
			board.placePiece(rook, sourceR);
			rook.decreaseMoveCount();
		}

		// Castling (Special Move) Queen-side Rook
		if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
			Position sourceR = new Position(source.getRow(), source.getColumn() - 4);
			Position targetR = new Position(source.getRow(), source.getColumn() - 1);
			ChessPiece rook = (ChessPiece) board.removePiece(targetR);
			board.placePiece(rook, sourceR);
			rook.decreaseMoveCount();
		}

		// En Passant (Special Move)
		if (p instanceof Pawn) {
			if (source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerable) {
				ChessPiece pawn = (ChessPiece)board.removePiece(target);
				Position pawnPosition;
				if (p.getColor() == Color.WHITE)
					pawnPosition = new Position(3, target.getColumn());
				else
					pawnPosition = new Position(4, target.getColumn());

				board.placePiece(pawn, pawnPosition);
			}
		}
	}

	private void validateSourcePosition(Position position) {
		if (!board.thereIsAPiece(position))
			throw new ChessException("There is no piece on source position");

		if (currentPlayer != ((ChessPiece) board.piece(position)).getColor()) // Downcasting
			throw new ChessException("The chosen piece is not yours");

		if (!board.piece(position).isThereAnyPossibleMove())
			throw new ChessException("There is no possible moves for the selected piece");
	}

	private void validateTargetPosition(Position source, Position target) {
		if (!board.piece(source).possibleMove(target))
			throw new ChessException("The chosen piece can't move to target position");

	}

	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}

	private Color opponent(Color color) {
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}

	private ChessPiece king(Color color) {
		List<Piece> list = piecesOnTheBoard.parallelStream().filter(x -> ((ChessPiece) x).getColor() == color)
				.collect(Collectors.toList());
		for (Piece p : list) {
			if (p instanceof King)
				return (ChessPiece) p;
		}

		throw new IllegalStateException("There is no " + color + " king on the board");
	}

	private boolean testCheck(Color color) {
		Position kingPosition = king(color).getChessPosition().toPosition();
		List<Piece> opponentPieces = piecesOnTheBoard.stream()
				.filter(x -> ((ChessPiece) x).getColor() == opponent(color)).collect(Collectors.toList());

		for (Piece p : opponentPieces) {
			boolean[][] mat = p.possibleMoves();
			if (mat[kingPosition.getRow()][kingPosition.getColumn()])
				return true;
		}

		return false;
	}

	private boolean testCheckmate(Color color) {
		if (!testCheck(color))
			return false;

		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece) x).getColor() == color)
				.collect(Collectors.toList());
		for (Piece p : list) {
			boolean[][] mat = p.possibleMoves();
			for (int i = 0; i < board.getRows(); i++) {
				for (int j = 0; j < board.getColumns(); j++) {
					if (mat[i][j]) {
						Position source = ((ChessPiece) p).getChessPosition().toPosition();
						Position target = new Position(i, j);
						Piece capturedPiece = makeMove(source, target);
						boolean testCheck = testCheck(color);
						undoMove(source, target, capturedPiece);

						if (!testCheck)
							return false;
					}
				}
			}
		}

		return true;
	}

	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece);
	}

	private void initialSetup() {
		placeNewPiece('a', 1, new Rook(board, Color.WHITE));
		placeNewPiece('b', 1, new Knight(board, Color.WHITE));
		placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
		placeNewPiece('d', 1, new Queen(board, Color.WHITE));
		placeNewPiece('e', 1, new King(board, Color.WHITE, this)); // 1.5*
		placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
		placeNewPiece('g', 1, new Knight(board, Color.WHITE));
		placeNewPiece('h', 1, new Rook(board, Color.WHITE));
		placeNewPiece('a', 2, new Pawn(board, Color.WHITE, this)); // 1.5*
		placeNewPiece('b', 7, new Pawn(board, Color.WHITE, this)); // 1.5*
		placeNewPiece('c', 2, new Pawn(board, Color.WHITE, this)); // 1.5*
		placeNewPiece('d', 2, new Pawn(board, Color.WHITE, this)); // 1.5*
		placeNewPiece('e', 2, new Pawn(board, Color.WHITE, this)); // 1.5*
		placeNewPiece('f', 2, new Pawn(board, Color.WHITE, this)); // 1.5*
		placeNewPiece('g', 2, new Pawn(board, Color.WHITE, this)); // 1.5*
		placeNewPiece('h', 2, new Pawn(board, Color.WHITE, this)); // 1.5*

		placeNewPiece('a', 8, new Rook(board, Color.BLACK));
		//placeNewPiece('b', 8, new Knight(board, Color.BLACK));
		placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
		placeNewPiece('d', 8, new Queen(board, Color.BLACK));
		placeNewPiece('e', 8, new King(board, Color.BLACK, this)); // 1.5*
		placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
		placeNewPiece('g', 8, new Knight(board, Color.BLACK));
		placeNewPiece('h', 8, new Rook(board, Color.BLACK));
		placeNewPiece('a', 7, new Pawn(board, Color.BLACK, this)); // 1.5*
		//placeNewPiece('b', 7, new Pawn(board, Color.BLACK, this)); // 1.5*
		placeNewPiece('c', 7, new Pawn(board, Color.BLACK, this)); // 1.5*
		placeNewPiece('d', 7, new Pawn(board, Color.BLACK, this)); // 1.5*
		placeNewPiece('e', 7, new Pawn(board, Color.BLACK, this)); // 1.5*
		placeNewPiece('f', 7, new Pawn(board, Color.BLACK, this)); // 1.5*
		placeNewPiece('g', 7, new Pawn(board, Color.BLACK, this)); // 1.5*
		placeNewPiece('h', 7, new Pawn(board, Color.BLACK, this)); // 1.5*
	}
}
