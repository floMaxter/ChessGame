package com.suai.chess;

import com.suai.chess.model.pieces.Alliance;
import com.suai.chess.model.board.Board;
import com.suai.chess.model.board.Board.Builder;
import com.suai.chess.model.board.BoardPlugins;
import com.suai.chess.model.board.movement.Move;
import com.suai.chess.model.board.movement.Move.MoveFactory;
import com.suai.chess.model.board.movement.MoveTransition;
import com.suai.chess.model.pieces.Bishop;
import com.suai.chess.model.pieces.King;
import com.suai.chess.model.pieces.Rook;
import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {
    @Test
    public void testSimpleEvaluation() {
        final Board board = Board.createStandardBoard();
        final MoveTransition t1 = board.getCurrentPlayer().makeMove(MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("e2"), BoardPlugins.getCoordinateAtPosition("e4")));
        assertTrue(t1.getMoveStatus().isDone());
        final MoveTransition t2 = t1.getToBoard().getCurrentPlayer().makeMove(MoveFactory
                .createMove(t1.getToBoard(), BoardPlugins.getCoordinateAtPosition("e7"), BoardPlugins.getCoordinateAtPosition("e5")));
        assertTrue(t2.getMoveStatus().isDone());
    }

    @Test
    public void testBug() {
        final Board board = Board.createStandardBoard();
        final MoveTransition t1 = board.getCurrentPlayer().makeMove(MoveFactory.
                createMove(board, BoardPlugins.getCoordinateAtPosition("c2"), BoardPlugins.getCoordinateAtPosition("c3")));
        assertTrue(t1.getMoveStatus().isDone());
        final MoveTransition t2 = t1.getToBoard().getCurrentPlayer().makeMove(MoveFactory
                .createMove(t1.getToBoard(), BoardPlugins.getCoordinateAtPosition("b8"), BoardPlugins.getCoordinateAtPosition("a6")));
        assertTrue(t2.getMoveStatus().isDone());
        final MoveTransition t3 = t2.getToBoard().getCurrentPlayer().makeMove(MoveFactory
                .createMove(t2.getToBoard(), BoardPlugins.getCoordinateAtPosition("d1"), BoardPlugins.getCoordinateAtPosition("a4")));
        assertTrue(t3.getMoveStatus().isDone());
        final MoveTransition t4 = t3.getToBoard().getCurrentPlayer().makeMove(MoveFactory
                .createMove(t3.getToBoard(), BoardPlugins.getCoordinateAtPosition("d7"), BoardPlugins.getCoordinateAtPosition("d6")));
        assertFalse(t4.getMoveStatus().isDone());
    }

    @Test
    public void testDiscoveredCheck() {
        final Builder builder = new Builder();
        builder.setPiece(new King(Alliance.BLACK, 4, false, false));
        builder.setPiece(new Rook(Alliance.BLACK, 24));
        builder.setPiece(new Bishop(Alliance.WHITE, 44));
        builder.setPiece(new Rook(Alliance.WHITE, 52));
        builder.setPiece(new King(Alliance.WHITE, 58, false, false));
        builder.setMoveMaker(Alliance.WHITE);

        final Board board = builder.build();
        final MoveTransition t1 = board.getCurrentPlayer().makeMove(MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("e3"), BoardPlugins.getCoordinateAtPosition("b6")));
        assertTrue(t1.getMoveStatus().isDone());
        assertTrue(t1.getToBoard().getCurrentPlayer().isInCheck());
        final MoveTransition t2 = t1.getToBoard().getCurrentPlayer().makeMove(MoveFactory
                .createMove(t1.getToBoard(), BoardPlugins.getCoordinateAtPosition("a5"), BoardPlugins.getCoordinateAtPosition("b5")));
        assertFalse(t2.getMoveStatus().isDone());
        final MoveTransition t3 = t1.getToBoard().getCurrentPlayer().makeMove(MoveFactory
                .createMove(t1.getToBoard(), BoardPlugins.getCoordinateAtPosition("a5"), BoardPlugins.getCoordinateAtPosition("e5")));
        assertTrue(t3.getMoveStatus().isDone());
    }

    @Test
    public void testIllegalMove() {
        final Board board = Board.createStandardBoard();
        final Move m1 = MoveFactory.createMove(board, BoardPlugins.getCoordinateAtPosition("e2"),
                BoardPlugins.getCoordinateAtPosition("e6"));
        final MoveTransition t1 = board.getCurrentPlayer().makeMove(m1);
        assertFalse(t1.getMoveStatus().isDone());
    }
}
