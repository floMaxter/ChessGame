package com.suai.chess.view;

import com.suai.chess.model.board.movement.Move;
import com.suai.chess.model.pieces.Piece;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.suai.chess.view.Table.*;

public class TakenPiecesPanel extends JPanel {
    private final JPanel northPanel;
    private final JPanel southPanel;

    private static final Color PANEL_COLOR = Color.decode("#ffffff0");
    private static final Dimension TAKEN_PIECES_DIMENSION = new Dimension(40, 80);
    private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);

    public TakenPiecesPanel() {
        super(new BorderLayout());
        this.setBackground(PANEL_COLOR);
        this.setBorder(PANEL_BORDER);
        this.northPanel = new JPanel(new GridLayout(8, 2));
        this.southPanel = new JPanel(new GridLayout(8, 2));
        this.northPanel.setBackground(PANEL_COLOR);
        this.southPanel.setBackground(PANEL_COLOR);
        this.add(this.northPanel, BorderLayout.NORTH);
        this.add(this.southPanel, BorderLayout.SOUTH);
        setPreferredSize(TAKEN_PIECES_DIMENSION);
    }

    public void remake(final MoveLog moveLog) {
        this.southPanel.removeAll();
        this.northPanel.removeAll();

        final List<Piece> whiteTakenPieces = new ArrayList<>();
        final List<Piece> blackTakenPieces = new ArrayList<>();

        for(final Move move : moveLog.getMoves()) {
            if(move.isAttack()) {
                final Piece takenPiece = move.getAttackedPiece();
                if(takenPiece.getPieceAlliance().isWhite()) {
                    whiteTakenPieces.add(takenPiece);
                } else if(takenPiece.getPieceAlliance().isBlack()) {
                    blackTakenPieces.add(takenPiece);
                } else {
                    throw new RuntimeException("Shouldn't reach here!");
                }
            }
        }

        whiteTakenPieces.sort(Comparator.comparingInt(Piece::getPieceWeight));

        blackTakenPieces.sort(Comparator.comparingInt(Piece::getPieceWeight));

        for(final Piece takenPiece : whiteTakenPieces) {
            try {
                final BufferedImage image =
                        ImageIO.read(new File( "pictures/pieces/plain/" +
                                takenPiece.getPieceAlliance().toString().charAt(0) + takenPiece + ".gif"));
                final ImageIcon icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel(icon);
                this.southPanel.add(imageLabel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for(final Piece takenPiece : blackTakenPieces) {
            try {
                final BufferedImage image =
                        ImageIO.read(new File( "pictures/pieces/plain/" +
                                takenPiece.getPieceAlliance().toString().charAt(0) + takenPiece + ".gif"));
                final ImageIcon icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel(icon);
                this.northPanel.add(imageLabel);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        validate();
    }
}
