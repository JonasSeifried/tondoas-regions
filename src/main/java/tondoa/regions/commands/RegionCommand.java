package tondoa.regions.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import tondoa.regions.persistent_data.PlayerState;
import tondoa.regions.persistent_data.ServerState;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;


public class RegionCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("region")
                .then(argument("region", StringArgumentType.word())
                        .executes(ctx -> region(ctx.getSource(), StringArgumentType.getString(ctx,"region")))));
    }

    public static int region(ServerCommandSource source, String region) {
        if (source.getPlayer() == null) return -1;

        Vec3d coords = source.getPosition();
        PlayerState playerState = ServerState.getPlayerState(source.getPlayer());
        Vec3d prev = playerState.regions.put(region, source.getPosition());
        ServerState.getServerState(source.getPlayer().server).markDirty();
        String overwrittenStr = (prev == null) ? "" : "Overwritten ";
        Text msg = Text.literal(overwrittenStr + String.format("%s [%.3f / %.3f / %.3f]", region, coords.x, coords.y, coords.x));

        source.getPlayer().sendMessage(msg);
        return 1;
    }
}
