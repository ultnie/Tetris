package ru.nsu.g.amaseevskii.tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.LinkedList;

import static java.lang.Math.*;

class Model {
    static final int wight = 10;
    static final int height = 20;
    LinkedList<Color[]> coloredboard;
    private LinkedList<Color[]> boarddata;
    private LinkedList<Color[]> prev_boarddata;
    String nickname;
    private boolean gameOver;
    private Timer stepTimer = null;
    private LinkedList<Integer> ids;
    private int id;
    int nextid;
    Color[] colors = {Color.cyan, Color.blue, Color.orange, Color.yellow, Color.green, Color.magenta.darker().darker(), Color.red};
    int score;
    int startlevel = 1;
    int level;
    int counter;
    int goal;
    private int nextGoal;
    private boolean paused;
    private boolean internalpause;
    private final int[] delay = {800, 717, 633, 550, 467, 383, 300, 217, 133, 100, 83, 83, 83, 67, 67, 67, 50, 50, 50, 33, 33, 33, 33, 33, 33, 33, 33, 33, 33, 17};
    private final Position spawn = new Position(3, 0);
    Tetromino nextPiece;
    private MovableTetromino movablePiece;
    private final TetrominoFactory factory = new TetrominoFactory();
    private final TetrisRNG tetrisRNG = new TetrisRNG();
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    private TimerListener tl = null;
    private boolean down_pressed;

    Model() {
        super();
        coloredboard = new LinkedList<>();
        boarddata = new LinkedList<>();
        for (int i = 0; i < height; i++)
            coloredboard.add(new Color[wight]);
        gameOver = false;
    }

    void newGame() {
        level = startlevel - 1;
        counter = 0;
        score = 0;
        goal = min(level * 10 + 10, max(100, 10 * level - 50));
        nextGoal = goal + 10;
        coloredboard.clear();
        for (int h = 0; h < height; h++) {
            coloredboard.add(new Color[wight]);
            boarddata.add(new Color[wight]);
        }

        gameOver = false;
        paused = false;
        nickname = null;

        ids = tetrisRNG.getSequence();
        id = ids.getFirst();
        ids.removeFirst();
        nextid = ids.getFirst();
        ids.removeFirst();
        movablePiece = new MovableTetromino(factory.getTetromino(id), spawn);
        getBoardData();
        nextPiece = factory.getTetromino(nextid);
        if (tl == null)
            tl = new TimerListener();
        if (stepTimer == null)
            stepTimer = new Timer(delay[level], tl);
        else
            stepTimer.setDelay(delay[level]);
        stepTimer.start();
        internalpause = false;
    }

    private void step() {
        if ((movablePiece.name.equals("ru.nsu.g.amaseevskii.tetris.I_piece") && movablePiece.position.y == 0) && !movablePiece.i_gimmik) {
            movablePiece.shape = movablePiece.piece.turn(0);
            getBoardData();
            movablePiece.i_gimmik = true;
        } else
            down();
    }

    private void down() {
        if (downIsEmpty(1, movablePiece.shape)) {
            movablePiece.position.y++;
            if (down_pressed)
                score++;
        } else {
            freeze(coloredboard);
            checkRows();
            if (!(gameOver = isGameOver())) {
                movablePiece = new MovableTetromino(nextPiece, spawn);
                id = nextid;
                if (ids.size() == 0) {
                    ids = tetrisRNG.getSequence();
                }
                nextid = ids.getFirst();
                ids.removeFirst();
                nextPiece = factory.getTetromino(nextid);

            } else stepTimer.stop();
        }
        getBoardData();
    }

    void pause() {
        if (!internalpause) {
            if (!gameOver) {
                if (!paused) {
                    paused = true;
                    stepTimer.stop();
                } else {
                    paused = false;
                    stepTimer.start();
                }
            }
        }
    }

    boolean getPauseStatus () {
        return paused;
    }

    void internalPause() {
        if (!internalpause) {
            internalpause = true;
            stepTimer.stop();
        } else {
            internalpause = false;
            stepTimer.start();
        }
    }

    boolean getInternalPauseStatus () {
        return internalpause;
    }

    void movedown() {
        down_pressed = true;
        if (downIsEmpty(1, movablePiece.shape))
            down();
        down_pressed = false;
    }

    void moveleft() {
        if (leftIsEmpty(1, movablePiece.shape))
            left(1);
        getBoardData();
    }

    void moveright() {
        if (rightIsEmpty(1, movablePiece.shape))
            right(1);
        getBoardData();
    }

    void drop() {
        if (!gameOver) {
            while (downIsEmpty(1, movablePiece.shape)) {
                down();
                score += 2;
            }
            down();
        }
    }

    private void left(int n) {
        movablePiece.position.x -= n;
    }

    private void right(int n) {
        movablePiece.position.x += n;
    }

    private void up(int n) {
        movablePiece.position.y -= n;
    }

