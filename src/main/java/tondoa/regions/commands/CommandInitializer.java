package tondoa.regions.commands;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import tondoa.regions.RegionMod;

public class CommandInitializer {

    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> RegionCommand.register(dispatcher));
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> RegionsCommand.register(dispatcher));
        RegionMod.LOGGER.info("Registering Mod Commands for " + RegionMod.MOD_ID);

    }
}
