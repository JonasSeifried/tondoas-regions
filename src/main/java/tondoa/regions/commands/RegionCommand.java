package tondoa.regions.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import tondoa.regions.data_storage.DataStorage;
import tondoa.regions.data_storage.TRegion;
import tondoa.regions.gui.RegionScreen;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;


public class RegionCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("region")
                .then(literal("del")
                    .then(argument("region", StringArgumentType.word())
                    .executes(ctx -> delRegion(ctx.getSource(), StringArgumentType.getString(ctx, "region")))))
                .then(literal("rename")
                    .then(argument("region", StringArgumentType.word())
                        .then(argument("newName", StringArgumentType.word())
                            .executes(ctx -> renameRegion(ctx.getSource(), StringArgumentType.getString(ctx, "region"), StringArgumentType.getString(ctx, "newName"))))))
                .then(argument("region", StringArgumentType.word())
                        .executes(ctx -> addRegion(ctx.getSource(), StringArgumentType.getString(ctx,"region"))))
                .executes(ctx -> addRegion(ctx.getSource(), null)));

    }
    private static int renameRegion(FabricClientCommandSource source, String region, String newName) throws CommandSyntaxException {
        ClientPlayerEntity player = source.getPlayer();
        if (player == null) {
            throw new SimpleCommandExceptionType(Text.translatable("tondoas-regions.command.region.not_player")).create();
        }

        if (!DataStorage.regions.containsKey(region)) {
            throw new SimpleCommandExceptionType(Text.translatable("tondoas-regions.command.region.not_found", region)).create();
        }
        if (DataStorage.regions.containsKey(newName)) {
            throw new SimpleCommandExceptionType(Text.translatable("tondoas-regions.name_already_used", newName)).create();
        }

        TRegion nTRegion = new TRegion(DataStorage.regions.get(region), newName);
        DataStorage.regions.put(newName, nTRegion);
        DataStorage.regions.remove(region);
        player.sendMessage(Text.translatable("tondoas-regions.command.region.renamed", region, newName)
                .setStyle(Style.EMPTY.withColor(Formatting.GREEN)));
        return 1;
    }

    public static int delRegion(FabricClientCommandSource source, String region) throws CommandSyntaxException {
        ClientPlayerEntity player = source.getPlayer();
        if (player == null) {
            throw new SimpleCommandExceptionType(Text.translatable("tondoas-regions.command.region.not_player")).create();
        }

        if (DataStorage.regions.containsKey(region)) {
            player.sendMessage(Text.translatable("tondoas-regions.command.region.not_found", region)
                    .setStyle(Style.EMPTY.withColor(Formatting.YELLOW)));
            return 0;
        }

        DataStorage.regions.remove(region);
        player.sendMessage(Text.translatable("tondoas-regions.command.region.delete.deleted")
                .setStyle(Style.EMPTY.withColor(Formatting.RED)));
        return 1;

    }

    public static int addRegion(FabricClientCommandSource source, String region) {
        ClientPlayerEntity player = source.getPlayer();
        if (player == null) return -1;

        Vec3d coords = source.getPosition();
        RegistryEntry<Biome> biomeRegistry = player.getWorld().getBiome(BlockPos.ofFloored(coords));
        if (biomeRegistry.getKey().isEmpty())
            return -1;
        Identifier biomeIdentifier = biomeRegistry.getKey().get().getValue();


        TRegion tRegion;
        if (region == null)
            tRegion = new TRegion(coords, biomeIdentifier, biomeIdentifier.getPath());
        else
            tRegion = new TRegion(coords, biomeIdentifier, region);

        if (DataStorage.regions.containsKey(tRegion.name)) {
            player.sendMessage(Text.translatable(
                    "tondoas-regions.name_already_used", tRegion.name));
            return 0;
        }
        DataStorage.regions.put(tRegion.name, tRegion);
        player.sendMessage(tRegion.getText());
        return 1;
    }
}
