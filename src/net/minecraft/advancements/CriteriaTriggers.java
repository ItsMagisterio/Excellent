package net.minecraft.advancements;

import com.google.common.collect.Maps;
import net.minecraft.advancements.criterion.*;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Map;

public class CriteriaTriggers {
    private static final Map<ResourceLocation, ICriterionTrigger<?>> REGISTRY = Maps.newHashMap();
    public static final ImpossibleTrigger IMPOSSIBLE = register(new ImpossibleTrigger());
    public static final KilledTrigger PLAYER_KILLED_ENTITY = register(new KilledTrigger(new ResourceLocation("player_killed_entity")));
    public static final KilledTrigger ENTITY_KILLED_PLAYER = register(new KilledTrigger(new ResourceLocation("entity_killed_player")));
    public static final EnterBlockTrigger ENTER_BLOCK = register(new EnterBlockTrigger());
    public static final InventoryChangeTrigger INVENTORY_CHANGED = register(new InventoryChangeTrigger());
    public static final RecipeUnlockedTrigger RECIPE_UNLOCKED = register(new RecipeUnlockedTrigger());
    public static final PlayerHurtEntityTrigger PLAYER_HURT_ENTITY = register(new PlayerHurtEntityTrigger());
    public static final EntityHurtPlayerTrigger ENTITY_HURT_PLAYER = register(new EntityHurtPlayerTrigger());
    public static final EnchantedItemTrigger ENCHANTED_ITEM = register(new EnchantedItemTrigger());
    public static final FilledBucketTrigger FILLED_BUCKET = register(new FilledBucketTrigger());
    public static final BrewedPotionTrigger BREWED_POTION = register(new BrewedPotionTrigger());
    public static final ConstructBeaconTrigger CONSTRUCT_BEACON = register(new ConstructBeaconTrigger());
    public static final UsedEnderEyeTrigger USED_ENDER_EYE = register(new UsedEnderEyeTrigger());
    public static final SummonedEntityTrigger SUMMONED_ENTITY = register(new SummonedEntityTrigger());
    public static final BredAnimalsTrigger BRED_ANIMALS = register(new BredAnimalsTrigger());
    public static final PositionTrigger LOCATION = register(new PositionTrigger(new ResourceLocation("location")));
    public static final PositionTrigger SLEPT_IN_BED = register(new PositionTrigger(new ResourceLocation("slept_in_bed")));
    public static final CuredZombieVillagerTrigger CURED_ZOMBIE_VILLAGER = register(new CuredZombieVillagerTrigger());
    public static final VillagerTradeTrigger VILLAGER_TRADE = register(new VillagerTradeTrigger());
    public static final ItemDurabilityTrigger ITEM_DURABILITY_CHANGED = register(new ItemDurabilityTrigger());
    public static final LevitationTrigger LEVITATION = register(new LevitationTrigger());
    public static final ChangeDimensionTrigger CHANGED_DIMENSION = register(new ChangeDimensionTrigger());
    public static final TickTrigger TICK = register(new TickTrigger());
    public static final TameAnimalTrigger TAME_ANIMAL = register(new TameAnimalTrigger());
    public static final PlacedBlockTrigger PLACED_BLOCK = register(new PlacedBlockTrigger());
    public static final ConsumeItemTrigger CONSUME_ITEM = register(new ConsumeItemTrigger());
    public static final EffectsChangedTrigger EFFECTS_CHANGED = register(new EffectsChangedTrigger());
    public static final UsedTotemTrigger USED_TOTEM = register(new UsedTotemTrigger());
    public static final NetherTravelTrigger NETHER_TRAVEL = register(new NetherTravelTrigger());
    public static final FishingRodHookedTrigger FISHING_ROD_HOOKED = register(new FishingRodHookedTrigger());
    public static final ChanneledLightningTrigger CHANNELED_LIGHTNING = register(new ChanneledLightningTrigger());
    public static final ShotCrossbowTrigger SHOT_CROSSBOW = register(new ShotCrossbowTrigger());
    public static final KilledByCrossbowTrigger KILLED_BY_CROSSBOW = register(new KilledByCrossbowTrigger());
    public static final PositionTrigger HERO_OF_THE_VILLAGE = register(new PositionTrigger(new ResourceLocation("hero_of_the_village")));
    public static final PositionTrigger VOLUNTARY_EXILE = register(new PositionTrigger(new ResourceLocation("voluntary_exile")));
    public static final SlideDownBlockTrigger SLIDE_DOWN_BLOCK = register(new SlideDownBlockTrigger());
    public static final BeeNestDestroyedTrigger BEE_NEST_DESTROYED = register(new BeeNestDestroyedTrigger());
    public static final TargetHitTrigger TARGET_HIT = register(new TargetHitTrigger());
    public static final RightClickBlockWithItemTrigger RIGHT_CLICK_BLOCK_WITH_ITEM = register(new RightClickBlockWithItemTrigger());
    public static final PlayerGeneratesContainerLootTrigger PLAYER_GENERATES_CONTAINER_LOOT = register(new PlayerGeneratesContainerLootTrigger());
    public static final ThrownItemPickedUpByEntityTrigger THROWN_ITEM_PICKED_UP_BY_ENTITY = register(new ThrownItemPickedUpByEntityTrigger());
    public static final PlayerEntityInteractionTrigger PLAYER_ENTITY_INTERACTION = register(new PlayerEntityInteractionTrigger());

    private static <T extends ICriterionTrigger<?>> T register(T criterion) {
        if (REGISTRY.containsKey(criterion.getId())) {
            throw new IllegalArgumentException("Duplicate criterion id " + criterion.getId());
        } else {
            REGISTRY.put(criterion.getId(), criterion);
            return criterion;
        }
    }

    @Nullable
    public static <T extends ICriterionInstance> ICriterionTrigger<T> get(ResourceLocation id) {
        return (ICriterionTrigger<T>) REGISTRY.get(id);
    }

    public static Iterable<? extends ICriterionTrigger<?>> getAll() {
        return REGISTRY.values();
    }
}
