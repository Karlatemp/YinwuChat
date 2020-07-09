package org.lintx.plugins.yinwuchat.bukkit;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.google.gson.reflect.TypeToken;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.lintx.plugins.yinwuchat.Const;
import org.lintx.plugins.yinwuchat.Util.GsonUtil;

import java.lang.reflect.Type;
import java.util.List;

public class Listeners implements Listener, PluginMessageListener {
    private static final Type ListStringType = new TypeToken<List<String>>() {
    }.getType();
    private final YinwuChat plugin;
    private static final Config CONFIG = Config.getInstance();

    Listeners(YinwuChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onChat(AsyncPlayerChatEvent event) {
        if (event.isAsynchronous() && CONFIG.eventDelayTime > 0) {
            try {
                Thread.sleep(Config.getInstance().eventDelayTime);
            } catch (InterruptedException ignored) {

            }
        }
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        String chat = event.getMessage();

        MessageManage.getInstance().onPublicMessage(player, chat);

        event.setCancelled(true);
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        if (!Const.PLUGIN_CHANNEL.equals(channel)) {
            return;
        }
        ByteArrayDataInput input = ByteStreams.newDataInput(bytes);
        String subchannel = input.readUTF();
        if (Const.PLUGIN_SUB_CHANNEL_AT.equals(subchannel)) {
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS, 1.0f, 1.0f);
        } else if (Const.PLUGIN_SUB_CHANNEL_PLAYER_LIST.equals(subchannel)) {
            try {
                plugin.bungeePlayerList = GsonUtil.GSON.fromJson(input.readUTF(), ListStringType);
            } catch (Exception ignored) {

            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void on(PlayerDeathEvent event) {
        if (!CONFIG.broadcastDeath) return;
        final Player entity = event.getEntity();
        final Object handle = NMSUtils.CraftPlayer$getHandle.apply(entity);
        final Object realDeathMessage =
                NMSUtils.EntityPlayer$getCombatTracker$getDeathMessage.apply(handle);
        final String realDeathMessage$toString = NMSUtils.IChatBaseComponent$toPlainString.apply(realDeathMessage);
        if (realDeathMessage$toString.equals(event.getDeathMessage())) {
            final String deathJson = NMSUtils.IChatBaseComponent$toJson.apply(realDeathMessage);
            MessageManage.getInstance().onPlayerDeath(entity, deathJson);
        } else {
            if (event.getDeathMessage() == null) return;
            MessageManage.getInstance().onPlayerDeath(entity, ComponentSerializer.toString(TextComponent.fromLegacyText(
                    event.getDeathMessage()
            )));
        }
    }
}
