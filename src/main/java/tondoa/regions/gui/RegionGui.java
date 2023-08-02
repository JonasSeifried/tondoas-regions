package tondoa.regions.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
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
import tondoa.regions.gui.wigets.AddRegionPanel;
import tondoa.regions.gui.wigets.InputPanel;
import tondoa.regions.gui.wigets.RegionPanel;
import tondoa.regions.gui.wigets.RenamePanel;

import java.util.Comparator;
import java.util.List;
import java.util.function.BiConsumer;

public class RegionGui extends LightweightGuiDescription {

    @Nullable
    InputPanel activeInputPanel = null;

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
                if (activeInputPanel != null)
                    root.remove(activeInputPanel);
                activeInputPanel = renamePanel;
                root.add(renamePanel, root.getWidth()/18/2 - renamePanel.getWidth()/18/2, 2);
                root.validate(this);
            });

        };

        List<TRegion> regions = DataStorage.regions.values().stream().sorted(Comparator.comparing(tRegion -> tRegion.name)).toList();

        WListPanel<TRegion, RegionPanel> listPanel = new WListPanel<>(regions, RegionPanel::new, config);
        root.add(listPanel,7,2, 12, 10);

        WLabel label = new WLabel(Text.literal("Tondoas Regions"), 0xFFFFFF);
        label.setHorizontalAlignment(HorizontalAlignment.CENTER);
        root.add(label, 12, 0, 2, 1);

        WButton addButton = new WButton(Text.translatable("tondoas-regions.add"));
        addButton.setOnClick(() -> {
            AddRegionPanel addRegionPanel = new AddRegionPanel();
            if (activeInputPanel != null)
                root.remove(activeInputPanel);
            activeInputPanel = addRegionPanel;
            root.add(addRegionPanel, root.getWidth()/18/2 - addRegionPanel.getWidth()/18/2, 2);
            root.validate(this);

        });
        root.add(addButton, 12, 11, 2, 1);

        root.validate(this);

    }

}
