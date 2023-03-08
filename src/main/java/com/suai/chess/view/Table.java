package com.suai.chess.view;


import com.suai.chess.io.FilePlugins;
import com.suai.chess.model.board.*;
import com.suai.chess.model.board.movement.Move;
import com.suai.chess.model.board.movement.MoveTransition;
import com.suai.chess.model.pieces.Piece;
import com.suai.chess.model.player.Player;
import com.suai.chess.model.player.ai.MinMax;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static com.suai.chess.model.board.movement.Move.MoveFactory.*;
import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Table extends Observable {
    private final JFrame gameFrame;
    private final JFrame menuFrame;
    private final HistoryPanel historyPanel;
    private final TakenPiecesPanel takenPiecesPanel;
    private final BoardPanel boardPanel;
    private Board chessBoard;
    private final MoveLog moveLog;
    private GameSettings gameSettings;
    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;
    private BoardDirection boardDirection;
    private boolean highlightLegalMoves;

    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(400, 350);
    private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10, 10);
    private static final String defaultPieceImagesPath = "pictures/pieces/plain/";

    private final Color lightTileColor = Color.decode("#E0FFFF");
    private final Color darkTileColor = Color.decode("#87CEEB");

    private static final Table INSTANCE = new Table();

    private Table() {
        this.menuFrame = createMenuFrame();
        this.menuFrame.setVisible(true);

        this.gameFrame = createJFrame("Game Chess");
        this.gameFrame.setVisible(false);

        this.gameFrame.setJMenuBar(createTableMenuBar());
        this.historyPanel = new HistoryPanel();
        this.takenPiecesPanel = new TakenPiecesPanel();

        this.chessBoard = Board.createStandardBoard();
        this.boardPanel = new BoardPanel();
        this.moveLog = new MoveLog();
        this.addObserver(new TableGameAIWatcher());
        this.gameSettings = new GameSettings(this.gameFrame, true, false, false);
        this.boardDirection = BoardDirection.NORMAL;
        this.highlightLegalMoves = false;

        this.gameFrame.add(this.takenPiecesPanel, BorderLayout.WEST);
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.add(this.historyPanel, BorderLayout.EAST);
        gameFrame.revalidate();
    }

    public static Table get() {
        return INSTANCE;
    }

    private GameSettings getGameSetup() {
        return this.gameSettings;
    }

    private Board getGameBoard() {
        return this.chessBoard;
    }

    public void setGameBoard(final Board board) {
        this.chessBoard = board;
    }

    private MoveLog getMoveLog() {
        return this.moveLog;
    }

    private JFrame getGameFrame() {
        return this.gameFrame;
    }

    private HistoryPanel getGameHistoryPanel() {
        return this.historyPanel;
    }

    private TakenPiecesPanel getTakenPiecesPanel() {
        return this.takenPiecesPanel;
    }

    private BoardPanel getBoardPanel() {
        return this.boardPanel;
    }

    public void show() {
        Table.get().getMoveLog().clear();
        Table.get().getGameHistoryPanel().remake(Table.get().getGameBoard(), Table.get().getMoveLog());
        Table.get().getTakenPiecesPanel().remake(Table.get().getMoveLog());
        Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
    }

    public JFrame createMenuFrame() {
        JFrame menuFrame = createJFrame("Menu");

        Container container = menuFrame.getContentPane();

        FlowLayout myFlow1 = new FlowLayout();
        myFlow1.setVgap(50);
        JPanel buttonPanel = new JPanel(myFlow1);
        FlowLayout myFlow2 = new FlowLayout();
        myFlow2.setVgap(50);
        JPanel labelPanel = new JPanel(myFlow2);

        JButton playWithPeople = new JButton("Play with people");
        playWithPeople.setFont(new Font("TimesRoman", Font.BOLD, 20));
        playWithPeople.setPreferredSize(new Dimension(500, 50));
        playWithPeople.addActionListener(actionEvent -> {
            createGameFrame(false, false);
        });
        buttonPanel.add(playWithPeople);

        JButton playWithComputer = new JButton("Play with computer");
        playWithComputer.setFont(new Font("TimesRoman", Font.BOLD, 20));
        playWithComputer.setPreferredSize(new Dimension(500, 50));
        playWithComputer.addActionListener(actionEvent -> {
            createGameFrame(false, true);
        });
        buttonPanel.add(playWithComputer);

        JButton loadGame = new JButton("Load Game");
        loadGame.setFont(new Font("TimesRoman", Font.BOLD, 20));
        loadGame.setPreferredSize(new Dimension(500, 50));
        loadGame.addActionListener(actionEvent -> {
            //Load game
        });
        buttonPanel.add(loadGame);

        JButton exit = new JButton("Exit");
        exit.setFont(new Font("TimesRoman", Font.BOLD, 20));
        exit.setPreferredSize(new Dimension(500, 50));
        exit.addActionListener(actionEvent -> {
            System.exit(0);
        });
        buttonPanel.add(exit);

        JLabel label = new JLabel("Chess");
        label.setFont(new Font("TimesRoman", Font.BOLD, 22));
        label.setPreferredSize(new Dimension(85, 50));
        labelPanel.add(label);

        JPanel content = new JPanel(new BorderLayout());
        content.add(labelPanel, BorderLayout.NORTH);
        content.add(buttonPanel, BorderLayout.CENTER);
        container.add(content);

        menuFrame.setVisible(true);

        return menuFrame;
    }

    public void createGameFrame(boolean isWhitePlayerComputer, boolean isBlackPlayerComputer) {
        this.menuFrame.setVisible(false);
        this.gameFrame.setVisible(true);
        this.gameSettings = new GameSettings(this.gameFrame, true, isWhitePlayerComputer, isBlackPlayerComputer);
        Table.get().setupUpdate(Table.get().getGameSetup());
        gameFrame.revalidate();
    }

    public static JFrame createJFrame(final String nameFrame) {
        JFrame jFrame = new JFrame(nameFrame);
        jFrame.setLayout(new BorderLayout());
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int sizeWidth = 800;
        int sizeHeight = 800;
        int locationX = (screenSize.width - sizeWidth) / 2;
        int locationY = (screenSize.height - sizeHeight) / 2;
        jFrame.setBounds(locationX, locationY, sizeWidth, sizeHeight);
        return jFrame;
    }

    private JMenuBar createTableMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createOptionsMenu());
        final JMenuItem removeMoveMenuItem = new JMenuItem("Remove last move", 'R');
        removeMoveMenuItem.addActionListener(actionEvent -> {
            if (Table.get().getMoveLog().size() > 0) {
                removeLastMove();
            }
        });
        tableMenuBar.add(removeMoveMenuItem);

        return tableMenuBar;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');

        final JMenuItem openPGN = new JMenuItem("Load PGN File", 'L');
        openPGN.addActionListener(actionEvent -> {
            System.out.println("Open up that PGN file!");
            //TODO: add open file
            JFileChooser chooser = new JFileChooser();
            int option = chooser.showOpenDialog(Table.get().getGameFrame());
            if (option == JFileChooser.APPROVE_OPTION) {
                loadPGNFile(chooser.getSelectedFile());
            }
        });
        fileMenu.add(openPGN);

        JMenuItem saveToPGN = new JMenuItem("Save Game", 'S');
        saveToPGN.addActionListener(actionEvent -> {
            System.out.println("Save Game");
            final JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return file.isDirectory() || file.getName().toLowerCase().endsWith("pgn");
                }
                @Override
                public String getDescription() {
                    return ".pgn";
                }
            });
            int option = chooser.showSaveDialog(Table.get().getGameHistoryPanel());
            if (option == JFileChooser.APPROVE_OPTION) {
                savePGNFile(chooser.getSelectedFile());
            }
        });
        fileMenu.add(saveToPGN);


        final JMenuItem exitMenuItem = new JMenuItem("Exit", 'E');
        exitMenuItem.addActionListener(actionEvent -> System.exit(0));
        JMenuItem exit = fileMenu.add(exitMenuItem);
        exit.addActionListener(actionEvent -> System.exit(0));
        exit.setAccelerator(KeyStroke.getKeyStroke("ctrl E"));

        return fileMenu;
    }

    private JMenu createOptionsMenu() {
        final JMenu optionsMenu = new JMenu("Options");
        optionsMenu.setMnemonic('O');

        final JMenuItem newGame = new JMenuItem("New Game", 'N');
        newGame.addActionListener(actionEvent -> {
            removeAllMove();
        });
        optionsMenu.add(newGame);

        final JMenuItem setupGameMenuItem = new JMenuItem("Settings Game", 'S');
        setupGameMenuItem.addActionListener(actionEvent -> {
            Table.get().getGameSetup().promptUser();
            Table.get().setupUpdate(Table.get().getGameSetup());
        });
        optionsMenu.add(setupGameMenuItem);

        final JMenuItem currentStateMenuItem = new JMenuItem("Current State", 'C');
        currentStateMenuItem.addActionListener(actionEvent -> {
            System.out.println(this.getGameBoard().getWhitePieces());
            System.out.println(this.getGameBoard().getBlackPieces());
            System.out.println(playerInfo(this.getGameBoard().getCurrentPlayer()));
            System.out.println(playerInfo(this.getGameBoard().getCurrentPlayer().getOpponent()));
        });
        optionsMenu.add(currentStateMenuItem);


        final JMenuItem flipBoardMenuItem = new JMenuItem("Flip Board", 'B');
        flipBoardMenuItem.addActionListener(actionEvent -> {
            boardDirection = boardDirection.opposite();
            boardPanel.drawBoard(chessBoard);
        });
        JMenuItem flipBoard = optionsMenu.add(flipBoardMenuItem);
        flipBoard.addActionListener(actionEvent -> {
            boardDirection = boardDirection.opposite();
            boardPanel.drawBoard(chessBoard);
        });
        flipBoard.setAccelerator(KeyStroke.getKeyStroke("ctrl B"));
        optionsMenu.addSeparator();

        final JCheckBoxMenuItem legalMovesHighlightCheckbox = new JCheckBoxMenuItem("Highlight Legal Moves", false);
        legalMovesHighlightCheckbox.setMnemonic('H');
        legalMovesHighlightCheckbox.addActionListener(actionEvent -> {
            highlightLegalMoves = legalMovesHighlightCheckbox.isSelected();
        });
        optionsMenu.add(legalMovesHighlightCheckbox);

        return optionsMenu;
    }

    private static void loadPGNFile(final File pgnFile) {
        try {
            FilePlugins.readGameToPGNFile(pgnFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void savePGNFile(final File pgnFile) {
        try {
            FilePlugins.writeGameToPGNFile(pgnFile, Table.INSTANCE.getMoveLog());
        } catch (final IOException ex) {
            ex.printStackTrace();
        }
    }

    private static String playerInfo(final Player player) {
        return ("Player is: " + player.getAlliance() + "\nlegal moves (" + player.getLegalMoves().size() + ") = " + player.getLegalMoves() + "\ninCheck = " +
                player.isInCheck() + "\nisInCheckMate = " + player.isInCheckMate() +
                "\nisCastled = " + player.isCastled()) + "\n";
    }

    private void removeLastMove() {
        final Move lastMove = Table.get().getMoveLog().removeMove(Table.get().getMoveLog().size() - 1);
        this.chessBoard = this.chessBoard.getCurrentPlayer().removeMakeMove(lastMove).getToBoard();
        Table.get().getMoveLog().removeMove(lastMove);
        Table.get().getGameHistoryPanel().remake(chessBoard, Table.get().getMoveLog());
        Table.get().getTakenPiecesPanel().remake(Table.get().getMoveLog());
        Table.get().getBoardPanel().drawBoard(chessBoard);
    }

    private void removeAllMove() {
        for (int i = Table.get().getMoveLog().size() - 1; i >= 0; i--) {
            removeLastMove();
        }
    }

    private void setupUpdate(final GameSettings gameSettings) {
        setChanged();
        notifyObservers(gameSettings);
    }

    private void moveMadeUpdate(final PlayerType playerType) {
        setChanged();
        notifyObservers(playerType);
    }


    public enum BoardDirection {
        NORMAL {
            @Override
            List<TilePanel> traverse(List<TilePanel> boardTiles) {
                return boardTiles;
            }

            @Override
            BoardDirection opposite() {
                return FLIPPED;
            }
        },
        FLIPPED {
            @Override
            List<TilePanel> traverse(List<TilePanel> boardTiles) {
                for (int i = 0, j = boardTiles.size() - 1; i < j; i++) {
                    boardTiles.add(i, boardTiles.remove(j));
                }
                return boardTiles;
            }

            @Override
            BoardDirection opposite() {
                return NORMAL;
            }
        };

        abstract List<TilePanel> traverse(final List<TilePanel> boardTiles);

        abstract BoardDirection opposite();
    }

    enum PlayerType {
        HUMAN,
        COMPUTER
    }

    public static class MoveLog {
        private final List<Move> moves;

        MoveLog() {
            this.moves = new ArrayList<>();
        }

        public List<Move> getMoves() {
            return this.moves;
        }

        public void addMove(final Move move) {
            this.moves.add(move);
        }

        public int size() {
            return this.moves.size();
        }

        public void clear() {
            this.moves.clear();
        }

        public Move removeMove(int index) {
            return this.moves.remove(index);
        }

        public void removeMove(final Move move) {
            this.moves.remove(move);
        }
    }

    private class BoardPanel extends JPanel {
        final List<TilePanel> boardTiles;

        BoardPanel() {
            super(new GridLayout(8, 8));
            this.boardTiles = new ArrayList<>();
            for (int i = 0; i < BoardPlugins.NUM_TILES; i++) {
                final TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                this.add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            setBackground(Color.WHITE);
            this.validate();
        }

        public void drawBoard(final Board board) {
            removeAll();
            for (final TilePanel tilePanel : boardDirection.traverse(boardTiles)) {
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }

    }

    private class TilePanel extends JPanel {
        private final int tileID;

        TilePanel(final BoardPanel boardPanel, final int tileID) {
            super(new GridLayout());
            this.tileID = tileID;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor();
            assignTilePieceIcon(chessBoard);
            highlightTileBorder(chessBoard);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(final MouseEvent mouseEvent) {
                    if (Table.get().getGameSetup().isAIPlayer(Table.get().getGameBoard().getCurrentPlayer()) ||
                            BoardPlugins.isEndGame(Table.get().getGameBoard())) {
                        return;
                    }

                    if (isRightMouseButton(mouseEvent)) {
                        sourceTile = null;
                        destinationTile = null;
                        humanMovedPiece = null;
                    } else if (isLeftMouseButton(mouseEvent)) {
                        if (sourceTile == null) {
                            sourceTile = chessBoard.getTile(tileID);
                            humanMovedPiece = sourceTile.getPiece();
                            if (humanMovedPiece == null) {
                                sourceTile = null;
                            }
                        } else {
                            destinationTile = chessBoard.getTile(tileID);
                            final Move move = createMove(chessBoard, sourceTile.getTileCoordinate(),
                                    destinationTile.getTileCoordinate());
                            final MoveTransition transition = chessBoard.getCurrentPlayer().makeMove(move);
                            if (transition.getMoveStatus().isDone()) {
                                chessBoard = transition.getToBoard();
                                moveLog.addMove(move);
                            }
                            sourceTile = null;
                            destinationTile = null;
                            humanMovedPiece = null;
                        }
                        SwingUtilities.invokeLater(() -> {
                            historyPanel.remake(chessBoard, moveLog);
                            takenPiecesPanel.remake(moveLog);
                            if (gameSettings.isAIPlayer(chessBoard.getCurrentPlayer())) {
                                Table.get().moveMadeUpdate(PlayerType.HUMAN);
                            }
                            boardPanel.drawBoard(chessBoard);
                        });
                    }
                }
            });
            validate();
        }

        public void drawTile(final Board board) {
            assignTileColor();
            assignTilePieceIcon(board);
            highlightTileBorder(board);
            highlightLegals(board);
            validate();
            repaint();
        }

        private void highlightLegals(final Board board) {
            if (highlightLegalMoves) {
                for (final Move move : pieceLegalMoves(board)) {
                    if (move.getDestinationCoordinate() == this.tileID) {
                        try {
                            if (!move.getBoard().getTile(this.tileID).isTileOccupied()) {
                                add(new JLabel(new ImageIcon(ImageIO.read(new File("pictures/hints/green_dot.png")))));
                            } else if (move.getBoard().getTile(this.tileID).isTileOccupied() &&
                                    move.getAttackedPiece().getPieceAlliance() != move.getMovedPiece().getPieceAlliance()) {
                                setBorder(BorderFactory.createLineBorder(Color.RED));
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        }

        private Collection<Move> pieceLegalMoves(final Board board) {
            if (humanMovedPiece != null && humanMovedPiece.getPieceAlliance() == board.getCurrentPlayer().getAlliance()) {
                return humanMovedPiece.calculateLegalMoves(board);
            }
            return Collections.emptyList();
        }

        private void assignTilePieceIcon(final Board board) {
            this.removeAll();
            if (board.getTile(this.tileID).isTileOccupied()) {
                try {
                    final BufferedImage image =
                            ImageIO.read(new File(defaultPieceImagesPath +
                                    board.getTile(this.tileID).getPiece().getPieceAlliance().toString().charAt(0) +
                                    board.getTile(this.tileID).getPiece().toString() + ".gif"));
                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void assignTileColor() {
            if (BoardPlugins.EIGHTH_ROW_FROM_BELOW[this.tileID] ||
                    BoardPlugins.SIXTH_ROW_FROM_BELOW[this.tileID] ||
                    BoardPlugins.FOURTH_ROW_FROM_BELOW[this.tileID] ||
                    BoardPlugins.SECOND_ROW_FROM_BELOW[this.tileID]) {
                setBackground(this.tileID % 2 == 0 ? lightTileColor : darkTileColor);
            } else if (BoardPlugins.SEVENTH_ROW_FROM_BELOW[this.tileID] ||
                    BoardPlugins.FIFTH_ROW_FROM_BELOW[this.tileID] ||
                    BoardPlugins.THIRD_ROW_FROM_BELOW[this.tileID] ||
                    BoardPlugins.FIRST_ROW_FROM_BELOW[this.tileID]) {
                setBackground(this.tileID % 2 != 0 ? lightTileColor : darkTileColor);
            }
        }

        private void highlightTileBorder(final Board board) {
            if (humanMovedPiece != null &&
                    humanMovedPiece.getPieceAlliance() == board.getCurrentPlayer().getAlliance() &&
                    humanMovedPiece.getPiecePosition() == this.tileID) {
                setBorder(BorderFactory.createLineBorder(Color.GREEN));
            } else {
                setBorder(BorderFactory.createLineBorder(darkTileColor));
            }
        }
    }

    private static class TableGameAIWatcher implements Observer {

        @Override
        public void update(final Observable observable, final Object o) {
            if (Table.get().getGameSetup().isAIPlayer(Table.get().getGameBoard().getCurrentPlayer()) &&
                    !Table.get().getGameBoard().getCurrentPlayer().isInCheckMate() &&
                    !Table.get().getGameBoard().getCurrentPlayer().isInStaleMate()) {
                final AIBrain thinkTank = new AIBrain();
                thinkTank.execute();
            }

            if (Table.get().getGameBoard().getCurrentPlayer().isInCheckMate()) {
                System.out.println("Game over, " + Table.get().getGameBoard().getCurrentPlayer() + " is in checkmate");
            }

            if (Table.get().getGameBoard().getCurrentPlayer().isInStaleMate()) {
                System.out.println("Game over, " + Table.get().getGameBoard().getCurrentPlayer() + " is in stalemate");
            }
        }
    }

    private static class AIBrain extends SwingWorker<Move, String> {
        private AIBrain() {
        }

        @Override
        protected Move doInBackground() {
            final MinMax miniMax = new MinMax(4);
            return miniMax.perform(Table.get().getGameBoard());
        }

        @Override
        public void done() {
            try {
                final Move bestMove = get();
                Table.get().setGameBoard(Table.get().getGameBoard().getCurrentPlayer().makeMove(bestMove).getToBoard());
                Table.get().getMoveLog().addMove(bestMove);
                Table.get().getGameHistoryPanel().remake(Table.get().getGameBoard(), Table.get().getMoveLog());
                Table.get().getTakenPiecesPanel().remake(Table.get().getMoveLog());
                Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
                Table.get().moveMadeUpdate(PlayerType.COMPUTER);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}