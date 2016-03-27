package com.apixandru.midguitar.swing;

import com.apixandru.midguitar.model.matcher.NoteMatcherListener;
import com.apixandru.utils.midi.javasound.JsMidiDevices;

import javax.sound.midi.MidiUnavailableException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 24, 2016
 */
public final class Main {

    public static void main(String[] args) throws MidiUnavailableException {
        final JFrame midguitar = new JFrame();
        midguitar.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        final JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        midguitar.setContentPane(contentPane);
        contentPane.setLayout(new GridBagLayout());
        final MidguitarPanel panel = new MidguitarPanel();
        midguitar.add(panel);
        final MidguitarSettings comp = new MidguitarSettings(new JsMidiDevices());
        comp.addNoteMatcherListener(panel);
        comp.addNoteMatcherListener(new FrameNameChange(midguitar));
        midguitar.add(comp);
        midguitar.pack();
        midguitar.setLocationRelativeTo(null);
        midguitar.setVisible(true);
    }

    private static class FrameNameChange implements NoteMatcherListener {

        private final JFrame frame;
        private int correct = 0;
        private int total = 0;

        FrameNameChange(JFrame frame) {
            this.frame = frame;
        }

        @Override
        public void newNote(final int note) {

        }

        @Override
        public void noteGuessed(final int expected, final int actual) {
            total++;
            if (actual == expected) {
                correct++;
            }
            frame.setTitle(String.format("%3s/%3s - %3s%%", correct, total, correct * 100 / total));
        }

    }
}
