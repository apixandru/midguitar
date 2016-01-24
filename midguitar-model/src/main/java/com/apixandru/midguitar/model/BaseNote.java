package com.apixandru.midguitar.model;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 23, 2016
 */
public enum BaseNote {

    C("C"), CS(C),
    D("D"), DS(D),
    E("E"),
    F("F"), FS(F),
    G("G"), GS(G),
    A("A"), AS(A),
    B("B");

    private final String value;
    private final boolean sharp;
    private final BaseNote baseNote;

    /**
     * @param value the actual note
     */
    BaseNote(final BaseNote value) {
        this(null, value, true);
    }

    /**
     * @param value the value of the note
     */
    BaseNote(final String value) {
        this(value, null, false);
    }

    /**
     * @param value the value of the note
     * @param sharp
     */
    BaseNote(final String value, final BaseNote baseNote, final boolean sharp) {
        this.sharp = sharp;
        this.baseNote = baseNote == null ? this : baseNote;
        this.value = baseNote == null ? value : baseNote.value;
    }

    /**
     * @return sharp
     */
    public boolean isSharp() {
        return sharp;
    }

    /**
     * @return
     */
    public BaseNote getBaseNote() {
        return baseNote;
    }

    /**
     * @return the value of the note
     */
    public String getValue() {
        if (isSharp()) {
            return value + '#';
        }
        return value;
    }

}
