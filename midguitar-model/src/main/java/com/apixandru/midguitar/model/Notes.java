package com.apixandru.midguitar.model;

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
    private static BaseNote getBaseNote(final int noteNumber) {
        ensureNoteInBounds(noteNumber);
        return BaseNote.values()[noteNumber % BaseNote.values().length];
    }

    /**
     * @param noteNumber the midi note number
     * @return if the note is sharp
     */
    public static boolean isSharp(final int noteNumber) {
        return getBaseNote(noteNumber).isSharp();
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

    /**
     * @param noteNumber the midi note number
     * @return the octave
     */
    public static int getOctave(final int noteNumber) {
        return noteNumber / BaseNote.values().length - 1;
    }

}
