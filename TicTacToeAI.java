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