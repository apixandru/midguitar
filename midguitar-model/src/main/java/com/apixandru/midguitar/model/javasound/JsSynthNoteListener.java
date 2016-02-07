package com.apixandru.midguitar.model.javasound;

import com.apixandru.midguitar.model.NoteListener;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.Synthesizer;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 24, 2016
 */
public final class JsSynthNoteListener implements NoteListener, AutoCloseable {

    private final Synthesizer synthesizer;
    private final MidiChannel midiChannel;

    /**
     * @param synthesizer the synthesizer
     */
    public JsSynthNoteListener(final Synthesizer synthesizer) {
        this.synthesizer = synthesizer;
        this.midiChannel = Arrays.stream(this.synthesizer.getChannels())
                .filter(Objects::nonNull)
                .findFirst().get();
    }

    @Override
    public void noteStart(final int noteNumber) {
        midiChannel.noteOn(noteNumber, 100);
    }

    @Override
    public void close() {
        synthesizer.close();
    }
}
