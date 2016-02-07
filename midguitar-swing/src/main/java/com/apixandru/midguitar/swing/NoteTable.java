package com.apixandru.midguitar.swing;

import com.apixandru.midguitar.model.MidiInput;
import com.apixandru.midguitar.model.NoteListener;
import com.apixandru.midguitar.model.Notes;
import com.apixandru.midguitar.model.matcher.NoteMatcherListener;

import javax.sound.midi.MidiUnavailableException;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
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
public class NoteTable extends JPanel implements MidiInput, NoteMatcherListener {

    private static final Border ACTIVE_BORDER = new CompoundBorder(
            new LineBorder(Color.LIGHT_GRAY, 1),
            new EmptyBorder(new Insets(5, 5, 5, 5)));

    private static final Border INACTIVE_BORDER = new CompoundBorder(
            new LineBorder(new JLabel().getBackground(), 1),
            new EmptyBorder(new Insets(5, 5, 5, 5)));

    private final List<JLabel> noteLabels;
    private final Map<String, JLabel> octaveLabels = new HashMap<>();
    private final Map<String, JLabel> noteHeaderLabels = new HashMap<>();

    private final List<NoteListener> listeners = new ArrayList<>();

    private final Map<Integer, Integer> actualCorrectNotes = new HashMap<>();
    private final Map<Integer, Integer> actualWrongNotes = new HashMap<>();
    private final List<Integer> allNotes = new ArrayList<>();

    /**
     *
     */
    public NoteTable() {
        this.noteLabels = Collections.unmodifiableList(createLabels());
        setLayout(new GridLayout(0, Notes.BASE_NOTE_NAMES.size() + 1));
        addHeader();
        addOctavesAndNotes();
        // some empty space that will allow the motion  listener to know
        // that we exited the notes and octaves zone
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
        for (int i = 0, to = Notes.ALL_NOTE_NAMES.size(); i < to; i++) {
            final JLabel noteLabel;
            if (i >= Notes.SUPPORTED_NOTE_FIRST && i <= Notes.SUPPORTED_NOTE_LAST) {
                noteLabel = new JLabel();
                noteLabel.setBorder(INACTIVE_BORDER);
                noteLabel.setText(" ");
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
        final int numBasicNotes = Notes.NOTES_IN_OCTAVE;

//        +1 to convert to index
        final int firstNote = Notes.getFirstNoteInOctave(Notes.SUPPORTED_NOTE_FIRST);
        final int lastNote = Math.min((Notes.getOctave(Notes.SUPPORTED_NOTE_LAST) + 2) * Notes.NOTES_IN_OCTAVE, Notes.ALL_NOTE_NAMES.size());

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

    public void configure(final List<Integer> allNotes) {
        this.allNotes.clear();
        this.allNotes.addAll(allNotes);

        actualCorrectNotes.clear();
        actualWrongNotes.clear();

        for (int noteNumber = 0; noteNumber < noteLabels.size(); noteNumber++) {
            final JLabel noteLabel = noteLabels.get(noteNumber);
            if (null == noteLabel) {
                continue;
            }
            noteLabel.setBorder(this.allNotes.contains(noteNumber) ? ACTIVE_BORDER : INACTIVE_BORDER);
            clearLabel(noteLabel);
        }
    }

    /**
     * @param label
     */
    private static void clearLabel(JLabel label) {
        label.setText(" ");
    }

    @Override
    public void open() throws MidiUnavailableException {

    }

    @Override
    public void close() throws MidiUnavailableException {
        this.listeners.clear();
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

    @Override
    public void newNote(final int noteNumber) {
    }

    @Override
    public void noteGuessed(final int current, final int actual) {
        if (!allNotes.contains(actual)) {
            return;
        }
        final boolean correct = current == actual;

        int correctTimes = getInt(actual, actualCorrectNotes);
        int wrongTimes = getInt(actual, actualWrongNotes);

        if (correct) {
            correctTimes++;
            actualCorrectNotes.put(actual, correctTimes);
        } else {
            wrongTimes++;
            actualWrongNotes.put(actual, wrongTimes);
        }

        final JLabel noteLabel = this.noteLabels.get(actual);
        if (0 == wrongTimes) {
            noteLabel.setText("100%");
            return;
        }
        if (0 == correctTimes) {
            noteLabel.setText("0%");
            return;
        }

        final int total = wrongTimes + correctTimes;
        final int currentPercent = correctTimes * 100 / total;
        noteLabel.setText(currentPercent + "%");
    }

    /**
     * @param expected
     * @return
     */
    private static int getInt(final int expected, final Map<Integer, Integer> map) {
        Integer integer = map.get(expected);
        if (null == integer) {
            integer = 0;
        }
        return integer;
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
