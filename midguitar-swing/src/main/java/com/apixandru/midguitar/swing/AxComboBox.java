package com.apixandru.midguitar.swing;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;
import java.util.prefs.Preferences;

/**
 * @author Alexandru-Constantin Bledea
 * @since February 07, 2016
 */
class AxComboBox<E> extends JComboBox<E> {

    private final PreferenceConverter preferenceConverter;
    private final Preferences preferences;
    private final String preferenceKey;
    private final ItemListener preferenceListener = new ItemListener() {
        @Override
        public void itemStateChanged(final ItemEvent e) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                preferences.put(preferenceKey, preferenceConverter.toPreference(e.getItem()));
            }
        }
    };

    AxComboBox(final JComponent parent, final String preferenceKey) {
        this(parent, preferenceKey, StringPreferenceConverter.INSTANCE);
    }

    private AxComboBox(final JComponent parent, final String preferenceKey, final PreferenceConverter preferenceConverter) {
        super(new DefaultComboBoxModel<>());
        this.preferenceConverter = preferenceConverter;

        this.preferenceKey = preferenceKey;
        this.preferences = Preferences.userNodeForPackage(parent.getClass());

        addItemListener(this.preferenceListener);
    }

    void setup(final Collection<E> items) {
        removeItemListener(this.preferenceListener);
        final DefaultComboBoxModel<E> model = getModel();
        model.removeAllElements();
        items.forEach(model::addElement);
        final String preference = preferences.get(preferenceKey, null);
        if (null != preference) {
            setSelectedItem(preferenceConverter.fromPreference(preference));
        }
        addItemListener(this.preferenceListener);
    }

    @Override
    public DefaultComboBoxModel<E> getModel() {
        return (DefaultComboBoxModel<E>) super.getModel();
    }

}
