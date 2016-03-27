package com.apixandru.midguitar.model.matcher;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 25, 2016
 */
public interface NoteMatcherListener {

    void newNote(int note);

    void noteGuessed(int expected, int actual);

}
