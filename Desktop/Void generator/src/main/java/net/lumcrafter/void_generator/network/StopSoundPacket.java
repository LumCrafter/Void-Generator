package net.lumcrafter.void_generator.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class StopSoundPacket {
    private final ResourceLocation soundId;

    public StopSoundPacket(ResourceLocation soundId) {
        this.soundId = soundId;
    }

    public StopSoundPacket(FriendlyByteBuf buf) {
        this.soundId = buf.readResourceLocation();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeResourceLocation(soundId);
    }

    public void handle(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            stopSoundClient(soundId);
        });
        context.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT)
    private static void stopSoundClient(ResourceLocation soundId) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) return;
        mc.getSoundManager().stop(soundId, null); // stoppe alle Sounds mit dieser ID
    }
}
