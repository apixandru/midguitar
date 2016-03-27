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

    private final List<Integer> allNotes;
    private final int size;

    public NoteGenerator(final int from, final int to, final boolean includeSharpNotes) {
        this.allNotes = generateNotes(from, to, includeSharpNotes);
        this.size = this.allNotes.size();
    }

    private static List<Integer> generateNotes(final int from, final int to, final boolean includeSharpNotes) {
        final List<Integer> notes = new ArrayList<>();
        for (int i = from; i <= to; i++) {
            if (includeSharpNotes || !Notes.isSharp(i)) {
                notes.add(i);
            }
        }
        return Collections.unmodifiableList(notes);
    }

    public int nextNote() {
        return allNotes.get(random.nextInt(size));
    }

    public List<Integer> getAllNotes() {
        return allNotes;
    }

}
