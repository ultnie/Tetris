package ru.nsu.g.amaseevskii.tetris;

class TetrominoFactory {
    Tetromino getTetromino(int num) {
        Tetromino piece = null;
        switch (num) {
            case (0):
                piece = new I_piece();
                break;
            case (1):
                piece = new J_piece();
                break;
            case (2):
                piece = new L_piece();
                break;
            case (3):
                piece = new O_piece();
                break;
            case (4):
                piece = new S_piece();
                break;
            case (5):
                piece = new T_piece();
                break;
            case (6):
                piece = new Z_piece();
                break;
        }
        return piece;
    }
}
