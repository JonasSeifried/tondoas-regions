package tondoa.regions.gui;

import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.minecraft.text.Text;

public class RegionPanel extends WPlainPanel {

    WLabel nameLabel;

    WLabel coordsLabel;

    WButton deleteButton;

    public RegionPanel() {
        nameLabel = new WLabel(Text.literal("Region Name"), 0xFFFFFF);
        this.add(nameLabel, 0, 0, 18*3, 18);
        coordsLabel = new WLabel(Text.literal("Coords"), 0xFFFFFF);
        this.add(coordsLabel, 18*3+2, 0, 18*6, 18 );
        deleteButton = new WButton(Text.literal("X"));
        this.add(deleteButton, 18*9, 0, 18, 18);

        setSize(18*11 + 6, 18);
    }
}
