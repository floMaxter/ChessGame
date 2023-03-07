package com.suai.chess.model.pieces;

public class Rook extends Piece {
    private static final int[] CANDIDATE_MOVE_VECTOR_COORDINATE = {-8, -1, 1, 8};

    public Rook(Alliance pieceAlliance, int piecePosition) {
        super(pieceAlliance, piecePosition, PieceType.ROOK, true);
    }

    public Rook(Alliance pieceAlliance, int piecePosition, boolean isFirstMove) {
        super(pieceAlliance, piecePosition, PieceType.ROOK, isFirstMove);
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
                        legalMoves.add(new Move.EmptyMove(board, this, candidateDestinationCoordinate));
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
    public Rook movePiece(Move move) {
        return new Rook(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    @Override
    public String toString() {
        return PieceType.ROOK.toString();
    }

    private static boolean isFirstColumnExclusion(int currentPosition,
                                                  int candidatePosition) {
        return BoardPlugins.FIRST_COLUMN[candidatePosition] && (currentPosition == -1);
    }

    private static boolean isEighthColumnExclusion(int currentPosition,
                                                   int candidatePosition) {
        return BoardPlugins.EIGHTH_COLUMN[candidatePosition] && (currentPosition == 1);
    }
}
