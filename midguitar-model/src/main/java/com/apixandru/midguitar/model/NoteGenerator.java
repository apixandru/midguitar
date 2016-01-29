package com.apixandru.midguitar.model;

import java.util.Random;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 24, 2016
 */
public final class NoteGenerator {

    private final Random random = new Random();

    private final int from;
    private final int step;
    private final boolean includeSharpNotes;

    /**
     * @param from
     * @param to
     * @param includeSharpNotes
     */
    public NoteGenerator(final int from, final int to, final boolean includeSharpNotes) {
        this.from = from;
        this.step = to - from + 1;
        this.includeSharpNotes = includeSharpNotes;
    }

    /**
     * @return
     */
    public int nextNote() {
        int nextNote;
        do {
            nextNote = random.nextInt(step) + from;
        } while (!includeSharpNotes && Notes.isSharp(nextNote));
        return nextNote;
    }

}
