package com.apixandru.midguitar.swing;

import javax.sound.midi.MidiDevice;
import javax.swing.*;
import java.awt.*;

/**
 * @author Alexandru-Constantin Bledea
 * @since January 29, 2016
 */
final class MidiCellRenderer extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(final JList<?> list,
                                                  final Object value,
                                                  final int index,
                                                  final boolean isSelected,
                                                  final boolean cellHasFocus) {
        final Component component = super.getListCellRendererComponent(list, value, index, isSelected,
                cellHasFocus);
        if (value instanceof MidiDevice && component instanceof JLabel) {
            final String description = ((MidiDevice) value).getDeviceInfo().getDescription();
            ((JLabel) component).setText(description);

        }
        return component;
    }

}