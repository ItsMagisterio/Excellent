package dev.luvbeeq.discord.rpc;

import dev.excellent.Excellent;
import dev.luvbeeq.discord.rpc.utils.DiscordEventHandlers;
import dev.luvbeeq.discord.rpc.utils.DiscordRPC;
import dev.luvbeeq.discord.rpc.utils.DiscordRichPresence;
import dev.luvbeeq.discord.rpc.utils.RPCButton;
import dev.luvbeeq.discord.webhook.DiscordWebhook;
import i.gishreloaded.protection.annotation.Native;
import lombok.Getter;

import java.awt.*;

@Getter
public class DiscordManager {

    private final long APPLICATION_ID = 1155480953058250804L;
    private final String image = "https://s9.gifyu.com/images/SUK4s.gif";
    private final String telegram = "https://t.me/excellent_client/";
    private final String discord = "https://discord.gg/EahYtazjtd";
    private final DiscordWebhook webhook = new DiscordWebhook("https://discord.com/api/webhooks/1171541610983600148/TYrIcXPUqnF2_3P9ZwkMNY6NqRHIoZwiJRlGla5kV_vXAsFa2wtHhl2lGHwGkidCwjFn");
    private DiscordDaemonThread discordDaemonThread;
    private boolean running;

    @Native
    public void init() {
        initDiscord();
        sendWebhookMessage();
    }

    @Native
    private void initDiscord() {
        DiscordRichPresence.Builder builder = new DiscordRichPresence.Builder();
        DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().build();

        DiscordRPC.INSTANCE.Discord_Initialize(String.valueOf(APPLICATION_ID), handlers, true, "");
        builder.setStartTimestamp(System.currentTimeMillis() / 1000);

        Excellent instance = Excellent.getInst();
        String username = instance.getProfile().getName();
        String uid = String.valueOf(instance.getProfile().getId());

        builder.setDetails("Beta version 2.0")
                .setState("User: " + username + " | Unique: " + uid)
                .setLargeImage(image, "Build: " + Excellent.getInst().getInfo().getBuild())
                .setButtons(
                        RPCButton.create("Telegram", telegram),
                        RPCButton.create("Discord", discord)
                );

        DiscordRPC.INSTANCE.Discord_UpdatePresence(builder.build());

        discordDaemonThread = new DiscordDaemonThread();
        discordDaemonThread.start();
        running = true;
    }

    private void sendWebhookMessage() {
        try {
            DiscordWebhook.EmbedObject embedObject = buildEmbedObject();
            webhook.addEmbed(embedObject);
            webhook.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private DiscordWebhook.EmbedObject buildEmbedObject() {
        Excellent instance = Excellent.getInst();
        DiscordWebhook.EmbedObject embedObject = new DiscordWebhook.EmbedObject()
                .setTitle("Excellent 2.0 (Beta)")
                .setDescription("Client join callback")
                .setUrl(telegram)
                .addField("user", instance.getProfile().getName(), true)
                .addField("uid", String.valueOf(instance.getProfile().getId()), true)
                .addField("expire", instance.getProfile().getExpireDate().toString(), true)
                .addField("role", String.valueOf(instance.getProfile().getRole()), true)
                .setThumbnail(image)
                .setColor(new Color(145, 145, 255));
        return embedObject;
    }

    public void stopRPC() {
        DiscordRPC.INSTANCE.Discord_Shutdown();
        if (discordDaemonThread != null && discordDaemonThread.isAlive()) {
            discordDaemonThread.interrupt();
        }
        running = false;
    }

    private class DiscordDaemonThread extends Thread {
        @Override
        public void run() {
            this.setName("Discord-RPC");
            try {
                while (running) {
                    DiscordRPC.INSTANCE.Discord_RunCallbacks();
                    Thread.sleep(15000);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                stopRPC();
            }
        }
    }
}
