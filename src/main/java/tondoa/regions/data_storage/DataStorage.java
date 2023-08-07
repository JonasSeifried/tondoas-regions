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
import net.minecraft.world.biome.Biome;
import tondoa.regions.RegionMod;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class DataStorage {

    public static Map<String, TRegion> regions = new TreeMap<>();
    public static TreeSet<Identifier> wordIdentifiers = new TreeSet<>();
    private static File worldFolder;

    public static final String WORLD_FILE_NAME = "worlds.json";
    public static final String REGIONS_FILE_NAME = "regions.json";

    public static void register() {

        ClientPlayConnectionEvents.JOIN.register(((handler, sender, client) -> load(client)));
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> store());
        RegionMod.LOGGER.info("Registering ClientPlayConnectionEvents for " + RegionMod.MOD_ID);
    }

    public static void load(MinecraftClient client) {
        regions.clear();
        setWorldFolder(client);
        File regionsFile = new File(worldFolder, REGIONS_FILE_NAME);
        File worldsFile = new File(worldFolder, WORLD_FILE_NAME);
        String regionsString;
        String worldsString;
        Gson gson = new Gson();
        try {
            if (regionsFile.createNewFile() && worldsFile.createNewFile()) return;
            regionsString = new String(Files.readAllBytes(Path.of(regionsFile.toURI())));
            worldsString = new String(Files.readAllBytes(Path.of(worldsFile.toURI())));

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
            wordIdentifiers = gson.fromJson(worldsString, treeSetTypeTokenToken.getType());
        }

        RegionMod.LOGGER.info("Loaded World data for " + RegionMod.MOD_ID);


    }

    public static void store() {
        Gson gson = new Gson();
        String regionsString = gson.toJson(regions);
        try (FileWriter writer = new FileWriter(new File(worldFolder, REGIONS_FILE_NAME))) {
            writer.write(regionsString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String worldsString = gson.toJson(wordIdentifiers);
        try (FileWriter writer = new FileWriter(new File(worldFolder, WORLD_FILE_NAME))) {
            writer.write(worldsString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        RegionMod.LOGGER.info("Stored World data  for " + RegionMod.MOD_ID);
    }

    private static void setWorldFolder(MinecraftClient client) {
        File parent = new File(FabricLoader.getInstance().getGameDir().toString() + "\\tondoas-regions");

        if (client.isInSingleplayer())
            worldFolder = new File(parent, Objects.requireNonNull(client.getServer()).getSavePath(WorldSavePath.ROOT).getParent().getFileName().toString());
        else
            worldFolder = new File(parent, "multiplayer_" + Objects.requireNonNull(client.getCurrentServerEntry()).address);

        if (worldFolder.exists()) return;
        try {
            if (!worldFolder.mkdirs()) {
                throw new RuntimeException(RegionMod.MOD_ID + ": Could not create " + worldFolder.getAbsolutePath());
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
        Vec3d coords = player.getPos();
        RegistryEntry<Biome> biomeRegistry = player.getWorld().getBiome(BlockPos.ofFloored(coords));
        Identifier biomeIdentifier = biomeRegistry.getKey().orElseThrow().getValue();
        Identifier worldIdentifier = player.getWorld().getRegistryKey().getValue();
        TRegion tRegion = new TRegion(name, coords, biomeIdentifier, worldIdentifier);
        regions.put(name, tRegion);
        wordIdentifiers.add(worldIdentifier);
        return tRegion;
    }
}
