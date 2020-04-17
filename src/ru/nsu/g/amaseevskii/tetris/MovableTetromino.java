package ru.nsu.g.amaseevskii.tetris;

class MovableTetromino {
    Tetromino piece;
    String name;
    Integer[][] shape;
    int state;
    Position position;
    boolean i_gimmik;

    MovableTetromino(Tetromino piece, Position position) {
        this.piece = piece;
        this.position = new Position(position.x, position.y);
        name = this.piece.getClass().getName();
        shape = piece.turn(-1);
        i_gimmik = false;
        state = 0;
    }
}
