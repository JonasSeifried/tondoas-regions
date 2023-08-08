package tondoa.regions;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tondoa.regions.commands.CommandInitializer;
import tondoa.regions.data_storage.DataStorage;
import tondoa.regions.keyBindings.RegionKeyBinding;

public class ClientRegionMod implements ClientModInitializer {


	public static final String MOD_ID = "tondoas-regions";
	public static final Logger LOGGER = LoggerFactory.getLogger("tondoas-regions");

	@Override
	public void onInitializeClient() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.


		DataStorage.register();
		CommandInitializer.registerCommands();
		RegionKeyBinding.registerKeyBindings();

		LOGGER.info("tondoas-regions ClientMod initialized");
	}

}