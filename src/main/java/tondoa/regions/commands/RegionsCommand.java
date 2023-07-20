package tondoa.regions.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import tondoa.regions.persistent_data.PlayerState;
import tondoa.regions.persistent_data.ServerState;
import tondoa.regions.persistent_data.TRegion;

import static net.minecraft.server.command.CommandManager.literal;

public class RegionsCommand {


    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("regions")
                .executes(ctx -> regions(ctx.getSource())));
    }

    public static int regions(ServerCommandSource source) {
        if (source.getPlayer() == null) return -1;
        PlayerState playerState = ServerState.getPlayerState(source.getPlayer());
        MutableText text = Text.literal("List of all your regions\n");
        for( TRegion region : playerState.regions.values()) {
            text.append(region.getText()).append("\n");
        }

        source.getPlayer().sendMessage(text);
        return 1;
    }
}
