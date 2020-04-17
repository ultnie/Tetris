package ru.nsu.g.amaseevskii.tetris;

class Tetris {
    Model model;
    private View view;
    private Controller controller;

    public Tetris() {
        model = new Model();
        view = new View(model);
        controller = new Controller(model, view);
    }

    void start(){
        controller.start();
    }
}
