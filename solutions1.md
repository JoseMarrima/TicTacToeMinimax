### 🟩 Ticket 1 — Setup Game Board UI with Swing
*** Commit: feat: create Swing UI layout and game board ***

```java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToeAI extends JFrame implements ActionListener {

    private final JButton[] buttons = new JButton[9];   // 3×3 grid buttons
    private final char[] board = new char[9];           // ' ' for empty
    private boolean playerTurn = true;                  // player = X, AI = O (AI not yet)

    public TicTacToeAI() {
        setTitle("Tic‑Tac‑Toe");
        setSize(400, 400);
        setLayout(new GridLayout(3, 3));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton("");
            buttons[i].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 60));
            buttons[i].addActionListener(this);
            add(buttons[i]);
            board[i] = ' ';
        }
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Click handler stub — full logic comes in later tickets
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToeAI::new);
    }
}
```


### 🟩 Ticket 2 — Player Move + Win/Tie Detection
*** Commit: feat: add player move handling and win/tie check ***

```java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToeAI extends JFrame implements ActionListener {

    private final JButton[] buttons = new JButton[9];
    private final char[] board = new char[9];
    private boolean playerTurn = true; // player = X

    public TicTacToeAI() {
        setTitle("Tic‑Tac‑Toe");
        setSize(400, 400);
        setLayout(new GridLayout(3, 3));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton("");
            buttons[i].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 60));
            buttons[i].addActionListener(this);
            add(buttons[i]);
            board[i] = ' ';
        }
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!playerTurn) return;                      // ignore clicks during AI turn (not yet used)
        JButton btn = (JButton) e.getSource();
        int idx = getButtonIndex(btn);
        if (idx == -1 || board[idx] != ' ') return;   // already filled

        makeMove(idx, 'X');
        checkAndHandleGameOver();
    }

    /* ---------- Helper methods ---------- */

    private int getButtonIndex(JButton b) {
        for (int i = 0; i < buttons.length; i++) if (buttons[i] == b) return i;
        return -1;
    }

    private void makeMove(int idx, char mark) {
        board[idx] = mark;
        buttons[idx].setText(String.valueOf(mark));
    }

    private void checkAndHandleGameOver() {
        Character winner = checkWinner();
        if (winner != null) {
            String msg = switch (winner) {
                case 'X' -> "You win!";
                case 'O' -> "AI wins!";
                default   -> "It's a tie!";
            };
            JOptionPane.showMessageDialog(this, msg);
            resetBoard();
        }
    }

    private Character checkWinner() {
        int[][] wins = {
            {0,1,2},{3,4,5},{6,7,8},
            {0,3,6},{1,4,7},{2,5,8},
            {0,4,8},{2,4,6}
        };
        for (int[] w : wins)
            if (board[w[0]] != ' ' && board[w[0]] == board[w[1]] && board[w[1]] == board[w[2]])
                return board[w[0]];
        for (char c : board) if (c == ' ') return null; // game not over
        return 'T';                                     // tie
    }

    private void resetBoard() {
        for (int i = 0; i < 9; i++) {
            board[i] = ' ';
            buttons[i].setText("");
        }
        playerTurn = true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToeAI::new);
    }
}
```


### 🟩 Ticket 3 — Add Minimax AI Move
*** Commit: feat: implement AI opponent using Minimax algorithm ***

```java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToeAI extends JFrame implements ActionListener {

    private final JButton[] buttons = new JButton[9];
    private final char[] board = new char[9];
    private boolean playerTurn = true; // true = player X, false = AI O

    public TicTacToeAI() {
        setTitle("Tic‑Tac‑Toe (Minimax AI)");
        setSize(400, 400);
        setLayout(new GridLayout(3, 3));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton("");
            buttons[i].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 60));
            buttons[i].addActionListener(this);
            add(buttons[i]);
            board[i] = ' ';
        }
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!playerTurn) return;                 // ignore if it's AI's turn
        int idx = getButtonIndex((JButton) e.getSource());
        if (idx == -1 || board[idx] != ' ') return;

        makeMove(idx, 'X');
        if (checkAndHandleGameOver()) return;

        playerTurn = false;
        aiMove();
    }

    /* ---------- AI ---------- */

    private void aiMove() {
        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;

        for (int i = 0; i < 9; i++) {
            if (board[i] == ' ') {
                board[i] = 'O';
                int score = minimax(false, 0);
                board[i] = ' ';
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = i;
                }
            }
        }

        makeMove(bestMove, 'O');
        if (!checkAndHandleGameOver()) playerTurn = true;
    }

    private int minimax(boolean isMax, int depth) {
        Character winner = getWinnerForMinimax();
        if (winner != null) {
            return switch (winner) {
                case 'O' -> 10 - depth;
                case 'X' -> depth - 10;
                default  -> 0; // tie
            };
        }

        int best = isMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int i = 0; i < 9; i++) {
            if (board[i] == ' ') {
                board[i] = isMax ? 'O' : 'X';
                int score = minimax(!isMax, depth + 1);
                board[i] = ' ';
                best = isMax ? Math.max(best, score) : Math.min(best, score);
            }
        }
        return best;
    }

    /* ---------- Shared helpers ---------- */

    private int getButtonIndex(JButton b) {
        for (int i = 0; i < buttons.length; i++) if (buttons[i] == b) return i;
        return -1;
    }

    private void makeMove(int idx, char mark) {
        board[idx] = mark;
        buttons[idx].setText(String.valueOf(mark));
    }

    private boolean checkAndHandleGameOver() {
        Character winner = checkWinner();
        if (winner != null) {
            JOptionPane.showMessageDialog(this,
                winner == 'X' ? "You win!" :
                winner == 'O' ? "AI wins!" : "It's a tie!");
            resetBoard();
            return true;
        }
        return false;
    }

    private Character checkWinner() {
        int[][] wins = {
            {0,1,2},{3,4,5},{6,7,8},
            {0,3,6},{1,4,7},{2,5,8},
            {0,4,8},{2,4,6}
        };
        for (int[] w : wins)
            if (board[w[0]] != ' ' && board[w[0]] == board[w[1]] && board[w[1]] == board[w[2]])
                return board[w[0]];
        for (char c : board) if (c == ' ') return null;
        return 'T';
    }

    private Character getWinnerForMinimax() {   // same as checkWinner but no JOptionPane
        return checkWinner();
    }

    private void resetBoard() {
        for (int i = 0; i < 9; i++) {
            board[i] = ' ';
            buttons[i].setText("");
        }
        playerTurn = true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToeAI::new);
    }
}
```



