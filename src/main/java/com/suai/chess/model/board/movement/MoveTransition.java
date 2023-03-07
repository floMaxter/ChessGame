package com.suai.chess.model.board.movement;

import com.suai.chess.model.board.Board;
import com.suai.chess.model.board.movement.Move.MoveStatus;
public class MoveTransition {
    private final Board fromBoard;
    private final Board toBoard;
    private final Move transitionMove;
    private final MoveStatus moveStatus;

    public MoveTransition(Board fromBoard,
                          Board toBoard,
                          Move transitionMove,
                          MoveStatus moveStatus) {
        this.fromBoard = fromBoard;
        this.toBoard = toBoard;
        this.transitionMove = transitionMove;
        this.moveStatus = moveStatus;
    }

    public MoveStatus getMoveStatus() {
        return this.moveStatus;
    }

    public Board getToBoard() {
        return this.toBoard;
    }

    public Board getFromBoard() {
        return this.fromBoard;
    }
    public Move getTransitionMove() {
        return this.transitionMove;
    }
}