package tondoa.regions.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import tondoa.regions.persistent_data.PlayerState;
import tondoa.regions.persistent_data.ServerState;
import tondoa.regions.persistent_data.TRegion;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;


public class RegionCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
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

    private static int renameRegion(ServerCommandSource source, String region, String newName) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayer();
        if (player == null) {
            throw new SimpleCommandExceptionType(Text.translatable("tondoas-region.command.region.not_player")).create();
        }
        PlayerState playerState = ServerState.getPlayerState(player);
        if (!playerState.regions.containsKey(region)) {
            throw new SimpleCommandExceptionType(Text.translatable("tondoas-region.command.region.not_found", region)).create();
        }
        if (playerState.regions.containsKey(newName)) {
            throw new SimpleCommandExceptionType(Text.translatable("tondoas-region.command.region.already_used", newName)).create();
        }

        TRegion nTRegion = new TRegion(playerState.regions.get(region), newName);
        playerState.regions.put(newName, nTRegion);
        playerState.regions.remove(region);
        ServerState.markDirty(player.server);
        player.sendMessage(Text.translatable("tondoas-region.command.region.renamed", region, newName)
                .setStyle(Style.EMPTY.withColor(Formatting.GREEN)));
        return 1;
    }

    public static int delRegion(ServerCommandSource source, String region) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayer();
        if (player == null) {
            throw new SimpleCommandExceptionType(Text.translatable("tondoas-region.command.region.not_player")).create();
        }
        PlayerState playerState = ServerState.getPlayerState(player);

        if (!playerState.regions.containsKey(region)) {
            player.sendMessage(Text.translatable("tondoas-region.command.region.not_found", region)
                    .setStyle(Style.EMPTY.withColor(Formatting.YELLOW)));
            return 0;
        }

        playerState.regions.remove(region);
        ServerState.markDirty(player.server);
        player.sendMessage(Text.translatable("tondoas-region.command.region.delete.deleted")
                .setStyle(Style.EMPTY.withColor(Formatting.RED)));
        return 1;

    }

    public static int addRegion(ServerCommandSource source, String region) {
        ServerPlayerEntity player = source.getPlayer();
        if (player == null) return -1;

        Vec3d coords = source.getPosition();
        RegistryEntry<Biome> biomeRegistry = player.getWorld().getBiome(BlockPos.ofFloored(coords));
        if (biomeRegistry.getKey().isEmpty())
            return -1;
        Identifier biomeIdentifier = biomeRegistry.getKey().get().getValue();


        PlayerState playerState = ServerState.getPlayerState(player);

        TRegion tRegion;
        if (region == null)
            tRegion = new TRegion(coords, biomeIdentifier, biomeIdentifier.getPath());
        else
            tRegion = new TRegion(coords, biomeIdentifier, region);

        if (playerState.regions.containsKey(tRegion.name)) {
            player.sendMessage(Text.translatable(
                    "tondoas-region.command.region.already_used", tRegion.name));
            return 0;
        }
        playerState.regions.put(tRegion.name, tRegion);
        ServerState.markDirty(player.server);

        player.sendMessage(tRegion.getText());
        return 1;
    }
}
