package com.suai.chess;

import com.suai.chess.model.pieces.Alliance;
import com.suai.chess.model.board.Board;
import com.suai.chess.model.board.Board.Builder;
import com.suai.chess.model.board.BoardPlugins;
import com.suai.chess.model.board.movement.Move;
import com.suai.chess.model.board.movement.Move.MoveFactory;
import com.suai.chess.model.board.movement.MoveTransition;
import com.suai.chess.model.pieces.King;
import com.suai.chess.model.pieces.Piece;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.suai.chess.model.board.movement.Move.MoveFactory.*;
import static org.junit.Assert.*;

public class BoardTest {

    @Test
    public void initialBoard() {
        final Board board = Board.createStandardBoard();
        assertEquals(board.getCurrentPlayer().getLegalMoves().size(), 20);
        assertEquals(board.getCurrentPlayer().getOpponent().getLegalMoves().size(), 20);
        assertFalse(board.getCurrentPlayer().isInCheck());
        assertFalse(board.getCurrentPlayer().isInCheckMate());
        assertFalse(board.getCurrentPlayer().isCastled());
        assertTrue(board.getCurrentPlayer().isKingSideCastleCapable());
        assertTrue(board.getCurrentPlayer().isQueenSideCastleCapable());
        assertEquals(board.getCurrentPlayer(), board.whitePlayer());
        assertEquals(board.getCurrentPlayer().getOpponent(), board.blackPlayer());
        assertFalse(board.getCurrentPlayer().getOpponent().isInCheck());
        assertFalse(board.getCurrentPlayer().getOpponent().isInCheckMate());
        assertFalse(board.getCurrentPlayer().getOpponent().isCastled());
        assertTrue(board.getCurrentPlayer().getOpponent().isKingSideCastleCapable());
        assertTrue(board.getCurrentPlayer().getOpponent().isQueenSideCastleCapable());
        assertEquals("White", board.whitePlayer().toString());
        assertEquals("Black", board.blackPlayer().toString());

        final Iterable<Piece> allPieces = board.getAllPieces();
        final List<Move> allLegalMoves = new ArrayList<>();
        allLegalMoves.addAll(board.whitePlayer().getLegalMoves());
        allLegalMoves.addAll(board.blackPlayer().getLegalMoves());
        for(final Move move : allLegalMoves) {
            assertFalse(move.isAttack());
            assertFalse(move.isCastlingMove());
        }

        assertFalse(BoardPlugins.isEndGame(board));
        assertFalse(BoardPlugins.isCheckSituation(board));
        assertNull(board.getPiece(35));
    }

    @Test
    public void testSimpleKingMove() {

        final Builder builder = new Builder();
        builder.setPiece(new King(Alliance.BLACK, 4, false, false));
        builder.setPiece(new King(Alliance.WHITE, 60, false, false));
        builder.setMoveMaker(Alliance.WHITE);
        final Board board = builder.build();
        System.out.println(board);

        assertEquals(board.whitePlayer().getLegalMoves().size(), 5);
        assertEquals(board.blackPlayer().getLegalMoves().size(), 5);
        assertFalse(board.getCurrentPlayer().isInCheck());
        assertFalse(board.getCurrentPlayer().isInCheckMate());
        assertFalse(board.getCurrentPlayer().getOpponent().isInCheck());
        assertFalse(board.getCurrentPlayer().getOpponent().isInCheckMate());
        assertEquals(board.getCurrentPlayer(), board.whitePlayer());
        assertEquals(board.getCurrentPlayer().getOpponent(), board.blackPlayer());

        final Move move = createMove(board, BoardPlugins.getCoordinateAtPosition("e1"),
                BoardPlugins.getCoordinateAtPosition("f1"));

        final MoveTransition moveTransition = board.getCurrentPlayer()
                .makeMove(move);

        assertEquals(moveTransition.getTransitionMove(), move);
        assertTrue(moveTransition.getMoveStatus().isDone());
    }

