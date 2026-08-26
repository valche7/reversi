package reversi;

import java.awt.*;
import javax.swing.*;

public class GUIView implements IView {
    private IModel model;
    private IController controller;
    private JFrame f1, f2;
    private JLabel label1, label2;
    private JButton[][] buttons1 = new JButton[8][8];
    private JButton[][] buttons2 = new JButton[8][8];

    @Override
    public void initialise(IModel model, IController controller) {
        this.model = model;
        this.controller = controller;
        createFrame(1);
        createFrame(2);
    }

    private void createFrame(final int p) {
        JFrame frame = new JFrame(p == 1 ? "Reversi – white player" : "Reversi – black player");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JLabel label = new JLabel("", SwingConstants.CENTER);
        frame.add(label, BorderLayout.NORTH);
        
        JPanel grid = new JPanel(new GridLayout(8, 8));
        
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                final int x = (p == 1) ? c : 7 - c;
                final int y = (p == 1) ? r : 7 - r;
                JButton b = new JButton();
                b.setBackground(new Color(34, 139, 34));
                b.setPreferredSize(new Dimension(50, 50));
                b.addActionListener(e -> controller.squareSelected(p, x, y));
                grid.add(b);
                
                if (p == 1) buttons1[x][y] = b;
                else buttons2[x][y] = b;
            }
        }
        frame.add(grid, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1));
        String aiText = (p == 1) ? "Greedy AI (play white)" : "Greedy AI (play black)";
        JButton ai = new JButton(aiText);
        ai.addActionListener(e -> controller.doAutomatedMove(p));
        JButton restart = new JButton("Restart");
        restart.addActionListener(e -> controller.startup());
        buttonPanel.add(ai);
        buttonPanel.add(restart);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        
        frame.pack();
        frame.setVisible(true);
        
        if (p == 1) {
            f1 = frame;
            label1 = label;
        } else {
            f2 = frame;
            label2 = label;
        }
    }

    @Override
    public void refreshView() {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                int val = model.getBoardContents(x, y);
                String symbol = "";
                if (val == 1) symbol = "●";
                else if (val == 2) symbol = "○";
                else symbol = " ";
                
                if (buttons1[x][y] != null) {
                    buttons1[x][y].setText(symbol);
                    buttons1[x][y].setForeground(val == 1 ? Color.WHITE : Color.BLACK);
                }
                if (buttons2[7-x][7-y] != null) {
                    buttons2[7-x][7-y].setText(symbol);
                    buttons2[7-x][7-y].setForeground(val == 1 ? Color.WHITE : Color.BLACK);
                }
            }
        }
        if (f1 != null) f1.repaint();
        if (f2 != null) f2.repaint();
    }

    @Override
    public void feedbackToUser(int player, String message) {
        if (player == 1 && label1 != null) label1.setText(message);
        else if (player == 2 && label2 != null) label2.setText(message);
    }
}
