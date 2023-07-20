package tondoa.regions.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import tondoa.regions.persistent_data.PlayerState;
import tondoa.regions.persistent_data.ServerState;

import java.util.Map;

import static net.minecraft.server.command.CommandManager.literal;

public class RegionsCommand {


    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("regions")
                .executes(ctx -> regions(ctx.getSource())));
    }

    public static int regions(ServerCommandSource source) {
        if (source.getPlayer() == null) return -1;
        PlayerState playerState = ServerState.getPlayerState(source.getPlayer());
        StringBuilder sb = new StringBuilder();
        sb.append("List of all your regions\n");
        for( Map.Entry<String, Vec3d> e : playerState.regions.entrySet()) {
            Vec3d v = e.getValue();
            sb.append(e.getKey()).append(String.format(": [%.3f / %.3f / %.3f]\n",  v.x, v.y, v.z));
        }

        Text msg = Text.literal(sb.toString());
        source.getPlayer().sendMessage(msg);
        return 1;
    }
}