    @Test
    public void testBoardConsistency() {
        final Board board = Board.createStandardBoard();
        assertEquals(board.getCurrentPlayer(), board.whitePlayer());

        final MoveTransition t1 = board.getCurrentPlayer()
                .makeMove(MoveFactory.createMove(board, BoardPlugins.getCoordinateAtPosition("e2"),
                        BoardPlugins.getCoordinateAtPosition("e4")));

        final MoveTransition t2 = t1.getToBoard().getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t1.getToBoard(), BoardPlugins.getCoordinateAtPosition("e7"),
                        BoardPlugins.getCoordinateAtPosition("e5")));

        final MoveTransition t3 = t2.getToBoard().getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t2.getToBoard(), BoardPlugins.getCoordinateAtPosition("g1"),
                        BoardPlugins.getCoordinateAtPosition("f3")));

        final MoveTransition t4 = t3.getToBoard().getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t3.getToBoard(), BoardPlugins.getCoordinateAtPosition("d7"),
                        BoardPlugins.getCoordinateAtPosition("d5")));

        final MoveTransition t5 = t4.getToBoard().getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t4.getToBoard(), BoardPlugins.getCoordinateAtPosition("e4"),
                        BoardPlugins.getCoordinateAtPosition("d5")));

        final MoveTransition t6 = t5.getToBoard().getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t5.getToBoard(), BoardPlugins.getCoordinateAtPosition("d8"),
                        BoardPlugins.getCoordinateAtPosition("d5")));

        final MoveTransition t7 = t6.getToBoard().getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t6.getToBoard(), BoardPlugins.getCoordinateAtPosition("f3"),
                        BoardPlugins.getCoordinateAtPosition("g5")));

        final MoveTransition t8 = t7.getToBoard().getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t7.getToBoard(), BoardPlugins.getCoordinateAtPosition("f7"),
                        BoardPlugins.getCoordinateAtPosition("f6")));

        final MoveTransition t9 = t8.getToBoard().getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t8.getToBoard(), BoardPlugins.getCoordinateAtPosition("d1"),
                        BoardPlugins.getCoordinateAtPosition("h5")));

        final MoveTransition t10 = t9.getToBoard().getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t9.getToBoard(), BoardPlugins.getCoordinateAtPosition("g7"),
                        BoardPlugins.getCoordinateAtPosition("g6")));

        final MoveTransition t11 = t10.getToBoard().getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t10.getToBoard(), BoardPlugins.getCoordinateAtPosition("h5"),
                        BoardPlugins.getCoordinateAtPosition("h4")));

        final MoveTransition t12 = t11.getToBoard().getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t11.getToBoard(), BoardPlugins.getCoordinateAtPosition("f6"),
                        BoardPlugins.getCoordinateAtPosition("g5")));

        final MoveTransition t13 = t12.getToBoard().getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t12.getToBoard(), BoardPlugins.getCoordinateAtPosition("h4"),
                        BoardPlugins.getCoordinateAtPosition("g5")));

        final MoveTransition t14 = t13.getToBoard().getCurrentPlayer()
                .makeMove(MoveFactory.createMove(t13.getToBoard(), BoardPlugins.getCoordinateAtPosition("d5"),
                        BoardPlugins.getCoordinateAtPosition("e4")));

        assertEquals(t14.getToBoard().whitePlayer().getActivePieces().size(), calculatedActivePieces(t14.getToBoard(), Alliance.WHITE));
        assertEquals(t14.getToBoard().blackPlayer().getActivePieces().size(), calculatedActivePieces(t14.getToBoard(), Alliance.BLACK));

    }

    private static int calculatedActivePieces(final Board board, final Alliance alliance) {
        int count = 0;
        for (final Piece piece : board.getAllPieces()) {
            if (piece.getPieceAlliance().equals(alliance)) {
                count++;
            }
        }
        return count;
    }
}