    void rotate(boolean clockwise) {
        if (turnIsPossible(clockwise)) {
            if (clockwise) {
                movablePiece.state++;
            }
            else {
                if (movablePiece.state == 0)
                    movablePiece.state = 3;
                else
                    movablePiece.state--;
            }
            movablePiece.shape = movablePiece.piece.turn(movablePiece.state);
            movablePiece.i_gimmik = true;
        }
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                if (movablePiece.shape[i][j] == 1) {
                    if (movablePiece.position.x + j < 0)
                        right(abs(movablePiece.position.x + j));
                    if (movablePiece.position.x + j > 9)
                        left(movablePiece.position.x + j - 9);
                    if (movablePiece.position.y + i > 19)
                        up(movablePiece.position.y + i - 19);
                }
        getBoardData();
    }

    private boolean leftIsEmpty(int n, Integer[][] shape) {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                if (shape[i][j] == 1)
                    if (movablePiece.position.x + j - n >= 0) {
                        if (coloredboard.get(movablePiece.position.y + i)[movablePiece.position.x + j - n] != null)
                            return false;
                    } else return false;
        return true;
    }

    private boolean rightIsEmpty(int n, Integer[][] shape) {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                if (shape[i][j] == 1)
                    if (movablePiece.position.x + j + n < 10) {
                        if (coloredboard.get(movablePiece.position.y + i)[movablePiece.position.x + j + n] != null)
                            return false;
                    } else return false;
        return true;
    }

    private boolean upIsEmpty(int n, Integer[][] shape) {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                if (shape[i][j] == 1)
                    if (movablePiece.position.y + i - n >= 0) {
                        if (coloredboard.get(movablePiece.position.y + i - n)[movablePiece.position.x + j] != null)
                            return false;
                    } else return false;
        return true;
    }

    private boolean downIsEmpty(int n, Integer[][] shape) {
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                if (shape[i][j] == 1)
                    if (movablePiece.position.y + i + n < 20) {
                        if (coloredboard.get(movablePiece.position.y + i + n)[movablePiece.position.x + j] != null)
                            return false;
                    } else return false;
        return true;
    }

    private boolean turnIsPossible(boolean clockwise) {
        if (!movablePiece.i_gimmik)
            movablePiece.i_gimmik = true;
        Integer[][] tempShape;
        int tempState;
        if (clockwise) {
            tempState = movablePiece.state + 1;
        }
        else {
            if (movablePiece.state == 0)
                tempState = 3;
            else
                tempState = movablePiece.state - 1;
        }
        Tetromino tempPiece = movablePiece.piece;
        tempShape = tempPiece.turn(tempState);
        for (int i = 3; i >= 0; i--)
            for (int j = 3; j >= 0; j--)
                if (tempShape[i][j] == 1) {
                    if (movablePiece.position.x + j > 9)
                        return leftIsEmpty(movablePiece.position.x + j - 9, tempShape);
                    else if (movablePiece.position.y + i > 19)
                        return upIsEmpty(movablePiece.position.y + i - 19, tempShape);
                }
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                if (tempShape[i][j] == 1) {
                    if (movablePiece.position.x + j < 0)
                        return rightIsEmpty(abs(movablePiece.position.x + j), tempShape);
                    else if (movablePiece.position.y + i < 0)
                        return downIsEmpty(abs(movablePiece.position.y + i), tempShape);
                    if (coloredboard.get(movablePiece.position.y + i)[movablePiece.position.x + j] != null)
                        return false;
                }
        return true;
    }

    private void freeze(LinkedList<Color[]> board) {
        Color[] colortemp;
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                if (movablePiece.shape[i][j] == 1) {
                    colortemp = board.get(movablePiece.position.y + i);
                    colortemp[movablePiece.position.x + j] = colors[id];
                    board.set(movablePiece.position.y + i, colortemp);
                }
    }

    private void checkRows() {
        int clear = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < wight; j++)
                if (coloredboard.get(i)[j] == null)
                    break;
                else if (j == 9) {
                    clear++;
                    counter++;
                    coloredboard.remove(i);
                    coloredboard.addFirst(new Color[wight]);
                }
        }

        switch (clear) {
            case 1:
                score += (level + 1) * 100;
                break;
            case 2:
                score += (level + 1) * 300;
                break;
            case 3:
                score += (level + 1) * 500;
                break;
            case 4:
                score += (level + 1) * 800;
                break;
            default: {
            }
        }
        if (counter >= goal) {
            goal = nextGoal;
            nextGoal = goal + 10;
            level++;
            if (level < 29) {
                stepTimer.setDelay(delay[level]);
                stepTimer.start();
            }
        }
    }

    private boolean isGameOver() {
        for (int i = spawn.y; i < spawn.y + 2; i++)
            for (int j = spawn.x; j < spawn.x + 4; j++)
                if (coloredboard.get(i)[j] != null)
                    return true;
        return false;
    }

    boolean getGameOverStatus() {
        return gameOver;
    }

    void getBoardData() {
        prev_boarddata = (LinkedList<Color[]>) boarddata.clone();
        boarddata.clear();
        for (final Color[] row : coloredboard)
            boarddata.add(row.clone());
        freeze(boarddata);
        setProperty();
    }

    private class TimerListener implements ActionListener {
        @Override
        public void actionPerformed(final ActionEvent e) {
            step();
        }
    }

    void addObserver(PropertyChangeListener l) {
        pcs.addPropertyChangeListener("Board", l);
    }

    private void setProperty() {
        pcs.firePropertyChange("Board", prev_boarddata, boarddata);
    }
}