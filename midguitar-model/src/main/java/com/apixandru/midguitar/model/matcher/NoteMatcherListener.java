package com.apixandru.midguitar.model.matcher;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 25, 2016
 */
public interface NoteMatcherListener {

    /**
     * @param note the new note
     */
    void newNote(int note);

    /**
     * @param expected the expected note
     * @param actual   the actual note
     */
    void noteGuessed(int expected, int actual);

}
