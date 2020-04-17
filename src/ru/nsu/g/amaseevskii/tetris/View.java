package ru.nsu.g.amaseevskii.tetris;


import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;

class View {
    private JFrame tetrisFrame;





    boolean donealready = false;






    JFrame highscoresFrame;
    JFrame aboutFrame;
    JButton restart;
    JButton changelevel;
    JButton highscores;
    JButton about;
    JButton exit;
    private Model model;
    private JLabel score;
    private JLabel level;
    private JLabel lines;
    private JLabel nextlvl;
    JPanel sidepanel;
    JPanel highscoresPanel;
    TetrisPanel tetris;
    JLabel gameOver;
    private NextPiecePanel next;

    View(Model model){
        tetrisFrame = new JFrame("Tetris");
        highscoresFrame = new JFrame("Highscores");
        aboutFrame = new JFrame("About");
        this.model = model;
    }

    void setupGUI() {
        tetrisFrame.setLayout(new GridBagLayout());
        tetris = new TetrisPanel();

        next = new NextPiecePanel();
        score = new JLabel("Score: " + model.score);
        level = new JLabel("Level: " + (model.level + 1));
        lines = new JLabel("Lines: " + model.counter);
        nextlvl = new JLabel("Next level: " + model.goal + " lines");

        restart = new JButton("Restart");
        restart.setFocusable(false);
        restart.setPreferredSize(new Dimension(60, 20));

        changelevel = new JButton("Set Level");
        changelevel.setFocusable(false);
        changelevel.setPreferredSize(new Dimension(60, 20));

        highscores = new JButton("Highscores");
        highscores.setFocusable(false);
        highscores.setPreferredSize(new Dimension(60, 20));

        about = new JButton(("About"));
        about.setFocusable(false);
        about.setPreferredSize(new Dimension(60, 20));

        exit = new JButton("Exit");
        exit.setFocusable(false);
        exit.setPreferredSize(new Dimension(60, 20));

        sidepanel = new JPanel();
        sidepanel.setFocusable(false);
        sidepanel.setLayout(new BoxLayout(sidepanel, BoxLayout.Y_AXIS));
        sidepanel.add(next);
        sidepanel.add(score);
        sidepanel.add(level);
        sidepanel.add(lines);
        sidepanel.add(nextlvl);
        sidepanel.add(restart);
        sidepanel.add(changelevel);
        sidepanel.add(highscores);
        sidepanel.add(about);
        sidepanel.add(exit);
        sidepanel.setPreferredSize(new Dimension(250,600));
        gameOver = new JLabel("Game Over");

        tetrisFrame.add(tetris);
        tetrisFrame.add(sidepanel);

        tetrisFrame.setSize(570,650);
        tetrisFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tetrisFrame.setLocationRelativeTo(null);
        tetrisFrame.setResizable(false);
        tetrisFrame.setVisible(true);

        highscoresFrame.setSize(500, 320);
        highscoresFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        highscoresPanel = new JPanel();
        highscoresPanel.setLayout(new BoxLayout(highscoresPanel, BoxLayout.Y_AXIS));
        highscoresPanel.setFocusable(false);
        highscoresPanel.setPreferredSize(new Dimension(200,200));
        JTextArea highscores = new JTextArea();
        highscores.setFont(new Font ("monospaced", Font.BOLD, 16));
        for (int i = 0; i < model.highscores.records.size(); i++) {
            if (i<9)
                highscores.append((i+1)+".  "+model.highscores.records.get(i).getValue()
                        +" ".repeat(11-model.highscores.records.get(i).getValue().length())
                        +model.highscores.records.get(i).getKey()+"\n");
            else
                highscores.append((i+1)+". "+model.highscores.records.get(i).getValue()
                        +" ".repeat(11-model.highscores.records.get(i).getValue().length())
                        +model.highscores.records.get(i).getKey()+"\n");
        }
        highscores.append("\nIf you want to unpause the game and still\nsee highscores click on window with\nthe gameboard and press ESCAPE");
        highscoresFrame.add(highscores);

        aboutFrame.setSize(600,300);
        aboutFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        JPanel aboutPanel = new JPanel();
        aboutPanel.setLayout(new BoxLayout(aboutPanel, BoxLayout.Y_AXIS));
        aboutPanel.setFocusable(false);
        aboutPanel.setPreferredSize(new Dimension(200,200));
        JTextArea aboutTextArea = new JTextArea();
        aboutTextArea.setFont(new Font ("monospaced", Font.BOLD, 14));
        aboutTextArea.append("This is Tetris. Nothing more to say about it.\n\n" +
                "Controls:\nZ - Counter Clockwise Turn       X - Clockwise Turn\n" +
                "DOWN ARROW - Soft Drop           LEFT/RIGHT ARROWS - Move Left/Right\n" +
                "SPACE - Hard drop                ESCAPE - Pause/Unpause\n\n\n\n" +
                "Made by Maseevsky Anton as a student task at NSU\n\n\n");
        aboutTextArea.append("If you want to see controls and play\njust click on window with\nthe gameboard and press ESCAPE");
        aboutFrame.add(aboutTextArea);

    }

