package com.apixandru.utils.midi.javasound;

import com.apixandru.utils.midi.MidiInput;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 24, 2016
 */
public class JsMidiDevices {

    /**
     * @return the midi devices
     * @throws MidiUnavailableException
     */
    private static Stream<MidiDevice> getDevices() throws MidiUnavailableException {
        final List<MidiDevice> result = new ArrayList<>();
        for (final MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
            result.add(MidiSystem.getMidiDevice(info));
        }
        return result.stream();
    }

    /**
     * @return the synthesizers
     * @throws MidiUnavailableException
     */
    public List<Synthesizer> getSynthesizers() throws MidiUnavailableException {
        return getDevices()
                .filter(Synthesizer.class::isInstance)
                .map(Synthesizer.class::cast)
                .collect(Collectors.toList());

    }

    /**
     * @return the input devices
     * @throws MidiUnavailableException
     */
    public List<MidiInput> getInputDevices() throws MidiUnavailableException {
        return getDevices()
                .filter(dev -> 0 != dev.getMaxTransmitters())
                .map(JsMidiDevice::new)
                .collect(Collectors.toList());
    }

}
