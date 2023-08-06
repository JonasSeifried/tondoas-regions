package tondoa.regions.data_storage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.WorldSavePath;
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
    private static File worldFolder;


    public static void register() {

        ClientPlayConnectionEvents.JOIN.register(((handler, sender, client) -> load(client)));
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> store());
        RegionMod.LOGGER.info("Registering ClientPlayConnectionEvents for " + RegionMod.MOD_ID);
    }

    public static void load(MinecraftClient client)  {
        regions.clear();
        setWorldFolder(client);
        File jsonFile = new File(worldFolder, "regions.json");
        String jsonString;
        Gson gson = new Gson();
        try {
            if (jsonFile.createNewFile())
                return;
            jsonString = new String(Files.readAllBytes(Path.of(jsonFile.toURI())));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (jsonString.isEmpty())
            return;

        TypeToken<Map<String, TRegion>> typeToken = new TypeToken<>() {};
        regions = gson.fromJson(jsonString, typeToken.getType());
        RegionMod.LOGGER.info("Loaded " + regions.size() + " regions for " + RegionMod.MOD_ID);
    }

    public static void store() {
        Gson gson = new Gson();
        String jsonString = gson.toJson(regions);
        try (FileWriter writer = new FileWriter(new File(worldFolder, "regions.json"))) {
            writer.write(jsonString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        RegionMod.LOGGER.info("Stored " + regions.size() + " regions for " + RegionMod.MOD_ID);
    }

    private static void setWorldFolder(MinecraftClient client) {
        File parent = new File(FabricLoader.getInstance().getGameDir().toString() + "\\tondoas-regions");

        if (client.isInSingleplayer())
            worldFolder = new File(parent, Objects.requireNonNull(client.getServer()).getSavePath(WorldSavePath.ROOT).getParent().getFileName().toString());
        else
            worldFolder = new File(parent, "multiplayer_" + Objects.requireNonNull(client.getCurrentServerEntry()).address);

        if (worldFolder.exists())
            return;
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
}
