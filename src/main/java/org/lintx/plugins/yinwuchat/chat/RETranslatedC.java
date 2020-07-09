package org.lintx.plugins.yinwuchat.chat;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class RETranslatedC {
    public static final JsonObject trans_zh_CN;
    private static final Pattern format = Pattern.compile("%(?:(\\d+)\\$)?([A-Za-z%]|$)");

    static {
        final InputStream resource = RETranslatedC.class.getResourceAsStream("/translate.zh_cn.1.16.1.json");
        if (resource == null) throw new ExceptionInInitializerError("Translate resource missing.");
        try (InputStreamReader reader = new InputStreamReader(resource, StandardCharsets.UTF_8)) {
            trans_zh_CN = new JsonParser().parse(reader).getAsJsonObject();
        } catch (IOException ioException) {
            throw new ExceptionInInitializerError(ioException);
        }
    }

    private static BaseComponent resolve(TranslatableComponent component) {
        final JsonPrimitive trans0 = trans_zh_CN.getAsJsonPrimitive(component.getTranslate());
        if (trans0 == null) {
            resolveWiths(component);
            return component;
        }
        LinkedList<BaseComponent> tc = new LinkedList<>();
        String trans = trans0.getAsString();

        Matcher matcher = format.matcher(trans);
        final List<BaseComponent> with = component.getWith();
        int position = 0;
        int i = 0;
        while (matcher.find(position)) {
            int pos = matcher.start();
            if (pos != position) {
                tc.add(new TextComponent(trans.substring(position, pos)));
            }
            position = matcher.end();

            String formatCode = matcher.group(2);
            switch (formatCode.charAt(0)) {
                case 's':
                case 'd':
                    String withIndex = matcher.group(1);

                    tc.add(with.get(withIndex != null ? Integer.parseInt(withIndex) - 1 : i++));

                    break;
                case '%':
                    tc.add(new TextComponent("%"));
                    break;
            }
        }
        if (trans.length() != position) {
            tc.add(new TextComponent(trans.substring(position)));
        }
        {
            final List<BaseComponent> extra = component.getExtra();
            if (extra != null)
                tc.addAll(extra);
        }
        {
            TextComponent tcc = new TextComponent();
            tcc.setExtra(tc);
            return tcc;
        }
    }

    private static void resolveWiths(TranslatableComponent component) {
        final List<BaseComponent> with = component.getWith();
        if (with != null) {
            component.setWith(with.stream().map(RETranslatedC::resolve).collect(Collectors.toCollection(LinkedList::new)));
        }
    }

    public static BaseComponent resolve(BaseComponent component) {
        if (component instanceof TranslatableComponent) {
            component = resolve((TranslatableComponent) component);
        }
        final List<BaseComponent> extra = component.getExtra();
        if (extra != null) {
            component.setExtra(extra.stream().map(RETranslatedC::resolve).collect(Collectors.toCollection(LinkedList::new)));
        }
        return component;
    }

    public static void resolve(BaseComponent[] components) {
        int end = components.length;
        for (int i = 0; i < end; i++) {
            components[i] = resolve(components[i]);
        }
    }
}
