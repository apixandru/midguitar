package com.apixandru.utils.midi;

import java.util.List;

/**
 * @author Alexandru-Constantin Bledea
 * @since February 07, 2016
 */
public interface MidiProvider {

    List<MidiInput> getInputDevices() throws MidiException;

}
