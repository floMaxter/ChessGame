package com.suai.chess.model.pieces;

import com.suai.chess.model.board.Board;
import com.suai.chess.model.board.BoardPlugins;
import com.suai.chess.model.board.Tile;
import com.suai.chess.model.board.movement.Move;
import com.suai.chess.model.board.movement.Move.EmptyMove;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.suai.chess.model.board.movement.Move.*;

public class Knight extends Piece {
    private final static int[] CANDIDATE_MOVE_COORDINATES = {-17, -15, -10, -6, 6, 10, 15, 17};

    public Knight(Alliance pieceAlliance, int piecePosition) {
        super(pieceAlliance, piecePosition, PieceType.KNIGHT, true);
    }

    public Knight(Alliance pieceAlliance, int piecePosition, boolean isFirstMove) {
        super(pieceAlliance, piecePosition, PieceType.KNIGHT, isFirstMove);
    }
    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();
        for (int currentCoordinateOffset : CANDIDATE_MOVE_COORDINATES) {
            int candidateDestinationCoordinate = this.piecePosition + currentCoordinateOffset;
            if (BoardPlugins.isValidTileCoordinate(candidateDestinationCoordinate)) {
                if (isFirstColumnExclusion(this.piecePosition, currentCoordinateOffset) ||
                        isSecondColumnExclusion(this.piecePosition, currentCoordinateOffset) ||
                        isSeventhColumnExclusion(this.piecePosition, currentCoordinateOffset) ||
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
    public Knight movePiece(Move move) {
        return new Knight(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    @Override
    public String toString() {
        return PieceType.KNIGHT.toString();
    }

    private static boolean isFirstColumnExclusion(int currentPosition, int candidateOffset) {
        return BoardPlugins.FIRST_COLUMN[currentPosition] && (candidateOffset == -17 || candidateOffset == -10 ||
                candidateOffset == 6 || candidateOffset == 15);
    }

    private static boolean isSecondColumnExclusion(int currentPosition, int candidateOffset) {
        return BoardPlugins.SECOND_COLUMN[currentPosition] && (candidateOffset == -10 || candidateOffset == 6);
    }

    private static boolean isSeventhColumnExclusion(int currentPosition, int candidateOffset) {
        return BoardPlugins.SEVENTH_COLUMN[currentPosition] && (candidateOffset == -6 || candidateOffset == 10);
    }

    private static boolean isEighthColumnExclusion(int currentPosition, int candidateOffset) {
        return BoardPlugins.EIGHTH_COLUMN[currentPosition] && (candidateOffset == 17 || candidateOffset == 10 ||
                candidateOffset == -6 || candidateOffset == -15);
    }
}