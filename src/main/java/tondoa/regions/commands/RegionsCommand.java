package tondoa.regions.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import tondoa.regions.persistent_data.PlayerState;
import tondoa.regions.persistent_data.ClientState;
import tondoa.regions.persistent_data.TRegion;

public class RegionsCommand {


    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("regions")
                .executes(ctx -> regions(ctx.getSource())));
    }

    public static int regions(FabricClientCommandSource source) throws CommandSyntaxException {
        ClientPlayerEntity player = source.getPlayer();
        if (player == null) {
            throw new SimpleCommandExceptionType(Text.translatable("tondoas-region.command.region.not_player")).create();
        }
        ClientState clientState = ClientState.getClientState();
        PlayerState playerState = clientState.playerState;

        MutableText text = Text.literal("List of all your regions\n");

        int c = playerState.regions.size();
        for( TRegion region : playerState.regions.values()) {
            text.append(region.getText());
            if (c-- != 1) text.append("\n");
        }
        player.sendMessage(text);
        return 1;
    }
}