### 🟩 Ticket 4 — Final Polish & Robustness
*** Commit: chore: refine reset logic, click‑guard & comments ***

```java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Tic‑Tac‑Toe with Minimax AI.
 * X = Human, O = Computer.
 */
public class TicTacToeAI extends JFrame implements ActionListener {

    private final JButton[] buttons = new JButton[9];
    private final char[] board = new char[9];
    private boolean playerTurn = true; // Human starts

    public TicTacToeAI() {
        setTitle("Tic‑Tac‑Toe (AI)");
        setSize(400, 400);
        setLayout(new GridLayout(3, 3));
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initBoardUI();
        resetBoard();
        setVisible(true);
    }

    /* ---------- UI setup ---------- */

    private void initBoardUI() {
        Font f = new Font(Font.SANS_SERIF, Font.BOLD, 60);
        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton();
            buttons[i].setFont(f);
            buttons[i].addActionListener(this);
            add(buttons[i]);
        }
    }

    /* ---------- Event handling ---------- */

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!playerTurn) return; // AI thinking / after game
        int idx = getButtonIndex((JButton) e.getSource());
        if (idx < 0 || board[idx] != ' ') return;

        makeMove(idx, 'X');
        if (checkAndHandleGameOver()) return;

        playerTurn = false;
        Timer aiTimer = new Timer(500, evt -> aiMove());
        aiTimer.setRepeats(false); 
        aiTimer.start();
    }

    /* ---------- AI ---------- */

    private void aiMove() {
        int bestScore = Integer.MIN_VALUE, bestMove = -1;
        for (int i = 0; i < 9; i++) if (board[i] == ' ') {
            board[i] = 'O';
            int score = minimax(false, 0);
            board[i] = ' ';
            if (score > bestScore) {
                bestScore = score;
                bestMove = i;
            }
        }
        makeMove(bestMove, 'O');
        if (!checkAndHandleGameOver()) playerTurn = true;
    }

    private int minimax(boolean isMax, int depth) {
        Character win = getWinnerForMinimax();
        if (win != null)
            return switch (win) {
                case 'O' -> 10 - depth;
                case 'X' -> depth - 10;
                default  -> 0;
            };

        int best = isMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (int i = 0; i < 9; i++) if (board[i] == ' ') {
            board[i] = isMax ? 'O' : 'X';
            int score = minimax(!isMax, depth + 1);
            board[i] = ' ';
            best = isMax ? Math.max(best, score) : Math.min(best, score);
        }
        return best;
    }

    /* ---------- Helpers ---------- */

    private int getButtonIndex(JButton b) {
        for (int i = 0; i < 9; i++) if (buttons[i] == b) return i;
        return -1;
    }

    private void makeMove(int idx, char mark) {
        board[idx] = mark;
        buttons[idx].setText(String.valueOf(mark));
    }

    private boolean checkAndHandleGameOver() {
        Character winner = checkWinner();
        if (winner == null) return false;

        String msg = switch (winner) {
            case 'X' -> "You win! 🎉";
            case 'O' -> "AI wins! 🤖";
            default  -> "It's a tie.";
        };
        JOptionPane.showMessageDialog(this, msg);
        resetBoard();
        return true;
    }

    private Character checkWinner() {
        int[][] w = {
            {0,1,2},{3,4,5},{6,7,8},
            {0,3,6},{1,4,7},{2,5,8},
            {0,4,8},{2,4,6}
        };
        for (int[] a : w)
            if (board[a[0]] != ' ' && board[a[0]] == board[a[1]] && board[a[1]] == board[a[2]])
                return board[a[0]];
        for (char c : board) if (c == ' ') return null;
        return 'T';
    }

    private Character getWinnerForMinimax() {
        return checkWinner();
    }

    private void resetBoard() {
        for (int i = 0; i < 9; i++) {
            board[i] = ' ';
            buttons[i].setText("");
        }
        playerTurn = true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToeAI::new);
    }
}
```