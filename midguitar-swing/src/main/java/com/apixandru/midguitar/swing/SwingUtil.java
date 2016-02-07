package com.apixandru.midguitar.swing;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import java.awt.event.ItemEvent;
import java.util.Vector;
import java.util.prefs.Preferences;

/**
 * @author Alexandru-Constantin Bledea
 * @since February 07, 2016
 */
public class SwingUtil {

    public static <T> JComboBox<T> preferenceBoundJComboBox(final Object parent,
                                                             final Vector<T> vector,
                                                             final String preference) {

        final Preferences preferences = Preferences.userNodeForPackage(parent.getClass());
        final String storedValue = preferences.get(preference, null);

        final DefaultComboBoxModel<T> model = new DefaultComboBoxModel<>(vector);
        if (null != storedValue) {
            model.setSelectedItem(vector.get(Integer.parseInt(storedValue)));
        }
        final JComboBox<T> comboBox = new JComboBox<>(model);
        comboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                final int index = vector.indexOf(e.getItem());
                preferences.put(preference, String.valueOf(index));
            }
        });
        return comboBox;
    }

}
