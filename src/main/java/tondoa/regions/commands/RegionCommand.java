package tondoa.regions.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import tondoa.regions.data_storage.DataStorage;
import tondoa.regions.data_storage.TRegion;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;


public class RegionCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("region")
                .then(argument("region", StringArgumentType.word())
                        .executes(ctx -> addRegion(ctx.getSource(), StringArgumentType.getString(ctx,"region"))))
                .executes(ctx -> addRegion(ctx.getSource(), null)));

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
