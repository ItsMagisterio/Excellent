package net.minecraft.advancements.criterion;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.JSONUtils;

import javax.annotation.Nullable;

public class DamagePredicate {
    public static final DamagePredicate ANY = DamagePredicate.Builder.create().build();
    private final MinMaxBounds.FloatBound dealt;
    private final MinMaxBounds.FloatBound taken;
    private final EntityPredicate sourceEntity;
    private final Boolean blocked;
    private final DamageSourcePredicate type;

    public DamagePredicate() {
        this.dealt = MinMaxBounds.FloatBound.UNBOUNDED;
        this.taken = MinMaxBounds.FloatBound.UNBOUNDED;
        this.sourceEntity = EntityPredicate.ANY;
        this.blocked = null;
        this.type = DamageSourcePredicate.ANY;
    }

    public DamagePredicate(MinMaxBounds.FloatBound dealt, MinMaxBounds.FloatBound taken, EntityPredicate sourceEntity, @Nullable Boolean blocked, DamageSourcePredicate type) {
        this.dealt = dealt;
        this.taken = taken;
        this.sourceEntity = sourceEntity;
        this.blocked = blocked;
        this.type = type;
    }

    public boolean test(ServerPlayerEntity player, DamageSource source, float dealt, float taken, boolean blocked) {
        if (this == ANY) {
            return true;
        } else if (!this.dealt.test(dealt)) {
            return false;
        } else if (!this.taken.test(taken)) {
            return false;
        } else if (!this.sourceEntity.test(player, source.getTrueSource())) {
            return false;
        } else if (this.blocked != null && this.blocked != blocked) {
            return false;
        } else {
            return this.type.test(player, source);
        }
    }

    public static DamagePredicate deserialize(@Nullable JsonElement element) {
        if (element != null && !element.isJsonNull()) {
            JsonObject jsonobject = JSONUtils.getJsonObject(element, "damage");
            MinMaxBounds.FloatBound minmaxbounds$floatbound = MinMaxBounds.FloatBound.fromJson(jsonobject.get("dealt"));
            MinMaxBounds.FloatBound minmaxbounds$floatbound1 = MinMaxBounds.FloatBound.fromJson(jsonobject.get("taken"));
            Boolean obool = jsonobject.has("blocked") ? JSONUtils.getBoolean(jsonobject, "blocked") : null;
            EntityPredicate entitypredicate = EntityPredicate.deserialize(jsonobject.get("source_entity"));
            DamageSourcePredicate damagesourcepredicate = DamageSourcePredicate.deserialize(jsonobject.get("type"));
            return new DamagePredicate(minmaxbounds$floatbound, minmaxbounds$floatbound1, entitypredicate, obool, damagesourcepredicate);
        } else {
            return ANY;
        }
    }

    public JsonElement serialize() {
        if (this == ANY) {
            return JsonNull.INSTANCE;
        } else {
            JsonObject jsonobject = new JsonObject();
            jsonobject.add("dealt", this.dealt.serialize());
            jsonobject.add("taken", this.taken.serialize());
            jsonobject.add("source_entity", this.sourceEntity.serialize());
            jsonobject.add("type", this.type.serialize());

            if (this.blocked != null) {
                jsonobject.addProperty("blocked", this.blocked);
            }

            return jsonobject;
        }
    }

    public static class Builder {
        private MinMaxBounds.FloatBound dealt = MinMaxBounds.FloatBound.UNBOUNDED;
        private MinMaxBounds.FloatBound taken = MinMaxBounds.FloatBound.UNBOUNDED;
        private EntityPredicate sourceEntity = EntityPredicate.ANY;
        private Boolean blocked;
        private DamageSourcePredicate type = DamageSourcePredicate.ANY;

        public static DamagePredicate.Builder create() {
            return new DamagePredicate.Builder();
        }

        public DamagePredicate.Builder blocked(Boolean blocked) {
            this.blocked = blocked;
            return this;
        }

        public DamagePredicate.Builder type(DamageSourcePredicate.Builder damageType) {
            this.type = damageType.build();
            return this;
        }

        public DamagePredicate build() {
            return new DamagePredicate(this.dealt, this.taken, this.sourceEntity, this.blocked, this.type);
        }
    }
}
