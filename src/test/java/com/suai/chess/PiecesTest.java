package com.suai.chess;

import com.suai.chess.model.pieces.Alliance;
import com.suai.chess.model.board.Board;
import com.suai.chess.model.board.BoardPlugins;
import com.suai.chess.model.board.movement.Move;
import com.suai.chess.model.board.movement.MoveTransition;
import com.suai.chess.model.pieces.*;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class PiecesTest {

    @Test
    public void testMiddleQueenOnEmptyBoard() {
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new King(Alliance.BLACK, 4, false, false));
        builder.setPiece(new Queen(Alliance.WHITE, 36));
        builder.setPiece(new King(Alliance.WHITE, 60, false, false));
        builder.setMoveMaker(Alliance.WHITE);

        final Board board = builder.build();
        final Collection<Move> whiteLegals = board.whitePlayer().getLegalMoves();
        final Collection<Move> blackLegals = board.blackPlayer().getLegalMoves();

        assertEquals(whiteLegals.size(), 31);
        assertEquals(blackLegals.size(), 5);

        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("e4"), BoardPlugins.getCoordinateAtPosition("e8"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("e4"), BoardPlugins.getCoordinateAtPosition("e7"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("e4"), BoardPlugins.getCoordinateAtPosition("e6"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("e4"), BoardPlugins.getCoordinateAtPosition("e5"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("e4"), BoardPlugins.getCoordinateAtPosition("e3"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("e4"), BoardPlugins.getCoordinateAtPosition("e2"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("e4"), BoardPlugins.getCoordinateAtPosition("a4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("e4"), BoardPlugins.getCoordinateAtPosition("b4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("e4"), BoardPlugins.getCoordinateAtPosition("c4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("e4"), BoardPlugins.getCoordinateAtPosition("d4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("e4"), BoardPlugins.getCoordinateAtPosition("f4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("e4"), BoardPlugins.getCoordinateAtPosition("g4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("e4"), BoardPlugins.getCoordinateAtPosition("h4"))));
    }

    @Test
    public void testLegalMoveAllAvailable() {
        final Board.Builder boardBuilder = new Board.Builder();
        boardBuilder.setPiece(new King(Alliance.BLACK, 4, false, false));
        boardBuilder.setPiece(new Knight(Alliance.BLACK, 28));
        boardBuilder.setPiece(new Knight(Alliance.WHITE, 36));
        boardBuilder.setPiece(new King(Alliance.WHITE, 60, false, false));
        boardBuilder.setMoveMaker(Alliance.WHITE);

        final Board board = boardBuilder.build();
        final Collection<Move> whiteLegals = board.whitePlayer().getLegalMoves();

        assertEquals(whiteLegals.size(), 13);
        final Move wm1 = Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("e4"), BoardPlugins.getCoordinateAtPosition("d6"));
        final Move wm2 = Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("e4"), BoardPlugins.getCoordinateAtPosition("f6"));
        final Move wm3 = Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("e4"), BoardPlugins.getCoordinateAtPosition("c5"));
        final Move wm4 = Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("e4"), BoardPlugins.getCoordinateAtPosition("g5"));
        final Move wm5 = Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("e4"), BoardPlugins.getCoordinateAtPosition("c3"));
        final Move wm6 = Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("e4"), BoardPlugins.getCoordinateAtPosition("g3"));
        final Move wm7 = Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("e4"), BoardPlugins.getCoordinateAtPosition("d2"));
        final Move wm8 = Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("e4"), BoardPlugins.getCoordinateAtPosition("f2"));

        assertTrue(whiteLegals.contains(wm1));
        assertTrue(whiteLegals.contains(wm2));
        assertTrue(whiteLegals.contains(wm3));
        assertTrue(whiteLegals.contains(wm4));
        assertTrue(whiteLegals.contains(wm5));
        assertTrue(whiteLegals.contains(wm6));
        assertTrue(whiteLegals.contains(wm7));
        assertTrue(whiteLegals.contains(wm8));

        final Board.Builder boardBuilder2 = new Board.Builder();
        boardBuilder2.setPiece(new King(Alliance.BLACK, 4, false, false));
        boardBuilder2.setPiece(new Knight(Alliance.BLACK, 28));
        boardBuilder2.setPiece(new Knight(Alliance.WHITE, 36));
        boardBuilder2.setPiece(new King(Alliance.WHITE, 60, false, false));
        boardBuilder2.setMoveMaker(Alliance.BLACK);

        final Board board2 = boardBuilder2.build();
        final Collection<Move> blackLegals = board.blackPlayer().getLegalMoves();

        final Move bm1 = Move.MoveFactory
                .createMove(board2, BoardPlugins.getCoordinateAtPosition("e5"), BoardPlugins.getCoordinateAtPosition("d7"));
        final Move bm2 = Move.MoveFactory
                .createMove(board2, BoardPlugins.getCoordinateAtPosition("e5"), BoardPlugins.getCoordinateAtPosition("f7"));
        final Move bm3 = Move.MoveFactory
                .createMove(board2, BoardPlugins.getCoordinateAtPosition("e5"), BoardPlugins.getCoordinateAtPosition("c6"));
        final Move bm4 = Move.MoveFactory
                .createMove(board2, BoardPlugins.getCoordinateAtPosition("e5"), BoardPlugins.getCoordinateAtPosition("g6"));
        final Move bm5 = Move.MoveFactory
                .createMove(board2, BoardPlugins.getCoordinateAtPosition("e5"), BoardPlugins.getCoordinateAtPosition("c4"));
        final Move bm6 = Move.MoveFactory
                .createMove(board2, BoardPlugins.getCoordinateAtPosition("e5"), BoardPlugins.getCoordinateAtPosition("g4"));
        final Move bm7 = Move.MoveFactory
                .createMove(board2, BoardPlugins.getCoordinateAtPosition("e5"), BoardPlugins.getCoordinateAtPosition("d3"));
        final Move bm8 = Move.MoveFactory
                .createMove(board2, BoardPlugins.getCoordinateAtPosition("e5"), BoardPlugins.getCoordinateAtPosition("f3"));

        assertEquals(blackLegals.size(), 13);

        assertTrue(blackLegals.contains(bm1));
        assertTrue(blackLegals.contains(bm2));
        assertTrue(blackLegals.contains(bm3));
        assertTrue(blackLegals.contains(bm4));
        assertTrue(blackLegals.contains(bm5));
        assertTrue(blackLegals.contains(bm6));
        assertTrue(blackLegals.contains(bm7));
        assertTrue(blackLegals.contains(bm8));
    }

    @Test
    public void testKnightInCorners() {
        final Board.Builder boardBuilder = new Board.Builder();
        boardBuilder.setPiece(new King(Alliance.BLACK, 4, false, false));
        boardBuilder.setPiece(new Knight(Alliance.BLACK, 0));
        boardBuilder.setPiece(new Knight(Alliance.WHITE, 56));
        boardBuilder.setPiece(new King(Alliance.WHITE, 60, false, false));
        boardBuilder.setMoveMaker(Alliance.WHITE);
        final Board board = boardBuilder.build();
        final Collection<Move> whiteLegals = board.whitePlayer().getLegalMoves();
        final Collection<Move> blackLegals = board.blackPlayer().getLegalMoves();
        assertEquals(whiteLegals.size(), 7);
        assertEquals(blackLegals.size(), 7);
        final Move wm1 = Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("a1"), BoardPlugins.getCoordinateAtPosition("b3"));
        final Move wm2 = Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("a1"), BoardPlugins.getCoordinateAtPosition("c2"));
        assertTrue(whiteLegals.contains(wm1));
        assertTrue(whiteLegals.contains(wm2));
        final Move bm1 = Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("a8"), BoardPlugins.getCoordinateAtPosition("b6"));
        final Move bm2 = Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("a8"), BoardPlugins.getCoordinateAtPosition("c7"));
        assertTrue(blackLegals.contains(bm1));
        assertTrue(blackLegals.contains(bm2));
    }

    @Test
    public void testMiddleBishopOnEmptyBoard() {
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new King(Alliance.BLACK, 4, false, false));
        builder.setPiece(new Bishop(Alliance.WHITE, 35));
        builder.setPiece(new King(Alliance.WHITE, 60, false, false));
        builder.setMoveMaker(Alliance.WHITE);

        final Board board = builder.build();
        final Collection<Move> whiteLegals = board.whitePlayer().getLegalMoves();
        final Collection<Move> blackLegals = board.blackPlayer().getLegalMoves();
        assertEquals(whiteLegals.size(), 18);
        assertEquals(blackLegals.size(), 5);
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("d4"), BoardPlugins.getCoordinateAtPosition("a7"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("d4"), BoardPlugins.getCoordinateAtPosition("b6"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("d4"), BoardPlugins.getCoordinateAtPosition("c5"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("d4"), BoardPlugins.getCoordinateAtPosition("e3"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("d4"), BoardPlugins.getCoordinateAtPosition("f2"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("d4"), BoardPlugins.getCoordinateAtPosition("g1"))));
    }

    @Test
    public void testTopLeftBishopOnEmptyBoard() {
        Board.Builder builder = new Board.Builder();
        builder.setPiece(new King(Alliance.BLACK, 4, false, false));
        builder.setPiece(new Bishop(Alliance.WHITE, 0));
        builder.setPiece(new King(Alliance.WHITE, 60, false, false));
        builder.setMoveMaker(Alliance.WHITE);

        final Board board = builder.build();
        final Collection<Move> whiteLegals = board.whitePlayer().getLegalMoves();
        final Collection<Move> blackLegals = board.blackPlayer().getLegalMoves();
        assertEquals(board.getPiece(0), board.getPiece(0));
        assertNotNull(board.getPiece(0));
        assertEquals(whiteLegals.size(), 12);
        assertEquals(blackLegals.size(), 5);
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("a8"), BoardPlugins.getCoordinateAtPosition("b7"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("a8"), BoardPlugins.getCoordinateAtPosition("c6"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("a8"), BoardPlugins.getCoordinateAtPosition("d5"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("a8"), BoardPlugins.getCoordinateAtPosition("e4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("a8"), BoardPlugins.getCoordinateAtPosition("f3"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("a8"), BoardPlugins.getCoordinateAtPosition("g2"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("a8"), BoardPlugins.getCoordinateAtPosition("h1"))));
    }

    @Test
    public void testTopRightBishopOnEmptyBoard() {
        Board.Builder builder = new Board.Builder();
        builder.setPiece(new King(Alliance.BLACK, 4, false, false));
        builder.setPiece(new Bishop(Alliance.WHITE, 7));
        builder.setPiece(new King(Alliance.WHITE, 60, false, false));
        builder.setMoveMaker(Alliance.WHITE);

        final Board board = builder.build();
        final Collection<Move> whiteLegals = board.whitePlayer().getLegalMoves();
        final Collection<Move> blackLegals = board.blackPlayer().getLegalMoves();
        assertEquals(whiteLegals.size(), 12);
        assertEquals(blackLegals.size(), 5);
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("h8"), BoardPlugins.getCoordinateAtPosition("g7"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("h8"), BoardPlugins.getCoordinateAtPosition("f6"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("h8"), BoardPlugins.getCoordinateAtPosition("e5"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("h8"), BoardPlugins.getCoordinateAtPosition("d4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("h8"), BoardPlugins.getCoordinateAtPosition("c3"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("h8"), BoardPlugins.getCoordinateAtPosition("b2"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("h8"), BoardPlugins.getCoordinateAtPosition("a1"))));
    }

    @Test
    public void testBottomLeftBishopOnEmptyBoard() {
        Board.Builder builder = new Board.Builder();
        builder.setPiece(new King(Alliance.BLACK, 4, false, false));
        builder.setPiece(new Bishop(Alliance.WHITE, 56));
        builder.setPiece(new King(Alliance.WHITE, 60, false, false));
        builder.setMoveMaker(Alliance.WHITE);

        final Board board = builder.build();
        final Collection<Move> whiteLegals = board.whitePlayer().getLegalMoves();
        final Collection<Move> blackLegals = board.blackPlayer().getLegalMoves();
        assertEquals(whiteLegals.size(), 12);
        assertEquals(blackLegals.size(), 5);
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("a1"), BoardPlugins.getCoordinateAtPosition("b2"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("a1"), BoardPlugins.getCoordinateAtPosition("c3"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("a1"), BoardPlugins.getCoordinateAtPosition("d4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("a1"), BoardPlugins.getCoordinateAtPosition("e5"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("a1"), BoardPlugins.getCoordinateAtPosition("f6"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("a1"), BoardPlugins.getCoordinateAtPosition("g7"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("a1"), BoardPlugins.getCoordinateAtPosition("h8"))));
    }

    @Test
    public void testBottomRightBishopOnEmptyBoard() {
        Board.Builder builder = new Board.Builder();
        builder.setPiece(new King(Alliance.BLACK, 4, false, false));
        builder.setPiece(new Bishop(Alliance.WHITE, 63));
        builder.setPiece(new King(Alliance.WHITE, 60, false, false));
        builder.setMoveMaker(Alliance.WHITE);

        final Board board = builder.build();
        final Collection<Move> whiteLegals = board.whitePlayer().getLegalMoves();
        final Collection<Move> blackLegals = board.blackPlayer().getLegalMoves();
        assertEquals(whiteLegals.size(), 12);
        assertEquals(blackLegals.size(), 5);
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("h1"), BoardPlugins.getCoordinateAtPosition("g2"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("h1"), BoardPlugins.getCoordinateAtPosition("f3"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("h1"), BoardPlugins.getCoordinateAtPosition("e4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("h1"), BoardPlugins.getCoordinateAtPosition("d5"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("h1"), BoardPlugins.getCoordinateAtPosition("c6"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("h1"), BoardPlugins.getCoordinateAtPosition("b7"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("h1"), BoardPlugins.getCoordinateAtPosition("a8"))));
    }

    @Test
    public void testMiddleRookOnEmptyBoard() {
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new King(Alliance.BLACK, 4, false, false));
        builder.setPiece(new Rook(Alliance.WHITE, 36));
        builder.setPiece(new King(Alliance.WHITE, 60, false, false));
        builder.setMoveMaker(Alliance.WHITE);

        final Board board = builder.build();
        final Collection<Move> whiteLegals = board.whitePlayer().getLegalMoves();
        final Collection<Move> blackLegals = board.blackPlayer().getLegalMoves();
        assertEquals(whiteLegals.size(), 18);
        assertEquals(blackLegals.size(), 5);
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("e4"), BoardPlugins.getCoordinateAtPosition("e8"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("e4"), BoardPlugins.getCoordinateAtPosition("e7"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("e4"), BoardPlugins.getCoordinateAtPosition("e6"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("e4"), BoardPlugins.getCoordinateAtPosition("e5"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("e4"), BoardPlugins.getCoordinateAtPosition("e3"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("e4"), BoardPlugins.getCoordinateAtPosition("e2"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("e4"), BoardPlugins.getCoordinateAtPosition("a4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("e4"), BoardPlugins.getCoordinateAtPosition("b4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("e4"), BoardPlugins.getCoordinateAtPosition("c4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("e4"), BoardPlugins.getCoordinateAtPosition("d4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("e4"), BoardPlugins.getCoordinateAtPosition("f4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("e4"), BoardPlugins.getCoordinateAtPosition("g4"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardPlugins.getCoordinateAtPosition("e4"), BoardPlugins.getCoordinateAtPosition("h4"))));
    }

    @Test
    public void testPawnPromotion() {
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new Rook(Alliance.BLACK, 3));
        builder.setPiece(new King(Alliance.BLACK, 22, false, false));
        builder.setPiece(new Pawn(Alliance.WHITE, 15));
        builder.setPiece(new King(Alliance.WHITE, 52, false, false));
        builder.setMoveMaker(Alliance.WHITE);

        final Board board = builder.build();
        final Move m1 = Move.MoveFactory.createMove(board, BoardPlugins.getCoordinateAtPosition("h7"),
                BoardPlugins.getCoordinateAtPosition("h8"));
        final MoveTransition t1 = board.getCurrentPlayer().makeMove(m1);
        assertTrue(t1.getMoveStatus().isDone());
        final Move m2 = Move.MoveFactory.createMove(t1.getToBoard(), BoardPlugins.getCoordinateAtPosition("d8"),
                BoardPlugins.getCoordinateAtPosition("h8"));
        final MoveTransition t2 = t1.getToBoard().getCurrentPlayer().makeMove(m2);
        assertTrue(t2.getMoveStatus().isDone());
        final Move m3 = Move.MoveFactory.createMove(t2.getToBoard(), BoardPlugins.getCoordinateAtPosition("e2"),
                BoardPlugins.getCoordinateAtPosition("d2"));
        final MoveTransition t3 = board.getCurrentPlayer().makeMove(m3);
        assertTrue(t3.getMoveStatus().isDone());
    }

    @Test
    public void testSimpleWhiteEnPassant() {
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new King(Alliance.BLACK, 4, false, false));
        builder.setPiece(new Pawn(Alliance.BLACK, 11));
        builder.setPiece(new Pawn(Alliance.WHITE, 52));
        builder.setPiece(new King(Alliance.WHITE, 60, false, false));
        builder.setMoveMaker(Alliance.WHITE);

        final Board board = builder.build();
        final Move m1 = Move.MoveFactory.createMove(board, BoardPlugins.getCoordinateAtPosition("e2"),
                BoardPlugins.getCoordinateAtPosition("e4"));
        final MoveTransition t1 = board.getCurrentPlayer().makeMove(m1);
        assertTrue(t1.getMoveStatus().isDone());
        final Move m2 = Move.MoveFactory.createMove(t1.getToBoard(), BoardPlugins.getCoordinateAtPosition("e8"),
                BoardPlugins.getCoordinateAtPosition("d8"));
        final MoveTransition t2 = t1.getToBoard().getCurrentPlayer().makeMove(m2);
        assertTrue(t2.getMoveStatus().isDone());
        final Move m3 = Move.MoveFactory.createMove(t2.getToBoard(), BoardPlugins.getCoordinateAtPosition("e4"),
                BoardPlugins.getCoordinateAtPosition("e5"));
        final MoveTransition t3 = t2.getToBoard().getCurrentPlayer().makeMove(m3);
        assertTrue(t3.getMoveStatus().isDone());
        final Move m4 = Move.MoveFactory.createMove(t3.getToBoard(), BoardPlugins.getCoordinateAtPosition("d7"),
                BoardPlugins.getCoordinateAtPosition("d5"));
        final MoveTransition t4 = t3.getToBoard().getCurrentPlayer().makeMove(m4);
        assertTrue(t4.getMoveStatus().isDone());
        final Move m5 = Move.MoveFactory.createMove(t4.getToBoard(), BoardPlugins.getCoordinateAtPosition("e5"),
                BoardPlugins.getCoordinateAtPosition("d6"));
        final MoveTransition t5 = t4.getToBoard().getCurrentPlayer().makeMove(m5);
        assertTrue(t5.getMoveStatus().isDone());
    }

    @Test
    public void testSimpleBlackEnPassant() {
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new King(Alliance.BLACK, 4, false, false));
        builder.setPiece(new Pawn(Alliance.BLACK, 11));
        builder.setPiece(new Pawn(Alliance.WHITE, 52));
        builder.setPiece(new King(Alliance.WHITE, 60, false, false));
        builder.setMoveMaker(Alliance.WHITE);

        final Board board = builder.build();
        final Move m1 = Move.MoveFactory.createMove(board, BoardPlugins.getCoordinateAtPosition("e1"),
                BoardPlugins.getCoordinateAtPosition("d1"));
        final MoveTransition t1 = board.getCurrentPlayer().makeMove(m1);
        assertTrue(t1.getMoveStatus().isDone());
        final Move m2 = Move.MoveFactory.createMove(t1.getToBoard(), BoardPlugins.getCoordinateAtPosition("d7"),
                BoardPlugins.getCoordinateAtPosition("d5"));
        final MoveTransition t2 = t1.getToBoard().getCurrentPlayer().makeMove(m2);
        assertTrue(t2.getMoveStatus().isDone());
        final Move m3 = Move.MoveFactory.createMove(t2.getToBoard(), BoardPlugins.getCoordinateAtPosition("d1"),
                BoardPlugins.getCoordinateAtPosition("c1"));
        final MoveTransition t3 = t2.getToBoard().getCurrentPlayer().makeMove(m3);
        assertTrue(t3.getMoveStatus().isDone());
        final Move m4 = Move.MoveFactory.createMove(t3.getToBoard(), BoardPlugins.getCoordinateAtPosition("d5"),
                BoardPlugins.getCoordinateAtPosition("d4"));
        final MoveTransition t4 = t3.getToBoard().getCurrentPlayer().makeMove(m4);
        assertTrue(t4.getMoveStatus().isDone());
        final Move m5 = Move.MoveFactory.createMove(t4.getToBoard(), BoardPlugins.getCoordinateAtPosition("e2"),
                BoardPlugins.getCoordinateAtPosition("e4"));
        final MoveTransition t5 = t4.getToBoard().getCurrentPlayer().makeMove(m5);
        assertTrue(t5.getMoveStatus().isDone());
        final Move m6 = Move.MoveFactory.createMove(t5.getToBoard(), BoardPlugins.getCoordinateAtPosition("d4"),
                BoardPlugins.getCoordinateAtPosition("e3"));
        final MoveTransition t6 = t5.getToBoard().getCurrentPlayer().makeMove(m6);
        assertTrue(t6.getMoveStatus().isDone());
    }

    @Test
    public void testEnPassant2() {
        final Board board = Board.createStandardBoard();
        final Move m1 = Move.MoveFactory.createMove(board, BoardPlugins.getCoordinateAtPosition("e2"),
                BoardPlugins.getCoordinateAtPosition("e3"));
        final MoveTransition t1 = board.getCurrentPlayer().makeMove(m1);
        assertTrue(t1.getMoveStatus().isDone());
        final Move m2 = Move.MoveFactory.createMove(t1.getToBoard(), BoardPlugins.getCoordinateAtPosition("h7"),
                BoardPlugins.getCoordinateAtPosition("h5"));
        final MoveTransition t2 = t1.getToBoard().getCurrentPlayer().makeMove(m2);
        assertTrue(t2.getMoveStatus().isDone());
        final Move m3 = Move.MoveFactory.createMove(t2.getToBoard(), BoardPlugins.getCoordinateAtPosition("e3"),
                BoardPlugins.getCoordinateAtPosition("e4"));
        final MoveTransition t3 = t2.getToBoard().getCurrentPlayer().makeMove(m3);
        assertTrue(t3.getMoveStatus().isDone());
        final Move m4 = Move.MoveFactory.createMove(t3.getToBoard(), BoardPlugins.getCoordinateAtPosition("h5"),
                BoardPlugins.getCoordinateAtPosition("h4"));
        final MoveTransition t4 = t3.getToBoard().getCurrentPlayer().makeMove(m4);
        assertTrue(t4.getMoveStatus().isDone());
        final Move m5 = Move.MoveFactory.createMove(t4.getToBoard(), BoardPlugins.getCoordinateAtPosition("g2"),
                BoardPlugins.getCoordinateAtPosition("g4"));
        final MoveTransition t5 = t4.getToBoard().getCurrentPlayer().makeMove(m5);
        assertTrue(t5.getMoveStatus().isDone());
    }

    @Test
    public void testKingEquality() {
        final Board board = Board.createStandardBoard();
        final Board board2 = Board.createStandardBoard();
        assertEquals(board.getPiece(60), board2.getPiece(60));
        assertNotEquals(null, board.getPiece(60));
    }

    @Test
    public void testHashCode() {
        final Board board = Board.createStandardBoard();
        final Set<Piece> pieceSet = new HashSet<>(board.getAllPieces());
        final Set<Piece> whitePieceSet = new HashSet<>(board.getWhitePieces());
        final Set<Piece> blackPieceSet = new HashSet<>(board.getBlackPieces());
        assertEquals(32, pieceSet.size());
        assertEquals(16, whitePieceSet.size());
        assertEquals(16, blackPieceSet.size());
    }
}
