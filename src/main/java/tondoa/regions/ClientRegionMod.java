package tondoa.regions;

import net.fabricmc.api.ClientModInitializer;
import tondoa.regions.commands.CommandInitializer;

public class ClientRegionMod implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.


		CommandInitializer.registerCommands();

		RegionMod.LOGGER.info("tondoas-regions ClientMod initialized");
	}

}