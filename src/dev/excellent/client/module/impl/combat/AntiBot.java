package dev.excellent.client.module.impl.combat;

import com.google.common.collect.Lists;
import dev.excellent.api.event.impl.other.WorldChangeEvent;
import dev.excellent.api.event.impl.other.WorldLoadEvent;
import dev.excellent.api.event.impl.player.UpdateEvent;
import dev.excellent.api.interfaces.event.Listener;
import dev.excellent.client.module.api.Category;
import dev.excellent.client.module.api.Module;
import dev.excellent.client.module.api.ModuleInfo;
import dev.excellent.impl.util.pattern.Singleton;
import dev.excellent.impl.value.impl.BooleanValue;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ModuleInfo(name = "Anti Bot", description = "Игнорирует ботов.", category = Category.COMBAT)
public class AntiBot extends Module {
    public static Singleton<AntiBot> singleton = Singleton.create(() -> Module.link(AntiBot.class));
    public static final List<PlayerEntity> bots = new ArrayList<>();

    public final BooleanValue remove = new BooleanValue("Удалять из мира", this, false);

    @Override
    protected void onEnable() {
        super.onEnable();
        clearBots();
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        clearBots();
    }

    private final Listener<UpdateEvent> onUpdate = event -> {
        for (PlayerEntity entity : mc.world.getPlayers()) {
            if (isBot(entity)) {
                synchronized (bots) {
                    if (!bots.contains(entity)) {
                        bots.add(entity);
                    }
                }
            }
        }

        if (remove.getValue()) {
            try {
                mc.world.getPlayers().removeIf(this::isBot);
            } catch (Exception ignored) {
                System.err.println("Ошибка при удалении ботов: " + ignored.getMessage());
            }
        }
    };

    public static boolean contains(LivingEntity entity) {
        if (entity instanceof PlayerEntity) {
            synchronized (bots) {
                return bots.contains(entity);
            }
        }
        return false;
    }

    public static boolean isEmpty() {
        synchronized (bots) {
            return bots.isEmpty();
        }
    }

    private boolean isBot(PlayerEntity entity) {
        UUID entityUUID = entity.getUniqueID();
        return !entityUUID.equals(PlayerEntity.getOfflineUUID(entity.getName().getString()));
    }

    private void clearBots() {
        synchronized (bots) {
            bots.clear();
        }
    }

    private final Listener<WorldChangeEvent> onWorldChange = event -> clearBots();
    private final Listener<WorldLoadEvent> onWorldLoad = event -> clearBots();
}
