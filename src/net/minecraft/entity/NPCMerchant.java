package net.minecraft.entity;

import net.minecraft.entity.merchant.IMerchant;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.MerchantInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MerchantOffer;
import net.minecraft.item.MerchantOffers;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class NPCMerchant implements IMerchant
{
    private final MerchantInventory merchantInventory;
    private final PlayerEntity customer;
    private MerchantOffers offers = new MerchantOffers();
    private int xp;

    public NPCMerchant(PlayerEntity player)
    {
        this.customer = player;
        this.merchantInventory = new MerchantInventory(this);
    }

    @Nullable
    public PlayerEntity getCustomer()
    {
        return this.customer;
    }

    public void setCustomer(@Nullable PlayerEntity player)
    {
    }

    public MerchantOffers getOffers()
    {
        return this.offers;
    }

    public void setClientSideOffers(@Nullable MerchantOffers offers)
    {
        this.offers = offers;
    }

    public void onTrade(MerchantOffer offer)
    {
        offer.increaseUses();
    }

    /**
     * Notifies the merchant of a possible merchantrecipe being fulfilled or not. Usually, this is just a sound byte
     * being played depending if the suggested itemstack is not null.
     */
    public void verifySellingItem(ItemStack stack)
    {
    }

    public World getWorld()
    {
        return this.customer.world;
    }

    public int getXp()
    {
        return this.xp;
    }

    public void setXP(int xpIn)
    {
        this.xp = xpIn;
    }

    public boolean hasXPBar()
    {
        return true;
    }

    public SoundEvent getYesSound()
    {
        return SoundEvents.ENTITY_VILLAGER_YES;
    }
}
