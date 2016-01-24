package com.apixandru.midguitar.model;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.Synthesizer;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 24, 2016
 */
final class SynthNoteListener implements NoteListener, AutoCloseable {

    private final Synthesizer synthesizer;
    private final MidiChannel midiChannel;

    /**
     * @param synthesizer the synthesizer
     */
    SynthNoteListener(final Synthesizer synthesizer) {
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
