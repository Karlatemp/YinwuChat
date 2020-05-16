package org.lintx.plugins.yinwuchat.bungee.json;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import org.lintx.plugins.yinwuchat.bungee.config.Config;

public class OutputCoolQ {
    @SerializedName("action")
    private String action = "send_group_msg";
    @SerializedName("params")
    private Params params;

    public OutputCoolQ(String message) {
        this.params = new Params(message);
    }

    public String getJSON() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static class Params {
        @SerializedName("group_id")
        private int group_id = Config.getInstance().coolQConfig.coolQGroup;
        @SerializedName("message")
        private String message = "";
        @SerializedName("auto_escape")
        private boolean auto_escape = true;

        Params(String message) {
            this.message = message;
        }
    }
}
