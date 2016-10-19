package com.apixandru.midguitar.swing;

import com.apixandru.utils.midi.MidiException;
import com.apixandru.utils.midi.MidiInput;
import com.apixandru.utils.midi.MidiProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alexandru-Constantin Bledea
 * @since February 07, 2016
 */
class MidiInputPreferenceConverter implements PreferenceConverter {

    private static final Logger log = LoggerFactory.getLogger(MidiInputPreferenceConverter.class);

    private final MidiProvider midiProvider;

    public MidiInputPreferenceConverter(final MidiProvider midiProvider) {
        this.midiProvider = midiProvider;
    }

    @Override
    public String toPreference(final Object o) {
        return ((MidiInput) o).getName();
    }

    @Override
    public Object fromPreference(final String s) {
        try {
            for (final com.apixandru.utils.midi.MidiInput midiInput : midiProvider.getInputDevices()) {
                if (midiInput.getName().equals(s)) {
                    return midiInput;
                }
            }
        } catch (MidiException ex) {
            log.error("Failed to load from preference", ex);
        }
        return null;
    }

}
