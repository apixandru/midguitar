package com.apixandru.midguitar.swing;

/**
 * @author Alexandru-Constantin Bledea
 * @since February 07, 2016
 */
public interface PreferenceConverter {

    String toPreference(Object element);

    Object fromPreference(String preference);

}
