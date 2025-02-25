package net.minecraft.data;

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

import java.nio.file.Path;

public class FluidTagsProvider extends TagsProvider<Fluid>
{
    public FluidTagsProvider(DataGenerator generatorIn)
    {
        super(generatorIn, Registry.FLUID);
    }

    protected void registerTags()
    {
        this.getOrCreateBuilder(FluidTags.WATER).add(Fluids.WATER, Fluids.FLOWING_WATER);
        this.getOrCreateBuilder(FluidTags.LAVA).add(Fluids.LAVA, Fluids.FLOWING_LAVA);
    }

    /**
     * Resolves a Path for the location to save the given tag.
     */
    protected Path makePath(ResourceLocation id)
    {
        return this.generator.getOutputFolder().resolve("data/" + id.getNamespace() + "/tags/fluids/" + id.getPath() + ".json");
    }

    /**
     * Gets a name for this provider, to use in logging.
     */
    public String getName()
    {
        return "Fluid Tags";
    }
}
