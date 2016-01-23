package com.apixandru.midguitar.model;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 23, 2016
 */
public enum BaseNote {

    C("C"), CS("C#"),
    D("D"), DS("D#"),
    E("E"),
    F("F"), FS("F#"),
    G("G"), GS("G#"),
    A("A"), AS("A#"),
    B("B");

    private final String value;

    /**
     * @param value the value of the note
     */
    BaseNote(final String value) {
        this.value = value;
    }

    /**
     * @return the value of the note
     */
    public String getValue() {
        return value;
    }

}
