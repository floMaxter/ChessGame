package com.suai.chess.model.pieces;

public abstract class Piece {
    protected final PieceType pieceType;
    protected final int piecePosition;
    protected final Alliance pieceAlliance;
    protected final boolean isFirstMove;
    Piece(Alliance pieceAlliance, int piecePosition,
          PieceType pieceType, boolean isFirstMove) {
        this.pieceType = pieceType;
        this.piecePosition = piecePosition;
        this.pieceAlliance = pieceAlliance;
        this.isFirstMove = isFirstMove;
    }

    public boolean isParticularPiece(PieceType pieceType) {
        return this.pieceType == pieceType;
    }

    public Alliance getPieceAlliance() {
        return this.pieceAlliance;
    }

    public int getPiecePosition() {
        return this.piecePosition;
    }

    public PieceType getPieceType() {
        return this.pieceType;
    }

    public boolean isFirstMove() {
        return this.isFirstMove;
    }

    public int getPieceWeight() {
        return this.pieceType.getPieceWeight();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Piece)) {
            return false;
        }
        if (this == other) {
            return true;
        }
        if (getClass() != other.getClass()) {
            return false;
        }
        final Piece otherPiece = (Piece) other;
        return piecePosition == otherPiece.getPiecePosition() && pieceType == otherPiece.getPieceType() &&
                pieceAlliance == otherPiece.getPieceAlliance() && isFirstMove == otherPiece.isFirstMove;
    }

    @Override
    public int hashCode() {
        int res = pieceType.hashCode();
        res = 31 * res + pieceAlliance.hashCode();
        res = 31 * res + (isFirstMove ? 1 : 0);
        return res;
    }

    public abstract Collection<Move> calculateLegalMoves(Board board);

    public abstract Piece movePiece(Move move);

    public enum PieceType {
        KNIGHT(300, "N"),
        BISHOP(300, "B"),
        ROOK(500, "R"),
        QUEEN(900, "Q"),
        KING(10000, "K"),
        PAWN(100, "P");

        private final String pieceName;
        private final int pieceWeight;
        PieceType(int pieceWeight, String pieceName) {
            this.pieceName = pieceName;
            this.pieceWeight = pieceWeight;
        }

        @Override
        public String toString() {
            return this.pieceName;
        }

        public int getPieceWeight() {
            return this.pieceWeight;
        }
    }
}
