package com.suai.chess.model.player;

import com.suai.chess.model.pieces.Alliance;
import com.suai.chess.model.board.Board;
import com.suai.chess.model.board.movement.Move;
import com.suai.chess.model.board.Tile;
import com.suai.chess.model.pieces.Piece;
import com.suai.chess.model.pieces.Rook;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.suai.chess.model.board.movement.Move.*;
import static com.suai.chess.model.pieces.Piece.*;

public class WhitePlayer extends Player {
    public WhitePlayer(Board board,
                       Collection<Move> whiteStandardMoves,
                       Collection<Move> blackStandardMoves) {
        super(board, whiteStandardMoves, blackStandardMoves);
    }

    @Override
    public Collection<Piece> getActivePieces() {
        return this.board.getWhitePieces();
    }

    @Override
    public Alliance getAlliance() {
        return Alliance.WHITE;
    }

    @Override
    public Player getOpponent() {
        return this.board.blackPlayer();
    }

    @Override
    public Collection<Move> calculateKingCastles(Collection<Move> playerLegals,
                                                 Collection<Move> opponentsLegals) {
        if (hasCastling()) {
            return Collections.emptyList();
        }

        List<Move> kingCastles = new ArrayList<Move>();
        if (this.playerKing.isFirstMove() && !this.isInCheck()) {
            if (!this.board.getTile(61).isTileOccupied() &&
                    !this.board.getTile(62).isTileOccupied()) {
                Tile rookTile = this.board.getTile(63);
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    if (Player.calculateAttacksOnTile(61, opponentsLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(62, opponentsLegals).isEmpty() &&
                            rookTile.getPiece().isParticularPiece(PieceType.ROOK)) {
                        kingCastles.add(new KingSideCastleMove(this.board, this.playerKing, 62,
                                (Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), 61));
                    }
                }
            }

            if (!this.board.getTile(59).isTileOccupied() &&
                    !this.board.getTile(58).isTileOccupied() &&
                    !this.board.getTile(57).isTileOccupied()) {
                Tile rookTile = this.board.getTile(56);
                if (rookTile.isTileOccupied() && rookTile.getPiece().isFirstMove()) {
                    if (Player.calculateAttacksOnTile(59, opponentsLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(58, opponentsLegals).isEmpty() &&
                            Player.calculateAttacksOnTile(57, opponentsLegals).isEmpty() &&
                            rookTile.getPiece().isParticularPiece(PieceType.ROOK)) {
                        kingCastles.add(new QueenSideCastleMove(this.board, this.playerKing, 58,
                                (Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), 59));
                    }
                }
            }
        }
        return Collections.unmodifiableList(kingCastles);
    }

    @Override
    public String toString() {
        return Alliance.WHITE.toString();
    }
}
