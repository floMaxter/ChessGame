package com.suai.chess.model.pieces;

public enum Alliance {
    WHITE {
        @Override
        public boolean isBlack() {
            return false;
        }
        @Override
        public boolean isWhite() {
            return true;
        }
        @Override
        public int getDirection() {
            return -1;
        }
        @Override
        public int getOppositeDirection() {
            return 1;
        }
        @Override
        public boolean isPawnPromotionSquare(int position) {
            return BoardPlugins.EIGHTH_ROW_FROM_BELOW[position];
        }
        @Override
        public Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer) {
            return whitePlayer;
        }

        @Override
        public String toString() {
            return "White";
        }
    },
    BLACK {
        @Override
        public boolean isBlack() {
            return true;
        }
        @Override
        public boolean isWhite() {
            return false;
        }
        @Override
        public int getDirection() {
            return 1;
        }
        @Override
        public int getOppositeDirection() {
            return -1;
        }
        @Override
        public boolean isPawnPromotionSquare(int position) {
            return BoardPlugins.FIRST_ROW_FROM_BELOW[position];
        }
        @Override
        public Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer) {
            return blackPlayer;
        }
        @Override
        public String toString() {
            return "Black";
        }
    };
    public abstract int getDirection();
    public abstract int getOppositeDirection();
    public abstract boolean isBlack();
    public abstract boolean isWhite();
    public abstract boolean isPawnPromotionSquare(int position);

    public abstract Player choosePlayer(WhitePlayer whitePlayer, BlackPlayer blackPlayer);
}
