package dev.excellent.client.module.impl.misc;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import dev.excellent.api.event.impl.player.UpdateEvent;
import dev.excellent.api.event.impl.server.PacketEvent;
import dev.excellent.api.interfaces.event.Listener;
import dev.excellent.client.module.api.Category;
import dev.excellent.client.module.api.Module;
import dev.excellent.client.module.api.ModuleInfo;
import dev.excellent.impl.util.time.TimerUtil;
import dev.excellent.impl.value.impl.BooleanValue;
import dev.excellent.impl.value.impl.MultiBooleanValue;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.play.NetworkPlayerInfo;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SChatPacket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@ModuleInfo(name = "Auto Duel", description = "Автоматически отправляет дуэль.", category = Category.MISC)
public class AutoDuel extends Module {

    private static final Pattern pattern = Pattern.compile("^\\w{3,16}$");

    private final MultiBooleanValue mode = new MultiBooleanValue("Киты", this) {{
        add(
                new BooleanValue("Шары", false),
                new BooleanValue("Щит", false),
                new BooleanValue("Шипы 3", false),
                new BooleanValue("Незеритка", false),
                new BooleanValue("Читерский рай", true),
                new BooleanValue("Лук", false),
                new BooleanValue("Классик", false),
                new BooleanValue("Тотемы", false),
                new BooleanValue("Нодебафф", false)
        );
    }};

    private final List<String> sent = Lists.newArrayList();
    private int duelAttempts = 0; // Counter for duel attempts

    private final TimerUtil timerUtil = TimerUtil.create();
    private final TimerUtil timerUtil2 = TimerUtil.create();
    private final TimerUtil timerUtilChoice = TimerUtil.create();
    private final TimerUtil timerUtilTo = TimerUtil.create();

    @Override
    public void onEnable() {
        duelAttempts = 0; // Reset attempts when module is enabled
        sent.clear(); // Clear sent players list when enabled
    }

    private final Listener<UpdateEvent> onUpdate = event -> {
        final List<String> players = getOnlinePlayers();

        if (timerUtil2.hasReached(800L * players.size())) {
            sent.clear();
            duelAttempts = 0; // Reset attempts
            timerUtil2.reset();
        }

        for (final String player : players) {
            if (!sent.contains(player) && !player.equals(mc.session.getProfile().getName())) {
                if (timerUtil.hasReached(500) && duelAttempts < 25) { // Change limit to 25 attempts
                    mc.player.sendChatMessage("/duel " + player);
                    sent.add(player);
                    duelAttempts++; // Increment the counter
                    timerUtil.reset();
                }
            }
        }

        // Если ни один набор не выбран, включаем "Классик"
        if (mode.getValues().stream().noneMatch(BooleanValue::getValue)) {
            mode.getValues().stream()
                    .filter(value -> value.getName().equals("Классик"))
                    .findFirst()
                    .ifPresent(value -> value.setValue(true));
        }

        try {
            if (mc.player.openContainer instanceof ChestContainer chest) {
                final Screen screen = mc.currentScreen;
                if (screen != null && screen.getTitle().getString().contains("Выбор набора (1/1)")) {
                    final List<Integer> slotsID = new ArrayList<>();
                    int index = 0;

                    for (BooleanValue value : mode.getValues()) {
                        if (!value.getValue()) {
                            index++;
                            continue;
                        }
                        slotsID.add(index);
                        index++;
                    }

                    if (slotsID.isEmpty()) {
                        throw new IllegalStateException("Не удалось выбрать ни один из доступных наборов.");
                    }

                    Collections.shuffle(slotsID);
                    final int slotID = slotsID.get(0);

                    if (timerUtilChoice.hasReached(150)) {
                        mc.playerController.windowClick(chest.windowId, slotID, 2, ClickType.CLONE, mc.player);
                        timerUtilChoice.reset();
                    }
                } else if (screen != null && screen.getTitle().getString().contains("Настройка поединка")) {
                    if (timerUtilTo.hasReached(150)) {
                        mc.playerController.windowClick(chest.windowId, 0, 2, ClickType.CLONE, mc.player);
                        timerUtilTo.reset();
                    }
                }
            }
        } catch (Exception e) {
            // Отключение автодуэли в случае ошибки
            toggle();
            e.printStackTrace();  // Для отладки, можно убрать на релизе
        }

        // Disable module after 25 attempts
        if (duelAttempts >= 25) {
            toggle();
        }
    };

    private final Listener<PacketEvent> onPacket = event -> {
        if (event.isReceive()) {
            IPacket<?> packet = event.getPacket();

            if (packet instanceof SChatPacket chat) {
                final String text = chat.getChatComponent().getString().toLowerCase();
                if ((text.contains("начало") && text.contains("через") && text.contains("секунд!")) ||
                        (text.equals("дуэли » во время поединка запрещено использовать команды"))) {
                    toggle();
                }
            }
        }
    };

    private List<String> getOnlinePlayers() {
        return mc.player.connection.getPlayerInfoMap().stream()
                .map(NetworkPlayerInfo::getGameProfile)
                .map(GameProfile::getName)
                .filter(profileName -> pattern.matcher(profileName).matches())
                .collect(Collectors.toList());
    }
}
