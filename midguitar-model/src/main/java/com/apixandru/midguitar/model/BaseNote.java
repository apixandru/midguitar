package com.apixandru.midguitar.model;

/**
 * This class seems overkill, maybe replace it? I'm not sure that all this information is actually needed.
 *
 * @author Alexandru-Constantin Bledea
 * @since January 23, 2016
 */
enum BaseNote {

    C, CS(C),
    D, DS(D),
    E,
    F, FS(F),
    G, GS(G),
    A, AS(A),
    B;

    private final BaseNote baseNote;

    /**
     *
     */
    BaseNote() {
        this(null);
    }

    /**
     * @param baseNote
     */
    BaseNote(final BaseNote baseNote) {
        this.baseNote = baseNote == null ? this : baseNote;
    }

    /**
     * @return
     */
    BaseNote getBaseNote() {
        return baseNote;
    }

}
