package net.minecraft.network.login.server;

import net.minecraft.client.network.login.IClientLoginNetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;

import java.io.IOException;

public class SDisconnectLoginPacket implements IPacket<IClientLoginNetHandler>
{
    private ITextComponent reason;

    public SDisconnectLoginPacket()
    {
    }

    public SDisconnectLoginPacket(ITextComponent p_i46853_1_)
    {
        this.reason = p_i46853_1_;
    }

    /**
     * Reads the raw packet data from the data stream.
     */
    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.reason = ITextComponent.Serializer.getComponentFromJsonLenient(buf.readString(262144));
    }

    /**
     * Writes the raw packet data to the data stream.
     */
    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeTextComponent(this.reason);
    }

    /**
     * Passes this Packet on to the NetHandler for processing.
     */
    public void processPacket(IClientLoginNetHandler handler)
    {
        handler.handleDisconnect(this);
    }

    public ITextComponent getReason()
    {
        return this.reason;
    }
}
