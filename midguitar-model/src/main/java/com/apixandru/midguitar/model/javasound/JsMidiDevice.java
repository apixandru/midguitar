package com.apixandru.midguitar.model.javasound;

import com.apixandru.utils.midi.MidiInput;
import com.apixandru.utils.midi.NoteListener;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 30, 2016
 */
public final class JsMidiDevice implements MidiInput {

    private final MidiDevice device;
    private final String name;

    private final JsMidiMessageReceiver receiver = new JsMidiMessageReceiver();

    public JsMidiDevice(final MidiDevice device) {
        this.device = device;
        this.name = device.getDeviceInfo().getName();
    }

    @Override
    public void open() throws MidiUnavailableException {
        device.getTransmitter().setReceiver(receiver);
        device.open();
        System.out.println(device.getDeviceInfo() + " Was Opened");
    }

    @Override
    public void close() throws MidiUnavailableException {
        this.receiver.close();
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

    @Override
    public String getName() {
        return name;
    }

}
