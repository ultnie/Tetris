package ru.nsu.g.amaseevskii.tetris;

import java.io.*;
import java.util.LinkedList;
import java.util.Map;

public class Highscores {
    LinkedList<Map.Entry<Integer, String>> records = new LinkedList<>();

    void readCSV () {
        String input;
        String[] splitinput;
        String nickname;
        int score;
        try {
            File file = new File("records.csv");
            if (!file.createNewFile()) {
                FileInputStream isr = new FileInputStream(file);
                BufferedReader br = new BufferedReader(new InputStreamReader(isr));
                int i = 0;
                while (true) {
                    input = br.readLine();
                    if (input == null || input.isEmpty() || i == 11)
                        break;
                    splitinput = input.split(",");
                    nickname = splitinput[0];
                    score = Integer.parseInt(splitinput[1]);
                    records.add(Map.entry(score, nickname));
                    i++;
                }
                isr.close();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    void makeCSV() {
        String output;
        FileOutputStream fos;
        try {
            fos = new FileOutputStream("records.csv");
            for (int i = 0; i < 10 && i < records.size(); i++){
                Integer score = records.get(i).getKey();
                String nickname = records.get(i).getValue();
                output = nickname+","+score+"\n";
                fos.write(output.getBytes());
            }
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
