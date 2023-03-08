package com.suai.chess.io;

import com.suai.chess.view.Table;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FilePlugins {

    public static void writeGameToPGNFile(final File pgnFile,
                                          final Table.MoveLog moveLog) throws IOException {
        final StringBuilder builder = new StringBuilder();
        builder.append(nameEvent()).append("\n");
        builder.append(currentData()).append("\n");
        builder.append(numberOfMoves(moveLog)).append("\n");

        for(int i = 0, j = 1; i < moveLog.size(); i++) {
            if(i % 2 == 0) {
                builder.append(j).append(".");
                j++;
            }
            builder.append(moveLog.getMoves().get(i)).append(" ");
            System.out.println(moveLog.getMoves().get(i).toString());
        }

        try (final Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pgnFile, true)))) {
            writer.write(builder.toString());
        }
    }

    private static String nameEvent() {
        return "[Event \"" + "Black Widow Game" + "\"]";
    }

    private static String currentData() {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        return "[Date \"" + dateFormat.format(new Date()) + "\"]";
    }

    private static String numberOfMoves(final Table.MoveLog moveLog) {
        return "[PlyCount \"" + moveLog.size() + "\"]";
    }


    public static void readGameToPGNFile(File pgnFile) throws IOException {
        try(final BufferedReader br = new BufferedReader(new FileReader(pgnFile))) {
            String line;
            StringBuilder sb = new StringBuilder();
            while((line = br.readLine()) != null) {
                if(!line.isEmpty()) {
                    if(isDescriptionGame(line)) {
                        continue;
                    } else if(isEngOfGame(line)) {
                        String[] ending = line.split(" ");
                        String outcome = ending[ending.length - 1];
                        sb.append(line.replace(outcome, "")).append(" ");
                        String gameText = sb.toString().trim();
                        if(!gameText.isEmpty()) {
                            //createGame(gameText, outcome);
                        }
                    } else {
                        sb.append(line).append(" ");
                    }
                }
            }
            br.readLine();
        }
        System.out.println("read done");
    }

    private static boolean isEngOfGame(String line) {
        return line.endsWith("1-0") || line.endsWith("0-1") ||
                line.endsWith("1/2-1/2") || line.endsWith("*");
    }

    private static boolean isDescriptionGame(String str) {
        return str.startsWith("[") && str.endsWith("]");
    }
}