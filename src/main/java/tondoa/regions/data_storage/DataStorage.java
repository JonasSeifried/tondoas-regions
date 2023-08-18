package tondoa.regions.data_storage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.Nullable;
import tondoa.regions.ClientRegionMod;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class DataStorage {

    public static Map<String, TRegion> regions = new TreeMap<>();

    public static Config config = new Config();
    public static TreeSet<Identifier> worldIdentifiers = new TreeSet<>();
    private static File worldFolder;

    public static final String WORLD_FILE_NAME = "worlds.json";
    public static final String REGIONS_FILE_NAME = "regions.json";
    public static final String CONFIG_FILE_NAME = "config.json";

    public static void register() {

        ClientPlayConnectionEvents.JOIN.register(((handler, sender, client) -> load(client)));
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> store());
        ClientRegionMod.LOGGER.info("Registering ClientPlayConnectionEvents for " + ClientRegionMod.MOD_ID);
    }

    public static void load(MinecraftClient client) {
        regions.clear();
        worldIdentifiers.clear();
        config = new Config();
        setWorldFolder(client);
        File regionsFile = new File(worldFolder, REGIONS_FILE_NAME);
        File worldsFile = new File(worldFolder, WORLD_FILE_NAME);
        File configFile = new File(worldFolder.getParent(), CONFIG_FILE_NAME);
        String regionsString;
        String worldsString;
        String configString;
        Gson gson = new Gson();
        try {
            if (regionsFile.createNewFile() && worldsFile.createNewFile()) return;
            regionsString = new String(Files.readAllBytes(Path.of(regionsFile.toURI())));
            worldsString = new String(Files.readAllBytes(Path.of(worldsFile.toURI())));
            configString = new String(Files.readAllBytes(Path.of(configFile.toURI())));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (!regionsString.isEmpty()) {
            TypeToken<Map<String, TRegion>> mapTypeToken = new TypeToken<>() {
            };
            regions = gson.fromJson(regionsString, mapTypeToken.getType());
        }
        if (!worldsString.isEmpty()) {
            TypeToken<TreeSet<Identifier>> treeSetTypeTokenToken = new TypeToken<>() {
            };
            worldIdentifiers = gson.fromJson(worldsString, treeSetTypeTokenToken.getType());
        }
        if (!configString.isEmpty()) {
            TypeToken<Config> configTypeToken = new TypeToken<>() {};
            config = gson.fromJson(configString, configTypeToken.getType());
        }

        ClientRegionMod.LOGGER.info("Loaded World data for " + ClientRegionMod.MOD_ID);


    }

    public static void store() {
        Gson gson = new Gson();
        String regionsString = gson.toJson(regions);
        try (FileWriter writer = new FileWriter(new File(worldFolder, REGIONS_FILE_NAME))) {
            writer.write(regionsString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String worldsString = gson.toJson(worldIdentifiers);
        try (FileWriter writer = new FileWriter(new File(worldFolder, WORLD_FILE_NAME))) {
            writer.write(worldsString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String configString = gson.toJson(config);
        try (FileWriter writer = new FileWriter(new File(worldFolder.getParent(), CONFIG_FILE_NAME))) {
            writer.write(configString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ClientRegionMod.LOGGER.info("Stored World data  for " + ClientRegionMod.MOD_ID);
    }

    private static void setWorldFolder(MinecraftClient client) {
        File parent = new File(FabricLoader.getInstance().getGameDir().toString() + "\\tondoas-regions");

        if (client.isInSingleplayer())
            worldFolder = new File(parent, Objects.requireNonNull(client.getServer()).getSavePath(WorldSavePath.ROOT).getParent().getFileName().toString());
        else
            worldFolder = new File(parent, "multiplayer_" + Objects.requireNonNull(client.getCurrentServerEntry()).address.replace(":", "_"));

        if (worldFolder.exists()) return;
        try {
            if (!worldFolder.mkdirs()) {
                throw new RuntimeException(ClientRegionMod.MOD_ID + ": Could not create " + worldFolder.getAbsolutePath());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Stream<TRegion> sortedRegions() {
        return sortedRegions(Comparator.comparing(t -> t.name));
    }

    public static Stream<TRegion> sortedRegions(java.util.Comparator<? super TRegion> comparator) {
        return regions.values().stream().sorted(comparator);
    }
    public static TRegion addTRegion(String name) {
        ClientPlayerEntity player = Objects.requireNonNull(MinecraftClient.getInstance().player);
        return addTRegion(name, player.getPos());
    }

    public static TRegion addTRegion(String name, Vec3d coords) {
        ClientPlayerEntity player = Objects.requireNonNull(MinecraftClient.getInstance().player);
        RegistryEntry<Biome> biomeRegistry = player.getWorld().getBiome(BlockPos.ofFloored(coords));
        Identifier biomeIdentifier = biomeRegistry.getKey().orElseThrow().getValue();
        Identifier worldIdentifier = player.getWorld().getRegistryKey().getValue();
        TRegion tRegion = new TRegion(name, coords, biomeIdentifier, worldIdentifier);
        //check if chunk is not loaded
        if (!player.getWorld().getChunkManager().isChunkLoaded(coords.x != 0 ? (int) (coords.x/16) : 0, coords.z != 0 ? (int) coords.z/16 : 0))
            tRegion.biomeUnknown = true;
        regions.put(name, tRegion);
        worldIdentifiers.add(worldIdentifier);
        return tRegion;
    }

    public static TRegion editTRegion(String oldKey, @Nullable String newKey, @Nullable Vec3d newCords) throws NullPointerException {
        TRegion tRegion = regions.get(oldKey);
        regions.remove(oldKey);
        if (newKey != null) tRegion.name = newKey;
        if (newCords == null) {
            regions.put(tRegion.name, tRegion);
            return tRegion;
        }
        return addTRegion(tRegion.name, newCords);
    }

    public static boolean chunkIsLoaded(double x, double z) {
        World world = Objects.requireNonNull(MinecraftClient.getInstance().player).getWorld();
        return world.getChunkManager().isChunkLoaded(x != 0 ? (int) (x/16) : 0, z != 0 ? (int) z/16 : 0);
    }

    public static TRegion updateIfInRange(TRegion tRegion) {
        ClientPlayerEntity player = Objects.requireNonNull(MinecraftClient.getInstance().player);
        if (chunkIsLoaded(player.getX(), player.getZ()))
            return editTRegion(tRegion.name, null, new Vec3d(tRegion.x, tRegion.y,  tRegion.z));
        return tRegion;
    }
}
