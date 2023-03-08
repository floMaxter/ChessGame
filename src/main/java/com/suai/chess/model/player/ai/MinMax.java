package com.suai.chess.model.player.ai;

import com.suai.chess.model.board.Board;
import com.suai.chess.model.board.movement.Move;
import com.suai.chess.model.board.movement.MoveTransition;

public class MinMax {
    private final BoardEvaluator boardEvaluator;
    private final int searchDepth;

    public MinMax(int searchDepth) {
        this.boardEvaluator = new StandardBoardEvaluator();
        this.searchDepth = searchDepth;
    }
    public Move perform(Board board) {
        Move bestMove = null;
        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentValue;

        System.out.println(board.getCurrentPlayer() + "THINKING with depth = " + this.searchDepth);
        for (Move move : board.getCurrentPlayer().getLegalMoves()) {
            MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                currentValue = board.getCurrentPlayer().getAlliance().isWhite() ?
                        min(moveTransition.getToBoard(), this.searchDepth - 1) :
                        max(moveTransition.getToBoard(), this.searchDepth - 1);
                if (board.getCurrentPlayer().getAlliance().isWhite() && currentValue >= highestSeenValue) {
                    highestSeenValue = currentValue;
                    bestMove = move;
                } else if (board.getCurrentPlayer().getAlliance().isBlack() && currentValue <= lowestSeenValue) {
                    lowestSeenValue = currentValue;
                    bestMove = move;
                }
            }
        }
        return bestMove;
    }

    public int min(Board board, int depth) {
        if (depth == 0 || isGameOver(board)) {
            return this.boardEvaluator.evaluate(board, depth);
        }
        int lowestSeenValue = Integer.MAX_VALUE;
        for (Move move : board.getCurrentPlayer().getLegalMoves()) {
            MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                int currentValue = max(moveTransition.getToBoard(), depth - 1);
                if (currentValue <= lowestSeenValue) {
                    lowestSeenValue = currentValue;
                }
            }
        }
        return lowestSeenValue;
    }

    public int max(Board board, int depth) {
        if (depth == 0 || isGameOver(board)) {
            return this.boardEvaluator.evaluate(board, depth);
        }
        int highestSeenValue = Integer.MIN_VALUE;
        for (Move move : board.getCurrentPlayer().getLegalMoves()) {
            MoveTransition moveTransition = board.getCurrentPlayer().makeMove(move);
            if (moveTransition.getMoveStatus().isDone()) {
                int currentValue = min(moveTransition.getToBoard(), depth - 1);
                if (currentValue >= highestSeenValue) {
                    highestSeenValue = currentValue;
                }
            }
        }
        return highestSeenValue;
    }

    private boolean isGameOver(Board board) {
        return board.getCurrentPlayer().isInCheckMate() ||
                board.getCurrentPlayer().isInStaleMate();
    }

    @Override
    public String toString() {
        return "MiniMax";
    }
}
