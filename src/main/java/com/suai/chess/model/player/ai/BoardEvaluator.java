package com.suai.chess.model.player.ai;

import com.suai.chess.model.board.Board;

public interface BoardEvaluator {
    int evaluate(Board board, int depth);
}
