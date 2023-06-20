package org.example;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class TicTacToe implements ActionListener {
    private final JFrame frame;
    private final JLabel textfield;
    private final JButton[] buttons;
    private boolean player1_turn;
    private boolean vsCPU; // New variable to track if playing against CPU
    private final Random random;


    public TicTacToe() {
        frame = new JFrame("Tic-Tac-Toe");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.getContentPane().setBackground(new Color(50, 50, 50));
        frame.setLayout(new BorderLayout());
        frame.setVisible(true);

        textfield = new JLabel();
        textfield.setBackground(new Color(25, 25, 25));
        textfield.setForeground(new Color(25, 255, 0));
        textfield.setFont(new Font("Ink Free", Font.BOLD, 75));
        textfield.setHorizontalAlignment(JLabel.CENTER);
        textfield.setText("Tic-Tac-Toe");
        textfield.setOpaque(true);

        JPanel title_panel = new JPanel();
        title_panel.setLayout(new BorderLayout());
        title_panel.setBounds(0, 0, 800, 100);

        JPanel button_panel = new JPanel();
        button_panel.setLayout(new GridLayout(3, 3));
        button_panel.setBackground(new Color(255, 255, 255));

        buttons = new JButton[9];
        for (int i = 0; i < 9; i++) {
            buttons[i] = new JButton();
            button_panel.add(buttons[i]);
            buttons[i].setFont(new Font("MV Boli", Font.BOLD, 120));
            buttons[i].setFocusable(false);
            buttons[i].addActionListener(this);
        }

        title_panel.add(textfield);
        frame.add(title_panel, BorderLayout.NORTH);
        frame.add(button_panel);

        random = new Random();
        firstTurn();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            for (int i = 0; i < 9; i++) {
                if (e.getSource() == buttons[i]) {
                    if (player1_turn) {
                        if (buttons[i].getText().equals("")) {
                            buttons[i].setForeground(new Color(255, 0, 0));
                            buttons[i].setText("X");
                            player1_turn = false;
                            textfield.setText("O turn");

                            check(); // Call check method after player's move

                            if (vsCPU && !isGameOver()) {
                                makeCPUMove();
                                check(); // Call check method after CPU's move
                            }
                        }
                    } else {
                        if (buttons[i].getText().equals("")) {
                            buttons[i].setForeground(new Color(0, 0, 255));
                            buttons[i].setText("O");
                            player1_turn = true;
                            textfield.setText("X turn");
                            check(); // Call check method after player's move
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void firstTurn() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (random.nextInt(2) == 0) {
            player1_turn = true;
            textfield.setText("X turn");
        } else {
            player1_turn = false;
            textfield.setText("O turn");
        }

        // Prompt user to choose opponent type
        int choice = JOptionPane.showOptionDialog(
                frame,
                "Choose opponent:",
                "Choose Opponent",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"Player", "CPU"},
                null
        );

        vsCPU = (choice == 1);

        // If playing against CPU, make the first move if it's CPU's turn
        if (vsCPU && !player1_turn) {
            makeCPUMove();
        }
    }

    public void check() {
        int[][] winningConditions = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Rows
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Columns
                {0, 4, 8}, {2, 4, 6} // Diagonals
        };

        for (int[] condition : winningConditions) {
            String symbol = buttons[condition[0]].getText();
            if (symbol.equals(buttons[condition[1]].getText()) &&
                    symbol.equals(buttons[condition[2]].getText())) {
                if (symbol.equals("X")) {
                    xWins(condition[0], condition[1], condition[2]);
                    return; // Exit the method after declaring X as the winner
                } else if (symbol.equals("O")) {
                    oWins(condition[0], condition[1], condition[2]);
                    return; // Exit the method after declaring O as the winner
                }
            }
        }

        if (!vsCPU && isBoardFull()) {
            // Game ends in a draw if playing against another player and board is full
            draw();
        } else if (vsCPU && isBoardFull()) {
            draw();
        }
    }

    public boolean isBoardFull() {
        for (int i = 0; i < 9; i++) {
            if (buttons[i].getText().equals("")) {
                return false;
            }
        }
        return true;
    }

    public boolean isGameOver() {
        int[][] winningConditions = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Rows
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Columns
                {0, 4, 8}, {2, 4, 6} // Diagonals
        };
        for (int[] condition : winningConditions) {
            String symbol = buttons[condition[0]].getText();
            if (symbol.equals(buttons[condition[1]].getText()) &&
                    symbol.equals(buttons[condition[2]].getText()) &&
                    !symbol.equals("")) {
                return true;
            }
        }

        return isBoardFull();
    }

    public void makeCPUMove() {
        try {
            // Check if the board is full (draw)
            if (isBoardFull()) {
                draw();
                return;
            }

            // Randomly choose an available button for CPU's move
            int index;
            do {
                index = random.nextInt(9);
            } while (!buttons[index].getText().equals(""));

            buttons[index].setForeground(new Color(0, 0, 255));
            buttons[index].setText("O");
            player1_turn = true;
            textfield.setText("X turn");
            check();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void xWins(int a, int b, int c) {
        highlightWinningButtons(a, b, c);
        disableButtons();
        textfield.setText("X wins");
        showRestartOptionDialog();
    }

    public void oWins(int a, int b, int c) {
        highlightWinningButtons(a, b, c);
        disableButtons();
        textfield.setText("O wins");
        showRestartOptionDialog();
    }

    public void draw() {
        disableButtons();
        textfield.setText("It's a draw");
        showRestartOptionDialog();
    }

    public void highlightWinningButtons(int a, int b, int c) {
        buttons[a].setBackground(Color.GREEN);
        buttons[b].setBackground(Color.GREEN);
        buttons[c].setBackground(Color.GREEN);
    }

    public void disableButtons() {
        for (int i = 0; i < 9; i++) {
            buttons[i].setEnabled(false);
        }
    }
    public void showRestartOptionDialog() {
        try {
            int option = JOptionPane.showOptionDialog(
                    frame,
                    "Game is Over! Do you want to play again?",
                    "Game Over",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Restart", "Quit"},
                    null
            );

            if (option == JOptionPane.YES_OPTION) {
                restartGame();
            } else {
                quitGame();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void restartGame() {
        // Reset button texts and enable buttons
        for (int i = 0; i < 9; i++) {
            buttons[i].setText("");
            buttons[i].setEnabled(true);
            buttons[i].setBackground(null);
        }

        // Reset game state
        player1_turn = random.nextBoolean();
        if (player1_turn) {
            textfield.setText("X turn");
        } else {
            textfield.setText("O turn");
            if (vsCPU) {
                makeCPUMove();
            }
        }
    }
    public void quitGame() {
        try {
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
