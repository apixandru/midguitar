package com.apixandru.midguitar.swing;

import com.apixandru.midguitar.model.MidiDevices;

import javax.sound.midi.MidiUnavailableException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 24, 2016
 */
public final class MidguitarFrame {

    public static void main(String[] args) throws MidiUnavailableException {
        final JFrame midguitar = new JFrame();
        midguitar.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        final JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        midguitar.setContentPane(contentPane);
        contentPane.setLayout(new GridBagLayout());
        final MidguitarPanel panel = new MidguitarPanel();
        midguitar.add(panel);
        midguitar.add(new MidguitarSettings(new MidiDevices(), panel));
        midguitar.pack();
        midguitar.setLocationRelativeTo(null);
        midguitar.setVisible(true);
    }

}
