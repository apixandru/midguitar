package com.apixandru.midguitar.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    private final List<Integer> allNotes;
    private final int size;

    /**
     * @param from
     * @param to
     * @param includeSharpNotes
     */
    public NoteGenerator(final int from, final int to, final boolean includeSharpNotes) {
        this.from = from;
        this.step = to - from + 1;
        this.includeSharpNotes = includeSharpNotes;

        final List<Integer> notes = new ArrayList<>();
        for (int i = from; i <= to; i++) {
            if (includeSharpNotes || !Notes.isSharp(i)) {
                notes.add(i);
            }
        }
        size = notes.size();
        this.allNotes = Collections.unmodifiableList(notes);
    }

    /**
     * @return
     */
    public int nextNote() {
        return allNotes.get(random.nextInt(size));
    }

    public List<Integer> getAllNotes() {
        return allNotes;
    }

}
