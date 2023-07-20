package tondoa.regions.persistent_data;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import tondoa.regions.RegionMod;

public class ModPersistence {

    public static void registerPersistentStates() {

        ServerPlayConnectionEvents.JOIN.register(((handler, sender, server) -> {
            PlayerState playerState = ServerState.getPlayerState(handler.player);

            PacketByteBuf data = PacketByteBufs.create();
            playerState.regions.forEach((region, tRegion) -> {
                data.writeDouble(tRegion.x);
                data.writeDouble(tRegion.y);
                data.writeDouble(tRegion.z);
                data.writeString(tRegion.name);
                data.writeString(tRegion.biomeNamespace);
                data.writeString(tRegion.biomePath);
            });
            ServerPlayNetworking.send(handler.player, new Identifier(RegionMod.MOD_ID, "regions"), data);
        }));
        RegionMod.LOGGER.info("Registering persistent states for " + RegionMod.MOD_ID);
    }

}
