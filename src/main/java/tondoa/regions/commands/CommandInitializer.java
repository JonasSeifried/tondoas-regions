package tondoa.regions.commands;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import tondoa.regions.RegionMod;

public class CommandInitializer {

    public static void registerCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> RegionCommand.register(dispatcher));
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> RegionsCommand.register(dispatcher));
        RegionMod.LOGGER.info("Registering Mod Commands for " + RegionMod.MOD_ID);

    }
}
