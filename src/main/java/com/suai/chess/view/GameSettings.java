package com.suai.chess.view;

import com.suai.chess.model.pieces.Alliance;
import com.suai.chess.model.player.Player;
import com.suai.chess.view.Table.PlayerType;

import javax.swing.*;
import java.awt.*;

class GameSettings extends JDialog {
    private PlayerType whitePlayerType;
    private PlayerType blackPlayerType;

    private static final String HUMAN_TEXT = "Human";
    private static final String COMPUTER_TEXT = "Computer";

    GameSettings(JFrame frame,
                 boolean modal,
                 boolean isWhitePlayerComputer,
                 boolean isBlackPlayerComputer) {
        super(frame, modal);
        final JPanel myPanel = new JPanel(new GridLayout(0, 1));
        final JRadioButton whiteHumanButton = new JRadioButton(HUMAN_TEXT);
        final JRadioButton whiteComputerButton = new JRadioButton(COMPUTER_TEXT);
        final JRadioButton blackHumanButton = new JRadioButton(HUMAN_TEXT);
        final JRadioButton blackComputerButton = new JRadioButton(COMPUTER_TEXT);
        whiteHumanButton.setActionCommand(HUMAN_TEXT);
        final ButtonGroup whiteGroup = new ButtonGroup();
        whiteGroup.add(whiteHumanButton);
        whiteGroup.add(whiteComputerButton);

        final ButtonGroup blackGroup = new ButtonGroup();
        blackGroup.add(blackHumanButton);
        blackGroup.add(blackComputerButton);

        if(isWhitePlayerComputer) {
            whiteComputerButton.setSelected(true);
        } else {
            whiteHumanButton.setSelected(true);
        }

        if(isBlackPlayerComputer) {
            blackComputerButton.setSelected(true);
        } else {
            blackHumanButton.setSelected(true);
        }
        changePlayer(whiteComputerButton, blackComputerButton);

        getContentPane().add(myPanel);
        myPanel.add(new JLabel("White"));
        myPanel.add(whiteHumanButton);
        myPanel.add(whiteComputerButton);
        myPanel.add(new JLabel("Black"));
        myPanel.add(blackHumanButton);
        myPanel.add(blackComputerButton);


        final JButton cancelButton = new JButton("Cancel");
        final JButton okButton = new JButton("OK");

        okButton.addActionListener(actionEvent -> {
            whitePlayerType = whiteComputerButton.isSelected() ? PlayerType.COMPUTER : PlayerType.HUMAN;
            blackPlayerType = blackComputerButton.isSelected() ? PlayerType.COMPUTER : PlayerType.HUMAN;

            GameSettings.this.setVisible(false);
        });

        cancelButton.addActionListener(actionEvent -> {
            System.out.println("Cancel");
            GameSettings.this.setVisible(false);
        });

        myPanel.add(cancelButton);
        myPanel.add(okButton);

        setLocationRelativeTo(frame);
        pack();
        setVisible(false);
    }

    public void changePlayer(JRadioButton w, JRadioButton b) {
        whitePlayerType = w.isSelected() ? PlayerType.COMPUTER : PlayerType.HUMAN;
        blackPlayerType = b.isSelected() ? PlayerType.COMPUTER : PlayerType.HUMAN;
    }

    public void promptUser() {
        setVisible(true);
        repaint();
    }

    public boolean isAIPlayer(final Player player) {
        if(player.getAlliance() == Alliance.WHITE) {
            return getWhitePlayerType() == PlayerType.COMPUTER;
        }
        return getBlackPlayerType() == PlayerType.COMPUTER;
    }

    PlayerType getWhitePlayerType() {
        return this.whitePlayerType;
    }

    PlayerType getBlackPlayerType() {
        return this.blackPlayerType;
    }
}
