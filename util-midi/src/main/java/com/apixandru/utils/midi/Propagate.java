package com.apixandru.utils.midi;

import java.util.concurrent.Callable;

/**
 * @author Alexandru-Constantin Bledea
 * @since February 07, 2016
 */
class Propagate {

    private Propagate() {
    }

    static <E> E withException(final Callable<E> callable) {
        try {
            return callable.call();
        } catch (final Exception x) {
            return mockRuntime(x);
        }
    }

    private static <T> T mockRuntime(final Exception e) {
        return Propagate.mock(e);
    }

    @SuppressWarnings("unchecked")
    private static <E extends Exception, T> T mock(final Exception t) throws E {
        throw (E) t;
    }

}
