package net.minecraft.block;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class SkullWallPlayerBlock extends WallSkullBlock
{
    protected SkullWallPlayerBlock(AbstractBlock.Properties properties)
    {
        super(SkullBlock.Types.PLAYER, properties);
    }

    /**
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic
     */
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack)
    {
        Blocks.PLAYER_HEAD.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder)
    {
        return Blocks.PLAYER_HEAD.getDrops(state, builder);
    }
}
