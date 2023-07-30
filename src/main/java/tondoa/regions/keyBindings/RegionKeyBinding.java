package tondoa.regions.keyBindings;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import tondoa.regions.RegionMod;
import tondoa.regions.gui.RegionGui;
import tondoa.regions.gui.RegionScreen;

public class RegionKeyBinding {
    private static KeyBinding openGuiKey;
    private static KeyBinding createRegionKey;
    public static void registerKeyBindings() {

        registerOpenGuiKey();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openGuiKey.wasPressed()) {
                client.setScreen(new RegionScreen(new RegionGui()));
            }
        });

        RegionMod.LOGGER.info("Registering Key Bindings for " + RegionMod.MOD_ID);
    }

    private static void registerOpenGuiKey() {
        openGuiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.tondoas-regions.open_gui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                "category.tondoas-regions"
        ));
    }

}