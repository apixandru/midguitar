package com.apixandru.utils.midi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Alexandru-Constantin Bledea
 * @since February 07, 2016
 */
public final class CompoundMidiProvider implements MidiProvider {

    private final List<MidiProvider> midiProviders = new ArrayList<>();

    public CompoundMidiProvider(List<MidiProvider> midiProviders) {
        this.midiProviders.addAll(midiProviders);
    }

    @Override
    public List<MidiInput> getInputDevices() throws MidiException {
        return midiProviders.stream()
                .map(provider -> Propagate.withException(provider::getInputDevices))
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

}
