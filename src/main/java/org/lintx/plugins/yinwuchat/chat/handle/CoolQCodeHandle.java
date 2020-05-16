package org.lintx.plugins.yinwuchat.chat.handle;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.lintx.plugins.yinwuchat.Util.MessageUtil;
import org.lintx.plugins.yinwuchat.bungee.config.Config;
import org.lintx.plugins.yinwuchat.chat.struct.Chat;
import org.lintx.plugins.yinwuchat.chat.struct.ChatSource;

public class CoolQCodeHandle extends ChatHandle {
    @Override
    public void handle(Chat chat) {
        if (chat.source != ChatSource.QQ) return;
        Config config = Config.getInstance();
        handle(chat, "\\[CQ:(.*?),(.*?)]", (matcher) -> {
            String func = matcher.group(1);
            String ext = matcher.group(2);

            TextComponent component = new TextComponent();
            if (func.equalsIgnoreCase("image")) {
                component.setText(MessageUtil.replace(config.coolQConfig.qqImageText));
            } else if (func.equalsIgnoreCase("record")) {
                component.setText(MessageUtil.replace(config.coolQConfig.qqRecordText));
            } else if (func.equalsIgnoreCase("at")) {
                component.setText(MessageUtil.replace(config.coolQConfig.qqAtText.replaceAll("\\{qq}", ext.replaceAll("qq=", ""))));
            } else if (func.equalsIgnoreCase("rich")) {
                component.setText(MessageUtil.replace("&7[富文本消息]&r"));
                chat.setHover(component, "有群友发送了一个我们难以解析的富文本消息，他的消息详细内容如下:\n"+ext);
            } else if (func.equalsIgnoreCase("music")) {
                String type = "";
                String id = "";
                String url = "";
                String title = "无音乐标题";
                String content = "无音乐介绍";
                String[] a = ext.split(",");
                for (String kv : a) {
                    String[] b = kv.split("=", 2);
                    if (b.length == 2 && b[0].equalsIgnoreCase("type")) {
                        type = b[1];
                        continue;
                    } else if (b.length == 2 && b[0].equalsIgnoreCase("id")) {
                        id = b[1];
                        break;
                    } else if (b.length == 2 && b[0].equalsIgnoreCase("url")) {
                        url = b[1];
                        continue;
                    } else if (b.length == 2 && b[0].equalsIgnoreCase("title")) {
                        title = b[1];
                        continue;
                    } else if (b.length == 2 && b[0].equalsIgnoreCase("content")) {
                        content = b[1];
                        break;
                    }
                }
                component.setText(MessageUtil.replace("&7[音乐]&r"));
                if (type.equalsIgnoreCase("qq")) {
                    chat.setHover(component, "群友分享了一个QQ音乐卡片\n点击以访问QQ音乐官网播放音乐");
                    chat.setClick(component, "https://i.y.qq.com/v8/playsong.html?songid=" + id);
                } else if (type.equalsIgnoreCase("163")) {
                    chat.setHover(component, "群友分享了一个QQ音乐卡片\n点击以访问网易云音乐官网播放音乐");
                    chat.setClick(component, "https://music.163.com/song?id=" + id);
                } else if (type.equalsIgnoreCase("custom")) {
                    chat.setHover(component, "群友分享了一首音乐\n音乐名:" + title + "\n介绍:" + content + "\n点击以访问音乐播放页面");
                    chat.setClick(component, url);
                }
            } else if (func.equalsIgnoreCase("sign")) {
                String location = "无位置信息";
                String title = "";
                String[] a = ext.split(",");
                for (String kv : a) {
                    String[] b = kv.split("=", 2);
                    if (b.length == 2 && b[0].equalsIgnoreCase("location")) {
                        location = b[1];
                        continue;
                    } else if (b.length == 2 && b[0].equalsIgnoreCase("title")) {
                        title = b[1];
                        break;
                    }
                }
                component.setText(MessageUtil.replace("&7[签到]&r"));
                chat.setHover(component,"地点:"+location+"\n心情:"+title);
            } else if (func.equalsIgnoreCase("location")) {
                String title = "";
                String content = "";
                String[] a = ext.split(",");
                for (String kv : a) {
                    String[] b = kv.split("=", 2);
                    if (b.length == 2 && b[0].equalsIgnoreCase("title")) {
                        title = b[1];
                        continue;
                    } else if (b.length == 2 && b[0].equalsIgnoreCase("content")) {
                        content = b[1];
                        break;
                    }
                }
                component.setText(MessageUtil.replace("&7[位置分享]&r"));
                chat.setHover(component,"标题:"+title+"\n地址:"+content);
            } else if (func.equalsIgnoreCase("face")) {
                component.setText(MessageUtil.replace("&7[表情]&r"));
            } else if (func.equalsIgnoreCase("emoji")) {
                component.setText(MessageUtil.replace("&7[emoji表情]&r"));
            } else if (func.equalsIgnoreCase("bface")) {
                component.setText(MessageUtil.replace("&7[动画表情]&r"));
            } else if (func.equalsIgnoreCase("share")) {
                String url = "";
                String[] a = ext.split(",");
                for (String kv : a) {
                    String[] b = kv.split("=", 2);
                    if (b.length == 2 && b[0].equalsIgnoreCase("url")) {
                        url = b[1];
                        break;
                    }
                }
                component.setText(MessageUtil.replace(config.tipsConfig.linkText));
                if (!"".equals(url)) {
                    chat.setHover(component, url);
                    chat.setClick(component, url);
                }
            } else {
                component.setText(MessageUtil.replace("&7[其他类型消息]&r"));
                chat.setHover(component,"消息类型:"+func+"\n额外信息:"+ext);
            }
            return component;
        });
    }
}
