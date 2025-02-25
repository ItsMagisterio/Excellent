package net.minecraft.util;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import javax.annotation.Nullable;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;

public class ObjectIntIdentityMap<T> implements IObjectIntIterable<T>
{
    private int nextId;
    private final IdentityHashMap<T, Integer> identityMap;
    private final List<T> objectList;

    public ObjectIntIdentityMap()
    {
        this(512);
    }

    public ObjectIntIdentityMap(int expectedSize)
    {
        this.objectList = Lists.newArrayListWithExpectedSize(expectedSize);
        this.identityMap = new IdentityHashMap<>(expectedSize);
    }

    public void put(T key, int value)
    {
        this.identityMap.put(key, value);

        while (this.objectList.size() <= value)
        {
            this.objectList.add((T)null);
        }

        this.objectList.set(value, key);

        if (this.nextId <= value)
        {
            this.nextId = value + 1;
        }
    }

    public void add(T key)
    {
        this.put(key, this.nextId);
    }

    /**
     * Gets the integer ID we use to identify the given object.
     */
    public int getId(T value)
    {
        Integer integer = this.identityMap.get(value);
        return integer == null ? -1 : integer;
    }

    @Nullable
    public final T getByValue(int value)
    {
        return (T)(value >= 0 && value < this.objectList.size() ? this.objectList.get(value) : null);
    }

    public Iterator<T> iterator()
    {
        return Iterators.filter(this.objectList.iterator(), Predicates.notNull());
    }

    public int size()
    {
        return this.identityMap.size();
    }
}
