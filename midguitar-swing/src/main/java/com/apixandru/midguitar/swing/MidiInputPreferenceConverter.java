package com.apixandru.midguitar.swing;

import com.apixandru.utils.midi.MidiException;
import com.apixandru.utils.midi.MidiInput;
import com.apixandru.utils.midi.MidiProvider;
import com.apixandru.utils.swing.components.PreferenceConverter;

/**
 * @author Alexandru-Constantin Bledea
 * @since February 07, 2016
 */
public class MidiInputPreferenceConverter implements PreferenceConverter {

    private final MidiProvider midiProvider;

    public MidiInputPreferenceConverter(final MidiProvider midiProvider) {
        this.midiProvider = midiProvider;
    }

    @Override
    public String toPreference(final Object o) {
        final MidiInput midiInput = (MidiInput) o;
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
            ex.printStackTrace(); // handle this?
        }
        return null;
    }

}
