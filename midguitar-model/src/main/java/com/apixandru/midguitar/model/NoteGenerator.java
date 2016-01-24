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

    /**
     * @param from
     * @param to
     */
    public NoteGenerator(final int from, final int to) {
        this.from = from;
        this.step = to - from + 1;
    }

    /**
     * @return
     */
    public int nextNote() {
        return random.nextInt(step) + from;
    }

}
