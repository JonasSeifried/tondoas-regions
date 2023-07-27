package tondoa.regions.persistent_data;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;
import tondoa.regions.RegionMod;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class ServerState extends PersistentState {

    public HashMap<UUID, PlayerState> players = new HashMap<>();

    public static ServerState createFromNbt(NbtCompound tag) {
        ServerState serverState = new ServerState();

        NbtCompound playersTag = tag.getCompound("players");
        playersTag.getKeys().forEach(key -> {
            PlayerState playerState = new PlayerState();

            NbtCompound regionCompound = playersTag.getCompound(key).getCompound("regions");

            regionCompound.getKeys().forEach(region -> {
                NbtCompound tRegionCompound = regionCompound.getCompound(region);
                double x = tRegionCompound.getDouble("x");
                double y = tRegionCompound.getDouble("y");
                double z = tRegionCompound.getDouble("z");
                String name = tRegionCompound.getString("name");
                String biomeNamespace = tRegionCompound.getString("biomeNamespace");
                String biomePath = tRegionCompound.getString("biomePath");
                playerState.regions.put(region, new TRegion(x,y,z,new Identifier(biomeNamespace, biomePath), name));

                });
        UUID uuid =  UUID.fromString(key);
        serverState.players.put(uuid, playerState);
        });

        return serverState;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtCompound playersNbt = new NbtCompound();

        players.forEach((UUID, playerData) -> {
            NbtCompound playerDataAsNbt = new NbtCompound();
            NbtCompound regionsTag = new NbtCompound();
            playerData.regions.forEach((region, tRegion) -> {
                NbtCompound tRegionCompound = new NbtCompound();
                tRegionCompound.putDouble("x", tRegion.x);
                tRegionCompound.putDouble("y", tRegion.y);
                tRegionCompound.putDouble("z", tRegion.z);
                tRegionCompound.putString("name", tRegion.name);
                tRegionCompound.putString("biomeNamespace", tRegion.biomeNamespace);
                tRegionCompound.putString("biomePath", tRegion.biomePath);
                regionsTag.put(region, tRegionCompound);
            });
            playerDataAsNbt.put("regions", regionsTag);
            playersNbt.put(UUID.toString(), playerDataAsNbt);
        });
        nbt.put("players", playersNbt);
        return nbt;
    }


    public static ServerState getServerState(MinecraftServer server) {
        PersistentStateManager persistentStateManager = Objects.requireNonNull(server.getWorld(World.OVERWORLD)).getPersistentStateManager();

        return persistentStateManager.getOrCreate(
                ServerState::createFromNbt,
                ServerState::new,
                RegionMod.MOD_ID);
    }

    public static PlayerState getPlayerState(LivingEntity player) {
        ServerState serverState = getServerState(Objects.requireNonNull(player.getWorld().getServer()));

        return serverState.players.computeIfAbsent(player.getUuid(), uuid -> new PlayerState());
    }

    public static void markDirty(MinecraftServer server) {
        getServerState(server).markDirty();
    }
}
