package tondoa.regions.gui;

import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.*;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import tondoa.regions.gui.components.RegionListViewComponent;

public class RegionScreen extends BaseOwoScreen<FlowLayout> {

    private boolean openAddRegionModal = false;
    public RegionScreen() {
    }
    public RegionScreen(boolean openAddRegionModal) {
        this.openAddRegionModal = openAddRegionModal;
    }
    @Override
    protected @NotNull OwoUIAdapter<FlowLayout> createAdapter() {
        return OwoUIAdapter.create(this, Containers::verticalFlow);
    }

    @Override
    protected void build(FlowLayout rootComponent) {
        rootComponent
                .surface(Surface.VANILLA_TRANSLUCENT)
                .horizontalAlignment(HorizontalAlignment.CENTER);

        RegionListViewComponent regionListViewComponent = new RegionListViewComponent();
        if (openAddRegionModal) {
            regionListViewComponent.createAddRegionModal();
        }

        rootComponent
                .child(Components.label(Text.literal("Tondoas Regions")).margins(Insets.of(5,20,0,0)))
                .child(regionListViewComponent);

    }
}
