package ru.nsu.g.amaseevskii.tetris;

public class O_piece implements Tetromino {
    private final Integer[][] shape = {{0, 1, 1, 0}, {0, 1, 1, 0}, {0, 0, 0, 0}, {0, 0, 0, 0}};

    @Override
    public Integer[][] turn(int state) {
        return shape;
    }
}
