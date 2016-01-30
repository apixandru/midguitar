package com.apixandru.midguitar.model;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 30, 2016
 */
public final class MidiInputRealDevice implements MidiInput {

    private final MidiDevice device;
    private MidiMessageReceiver receiver = new MidiMessageReceiver();

    private MidiInputRealDevice(final MidiDevice device) {
        this.device = device;
    }

    @Override
    public void open() throws MidiUnavailableException {
        device.getTransmitter().setReceiver(receiver);
        device.open();
        System.out.println(device.getDeviceInfo() + " Was Opened");
    }

    @Override
    public void close() throws MidiUnavailableException {
        device.getTransmitter().setReceiver(null);
        device.close();
        System.out.println(device.getDeviceInfo() + " Was Closed");
    }

    @Override
    public void addListener(final NoteListener listener) {
        receiver.addListener(listener);
    }

    @Override
    public void removeListener(final NoteListener listener) {
        receiver.removeListener(listener);
    }

}
