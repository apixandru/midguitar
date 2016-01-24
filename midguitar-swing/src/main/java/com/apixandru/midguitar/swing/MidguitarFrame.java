package com.apixandru.midguitar.swing;

import com.apixandru.midguitar.model.MidGuitarFactory;
import com.apixandru.midguitar.model.MidiDevices;
import com.apixandru.midguitar.model.MidiHandler;

import javax.sound.midi.MidiUnavailableException;
import javax.swing.*;
import java.awt.*;
import java.util.Collections;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 24, 2016
 */
public final class MidguitarFrame {

    public static void main(String[] args) throws MidiUnavailableException {
        final JFrame midguitar = new JFrame("Midguitar");
        midguitar.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        midguitar.getContentPane().setLayout(new GridBagLayout());
        midguitar.add(new MidguitarPanel());
        midguitar.pack();
        midguitar.setLocationRelativeTo(null);
        midguitar.setVisible(true);

        MidiHandler.connect(MidiDevices.getInputDevices().get(0), Collections.singletonList(MidGuitarFactory.newSynthNoteListener()));
    }

}
