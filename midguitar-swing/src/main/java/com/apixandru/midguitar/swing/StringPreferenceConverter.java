package com.apixandru.midguitar.swing;

/**
 * @author Alexandru-Constantin Bledea
 * @since February 07, 2016
 */
final class StringPreferenceConverter implements PreferenceConverter {

    static final PreferenceConverter INSTANCE = new StringPreferenceConverter();

    @Override
    public String toPreference(final Object element) {
        return String.valueOf(element);
    }

    @Override
    public Object fromPreference(final String preference) {
        return preference;
    }

}
