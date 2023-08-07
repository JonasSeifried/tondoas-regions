package tondoa.regions.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import tondoa.regions.data_storage.DataStorage;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;


public class RegionCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("region").then(argument("region", StringArgumentType.word())
                .executes(ctx -> addRegion(ctx.getSource(), StringArgumentType.getString(ctx, "region")))));

    }

    public static int addRegion(FabricClientCommandSource source, String region) {
        ClientPlayerEntity player = source.getPlayer();
        if (player == null) return -1;
        if (DataStorage.regions.containsKey(region)) {
            player.sendMessage(Text.translatable("tondoas-regions.name_already_used", region));
            return 0;
        }
        player.sendMessage(DataStorage.addTRegion(region).getText());
        return 1;
    }
}
