package org.lintx.plugins.yinwuchat.Util;

import net.md_5.bungee.api.chat.HoverEvent;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class BCCUtils {
    private static HLS hls;

    private interface HLS {
        void setLegacy(HoverEvent event, boolean legacy);
    }

    public static void set(HoverEvent event, boolean legacy) {
        hls.setLegacy(event, legacy);
    }

    static {
        try {

            final MethodHandles.Lookup caller = MethodHandles.lookup();
            final MethodHandle handle = caller.unreflect(HoverEvent.class.getMethod("setLegacy", boolean.class));
            hls = (HLS) LambdaMetafactory.metafactory(caller,
                    "setLegacy",
                    MethodType.methodType(HLS.class),
                    MethodType.methodType(void.class, HoverEvent.class, boolean.class),
                    handle,
                    MethodType.methodType(void.class, HoverEvent.class, boolean.class)
            ).getTarget().invoke();
        } catch (Throwable ignore) {
        }
        if (hls == null) hls = (v, b) -> {
        };
    }

    public static void setLegacy(HoverEvent event) {
        set(event, true);
    }
}
