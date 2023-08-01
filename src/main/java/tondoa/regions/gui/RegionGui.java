package tondoa.regions.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WListPanel;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import tondoa.regions.data_storage.DataStorage;
import tondoa.regions.data_storage.TRegion;
import tondoa.regions.gui.wigets.RegionPanel;
import tondoa.regions.gui.wigets.RenamePanel;

import java.util.function.BiConsumer;

public class RegionGui extends LightweightGuiDescription {

    @Nullable
    RenamePanel activeRenamePanel = null;

    public RegionGui() {
        setFullscreen(true);
        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setInsets(Insets.ROOT_PANEL);


        BiConsumer<TRegion, RegionPanel> config = (TRegion tRegion, RegionPanel panel) -> {
            int maxLength = 10;
            String name = tRegion.name;
            if (name.length() >= maxLength )
                name = name.substring(0, maxLength-2) + "..";
            panel.nameLabel.setText(Text.literal(name));
            panel.coordsLabel.setText(Text.literal(String.format("%.2f %.2f %.2f", tRegion.x, tRegion.y, tRegion.z)));

            panel.deleteButton.setOnClick(() -> {
                DataStorage.regions.remove(tRegion.name);
                MinecraftClient.getInstance().setScreen(new RegionScreen(new RegionGui()));

            });

            panel.renameButton.setOnClick(() -> {
                RenamePanel renamePanel = new RenamePanel(tRegion);
                if (activeRenamePanel != null)
                    root.remove(activeRenamePanel);
                activeRenamePanel = renamePanel;
                root.add(renamePanel, root.getWidth()/18/2 - renamePanel.getWidth()/18/2, 2);
                root.validate(this);
            });

        };
        WListPanel<TRegion, RegionPanel> listPanel = new WListPanel<>(DataStorage.regions.values().stream().toList(), RegionPanel::new, config);
        root.add(listPanel,7,2, 12, 10);

        WLabel label = new WLabel(Text.literal("Tondoas Regions"), 0xFFFFFF);
        label.setHorizontalAlignment(HorizontalAlignment.CENTER);
        root.add(label, 12, 0, 2, 1);

        root.validate(this);
    }

}
