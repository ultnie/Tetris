package ru.nsu.g.amaseevskii.tetris;

import javax.swing.*;
import java.awt.*;

import static java.lang.System.exit;

class Menu {
    Tetris tetris = new Tetris();
    Highscores highscores = new Highscores();

    void menu() {
        highscores.readCSV();
        JFrame frame = new JFrame("Tetris Menu");
        frame.setPreferredSize(new Dimension(300, 400));
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JFrame aboutFrame = new JFrame("About");
        aboutFrame.setSize(630,250);
        aboutFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        JPanel aboutPanel = new JPanel();
        aboutPanel.setLayout(new BoxLayout(aboutPanel, BoxLayout.Y_AXIS));
        aboutPanel.setFocusable(false);
        aboutPanel.setSize(630,250);
        JTextArea aboutTextArea = new JTextArea();
        aboutTextArea.setFont(new Font ("monospaced", Font.BOLD, 14));
        aboutTextArea.append("This is Tetris. Nothing more to say about it.\n\n" +
                "Controls:\nZ - Counter Clockwise Turn       X - Clockwise Turn\n" +
                            "DOWN ARROW - Soft Drop           LEFT/RIGHT ARROWS - Move Left/Right\n" +
                            "SPACE - Hard drop                ESCAPE - Pause/Unpause\n\n\n\n" +
                "Made by Maseevsky Anton as a student task");
        aboutFrame.add(aboutTextArea);

        JFrame highscoresFrame = new JFrame("Highscores");
        highscoresFrame.setSize(450, 550);
        highscoresFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        JPanel highscoresPanel = new JPanel();
        highscoresPanel.setLayout(new BoxLayout(highscoresPanel, BoxLayout.Y_AXIS));
        highscoresPanel.setFocusable(false);
        highscoresPanel.setSize(450,550);
        JTextArea highscoresTextArea = new JTextArea();
        highscoresTextArea.setFont(new Font ("monospaced", Font.BOLD, 16));
        for (int i = 0; i < highscores.records.size(); i++) {
            if (i<9)
                highscoresTextArea.append((i+1)+".  "+highscores.records.get(i).getValue()
                        +" ".repeat(11-highscores.records.get(i).getValue().length())
                        +highscores.records.get(i).getKey()+"\n");
            else {
                highscoresTextArea.append((i + 1) + ". " + highscores.records.get(i).getValue()
                        + " ".repeat(11 - highscores.records.get(i).getValue().length())
                        + highscores.records.get(i).getKey());
            }
        }

        highscoresFrame.add(highscoresTextArea);


        JPanel menupanel = new JPanel();
        menupanel.setPreferredSize(new Dimension(200, 300));

        JButton start = new JButton("Play");
        start.setPreferredSize(new Dimension(200, 50));
        start.addActionListener(actionEvent -> {
            tetris.start();
            frame.setVisible(false);
            highscoresFrame.setVisible(false);
            aboutFrame.setVisible(false);
        });

        JButton setlevel = new JButton("Set Level");
        setlevel.setPreferredSize(new Dimension(200, 50));
        setlevel.addActionListener(actionEvent -> {
            int templevel;
            String option;
            option = JOptionPane.showInputDialog("Choose level from 1 to 30", tetris.model.startlevel);
            try {
                templevel = Integer.parseInt(option);
                if (templevel > 0 && templevel <= 30)
                    tetris.model.startlevel = templevel;
            }
            catch (Exception e){}
        });

        JButton highscores = new JButton("Highscores");
        highscores.setPreferredSize(new Dimension(200, 50));
        highscores.addActionListener(actionEvent -> {
            highscoresFrame.setVisible(true);
        });

        JButton about = new JButton( ("About"));
        about.setPreferredSize(new Dimension(200, 50));
        about.addActionListener(actionEvent -> {
            aboutFrame.setVisible(true);
        });

        JButton exit = new JButton("Exit");
        exit.setPreferredSize(new Dimension(200, 50));
        exit.addActionListener(actionEvent -> exit(0));

        menupanel.add(start);
        menupanel.add(setlevel);
        menupanel.add(highscores);
        menupanel.add(about);
        menupanel.add(exit);

        frame.add(menupanel, BorderLayout.CENTER);
        frame.pack();

        frame.setVisible(true);
    }
}
