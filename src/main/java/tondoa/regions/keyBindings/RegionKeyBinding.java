package tondoa.regions.keyBindings;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import tondoa.regions.ClientRegionMod;
import tondoa.regions.gui.RegionScreen;

public class RegionKeyBinding {
    private static KeyBinding openGuiKey;
    private static KeyBinding createRegionKey;
    public static void registerKeyBindings() {

        registerOpenGuiKey();
        registerCreateRegionGuiKey();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.wasPressed()) {
                client.setScreen(new RegionScreen());
            }
            while (createRegionKey.wasPressed()) {
                client.setScreen(new RegionScreen(true));
            }
        });

        ClientRegionMod.LOGGER.info("Registering Key Bindings for " + ClientRegionMod.MOD_ID);
    }

    private static void registerOpenGuiKey() {
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.tondoas-regions.open_gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                "category.tondoas-regions"
        ));
    }
    private static void registerCreateRegionGuiKey() {
        createRegionKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.tondoas-regions.add_region",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_B,
                "category.tondoas-regions"
        ));
    }

}
