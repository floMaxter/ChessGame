package com.suai.chess.model.player;

import com.suai.chess.model.pieces.Alliance;
import com.suai.chess.model.board.Board;
import com.suai.chess.model.board.movement.Move;
import com.suai.chess.model.board.movement.Move.MoveStatus;
import com.suai.chess.model.board.movement.MoveTransition;
import com.suai.chess.model.pieces.King;
import com.suai.chess.model.pieces.Piece;
import com.suai.chess.model.pieces.Piece.PieceType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public abstract class Player {
    protected final Board board;
    protected final King playerKing;
    protected final Collection<Move> legalMoves;
    private final boolean isInCheck;

    public Player(Board board,
                  Collection<Move> legalMoves,
                  Collection<Move> opponentMoves) {
        this.board = board;
        this.playerKing = establishKing();
        //TODO: legal moves add for player, but should for king
        this.legalMoves = createAllLegalMoves(legalMoves, opponentMoves);
        this.isInCheck = !Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(), opponentMoves).isEmpty();
    }

    public King getPlayerKing() {
        return this.playerKing;
    }

    public Collection<Move> getLegalMoves() {
        return this.legalMoves;
    }

    public Collection<Move> createAllLegalMoves(Collection<Move> legalMoves,
                                                Collection<Move> opponentMoves) {
        List<Move> allLegalMoves = new ArrayList<>();
        allLegalMoves.addAll(legalMoves);
        allLegalMoves.addAll(calculateKingCastles(legalMoves, opponentMoves));
        return allLegalMoves;
    }

    protected static Collection<Move> calculateAttacksOnTile(int position, Collection<Move> opponentMoves) {
        List<Move> attackMoves = new ArrayList<>();
        for (Move move : opponentMoves) {
            if (position == move.getDestinationCoordinate()) {
                attackMoves.add(move);
            }
        }
        return Collections.unmodifiableList(attackMoves);
    }

    private King establishKing() {
        for (Piece piece : getActivePieces()) {
            if (piece.isParticularPiece(PieceType.KING)) {
                return (King) piece;
            }
        }
        System.out.println("Print active pieces");
        Collection<Piece> pieces = getActivePieces();
        for (int i = 0; i < pieces.size(); i++) {
            System.out.println(pieces);
        }

        throw new RuntimeException("Shouldn't reach here! Not a valid board!");
    }

    public boolean isKingSideCastleCapable() {
        return this.playerKing.isKingSideCastleCapable();
    }

    public boolean isQueenSideCastleCapable() {
        return this.playerKing.isQueenSideCastleCapable();
    }

    public abstract Collection<Piece> getActivePieces();

    public abstract Alliance getAlliance();

    public abstract Player getOpponent();

    public abstract Collection<Move> calculateKingCastles(Collection<Move> playerLegals,
                                                          Collection<Move> opponentsLegals);

    public boolean isMoveLegal(Move move) {
        return this.legalMoves.contains(move);
    }

    public boolean isInCheck() {
        return this.isInCheck;
    }

    public boolean isInCheckMate() {
        return this.isInCheck && !hasEscapeMoves();
    }

    public boolean isInStaleMate() {
        return !this.isInCheck && !hasEscapeMoves();
    }

    public boolean isCastled() {
        return this.playerKing.isCastle();
    }

    private boolean hasEscapeMoves() {
        for (Move move : this.legalMoves) {
            MoveTransition transition = makeMove(move);
            if (transition.getMoveStatus().isDone()) {
                return true;
            }
        }
        return false;
    }

    public MoveTransition makeMove(Move move) {
        if (!isMoveLegal(move)) {
            return new MoveTransition(this.board, this.board, move, MoveStatus.ILLEGAL_MOVE);
        }
        Board transitionBoard = move.perform();
        Player previousPlayer = transitionBoard.getCurrentPlayer().getOpponent();
        int kingPositionOpponent = previousPlayer.getPlayerKing().getPiecePosition();
        Collection<Move> legalMovesCurrentPlayer = transitionBoard.getCurrentPlayer().getLegalMoves();
        Collection<Move> kingAttacks =
                Player.calculateAttacksOnTile(kingPositionOpponent, legalMovesCurrentPlayer);
        if (!kingAttacks.isEmpty()) {
            return new MoveTransition(this.board, this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }
        return new MoveTransition(this.board, transitionBoard, move, MoveStatus.DONE);
    }

    public MoveTransition removeMakeMove(Move move) {
        return new MoveTransition(this.board, move.previousBoard(), move, MoveStatus.DONE);
    }

    protected boolean hasCastling() {
        return this.isInCheck || this.getPlayerKing().isCastle() ||
                (!this.playerKing.isKingSideCastleCapable() && !this.playerKing.isQueenSideCastleCapable());
    }
}
