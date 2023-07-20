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
            ServerState serverState = ServerState.getServerState(handler.player.server);
            PlayerState playerState = ServerState.getPlayerState(handler.player);

            PacketByteBuf data = PacketByteBufs.create();
            playerState.regions.forEach((region, coords) -> {
                data.writeDouble(coords.x);
                data.writeDouble(coords.y);
                data.writeDouble(coords.z);
            });
            ServerPlayNetworking.send(handler.player, new Identifier(RegionMod.MOD_ID, "regions"), data);
        }));
        RegionMod.LOGGER.info("Registering persistent states for " + RegionMod.MOD_ID);
    }

}
