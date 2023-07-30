package tondoa.regions.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import tondoa.regions.data_storage.DataStorage;
import tondoa.regions.data_storage.TRegion;

public class RegionsCommand {


    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("regions")
                .executes(ctx -> regions(ctx.getSource())));
    }

    public static int regions(FabricClientCommandSource source) throws CommandSyntaxException {
        ClientPlayerEntity player = source.getPlayer();
        if (player == null) {
            throw new SimpleCommandExceptionType(Text.translatable("tondoas-regions.command.region.not_player")).create();
        }

        MutableText text = Text.literal("List of all your regions\n");

        int c = DataStorage.regions.size();
        for( TRegion region : DataStorage.regions.values()) {
            text.append(region.getText());
            if (c-- != 1) text.append("\n");
        }
        player.sendMessage(text);
        return 1;
    }
}
