package tondoa.regions.commands;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import tondoa.regions.ClientRegionMod;

public class CommandInitializer {

    public static void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> RegionCommand.register(dispatcher));
        ClientRegionMod.LOGGER.info("Registering Mod Commands for " + ClientRegionMod.MOD_ID);

    }
}
