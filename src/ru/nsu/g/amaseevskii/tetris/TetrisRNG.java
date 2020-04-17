package ru.nsu.g.amaseevskii.tetris;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

class TetrisRNG {
    private LinkedList<Integer> buffer = new LinkedList<>();
    private Random random = new Random();

    LinkedList<Integer> getSequence () {
        LinkedList<Integer> bag = new LinkedList<>(Arrays.asList(0,1,2,3,4,5,6));
        LinkedList<Integer> sequence = new LinkedList<>();
        int temp;
        for (int i = 7; i>0; i--){
            temp = random.nextInt(i);
            sequence.addFirst(bag.get(temp));
            bag.remove(temp);
        }

        if (buffer.size() == 0) {
            buffer = (LinkedList)sequence.clone();
            return sequence;
        }

        Integer buffer_i_piece = buffer.indexOf(0)+1;
        Integer sequence_i_piece = sequence.indexOf(0)+1;
        if (buffer_i_piece+sequence_i_piece>12)
            sequence = getSequence();
        buffer = (LinkedList)sequence.clone();
        return sequence;
    }
}