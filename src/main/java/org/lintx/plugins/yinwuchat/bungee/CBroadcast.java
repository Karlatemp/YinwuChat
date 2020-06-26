package org.lintx.plugins.yinwuchat.bungee;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class CBroadcast extends Command {
    public CBroadcast() {
        super("yb", "yinwuchat.broadcast");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(new ComponentBuilder("/yb [message]").color(ChatColor.RED).create());
        }
        String line = String.join(" ", args);
        final BaseComponent[] components = new ComponentBuilder("[YinwuChat]").color(ChatColor.BOLD)
                .append(sender.getName()).color(ChatColor.DARK_AQUA)
                .append(" >> ").reset()
                .appendLegacy(line)
                .create();
        final ProxyServer server = ProxyServer.getInstance();
        server.getConsole().sendMessage(components);
        server.getPlayers().forEach(proxiedPlayer -> proxiedPlayer.sendMessage(components));
        MessageManage.getInstance().broadcast(null, new TextComponent(components), false, p -> false);
    }
}
