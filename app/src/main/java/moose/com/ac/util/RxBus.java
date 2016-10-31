package moose.com.ac.util;

import com.hwangjr.rxbus.Bus;

public static final class RxBus {
    private static Bus sBus;

    public static synchronized Bus get() {
        if (sBus == null) {
            sBus = new Bus();
        }
        return sBus;
    }
}