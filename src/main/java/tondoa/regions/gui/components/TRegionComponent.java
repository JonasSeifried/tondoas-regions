package tondoa.regions.gui.components;

import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.*;
import net.minecraft.text.Text;
import tondoa.regions.data_storage.DataStorage;
import tondoa.regions.data_storage.TRegion;

public class TRegionComponent extends FlowLayout {


    TRegion tRegion;

    RegionListViewComponent regionListViewComponent;
    ButtonComponent deleteButton = Components.button(Text.literal("X"), b -> handleDelete());
    ButtonComponent renameButton = Components.button(Text.literal(".."), b -> createRenameModal());

    public TRegionComponent(TRegion tRegion, RegionListViewComponent regionListViewComponent) {
        this(tRegion, regionListViewComponent, Sizing.fill(100), Sizing.content(), Algorithm.HORIZONTAL);
    }

    public TRegionComponent(TRegion tRegion, RegionListViewComponent regionListViewComponent, Sizing horizontalSizing, Sizing verticalSizing, Algorithm algorithm) {
        super(horizontalSizing, verticalSizing, algorithm);
        this.tRegion = tRegion;
        this.regionListViewComponent = regionListViewComponent;
        deleteButton
                .tooltip(Text.translatable("tondoas-regions.delete"))
                .horizontalSizing(Sizing.fill(5));
        renameButton
                .tooltip(Text.translatable("tondoas-regions.rename"))
                .horizontalSizing(Sizing.fill(5))
                .margins(Insets.right(5));
        this
                .child(deleteButton)
                .child(renameButton)
                .child(Components.label(Text.literal(tRegion.name))
                        .verticalTextAlignment(VerticalAlignment.CENTER)
                        .horizontalSizing(Sizing.fill(30)))
                .child(Components.label(Text.literal(String.format("%.0f %.0f %.0f", tRegion.x, tRegion.y, tRegion.z)))
                        .horizontalSizing(Sizing.fill(30)))
                .child(Components.label(tRegion.getTranslatedBiome()).color(Color.ofRgb(0x808080))
                        .horizontalSizing(Sizing.fill(30)))
                .verticalAlignment(VerticalAlignment.CENTER)
                .surface(Surface.DARK_PANEL)
                .padding(Insets.of(4))
                .margins(Insets.bottom(2));
    }

    public void handleDelete() {
        DataStorage.regions.remove(tRegion.name);
        assert this.parent() != null;
        this.parent().removeChild(this);
    }

    public void createRenameModal() {
        TextBoxModalComponent renameModal = new TextBoxModalComponent();
        renameModal.label.text(Text.translatable("tondoas-regions.rename"));
        renameModal.acceptButton.setMessage(Text.translatable("tondoas-regions.rename"));
        renameModal.acceptButton.onPress(b -> handleRename(renameModal.textBox.getText()));
        renameModal.horizontalSizing(Sizing.fill(100));
        this.child(0, renameModal);
    }

    private void handleRename(String newName) {
        if (newName.isEmpty()) {
            // addErrorLabel(Text.translatable("tondoas-regions.no_empty_name"));
            return;
        }
        if(DataStorage.regions.containsKey(newName)) {
            // addErrorLabel(Text.translatable("tondoas-regions.name_taken"));
            return;
        }

        DataStorage.regions.put(newName, new TRegion(tRegion, newName));
        DataStorage.regions.remove(tRegion.name);

        regionListViewComponent.updateTRegionComponentContainer();
    }
}
