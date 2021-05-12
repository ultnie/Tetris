package ru.nsu.g.amaseevskii.tetris;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static java.lang.System.exit;

class Controller {
    private Model model;
    private View view;

    Controller(Model model, View view) {
        this.model = model;
        this.view = view;
    }

    void start() {
        model.newGame();
        view.setupGUI();

        view.restart.addActionListener(actionEvent -> {
            model.newGame();
            view.sidepanel.remove(view.gameOver);
            view.sidepanel.updateUI();
        });

        view.changelevel.addActionListener(actionEvent -> {
            if (!model.getInternalPauseStatus())
                model.internalPause();
            int templevel;
            String option;
            option = JOptionPane.showInputDialog("Choose level from 1 to 30 (will change on restart)", model.startlevel);
            try {
                templevel = Integer.parseInt(option);
                if (templevel > 0 && templevel <= 30)
                    model.startlevel = templevel;
            } catch (Exception e) {
            }
            if (!model.getGameOverStatus())
            model.internalPause();
        });

        view.about.addActionListener(actionEvent ->
        {
            if (!model.getInternalPauseStatus() && !model.getPauseStatus())
            model.pause();
            view.aboutFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    if (model.getPauseStatus())
                        model.pause();
                }
            });
            view.aboutFrame.setVisible(true);
        });

        view.exit.addActionListener(actionEvent -> exit(0));

        view.tetris.setFocusable(true);
        view.tetris.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                switch (e.getKeyCode()) {
                    case (KeyEvent.VK_ESCAPE):
                        if (!model.getInternalPauseStatus() && !model.getGameOverStatus())
                            model.pause();
                        break;
                    case (KeyEvent.VK_Z):
                        if (!model.getPauseStatus() && !model.getInternalPauseStatus() && !model.getGameOverStatus())
                            model.rotate(false);
                        break;
                    case (KeyEvent.VK_X):
                    case (KeyEvent.VK_UP):
                        if (!model.getPauseStatus() && !model.getInternalPauseStatus() && !model.getGameOverStatus())
                            model.rotate(true);
                        break;
                    case (KeyEvent.VK_DOWN):
                        if (!model.getPauseStatus() && !model.getInternalPauseStatus() && !model.getGameOverStatus())
                            model.movedown();
                        break;
                    case (KeyEvent.VK_LEFT):
                        if (!model.getPauseStatus() && !model.getInternalPauseStatus() && !model.getGameOverStatus())
                            model.moveleft();
                        break;
                    case (KeyEvent.VK_RIGHT):
                        if (!model.getPauseStatus() && !model.getInternalPauseStatus() && !model.getGameOverStatus())
                            model.moveright();
                        break;
                    case (KeyEvent.VK_SPACE):
                        if (!model.getPauseStatus() && !model.getInternalPauseStatus() && !model.getGameOverStatus())
                            model.drop();
                        break;
                    default: {
                    }
                }
            }
        });
    }
}
