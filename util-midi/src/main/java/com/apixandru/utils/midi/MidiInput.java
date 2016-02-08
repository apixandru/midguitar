package com.apixandru.utils.midi;

import javax.sound.midi.MidiUnavailableException;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 30, 2016
 */
public interface MidiInput extends MidiDevice {

    /**
     * @throws MidiUnavailableException
     */
    void open() throws MidiUnavailableException;

    /**
     * @throws MidiUnavailableException
     */
    void close() throws MidiUnavailableException;

    /**
     * @param listener
     */
    void addListener(NoteListener listener);

    /**
     * @param listener
     */
    void removeListener(NoteListener listener);

}