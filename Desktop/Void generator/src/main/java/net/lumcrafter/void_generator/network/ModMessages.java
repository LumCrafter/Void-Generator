package net.lumcrafter.void_generator.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;

public class ModMessages {
    private static SimpleChannel INSTANCE;
    private static int packetId = 0;

    private static int id() { return packetId++; }

    public static void register() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation("void_generator", "messages"),
                () -> "1.0",
                s -> true,
                s -> true
        );

        INSTANCE.messageBuilder(StopSoundPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(StopSoundPacket::new)
                .encoder(StopSoundPacket::toBytes)
                .consumerMainThread(StopSoundPacket::handle)
                .add();
    }

    public static <MSG> void sendToClients(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
}
