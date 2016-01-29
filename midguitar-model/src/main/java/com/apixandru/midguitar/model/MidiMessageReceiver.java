package com.apixandru.midguitar.model;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 24, 2016
 */
final class MidiMessageReceiver implements Receiver {

    private final List<NoteListener> listeners = new ArrayList<>();

    @Override
    public void send(final MidiMessage msg, final long timeStamp) {
        final int noteNumber = ((ShortMessage) msg).getData1();
        switch (msg.getStatus()) {
            case ShortMessage.NOTE_ON:
                listeners.stream()
                        .forEachOrdered(listener -> listener.noteStart(noteNumber));
                break;
            case ShortMessage.NOTE_OFF:
                break;
        }
    }

    @Override
    public void close() {
    }

    /**
     * @param listener note listener
     */
    public void addListener(final NoteListener listener) {
        this.listeners.add(listener);
    }

    /**
     * @param listener note listener
     */
    public void removeListener(final NoteListener listener) {
        this.listeners.remove(listener);
    }

}
