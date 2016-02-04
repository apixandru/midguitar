package com.apixandru.midguitar.swing;

import com.apixandru.midguitar.model.Notes;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Alexandru-Constantin Bledea
 * @since February 03, 2016
 */
public class NoteTable extends JPanel {

    private final List<JLabel> noteLabels;

    /**
     *
     */
    public NoteTable() {
        this.noteLabels = Collections.unmodifiableList(createLabels());
        setLayout(new GridLayout(0, Notes.BASE_NOTE_NAMES.size() + 1));
        addHeader();
        addOctavesAndNotes();
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
                final int octave = Notes.getOctave(i);
                add(new JLabel(String.valueOf(octave), SwingConstants.CENTER));
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
                .forEachOrdered(this::add);
    }

}
