package com.apixandru.midguitar.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 23, 2016
 */
public final class Notes {

    public static final int SUPPORTED_NOTE_FIRST = 40;
    public static final int SUPPORTED_NOTE_LAST = 88;


    public static final List<String> ALL_NOTE_NAMES;
    public static final List<String> BASE_NOTE_NAMES;
    public static final int NOTES_IN_OCTAVE = 12;

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

    private Notes() {
    }

    public static int getFirstNoteInOctave(final int noteNumber) {
        return (getOctave(noteNumber) + 1) * NOTES_IN_OCTAVE;
    }

    public static String getNoteNameWithOctave(final int noteNumber) {
        return ALL_NOTE_NAMES.get(noteNumber);
    }

    public static int getOctave(final int noteNumber) {
        return noteNumber / NOTES_IN_OCTAVE - 1;
    }

    public static boolean isSharp(final int noteNumber) {
        return getNoteNameWithOctave(noteNumber).contains("#");
    }

    public static int getFullNotesInBetween(int from, int to) {
        if (from > to) {
            return -getFullNotesInBetween0(to, from);
        }
        return getFullNotesInBetween0(from, to);
    }

    private static int getFullNotesInBetween0(final int from, final int to) {
        int count = 0;
        char lastNote = getNoteNameWithOctave(from).charAt(0);
        for (int i = from; i <= to; i++) {
            final char currentNote = getNoteNameWithOctave(i).charAt(0);
            if (currentNote != lastNote) {
                count++;
            }
            lastNote = currentNote;
        }
        return count;
    }

    public static List<String> getNoteNames() {
        return ALL_NOTE_NAMES;
    }

    public static List<String> getSupportedNoteNames(final boolean includeSharp) {
        final List<String> supportedNoteNames = getSupportedNoteNames();
        if (includeSharp) {
            return supportedNoteNames;
        }
        final List<String> newNotes = new ArrayList<>(supportedNoteNames);
        removeSharpNotes(newNotes);
        return Collections.unmodifiableList(newNotes);
    }

    private static List<String> getSupportedNoteNames() {
        return ALL_NOTE_NAMES.subList(SUPPORTED_NOTE_FIRST, SUPPORTED_NOTE_LAST + 1);
    }

    private static void removeSharpNotes(final List<String> noteNames) {
        final Iterator<String> it = noteNames.iterator();
        while (it.hasNext()) {
            if (it.next().contains("#")) {
                it.remove();
            }
        }
    }

}
