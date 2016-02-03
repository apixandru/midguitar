package com.apixandru.midguitar.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 23, 2016
 */
public final class Notes {

    public static final List<String> ALL_NOTE_NAMES;
    public static final List<String> BASE_NOTE_NAMES;

    static {
        final List<String> noteNames = new ArrayList<>(128);
        final List<String> baseNotes = Arrays.asList("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B");
        int octave = -1;
        int maxNotes = baseNotes.size();
        for (int i = 0, currentNote = 0; i <= 127; i++) {
            final String baseNote = baseNotes.get(currentNote);
            noteNames.add(baseNote + octave);
            currentNote++;
            if (currentNote == maxNotes) {
                currentNote = 0;
                octave++;
            }
        }
        ALL_NOTE_NAMES = Collections.unmodifiableList(noteNames);
        BASE_NOTE_NAMES = Collections.unmodifiableList(baseNotes);
    }

    /**
     *
     */
    private Notes() {
    }

    /**
     * @param noteNumber the note number from the midi input
     * @return the note name with octave
     */
    public static String getNoteNameWithOctave(final int noteNumber) {
        return ALL_NOTE_NAMES.get(noteNumber);
    }

    /**
     * @param noteNumber the midi note number
     * @return the base note
     */
    private static BaseNote getBaseNote(final int noteNumber) {
        ensureNoteInBounds(noteNumber);
        return BaseNote.values()[noteNumber % BaseNote.values().length];
    }

    /**
     * @param noteNumber the midi note number
     * @return if the note is sharp
     */
    public static boolean isSharp(final int noteNumber) {
        return getNoteNameWithOctave(noteNumber).contains("#");
    }

    /**
     * @param noteNumber the midi note number
     */
    private static void ensureNoteInBounds(final int noteNumber) {
        if (noteNumber < 0 || noteNumber > 127) {
            throw new IndexOutOfBoundsException("Expected note to be between 0 and 127");
        }
    }

    /**
     * @param from the note from which to start
     * @param to   the note to which to check
     * @return the number of full notes in between
     */
    public static int getFullNotesInBetween(int from, int to) {
        if (from > to) {
            return -getFullNotesInBetween0(to, from);
        }
        return getFullNotesInBetween0(from, to);
    }

    /**
     * @param from the note from which to start
     * @param to   the note to which to check
     * @return the number of full notes in between
     */
    private static int getFullNotesInBetween0(final int from, final int to) {
        int count = 0;
        BaseNote lastNote = Notes.getBaseNote(from).getBaseNote();
        for (int i = from; i <= to; i++) {
            final BaseNote currentNote = Notes.getBaseNote(i);
            if (currentNote.getBaseNote() != lastNote) {
                count++;
            }
            lastNote = currentNote.getBaseNote();
        }
        return count;
    }

    public static List<String> getNoteNames() {
        return ALL_NOTE_NAMES;
    }

}
