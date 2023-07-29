package tondoa.regions.persistent_data;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

public class ClientState extends PersistentState {

    public PlayerState playerState = new PlayerState();

    public static ClientState createFromNbt(NbtCompound tag) {
        ClientState clientState = new ClientState();

        NbtCompound regionsNbt = tag.getCompound("regions");
        PlayerState playerState = new PlayerState();


        regionsNbt.getKeys().forEach(region -> {
            NbtCompound tRegionCompound = regionsNbt.getCompound(region);
            double x = tRegionCompound.getDouble("x");
            double y = tRegionCompound.getDouble("y");
            double z = tRegionCompound.getDouble("z");
            String name = tRegionCompound.getString("name");
            String biomeNamespace = tRegionCompound.getString("biomeNamespace");
            String biomePath = tRegionCompound.getString("biomePath");
            playerState.regions.put(region, new TRegion(x,y,z,new Identifier(biomeNamespace, biomePath), name));

        });
        clientState.playerState = playerState;

        return clientState;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtCompound regionsCompound = new NbtCompound();

            playerState.regions.forEach((region, tRegion) -> {
                NbtCompound tRegionCompound = new NbtCompound();
                tRegionCompound.putDouble("x", tRegion.x);
                tRegionCompound.putDouble("y", tRegion.y);
                tRegionCompound.putDouble("z", tRegion.z);
                tRegionCompound.putString("name", tRegion.name);
                tRegionCompound.putString("biomeNamespace", tRegion.biomeNamespace);
                tRegionCompound.putString("biomePath", tRegion.biomePath);
                regionsCompound.put(region, tRegionCompound);
            });
        nbt.put("regions", regionsCompound);
        return nbt;
    }


    public static ClientState getClientState() {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;

        PersistentStateManager persistentStateManager = player.getWorld().getServer()
                .getWorld(World.OVERWORLD).getPersistentStateManager();

        return persistentStateManager.getOrCreate(
                ClientState::createFromNbt,
                ClientState::new,
                player.getUuidAsString());
    }
}
