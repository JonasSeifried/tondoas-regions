package tondoa.regions.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WSprite;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import tondoa.regions.data_storage.DataStorage;
import tondoa.regions.data_storage.TRegion;

public class RegionGui extends LightweightGuiDescription {
    public RegionGui() {
        setFullscreen(true);
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setInsets(Insets.ROOT_PANEL);
        WSprite icon = new WSprite(new Identifier("minecraft:textures/item/diamond.png"));
        root.add(icon, 10, 6, 1, 1);

        WGridPanel regionPanel = new WGridPanel();
        int c = 0;
        for(TRegion tRegion : DataStorage.regions.values()) {


            WLabel label = new WLabel(tRegion.getText(), 0xFFFFFF);
            label.setVerticalAlignment(VerticalAlignment.CENTER);


            WButton button = new WButton(Text.literal("X"));
            button.setOnClick(() -> {
                DataStorage.regions.remove(tRegion.name);
                MinecraftClient.getInstance().setScreen(new RegionScreen(new RegionGui()));

            });
            regionPanel.add(label, 0, c, 20, 2);
            regionPanel.add(button, 5, c);
            c++;
        }

        root.add(regionPanel,0, 2);


        WLabel label = new WLabel(Text.literal("Tondoas Regions"), 0xFFFFFF);
        root.add(label, 10, 0, 2, 1);

        root.validate(this);
    }

}
