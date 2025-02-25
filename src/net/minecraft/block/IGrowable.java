package net.minecraft.block;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public interface IGrowable
{
    /**
     * Whether this IGrowable can grow
     */
    boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient);

    boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state);

    void grow(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state);
}
