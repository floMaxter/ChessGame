package com.suai.chess.model.pieces;

public class Pawn extends Piece {
    private final static int[] CANDIDATE_MOVE_COORDINATES = {7, 8, 9, 16};

    public Pawn(Alliance pieceAlliance, int piecePosition) {
        super(pieceAlliance, piecePosition, PieceType.PAWN, true);
    }

    public Pawn(Alliance pieceAlliance, int piecePosition, boolean isFirstMove) {
        super(pieceAlliance, piecePosition, PieceType.PAWN, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        List<Move> legalMoves = new ArrayList<>();
        for (int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES) {
            int candidateDestinationCoordinate =
                    this.piecePosition + (this.getPieceAlliance().getDirection() * currentCandidateOffset);
            if (!BoardPlugins.isValidTileCoordinate(candidateDestinationCoordinate)) {
                continue;
            }
            if (currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                if(this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)) {
                    legalMoves.add(new PawnPromotion(new PawnMove(board, this, candidateDestinationCoordinate)));
                } else {
                    legalMoves.add(new PawnMove(board, this, candidateDestinationCoordinate));
                }
            }
            else if (currentCandidateOffset == 16 && this.isFirstMove() &&
                    ((BoardPlugins.SEVENTH_ROW_FROM_BELOW[this.piecePosition] && this.pieceAlliance.isBlack()) ||
                            (BoardPlugins.SECOND_ROW_FROM_BELOW[this.piecePosition] && this.pieceAlliance.isWhite()))) {
                int behindCandidateDestinationCoordinate =
                        this.piecePosition + (this.pieceAlliance.getDirection() * 8);
                if (!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() &&
                        !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
                    legalMoves.add(new PawnJump(board, this, candidateDestinationCoordinate));
                }
            } else if (currentCandidateOffset == 7 &&
                    !((BoardPlugins.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()) ||
                            (BoardPlugins.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))) {
                Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if (candidateDestinationTile.isTileOccupied()) {
                    Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    Alliance selectedPieceAlliance = pieceAtDestination.getPieceAlliance();
                    if (this.pieceAlliance != selectedPieceAlliance) {
                        if(this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)) {
                            legalMoves.add(new PawnPromotion(new PawnAttackedMove(board,
                                    this,
                                    candidateDestinationCoordinate,
                                    pieceAtDestination)));
                        } else {
                            legalMoves.add(new PawnAttackedMove(board, this, candidateDestinationCoordinate,
                                    pieceAtDestination));
                        }
                    }
                } else if(board.getEnPassantPawn() != null) {
                    if(board.getEnPassantPawn().getPiecePosition() ==
                            (this.piecePosition + (this.pieceAlliance.getOppositeDirection()))) {
                        Piece pieceAtDestination = board.getEnPassantPawn();
                        if(this.pieceAlliance != pieceAtDestination.getPieceAlliance()) {
                            legalMoves.add(new PawnEnPassantAttackedMove(board, this,
                                    candidateDestinationCoordinate, pieceAtDestination));
                        }
                    }
                }
            } else if (currentCandidateOffset == 9 &&
                    !((BoardPlugins.FIRST_COLUMN[this.piecePosition] && this.pieceAlliance.isWhite()) ||
                            (BoardPlugins.EIGHTH_COLUMN[this.piecePosition] && this.pieceAlliance.isBlack()))) {
                Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if (candidateDestinationTile.isTileOccupied()) {
                    Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    Alliance selectedPieceAlliance = pieceAtDestination.getPieceAlliance();
                    if (this.pieceAlliance != selectedPieceAlliance) {
                        if(this.pieceAlliance.isPawnPromotionSquare(candidateDestinationCoordinate)) {
                            legalMoves.add(new PawnPromotion(new PawnAttackedMove(board,
                                    this,
                                    candidateDestinationCoordinate,
                                    pieceAtDestination)));
                        } else {
                            legalMoves.add(new PawnAttackedMove(board, this, candidateDestinationCoordinate,
                                    pieceAtDestination));
                        }
                    }
                } else if(board.getEnPassantPawn() != null) {
                    if(board.getEnPassantPawn().getPiecePosition() ==
                            (this.piecePosition - (this.pieceAlliance.getOppositeDirection()))) {
                        Piece pieceAtDestination = board.getEnPassantPawn();
                        if(this.pieceAlliance != pieceAtDestination.getPieceAlliance()) {
                            legalMoves.add(new PawnEnPassantAttackedMove(board, this,
                                    candidateDestinationCoordinate, pieceAtDestination));
                        }
                    }
                }
            }
        }
        return Collections.unmodifiableList(legalMoves);
    }

    @Override
    public Pawn movePiece(Move move) {
        return new Pawn(move.getMovedPiece().getPieceAlliance(), move.getDestinationCoordinate());
    }

    public Piece getPromotionPiece() {
        return new Queen(this.pieceAlliance, this.piecePosition, false);
    }

    @Override
    public String toString() {
        return PieceType.PAWN.toString();
    }
}
