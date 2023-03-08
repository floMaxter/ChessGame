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

public class Bishop extends Piece {
    private static final int[] CANDIDATE_MOVE_VECTOR_COORDINATE = {-9, -7, 7, 9};

    public Bishop(Alliance pieceAlliance, int piecePosition) {
        super(pieceAlliance, piecePosition, PieceType.BISHOP, true);
    }

    public Bishop(Alliance pieceAlliance, int piecePosition, boolean isFirstMove) {
        super(pieceAlliance, piecePosition, PieceType.BISHOP, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        List<Move> legalMoves = new ArrayList<>();
        for (int currentCoordinateOffset : CANDIDATE_MOVE_VECTOR_COORDINATE) {
            int candidateDestinationCoordinate = this.piecePosition;
            while (BoardPlugins.isValidTileCoordinate(candidateDestinationCoordinate)) {
                if (isFirstColumnExclusion(currentCoordinateOffset, candidateDestinationCoordinate) ||
                        isEighthColumnExclusion(currentCoordinateOffset, candidateDestinationCoordinate)) {
                    break;
                }
                candidateDestinationCoordinate += currentCoordinateOffset;
                if (BoardPlugins.isValidTileCoordinate(candidateDestinationCoordinate)) {
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
                        break;
                    }
                }
            }
        }
        return Collections.unmodifiableList(legalMoves);
    }

    @Override
    public Bishop movePiece(Move move) {
        return new Bishop(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate(), false);
    }

    @Override
    public String toString() {
        return PieceType.BISHOP.toString();
    }

    private static boolean isFirstColumnExclusion(int currentCandidate,
                                                  int candidateDestinationCoordinate) {
        return (BoardPlugins.FIRST_COLUMN[candidateDestinationCoordinate] &&
                ((currentCandidate == -9) || (currentCandidate == 7)));
    }

    private static boolean isEighthColumnExclusion(int currentCandidate,
                                                   int candidateDestinationCoordinate) {
        return (BoardPlugins.EIGHTH_COLUMN[candidateDestinationCoordinate] &&
                ((currentCandidate == -7) || (currentCandidate == 9)));
    }
}
