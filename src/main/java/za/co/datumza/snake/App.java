package za.co.datumza.snake;

import javax.swing.*;

public class App {
    static void main(String[] args) throws Exception {
        final int BOARD_WIDTH = 800;
        final int BOARD_HEIGHT = 800;
        final String NAME = "Snake";

        JFrame frame = new JFrame(NAME);
        frame.setVisible(true);
        frame.setSize(BOARD_WIDTH, BOARD_HEIGHT);
//        frame.setLocation(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

