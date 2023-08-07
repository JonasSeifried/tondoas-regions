package tondoa.regions.gui.components;

import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.*;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import tondoa.regions.data_storage.DataStorage;
import tondoa.regions.data_storage.TRegion;

public class TRegionComponent extends FlowLayout {

    TRegion tRegion;
    RegionListViewComponent regionListViewComponent;
    ButtonComponent deleteButton = Components.button(Text.literal("X"), b -> handleDelete());
    ButtonComponent renameButton = Components.button(Text.literal(".."), b -> createRenameModal());
    LabelComponent nameLabel;
    LabelComponent coordsLabel;
    LabelComponent biomeLabel;
    TextBoxModalComponent renameModal = new TextBoxModalComponent();

    public TRegionComponent(TRegion tRegion, RegionListViewComponent regionListViewComponent) {
        this(tRegion, regionListViewComponent, Sizing.fill(100), Sizing.content(), Algorithm.HORIZONTAL);
    }

    public TRegionComponent(TRegion tRegion, RegionListViewComponent regionListViewComponent, Sizing horizontalSizing, Sizing verticalSizing, Algorithm algorithm) {
        super(horizontalSizing, verticalSizing, algorithm);
        this.tRegion = tRegion;
        this.regionListViewComponent = regionListViewComponent;
        deleteButton.tooltip(Text.translatable("tondoas-regions.delete")).horizontalSizing(Sizing.content());
        renameButton.tooltip(Text.translatable("tondoas-regions.rename")).horizontalSizing(Sizing.content());

        nameLabel = Components.label(Text.literal(tRegion.name));
        coordsLabel = Components.label(Text.literal(String.format("%.0f %.0f %.0f", tRegion.x, tRegion.y, tRegion.z)));
        biomeLabel = Components.label(tRegion.getTranslatedBiome());

        if (tRegion.worldIdentifier.equals(World.OVERWORLD.getValue()))
            biomeLabel.color(Color.ofRgb(0x228B22));
        else if (tRegion.worldIdentifier.equals(World.NETHER.getValue()))
            biomeLabel.color(Color.ofRgb(0x960018));
        else if (tRegion.worldIdentifier.equals(World.END.getValue()))
            biomeLabel.color(Color.ofRgb(0x6F00FF));

        this.child(nameLabel.verticalTextAlignment(VerticalAlignment.CENTER).horizontalSizing(Sizing.fill(20)))
                .child(coordsLabel.horizontalSizing(Sizing.fill(35)))
                .child(biomeLabel.horizontalSizing(Sizing.fill(35)))
                .child(deleteButton)
                .child(renameButton).verticalAlignment(VerticalAlignment.CENTER)
                .surface(Surface.DARK_PANEL).padding(Insets.of(8)).margins(Insets.bottom(2));
    }

    public void handleDelete() {
        DataStorage.regions.remove(tRegion.name);
        regionListViewComponent.tRegionComponentContainer.removeChild(this);
    }

    public void createRenameModal() {
        renameModal.label.text(Text.translatable("tondoas-regions.rename"));
        renameModal.label.color(Color.WHITE);
        renameModal.acceptButton.setMessage(Text.translatable("tondoas-regions.rename"));
        renameModal.acceptButton.onPress(b -> handleRename(renameModal.textBox.getText()));
        renameModal.textBox.text(tRegion.name);
        renameModal.horizontalSizing(Sizing.fill(100));
        this.child(0, renameModal);
    }

    private void handleRename(String newName) {
        if (RegionListViewComponent.isEmptyOrContainsKey(newName, renameModal))
            return;
        tRegion.name = newName;
        DataStorage.regions.put(newName, tRegion);
        DataStorage.regions.remove(tRegion.name);
        regionListViewComponent.updateTRegionComponentContainer();
    }
}
