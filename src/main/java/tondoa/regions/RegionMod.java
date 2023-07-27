package tondoa.regions;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tondoa.regions.commands.CommandInitializer;
import tondoa.regions.persistent_data.ModPersistence;

public class RegionMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MOD_ID = "tondoas-regions";
    public static final Logger LOGGER = LoggerFactory.getLogger("tondoas-regions");

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.


		CommandInitializer.registerCommands();
		ModPersistence.registerPersistentStates();

		LOGGER.info("tondoas-regions Mod initialized");
	}
}