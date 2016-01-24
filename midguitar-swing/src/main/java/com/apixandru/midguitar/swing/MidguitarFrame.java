package com.apixandru.midguitar.swing;

import com.apixandru.midguitar.model.MidGuitarFactory;
import com.apixandru.midguitar.model.MidiDevices;
import com.apixandru.midguitar.model.MidiHandler;

import javax.sound.midi.MidiUnavailableException;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.function.BiConsumer;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 24, 2016
 */
public final class MidguitarFrame {

    public static void main(String[] args) throws MidiUnavailableException {
        final JFrame midguitar = new JFrame();
        BiConsumer<Integer, Integer> stats = (c, w) -> midguitar.setTitle(String.format("Correct %3s Wrong %3d", c, w));
        stats.accept(0, 0);
        midguitar.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        midguitar.getContentPane().setLayout(new GridBagLayout());
        final MidguitarPanel panel = new MidguitarPanel(stats);
        midguitar.add(panel);
        midguitar.pack();
        midguitar.setLocationRelativeTo(null);
        midguitar.setVisible(true);

        MidiHandler.connect(MidiDevices.getInputDevices().get(0),
                Arrays.asList(MidGuitarFactory.newSynthNoteListener(), panel));
    }

}