    class TetrisPanel extends JPanel implements PropertyChangeListener {
        int blocksize = 30;
        LinkedList<Color[]> board = new LinkedList<>();

        TetrisPanel() {
            super();
            for (int h = 0; h < model.height; h++)
                board.add(new Color[model.wight]);
            setBackground(Color.darkGray);
            setPreferredSize(new Dimension(300, 600));
            model.addObserver(this);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D graphics2D = (Graphics2D) g;
            for (int i = 0; i < board.size(); i++) {
                for (int j = 0; j < board.get(i).length; j++) {
                    graphics2D.setColor(Color.black);
                    graphics2D.drawRect(j * blocksize, i * blocksize, blocksize, blocksize);
                    Color color = board.get(i)[j];
                    if (color != null) {
                        graphics2D.setColor(color);
                        graphics2D.fillRect(j * blocksize+1, i * blocksize+1, blocksize - 1, blocksize - 1);
                    }
                }
            }
        }

        @Override
        public void propertyChange(PropertyChangeEvent e) {
            tetris.board = (LinkedList<Color[]>) e.getNewValue();

            if (model.gameOver) {
                sidepanel.add(gameOver);
                if (model.highscoreset) {
                    model.nickname = JOptionPane.showInputDialog("New highscore!\nIf you want to save it in a highscore table\nEnter you nickname (up to 10 characters) here");
                    while (model.nickname.length() > 10) {
                        model.nickname = JOptionPane.showInputDialog("Wow, that' a long nickname.\nSadly for the clean smooth table I had to make a limit.\nUp to 10 characters, please");
                    }
                }
            }
            else {
                tetris.repaint();
                score.setText("Score: " + model.score);
                level.setText("Level: " + (model.level+1));
                lines.setText("Lines: " + model.counter);
                nextlvl.setText("Next level: " + model.goal + " lines");
                next.repaint();
            }
            sidepanel.updateUI();
        }
    }

    public class NextPiecePanel extends JPanel {
        Dimension size = new Dimension(130,130);
        int blocksize = 30;

        NextPiecePanel(){
            super();
            setPreferredSize(size);
        }

        @Override
        protected void paintComponent (Graphics g){
            super.paintComponent(g);
            Graphics2D graphics2D = (Graphics2D) g;
            Integer[][] shape = model.nextPiece.turn(-1);
            for (int i=0; i<4; i++)
                for (int j=0; j<4; j++)
                    if (shape[i][j]==1) {
                        graphics2D.setColor(Color.black);
                        graphics2D.drawRect(j*blocksize, i*blocksize, blocksize, blocksize);
                        graphics2D.setColor(model.colors[model.nextid]);
                        graphics2D.fillRect(j*blocksize+1, i*blocksize+1, blocksize-1, blocksize-1);
                    }
        }
    }
}
