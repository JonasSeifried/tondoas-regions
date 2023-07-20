package tondoa.regions.persistent_data;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.Vec3d;
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
                NbtCompound coordsCompound = regionCompound.getCompound(region);
                double x = coordsCompound.getDouble("x");
                double y = coordsCompound.getDouble("y");
                double z = coordsCompound.getDouble("z");
                playerState.regions.put(region, new Vec3d(x,y,z));

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
            playerData.regions.forEach((region, coords) -> {
                NbtCompound coordsNbt = new NbtCompound();
                coordsNbt.putDouble("x", coords.x);
                coordsNbt.putDouble("y", coords.y);
                coordsNbt.putDouble("z", coords.z);
                regionsTag.put(region, coordsNbt);
            });
            playerDataAsNbt.put("regions", regionsTag);
            playersNbt.put(UUID.toString(), playerDataAsNbt);
        });
        nbt.put("players", playersNbt);
        return nbt;
    }


    public static ServerState getServerState(MinecraftServer server) {
        PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();

        return persistentStateManager.getOrCreate(
                ServerState::createFromNbt,
                ServerState::new,
                RegionMod.MOD_ID);
    }

    public static PlayerState getPlayerState(LivingEntity player) {
        ServerState serverState = getServerState(Objects.requireNonNull(player.getWorld().getServer()));

        return serverState.players.computeIfAbsent(player.getUuid(), uuid -> new PlayerState());
    }
}
