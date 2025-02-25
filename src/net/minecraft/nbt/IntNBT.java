package net.minecraft.nbt;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IntNBT extends NumberNBT
{
    public static final INBTType<IntNBT> TYPE = new INBTType<IntNBT>()
    {
        public IntNBT readNBT(DataInput input, int depth, NBTSizeTracker accounter) throws IOException
        {
            accounter.read(96L);
            return IntNBT.valueOf(input.readInt());
        }
        public String getName()
        {
            return "INT";
        }
        public String getTagName()
        {
            return "TAG_Int";
        }
        public boolean isPrimitive()
        {
            return true;
        }
    };
    private final int data;

    private IntNBT(int data)
    {
        this.data = data;
    }

    public static IntNBT valueOf(int dataIn)
    {
        return dataIn >= -128 && dataIn <= 1024 ? IntNBT.Cache.CACHE[dataIn + 128] : new IntNBT(dataIn);
    }

    /**
     * Write the actual data contents of the tag, implemented in NBT extension classes
     */
    public void write(DataOutput output) throws IOException
    {
        output.writeInt(this.data);
    }

    /**
     * Gets the type byte for the tag.
     */
    public byte getId()
    {
        return 3;
    }

    public INBTType<IntNBT> getType()
    {
        return TYPE;
    }

    public String toString()
    {
        return String.valueOf(this.data);
    }

    /**
     * Creates a clone of the tag.
     */
    public IntNBT copy()
    {
        return this;
    }

    public boolean equals(Object p_equals_1_)
    {
        if (this == p_equals_1_)
        {
            return true;
        }
        else
        {
            return p_equals_1_ instanceof IntNBT && this.data == ((IntNBT)p_equals_1_).data;
        }
    }

    public int hashCode()
    {
        return this.data;
    }

    public ITextComponent toFormattedComponent(String indentation, int indentDepth)
    {
        return (new StringTextComponent(String.valueOf(this.data))).mergeStyle(SYNTAX_HIGHLIGHTING_NUMBER);
    }

    public long getLong()
    {
        return (long)this.data;
    }

    public int getInt()
    {
        return this.data;
    }

    public short getShort()
    {
        return (short)(this.data & 65535);
    }

    public byte getByte()
    {
        return (byte)(this.data & 255);
    }

    public double getDouble()
    {
        return (double)this.data;
    }

    public float getFloat()
    {
        return (float)this.data;
    }

    public Number getAsNumber()
    {
        return this.data;
    }

    static class Cache
    {
        static final IntNBT[] CACHE = new IntNBT[1153];

        static
        {
            for (int i = 0; i < CACHE.length; ++i)
            {
                CACHE[i] = new IntNBT(-128 + i);
            }
        }
    }
}
