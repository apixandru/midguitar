package com.apixandru.utils.midi;

import javax.sound.midi.MidiUnavailableException;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 30, 2016
 */
public interface MidiInput extends MidiDevice {

    void open() throws MidiUnavailableException;

    void close() throws MidiUnavailableException;

    void addListener(NoteListener listener);

    void removeListener(NoteListener listener);

}
