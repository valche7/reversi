## Reversi Game — Java

A Java implementation of the classic **Reversi (Othello)** board game, combining an interactive graphical interface with a controller responsible for the game's rules, move validation, scoring, and automated gameplay.

The project uses an **MVC-style architecture**, separating the model, view, and controller. The controller manages the game state and processes both player actions and AI moves, while the Swing-based GUI provides an interactive 8×8 board for two players.

### Key features

* Interactive **8×8 Reversi board** developed with Java Swing.
* Implements the complete core Reversi gameplay logic.
* Validates player moves across all **8 possible directions**.
* Automatically flips opponent pieces when a valid move is made.
* Detects when a player has no valid moves and automatically passes the turn.
* Calculates the number of pieces for each player and determines the winner or a draw.
* Provides separate board views for white and black players, including reversed board orientation.
* Includes a **Greedy AI** that evaluates all available moves and selects the move that flips the highest number of opponent pieces.
* Supports restarting the game from the initial board configuration.
* Provides real-time feedback about the current player, invalid turns, and game results.
* Uses event-driven programming to handle user interactions with the board.

### Technologies & Concepts

**Java • Java Swing • Object-Oriented Programming • MVC Architecture • Interfaces • Event-Driven Programming • 2D Arrays • Game Logic • Move Validation • Greedy AI • Algorithm Design**

### Algorithms

The controller checks potential moves by traversing the board in all eight directions. A move is considered valid when it captures at least one opponent piece and is bounded by the current player's piece.

The Greedy AI evaluates every valid move and counts how many opponent pieces each move would capture. It then selects the move with the highest number of flips, providing a simple rule-based automated opponent.

The project demonstrates practical experience with **object-oriented design, algorithm implementation, GUI development, state management, and basic game AI**.
