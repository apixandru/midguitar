package com.apixandru.midguitar.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 23, 2016
 */
public final class Notes {

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
        return getBaseNote(noteNumber).getValue() + getOctave(noteNumber);
    }

    /**
     * @param noteNumber the midi note number
     * @return the base note
     */
    public static BaseNote getBaseNote(final int noteNumber) {
        ensureNoteInBounds(noteNumber);
        return BaseNote.values()[noteNumber % BaseNote.values().length];
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
     * @param noteNumber the midi note number
     * @return the octave
     */
    public static int getOctave(final int noteNumber) {
        return noteNumber / BaseNote.values().length - 1;
    }

}
