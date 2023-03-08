# ChessGame
Java Chess game with using graphics library Swing.
#### Prerequisites: [Java 8](https://www.oracle.com/java/technologies/javase/javase8-archive-downloads.html), [Maven](https://maven.apache.org/), [Swing](https://docs.oracle.com/javase/8/docs/api/javax/swing/package-summary.html), [JUnit 4](https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api).

## Game description

**Chess** is a two-player strategy board game that is played on a square board consisting of 64 squares arranged in an 8x8 grid. The game is played with 16 pieces, eight for each player, which are placed at the beginning of the game on opposite sides of the board.

The pieces consist of one king, one queen, two rooks, two knights, two bishops, and eight pawns. Each piece has its own unique way of moving across the board.

The goal of the game is to checkmate the opponent's king, which means placing the king in a position where it is in check (under attack) and there is no legal move that the opponent can make to get out of check.

Players take turns moving their pieces across the board, with the white pieces moving first. Each player can make one move per turn, and a move consists of moving one of their pieces to a different square on the board.

A player may capture an opponent's piece by moving one of their pieces to a square occupied by an opponent's piece. When a piece is captured, it is removed from the board.

The game can end in several ways, including checkmate, stalemate  or a draw by agreement or by a specific set of rules.

## Players
This application allows you to play against both a person and a computer. The computer uses the minimax algorithm to generate its moves.

![Chess](https://user-images.githubusercontent.com/79751387/223693155-acb45854-9ba0-45fb-8251-bbcedbb9bb98.jpg)

## Getting Started
#### 1. Clone the application
    git clone https://github.com/floMaxter/ChessGame.git
    cd ChessGame
#### 2.Run the app using Maven
    mvn compile
    mvn exec:java -Dexec.mainClass="com.suai.chess.GameChess"
    
