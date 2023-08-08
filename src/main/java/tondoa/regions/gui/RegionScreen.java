package tondoa.regions.gui;

import io.wispforest.owo.ui.base.BaseOwoScreen;
import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.container.GridLayout;
import io.wispforest.owo.ui.core.*;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import tondoa.regions.gui.components.RegionListViewComponent;
import tondoa.regions.gui.components.SettingsComponent;

public class RegionScreen extends BaseOwoScreen<FlowLayout> {

    private boolean openAddRegionModal = false;

    GridLayout headerContainer = Containers.grid(Sizing.fill(100), Sizing.content(), 1, 3);

    FlowLayout root;

    LabelComponent label = Components.label(Text.literal("Tondoas Regions"));

    RegionListViewComponent regionListViewComponent = new RegionListViewComponent();

    SettingsComponent settingsComponent = new SettingsComponent();
    ButtonComponent settingsButton = Components.button(Text.translatable("tondoas-regions.settings"), this::openSettings);

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
        root = rootComponent;
        root.surface(Surface.VANILLA_TRANSLUCENT).horizontalAlignment(HorizontalAlignment.CENTER);

        regionListViewComponent = new RegionListViewComponent();
        if (openAddRegionModal) {
            regionListViewComponent.createAddRegionModal();
        }
        label.margins(Insets.top(5));

        settingsComponent.closeButton.onPress(b -> {
            root.removeChild(settingsComponent);
            root.child(regionListViewComponent);
            regionListViewComponent.updateTRegionComponentContainer();
        });

        headerContainer.child(label, 0, 1)
                .child(Containers.horizontalFlow(Sizing.fill(33), Sizing.content())
                        .child(settingsButton).horizontalAlignment(HorizontalAlignment.RIGHT), 0, 2);
        headerContainer.margins(Insets.of(1, 20, 0, 0)).horizontalAlignment(HorizontalAlignment.CENTER);

        root.child(headerContainer).child(regionListViewComponent);

    }

    private void openSettings(ButtonComponent buttonComponent) {
        root.removeChild(regionListViewComponent);
        root.removeChild(settingsComponent);
        root.child(settingsComponent);
    }
}
