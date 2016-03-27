package com.apixandru.utils.midi.javasound;

import com.apixandru.utils.midi.MidiInput;
import com.apixandru.utils.midi.NoteListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiUnavailableException;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 30, 2016
 */
final class JsMidiDevice implements MidiInput {

    private static final Logger log = LoggerFactory.getLogger(JsMidiDevice.class);

    private final MidiDevice device;
    private final String name;

    private final JsMidiMessageReceiver receiver = new JsMidiMessageReceiver();

    JsMidiDevice(final MidiDevice device) {
        this.device = device;
        this.name = device.getDeviceInfo().getName();
    }

    @Override
    public void open() throws MidiUnavailableException {
        device.getTransmitter().setReceiver(receiver);
        device.open();
        log.info("{} was opened", device.getDeviceInfo());
    }

    @Override
    public void close() throws MidiUnavailableException {
        this.receiver.close();
        device.getTransmitter().setReceiver(null);
        device.close();
        log.info("{} was closed", device.getDeviceInfo());
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
