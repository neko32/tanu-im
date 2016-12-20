package org.tanuneko.im.util;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by neko32 on 2016/09/10.
 */

public class SimpleIDGen {

    private SimpleIDGen() {
        throw new IllegalStateException("No use");
    }

    private static AtomicInteger id = new AtomicInteger();

    public static int generateID() {
        return id.incrementAndGet();
    }

    public static void reset() {
        id.set(0);
    }
}
