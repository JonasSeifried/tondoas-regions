package tondoa.regions.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import tondoa.regions.gui.wigets.AddRegionPanel;

public class AddRegionGui extends LightweightGuiDescription {

    WGridPanel root = new WGridPanel();

    public AddRegionGui() {
        setRootPanel(root);

        AddRegionPanel addRegionPanel = new AddRegionPanel();

        root.add(addRegionPanel, 0, 0);
    }
}
