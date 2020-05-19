package org.lintx.plugins.yinwuchat.bukkit;

import org.bukkit.entity.Player;
import org.lintx.plugins.yinwuchat.Util.ReflectionUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Function;

import static org.lintx.plugins.yinwuchat.Util.ReflectionUtil.bindTo;

public class NMSUtils {
    public static final Class<?> CraftPlayer = ReflectionUtil.getOBCClass("entity.CraftPlayer");
    public static final Method CraftPlayer$getHandle0 = ReflectionUtil.getMethod(CraftPlayer, "getHandle");
    public static final Class<?> EntityPlayer = ReflectionUtil.getNMSClass("EntityPlayer");
    public static final Class<?> CombatTracker = ReflectionUtil.getNMSClass("CombatTracker");
    public static final Method EntityPlayer$getCombatTracker = ReflectionUtil.getMethod(EntityPlayer, "getCombatTracker");
    public static final Method CombatTracker$getDeathMessage = ReflectionUtil.getMethod(CombatTracker, "getDeathMessage");
    public static final Class<?> IChatBaseComponent = ReflectionUtil.getNMSClass("IChatBaseComponent");
    public static final Method IChatBaseComponent$getString;
    public static final Class<?> IChatBaseComponent$ChatSerializer = ReflectionUtil.getNMSClass("IChatBaseComponent$ChatSerializer");
    public static final Method IChatBaseComponent$ChatSerializer$toJson = ReflectionUtil.findMethod(
            IChatBaseComponent$ChatSerializer,
            Modifier.STATIC, 0, String.class, IChatBaseComponent);

    static {
        Method a;
        try {
            a = ReflectionUtil.getMethod(IChatBaseComponent, "getString");
        } catch (Throwable ignored) {
            a = ReflectionUtil.getMethod(IChatBaseComponent, "toPlainText");
        }
        IChatBaseComponent$getString = a;
    }

    public static final Function<Player, Object> CraftPlayer$getHandle = bindTo(Object.class, CraftPlayer$getHandle0);
    public static final Function<Object, Object> EntityPlayer$getCombatTracker$getDeathMessage = bindTo(
            Object.class, EntityPlayer$getCombatTracker
    ).andThen(bindTo(Object.class, CombatTracker$getDeathMessage));
    public static final Function<Object, String> IChatBaseComponent$toPlainString = bindTo(
            String.class, IChatBaseComponent$getString
    );
    public static final Function<Object, String> IChatBaseComponent$toJson = bindTo(
            String.class, IChatBaseComponent$ChatSerializer$toJson
    );
}
