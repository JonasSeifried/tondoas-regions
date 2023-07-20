package tondoa.regions.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.ServerCommandSource;
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
                .then(argument("region", StringArgumentType.word())
                        .executes(ctx -> region(ctx.getSource(), StringArgumentType.getString(ctx,"region"))))
                .executes(ctx -> region(ctx.getSource(), null)));

    }

    public static int region(ServerCommandSource source, String region) {
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
