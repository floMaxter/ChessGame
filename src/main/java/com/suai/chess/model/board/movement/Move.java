package com.suai.chess.model.board.movement;

import com.suai.chess.model.board.Board;
import com.suai.chess.model.board.BoardPlugins;
import com.suai.chess.model.pieces.Pawn;
import com.suai.chess.model.pieces.Piece;
import com.suai.chess.model.pieces.Rook;

import static com.suai.chess.model.board.Board.*;

public abstract class Move {
    protected final Board board;
    protected final Piece movedPiece;
    protected final int destinationCoordinate;
    protected final boolean isFirstMove;

    private Move(Board board, Piece movedPiece, int destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
        this.isFirstMove = movedPiece.isFirstMove();
    }

    private Move(Board board, int destinationCoordinate) {
        this.board = board;
        this.destinationCoordinate = destinationCoordinate;
        this.movedPiece = null;
        this.isFirstMove = false;
    }

    public Board getBoard() {
        return this.board;
    }

    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }

    public Piece getMovedPiece() {
        return this.movedPiece;
    }

    public int getCurrentCoordinate() {
        return this.movedPiece.getPiecePosition();
    }

    public boolean isAttack() {
        return false;
    }

    public boolean isCastlingMove() {
        return false;
    }

    public Piece getAttackedPiece() {
        return null;
    }

    public Board perform() {
        Builder builder = new Builder();
        for (Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
            if (!this.movedPiece.equals(piece)) {
                builder.setPiece(piece);
            }
        }
        for (Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
            builder.setPiece(piece);
        }
        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
        return builder.build();
    }

    public Board previousBoard() {
        Builder builder = new Builder();
        for (Piece piece : this.board.getAllPieces()) {
            builder.setPiece(piece);
        }
        builder.setMoveMaker(this.board.getCurrentPlayer().getAlliance());
        return builder.build();
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int res = 1;
        res = prime * res + this.destinationCoordinate;
        res = prime * res + this.movedPiece.hashCode();
        res = prime * res + this.movedPiece.getPiecePosition();
        return res;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Move)) {
            return false;
        }
        if (getClass() != other.getClass()) {
            return false;
        }
        Move otherMove = (Move) other;
        return getCurrentCoordinate() == otherMove.getCurrentCoordinate() &&
                getDestinationCoordinate() == otherMove.getDestinationCoordinate() &&
                getMovedPiece().equals(otherMove.getMovedPiece());
    }

    public enum MoveStatus {
        DONE {
            @Override
            public boolean isDone() {
                return true;
            }
        },
        ILLEGAL_MOVE {
            @Override
            public boolean isDone() {
                return false;
            }
        },
        LEAVES_PLAYER_IN_CHECK {
            @Override
            public boolean isDone() {
                return false;
            }
        };

        public abstract boolean isDone();
    }


    public static class EmptyMove extends Move {
        public EmptyMove(Board board, Piece movedPiece, int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof EmptyMove)) {
                return false;
            }
            return super.equals(other);
        }

        @Override
        public String toString() {
            return movedPiece.getPieceType().toString() +
                    BoardPlugins.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    public static class AttackedMove extends Move {
        private final Piece attackedPiece;

        public AttackedMove(Board board,
                            Piece movedPiece,
                            int destinationCoordinate,
                            Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public boolean isAttack() {
            return true;
        }

        @Override
        public Piece getAttackedPiece() {
            return this.attackedPiece;
        }

        @Override
        public int hashCode() {
            return this.attackedPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof AttackedMove)) {
                return false;
            }
            if (getClass() != other.getClass()) {
                return false;
            }
            AttackedMove otherAttackMove = (AttackedMove) other;
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }

        @Override
        public String toString() {
            return movedPiece.getPieceType() +
                    BoardPlugins.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    public static class PawnMove extends Move {
        public PawnMove(Board board, Piece movedPiece, int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof PawnMove)) {
                return false;
            }
            if (getClass() != other.getClass()) {
                return false;
            }
            return super.equals(other);
        }

        @Override
        public String toString() {
            return BoardPlugins.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    public static class PawnAttackedMove extends AttackedMove {
        public PawnAttackedMove(Board board,
                                Piece movedPiece,
                                int destinationCoordinate,
                                Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof PawnAttackedMove)) {
                return false;
            }
            if (getClass() != other.getClass()) {
                return false;
            }
            return super.equals(other);
        }

        @Override
        public String toString() {
            return BoardPlugins.getPositionAtCoordinate(this.movedPiece.getPiecePosition()).charAt(0) + "x" +
                    BoardPlugins.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    public static class PawnEnPassantAttackedMove extends PawnAttackedMove {
        public PawnEnPassantAttackedMove(Board board,
                                         Piece movedPiece,
                                         int destinationCoordinate,
                                         Piece attackedPiece) {
            super(board, movedPiece, destinationCoordinate, attackedPiece);
        }

        @Override
        public Board perform() {
            Builder builder = new Builder();
            for (Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
                if (!this.movedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for (Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
                if (!piece.equals(this.getAttackedPiece())) {
                    builder.setPiece(piece);
                }
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
        }

        @Override
        public Board previousBoard() {
            Builder builder = new Builder();
            for (Piece piece : board.getAllPieces()) {
                builder.setPiece(piece);
            }
            builder.setEnPassantPawn((Pawn) this.getAttackedPiece());
            builder.setMoveMaker(this.board.getCurrentPlayer().getAlliance());
            return builder.build();
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof PawnAttackedMove)) {
                return false;
            }
            return super.equals(other);
        }
    }

    public static class PawnJump extends Move {
        public PawnJump(Board board, Piece movedPiece, int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public Board perform() {
            Builder builder = new Builder();
            for (Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
                if (!this.movedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for (Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }

            Pawn movePawn = (Pawn) this.movedPiece.movePiece(this);
            builder.setPiece(movePawn);
            builder.setEnPassantPawn(movePawn);
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());
            return builder.build();
        }

        @Override
        public String toString() {
            return BoardPlugins.getPositionAtCoordinate(this.destinationCoordinate);
        }
    }

    public static class PawnPromotion extends Move {
        Move decoratedMove;
        Pawn promotedPawn;

        public PawnPromotion(Move decorateMove) {
            super(decorateMove.getBoard(), decorateMove.getMovedPiece(), decorateMove.getDestinationCoordinate());
            this.decoratedMove = decorateMove;
            this.promotedPawn = (Pawn) decoratedMove.getMovedPiece();
        }

        @Override
        public Board perform() {
            Board pawnMovedBoard = this.decoratedMove.perform();
            Builder builder = new Builder();
            for (Piece piece : pawnMovedBoard.getCurrentPlayer().getActivePieces()) {
                if (!this.promotedPawn.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for (Piece piece : pawnMovedBoard.getCurrentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            builder.setPiece(this.promotedPawn.getPromotionPiece().movePiece(this));
            builder.setMoveMaker(pawnMovedBoard.getCurrentPlayer().getAlliance());
            return builder.build();
        }

        @Override
        public boolean isAttack() {
            return this.decoratedMove.isAttack();
        }

        @Override
        public Piece getAttackedPiece() {
            return this.decoratedMove.getAttackedPiece();
        }

        @Override
        public int hashCode() {
            return decoratedMove.hashCode() + (31 * promotedPawn.hashCode());
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof PawnPromotion)) {
                return false;
            }
            if (getClass() != other.getClass()) {
                return false;
            }
            return super.equals(other);
        }

        @Override
        public String toString() {
            return "";
        }
    }

    public static abstract class CastleMove extends Move {
        protected Rook castleRook;
        protected int castleRookStart;
        protected int castleRookDestination;

        public CastleMove(Board board,
                          Piece movedPiece,
                          int destinationCoordinate,
                          Rook castleRook,
                          int castleRookStart,
                          int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }

        public Rook getCastleRook() {
            return this.castleRook;
        }

        @Override
        public boolean isCastlingMove() {
            return true;
        }

        @Override
        public Board perform() {
            Builder builder = new Builder();
            for (Piece piece : this.board.getCurrentPlayer().getActivePieces()) {
                if (!this.movedPiece.equals(piece) && !this.castleRook.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for (Piece piece : this.board.getCurrentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setPiece(new Rook(this.castleRook.getPieceAlliance(), this.castleRookDestination, false));
            builder.setMoveMaker(this.board.getCurrentPlayer().getOpponent().getAlliance());

            return builder.build();
        }

        @Override
        public int hashCode() {
            int prime = 31;
            int result = super.hashCode();
            result = prime * result + this.castleRook.hashCode();
            result = prime * result + this.castleRookDestination;
            return result;
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof CastleMove)) {
                return false;
            }
            if (getClass() != other.getClass()) {
                return false;
            }
            CastleMove otherCastleMove = (CastleMove) other;
            return super.equals(otherCastleMove) && this.castleRook.equals(otherCastleMove.getCastleRook());
        }
    }

    public static class KingSideCastleMove extends CastleMove {
        public KingSideCastleMove(Board board,
                                  Piece movedPiece,
                                  int destinationCoordinate,
                                  Rook castleRook,
                                  int castleRookStart,
                                  int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof KingSideCastleMove)) {
                return false;
            }
            return super.equals(other);
        }

        @Override
        public String toString() {
            return "0-0";
        }
    }

    public static class QueenSideCastleMove extends CastleMove {
        public QueenSideCastleMove(Board board,
                                   Piece movedPiece,
                                   int destinationCoordinate,
                                   Rook castleRook,
                                   int castleRookStart,
                                   int castleRookDestination) {
            super(board, movedPiece, destinationCoordinate, castleRook, castleRookStart, castleRookDestination);
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof QueenSideCastleMove)) {
                return false;
            }
            return super.equals(other);
        }

        @Override
        public String toString() {
            return "0-0";
        }
    }

    public static class NullMove extends Move {
        public NullMove() {
            super(null, 65);
        }

        @Override
        public Board perform() {
            throw new RuntimeException("Can't perform the null move!");
        }

        @Override
        public int getCurrentCoordinate() {
            return -1;
        }
    }

    public static class MoveFactory {
        private MoveFactory() {
            throw new RuntimeException("Not instantiable!");
        }

        public static Move createMove(Board board,
                                      int currentCoordinate,
                                      int destinationCoordinate) {
            for (Move move : board.getAllLegalMoves()) {
                if (move.getCurrentCoordinate() == currentCoordinate &&
                        move.getDestinationCoordinate() == destinationCoordinate) {
                    return move;
                }
            }
            return new NullMove();
        }

        public static Move getNullMove() {
            return new NullMove();
        }
    }
}
