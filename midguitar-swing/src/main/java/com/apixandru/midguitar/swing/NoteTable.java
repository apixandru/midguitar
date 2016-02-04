package com.apixandru.midguitar.swing;

import com.apixandru.midguitar.model.MidiInput;
import com.apixandru.midguitar.model.NoteListener;
import com.apixandru.midguitar.model.Notes;

import javax.sound.midi.MidiUnavailableException;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Alexandru-Constantin Bledea
 * @since February 03, 2016
 */
public class NoteTable extends JPanel implements MidiInput {

    private final List<JLabel> noteLabels;
    private final Map<String, JLabel> octaveLabels = new HashMap<>();
    private final Map<String, JLabel> noteHeaderLabels = new HashMap<>();

    private final List<NoteListener> listeners = new ArrayList<>();

    /**
     *
     */
    public NoteTable() {
        this.noteLabels = Collections.unmodifiableList(createLabels());
        setLayout(new GridLayout(0, Notes.BASE_NOTE_NAMES.size() + 1));
        addHeader();
        addOctavesAndNotes();
        add(new JPanel());
        final NoteTableMouseListener listener = new NoteTableMouseListener();
        addMouseListener(listener);
        addMouseMotionListener(listener);
    }

    /**
     * @return
     */
    private List<JLabel> createLabels() {
        final List<JLabel> noteLabels = new ArrayList<>(Notes.ALL_NOTE_NAMES.size());
        Insets buttonMargin = new Insets(5, 5, 5, 5);
        final CompoundBorder border = new CompoundBorder(new LineBorder(Color.lightGray, 1), new EmptyBorder(buttonMargin));
        for (int i = 0, to = Notes.ALL_NOTE_NAMES.size(); i < to; i++) {
            final JLabel noteLabel;
            if (i >= Notes.SUPPORTED_NOTE_FIRST && i <= Notes.SUPPORTED_NOTE_LAST) {
                noteLabel = new JLabel();
                noteLabel.setBorder(border);
                noteLabel.setText(i + "%");
                noteLabel.setHorizontalAlignment(SwingConstants.RIGHT);
            } else {
                noteLabel = null;
            }
            noteLabels.add(noteLabel);
            ;
        }
        return noteLabels;
    }

    /**
     *
     */
    private void addOctavesAndNotes() {
        final int numBasicNotes = Notes.BASE_NOTE_NAMES.size();

//        +1 to convert to index
        final int firstNote = Notes.getFirstNoteInOctave(Notes.SUPPORTED_NOTE_FIRST);
        final int lastNote = Math.min((Notes.getOctave(Notes.SUPPORTED_NOTE_LAST) + 2) * 12, Notes.ALL_NOTE_NAMES.size());

        for (int i = firstNote; i < lastNote; i++) {
            if (i % numBasicNotes == 0) {
                final String octaveString = String.valueOf(Notes.getOctave(i));
                final JLabel octaveLabel = new JLabel(octaveString, SwingConstants.CENTER);
                octaveLabel.setOpaque(true);
                octaveLabels.put(octaveString, octaveLabel);
                add(octaveLabel);
            }
            final JLabel noteLabel = noteLabels.get(i);
            if (null != noteLabel) {
                add(noteLabel);
            } else {
                add(new JLabel());
            }
        }
    }

    /**
     *
     */
    private void addHeader() {
        add(new JLabel());
        Notes.BASE_NOTE_NAMES.
                stream()
                .map(note -> new JLabel(note, SwingConstants.CENTER))
                .forEachOrdered(label -> {
                    label.setOpaque(true);
                    noteHeaderLabels.put(label.getText(), label);
                    add(label);
                });
    }


    @Override
    public void open() throws MidiUnavailableException {

    }

    @Override
    public void close() throws MidiUnavailableException {

    }

    @Override
    public void addListener(final NoteListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void removeListener(final NoteListener listener) {
        this.listeners.remove(listener);
    }

    @Override
    public String getName() {
        return "Note Table";
    }


    private class NoteTableMouseListener extends MouseAdapter {

        private final LineBorder border = new LineBorder(Color.RED);

        private String fullNoteName;
        private String noteName;
        private String octave;

        private JLabel octaveLabel = new JLabel();
        private JLabel noteLabel = new JLabel();

        @Override
        public void mouseReleased(final MouseEvent e) {
            final Component componentAt = findComponentAt(e.getPoint());
            final int noteNumber = noteLabels.indexOf(componentAt);
            if (-1 != noteNumber) {
                listeners.forEach(noteListener -> noteListener.noteStart(noteNumber));
            }
        }

        @Override
        public void mouseMoved(final MouseEvent e) {
            final JComponent componentAt = (JComponent) findComponentAt(e.getPoint());
            final int noteNumber = noteLabels.indexOf(componentAt);
            if (-1 == noteNumber) {
                clearStyle();
                return;
            }

            final String noteNameWithOctave = Notes.getNoteNameWithOctave(noteNumber);
            final String octave = noteNameWithOctave.substring(noteNameWithOctave.length() - 1);
            final String noteName = noteNameWithOctave.substring(0, noteNameWithOctave.length() - 1);
            final JLabel octaveLabel = octaveLabels.get(octave);
            final JLabel noteLabel = noteHeaderLabels.get(noteName);

            synchronized (this) {
                if (noteNameWithOctave.equals(fullNoteName)) {
                    return;
                }

                clearStyle();

                this.fullNoteName = noteNameWithOctave;
                this.octaveLabel = octaveLabel;
                this.noteLabel = noteLabel;

                octaveLabel.setBorder(border);
                noteLabel.setBorder(border);

            }
        }

        private void clearStyle() {
            this.fullNoteName = null;
            this.octaveLabel.setBorder(null);
            this.noteLabel.setBorder(null);
        }

    }
}
