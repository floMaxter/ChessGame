package com.suai.chess.model.board;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BoardPlugins {
    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static final boolean[] EIGHTH_COLUMN = initColumn(7);


    public static final boolean[] EIGHTH_ROW_FROM_BELOW = initRow(0);
    public static final boolean[] SEVENTH_ROW_FROM_BELOW = initRow(8);
    public static final boolean[] SIXTH_ROW_FROM_BELOW = initRow(16);
    public static final boolean[] FIFTH_ROW_FROM_BELOW = initRow(24);
    public static final boolean[] FOURTH_ROW_FROM_BELOW = initRow(32);
    public static final boolean[] THIRD_ROW_FROM_BELOW = initRow(40);
    public static final boolean[] SECOND_ROW_FROM_BELOW = initRow(48);
    public static final boolean[] FIRST_ROW_FROM_BELOW = initRow(56);


    public static final String[] ALGEBRAIC_NOTATION = initializeAlgebraicNotation();
    public static final Map<String, Integer> POSITION_TO_COORDINATE = initializePositionToCoordinateMap();


    public static final int NUM_TILES = 64;
    public static final int NUM_TILES_PER_COLUMN_OR_ROW = 8;

    private BoardPlugins() {
        throw new RuntimeException("It is impossible to create such a board!");
    }

    private static String[] initializeAlgebraicNotation() {
        /*"a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
         "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
         "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
         "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
         "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
         "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
         "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
         "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"));*/

        String[] finalArray = new String[64];
        int[] numbers = {8, 7, 6, 5, 4, 3, 2, 1};
        String[] letters = {"a", "b", "c", "d", "e", "f", "g", "h"};
        int count = 0;
        for(int x : numbers) {
            for (String y : letters) {
                finalArray[count] = y + x;
                count++;
            }
        }
        return finalArray;
    }

    private static Map<String, Integer> initializePositionToCoordinateMap() {
        Map<String, Integer> positionToCoordinate = new HashMap<>();
        for (int i = 0; i < NUM_TILES; i++) {
            positionToCoordinate.put(ALGEBRAIC_NOTATION[i], i);
        }
        return Collections.unmodifiableMap(positionToCoordinate);
    }

    private static boolean[] initColumn(int columnNumber) {
        boolean[] column = new boolean[NUM_TILES];
        for (int i = 0; i < NUM_TILES_PER_COLUMN_OR_ROW; i++)
            column[i * NUM_TILES_PER_COLUMN_OR_ROW + columnNumber] = true;
        return column;
    }

    private static boolean[] initRow(int rowNumber) {
        final boolean[] row = new boolean[NUM_TILES];
        do {
            row[rowNumber] = true;
            rowNumber++;
        } while ((rowNumber % NUM_TILES_PER_COLUMN_OR_ROW) != 0);
        return row;
    }

    public static boolean isValidTileCoordinate(int coordinate) {
        return coordinate >= 0 && coordinate < NUM_TILES;
    }

    public static int getCoordinateAtPosition(String position) {
        return POSITION_TO_COORDINATE.get(position);
    }
    public static String getPositionAtCoordinate(int destinationCoordinate) {
        return ALGEBRAIC_NOTATION[destinationCoordinate];
    }

    public static boolean isEndGame(Board board) {
        return board.getCurrentPlayer().isInCheckMate() ||
                board.getCurrentPlayer().getOpponent().isInCheckMate();
    }

    public static boolean isCheckSituation(Board board) {
        return board.getCurrentPlayer().isInCheck() ||
                board.getCurrentPlayer().getOpponent().isInCheck();
    }
}
