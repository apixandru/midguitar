package com.apixandru.utils.midi.javasound;

import com.apixandru.utils.midi.NoteListener;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 24, 2016
 */
final class JsMidiMessageReceiver implements Receiver {

    private final List<NoteListener> listeners = new ArrayList<>();

    @Override
    public void send(final MidiMessage msg, final long timeStamp) {
        final int noteNumber = ((ShortMessage) msg).getData1();
        if (ShortMessage.NOTE_ON == msg.getStatus()) {
            listeners.stream()
                    .forEachOrdered(listener -> listener.noteStart(noteNumber));
        }
    }

    @Override
    public void close() {
        this.listeners.clear();
    }

    void addListener(final NoteListener listener) {
        this.listeners.add(listener);
    }

    void removeListener(final NoteListener listener) {
        this.listeners.remove(listener);
    }

}
