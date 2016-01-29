package com.apixandru.midguitar.model;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 30, 2016
 */
public class MidguitarModel {

    private final MidiMessageReceiver receiver = new MidiMessageReceiver();
    private MidiDevice device;

    public void start() throws MidiUnavailableException {
        if (null == device) {
            throw new MidiUnavailableException();
        }
        device.getTransmitter().setReceiver(receiver);
        device.open();
        System.out.println(device.getDeviceInfo() + " Was Opened");
    }

    public void stop() {
        device.close();
    }

    public void addListener(NoteListener listener) {
        receiver.addListener(listener);
    }

    public void removeListener(NoteListener listener) {
        receiver.removeListener(listener);
    }

    public void setDevice(final MidiDevice device) {
        this.device = device;
    }

}
