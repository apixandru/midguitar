package com.apixandru.midguitar.model.matcher;

import com.apixandru.midguitar.model.NoteGenerator;
import com.apixandru.utils.midi.NoteListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 25, 2016
 */
public final class NoteMatcher implements NoteListener {

    private final NoteGenerator noteGenerator;
    private final List<NoteMatcherListener> listeners = new ArrayList<>();
    private int noteExpected;

    public NoteMatcher(final int from, final int to, final boolean includeSharp) {
        this.noteGenerator = new NoteGenerator(from, to, includeSharp);
        this.noteExpected = noteGenerator.nextNote();
    }

    @Override
    public void noteStart(final int noteNumber) {
        listeners.forEach(listener -> listener.noteGuessed(noteExpected, noteNumber));
        if (noteNumber == noteExpected) {
            newNote();
        }
    }

    private void newNote() {
        noteExpected = noteGenerator.nextNote();
        listeners.forEach(listener -> listener.newNote(noteExpected));
    }

    public void addNoteMatchListener(final NoteMatcherListener listener) {
        this.listeners.add(listener);
        listener.newNote(noteExpected);
    }

    public void removeNoteMatchListener(final NoteMatcherListener listener) {
        this.listeners.remove(listener);
    }

    public List<Integer> getAllNotes() {
        return noteGenerator.getAllNotes();
    }

}


