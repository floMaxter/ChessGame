package com.suai.chess.model.pieces;

import com.suai.chess.model.board.Board;
import com.suai.chess.model.board.BoardPlugins;
import com.suai.chess.model.board.Tile;
import com.suai.chess.model.board.movement.Move;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.suai.chess.model.board.movement.Move.*;

public class King extends Piece {
    private static final int[] CANDIDATE_MOVE_VECTOR_COORDINATE = {-9, -8, -7, -1, 1, 7, 8, 9};
    private final boolean isCastle;
    private final boolean kingSideCastleCapable;
    private final boolean queenSideCastleCapable;

    public King(Alliance pieceAlliance,
                int piecePosition,
                boolean kingSideCastleCapable,
                boolean queenSideCastleCapable) {
        super(pieceAlliance, piecePosition, PieceType.KING, true);
        this.isCastle = false;
        this.kingSideCastleCapable = kingSideCastleCapable;
        this.queenSideCastleCapable = queenSideCastleCapable;
    }

    public King(Alliance pieceAlliance,
                int piecePosition,
                boolean isFirstMove,
                boolean isCastle,
                boolean kingSideCastleCapable,
                boolean queenSideCastleCapable) {
        super(pieceAlliance, piecePosition, PieceType.KING, isFirstMove);
        this.isCastle = isCastle;
        this.kingSideCastleCapable = kingSideCastleCapable;
        this.queenSideCastleCapable = queenSideCastleCapable;
    }

    public boolean isCastle() {
        return this.isCastle;
    }

    public boolean isKingSideCastleCapable() {
        return this.kingSideCastleCapable;
    }

    public boolean isQueenSideCastleCapable() {
        return this.queenSideCastleCapable;
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        List<Move> legalMoves = new ArrayList<>();
        for (int currentCoordinateOffset : CANDIDATE_MOVE_VECTOR_COORDINATE) {
            int candidateDestinationCoordinate = this.piecePosition + currentCoordinateOffset;
            if (BoardPlugins.isValidTileCoordinate(candidateDestinationCoordinate)) {
                if (isFirstColumnExclusion(this.piecePosition, currentCoordinateOffset) ||
                        isEighthColumnExclusion(this.piecePosition, currentCoordinateOffset)) {
                    continue;
                }
                Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if (!candidateDestinationTile.isTileOccupied()) {
                    legalMoves.add(new EmptyMove(board, this, candidateDestinationCoordinate));
                } else {
                    Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    Alliance selectedPieceAlliance = pieceAtDestination.getPieceAlliance();
                    if (this.pieceAlliance != selectedPieceAlliance) {
                        legalMoves.add(new AttackedMove(board, this, candidateDestinationCoordinate,
                                pieceAtDestination));
                    }
                }
            }
        }
        return Collections.unmodifiableList(legalMoves);
    }


    @Override
    public King movePiece(Move move) {
        return new King(move.getMovedPiece().getPieceAlliance(),
                move.getDestinationCoordinate(),
                false,
                move.isCastlingMove(),
                false,
                false);
    }

    @Override
    public String toString() {
        return PieceType.KING.toString();
    }

    private static boolean isFirstColumnExclusion(int currentPosition, int candidateOffset) {
        return BoardPlugins.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == -1 ||
                candidateOffset == 7);
    }

    private static boolean isEighthColumnExclusion(int currentPosition, int candidateOffset) {
        return BoardPlugins.FIRST_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == 1 ||
                candidateOffset == 9);
    }
}