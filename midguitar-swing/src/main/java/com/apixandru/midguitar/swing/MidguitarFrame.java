package com.apixandru.midguitar.swing;

import com.apixandru.midguitar.model.MidGuitarFactory;
import com.apixandru.midguitar.model.MidiDevices;
import com.apixandru.midguitar.model.MidiHandler;
import com.apixandru.midguitar.model.matcher.NoteMatcher;

import javax.sound.midi.MidiUnavailableException;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 24, 2016
 */
public final class MidguitarFrame {

    public static void main(String[] args) throws MidiUnavailableException {
        final JFrame midguitar = new JFrame();
        midguitar.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        midguitar.getContentPane().setLayout(new GridBagLayout());
        final MidguitarPanel panel = new MidguitarPanel();
        midguitar.add(panel);
        midguitar.pack();
        midguitar.setLocationRelativeTo(null);
        midguitar.setVisible(true);

        final NoteMatcher noteMatcher = new NoteMatcher();
        noteMatcher.addNoteMatchListener(panel);

        MidiHandler.connect(new MidiDevices().getInputDevices().get(0),
                Arrays.asList(MidGuitarFactory.newSynthNoteListener(), noteMatcher));
    }

}
