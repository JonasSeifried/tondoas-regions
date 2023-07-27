package tondoa.regions.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
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
                .then(argument("region", StringArgumentType.word())
                        .executes(ctx -> addRegion(ctx.getSource(), StringArgumentType.getString(ctx,"region"))))
                .executes(ctx -> addRegion(ctx.getSource(), null)));

    }

    public static int delRegion(ServerCommandSource source, String region) throws CommandSyntaxException {
        ServerPlayerEntity player = source.getPlayer();
        if (player == null) {
            throw new SimpleCommandExceptionType(Text.translatable("tondoas-region.command.region.not_player")).create();
        }
        PlayerState playerState = ServerState.getPlayerState(player);

        if (!playerState.regions.containsKey(region)) {
            player.sendMessage(Text.translatable("tondoas-region.command.region.delete.not_found", region));
            return 0;
        }

        playerState.regions.remove(region);
        ServerState.markDirty(player.server);
        player.sendMessage(Text.translatable("tondoas-region.command.region.delete.deleted"));
        return 1;

    }

    public static int addRegion(ServerCommandSource source, String region) {
        if (source.getPlayer() == null) return -1;

        Vec3d coords = source.getPosition();
        RegistryEntry<Biome> biomeRegistry = source.getPlayer().getWorld().getBiome(BlockPos.ofFloored(coords));
        if (biomeRegistry.getKey().isEmpty())
            return -1;
        Identifier biomeIdentifier = biomeRegistry.getKey().get().getValue();


        PlayerState playerState = ServerState.getPlayerState(source.getPlayer());

        TRegion tRegion;
        if (region == null)
            tRegion = new TRegion(coords, biomeIdentifier, biomeIdentifier.getPath());
        else
            tRegion = new TRegion(coords, biomeIdentifier, region);

        if (playerState.regions.containsKey(tRegion.name)) {
            source.getPlayer().sendMessage(Text.translatable(tRegion.name).append(" is already being used!"));
            return 0;
        }
        playerState.regions.put(tRegion.name, tRegion);
        ServerState.getServerState(source.getPlayer().server).markDirty();

        source.getPlayer().sendMessage(tRegion.getText());
        return 1;
    }
}
