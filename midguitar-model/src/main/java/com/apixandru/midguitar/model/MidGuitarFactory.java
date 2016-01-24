package com.apixandru.midguitar.model;

import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 24, 2016
 */
public final class MidGuitarFactory {

    /**
     *
     */
    private MidGuitarFactory() {
    }

    /**
     * @return a new SynthNoteListener
     * @throws MidiUnavailableException
     */
    public static SynthNoteListener newSynthNoteListener() throws MidiUnavailableException {
        final Synthesizer synthesizer = MidiDevices.getSynthesizers().get(0);
        synthesizer.open();
        return new SynthNoteListener(synthesizer);
    }

}
