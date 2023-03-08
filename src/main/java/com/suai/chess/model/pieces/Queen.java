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

public class Queen extends Piece {
    private static final int[] CANDIDATE_MOVE_VECTOR_COORDINATE = {-9, -8, -7, -1, 1, 7, 8, 9};

    public Queen(Alliance pieceAlliance, int piecePosition) {
        super(pieceAlliance, piecePosition, PieceType.QUEEN, true);
    }

    public Queen(Alliance pieceAlliance, int piecePosition, boolean isFirstMove) {
        super(pieceAlliance, piecePosition, PieceType.QUEEN, isFirstMove);
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
    public Queen movePiece(Move move) {
        return new Queen(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    @Override
    public String toString() {
        return PieceType.QUEEN.toString();
    }

    private static boolean isFirstColumnExclusion(int currentPosition,
                                                  int candidatePosition) {
        return BoardPlugins.FIRST_COLUMN[candidatePosition] && ((currentPosition == -9)
                || (currentPosition == -1) || (currentPosition == 7));
    }

    private static boolean isEighthColumnExclusion(int currentPosition,
                                                   int candidatePosition) {
        return BoardPlugins.EIGHTH_COLUMN[candidatePosition] && ((currentPosition == -7)
                || (currentPosition == 1) || (currentPosition == 9));
    }
}
