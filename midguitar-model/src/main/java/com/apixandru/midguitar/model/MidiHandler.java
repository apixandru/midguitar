package com.apixandru.midguitar.model;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;
import java.util.List;

public class MidiHandler {

    /**
     * @param device
     * @param listeners
     * @return
     * @throws MidiUnavailableException
     */
    public static AutoCloseable connect(final MidiDevice device, final List<NoteListener> listeners) throws MidiUnavailableException {
        final MidiMessageReceiver receiver = new MidiMessageReceiver();
        listeners.stream().forEachOrdered(receiver::addListener);
        device.getTransmitter().setReceiver(receiver);

        device.open();
        System.out.println(device.getDeviceInfo() + " Was Opened");
        return null;
    }

}
