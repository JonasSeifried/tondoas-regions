package tondoa.regions.persistent_data;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import tondoa.regions.RegionMod;

public class ModPersistence {

    public static void registerPersistentStates() {

        ClientPlayConnectionEvents.JOIN.register(((handler, sender, client) -> {
            PlayerState playerState = ClientState.getClientState().playerState;

            PacketByteBuf data = PacketByteBufs.create();
            playerState.regions.forEach((region, tRegion) -> {
                data.writeDouble(tRegion.x);
                data.writeDouble(tRegion.y);
                data.writeDouble(tRegion.z);
                data.writeString(tRegion.name);
                data.writeString(tRegion.biomeNamespace);
                data.writeString(tRegion.biomePath);
            });
            ClientPlayNetworking.send(new Identifier(RegionMod.MOD_ID, "regions"), data);
        }));
        RegionMod.LOGGER.info("Registering persistent states for " + RegionMod.MOD_ID);
    }

}
