import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TicTacToeAI extends JFrame implements ActionListener {
    private JButton[] buttons = new JButton[9];
    private char[] board = new char[9];
    private boolean playerTurn = true; // true = Player (X), false = AI (O)

    public TicTacToeAI() {
        setTitle("Tic-Tac-Toe with Minimax");
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
        JButton buttonClicked = (JButton) e.getSource();

        int idx = -1;
        for (int i = 0; i < 9; i++) {
            if (buttons[i] == buttonClicked) {
                idx = i;
                break;
            }
        }

        if (playerTurn && board[idx] == ' ') {
            makeMove(idx, 'X');
            if (!isGameOver()) {
                playerTurn = false;
                Timer timer = new Timer(500, evt -> {
                    aiMove();
                });
                timer.setRepeats(false);
                timer.start();

            }
        }
    }

    private void makeMove(int idx, char player) {
        board[idx] = player;
        buttons[idx].setText(String.valueOf(player));
    }

    private void aiMove() {
        int bestScore = Integer.MIN_VALUE;
        int bestMove = -1;

        for (int i = 0; i < 9; i++) {
            if (board[i] == ' ') {
                board[i] = 'O';
                int score = minimax(board, 0, false);
                board[i] = ' ';
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = i;
                }
            }
        }

        if (bestMove != -1) {
            makeMove(bestMove, 'O');
            if (!isGameOver()) {
                playerTurn = true;
            }
        }
    }

    private int minimax(char[] boardState, int depth, boolean isMaximizing) {
        Character winner = checkWinner(boardState);
        if (winner != null) {
            if (winner == 'O') return 10 - depth;
            else if (winner == 'X') return depth - 10;
            else return 0;
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < 9; i++) {
                if (boardState[i] == ' ') {
                    boardState[i] = 'O';
                    int score = minimax(boardState, depth + 1, false);
                    boardState[i] = ' ';
                    bestScore = Math.max(score, bestScore);
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < 9; i++) {
                if (boardState[i] == ' ') {
                    boardState[i] = 'X';
                    int score = minimax(boardState, depth + 1, true);
                    boardState[i] = ' ';
                    bestScore = Math.min(score, bestScore);
                }
            }
            return bestScore;
        }
    }

    private boolean isGameOver() {
        Character winner = checkWinner(board);
        if (winner != null) {
            String message;
            if (winner == 'X') message = "You Win!";
            else if (winner == 'O') message = "AI Wins!";
            else message = "It's a Tie!";
            JOptionPane.showMessageDialog(this, message);
            resetGame();
            return true;
        }
        return false;
    }

    private Character checkWinner(char[] boardState) {
        int[][] wins = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // rows
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // columns
                {0, 4, 8}, {2, 4, 6}              // diagonals
        };

        for (int[] win : wins) {
            if (boardState[win[0]] != ' ' &&
                boardState[win[0]] == boardState[win[1]] &&
                boardState[win[1]] == boardState[win[2]]) {
                return boardState[win[0]];
            }
        }

        // Check tie
        boolean emptyFound = false;
        for (char c : boardState) {
            if (c == ' ') {
                emptyFound = true;
                break;
            }
        }
        return emptyFound ? null : 'T';
    }

    private void resetGame() {
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
