package tondoa.regions.gui.components;

import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.TextBoxComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.*;
import net.minecraft.text.Text;
import tondoa.regions.data_storage.DataStorage;
import tondoa.regions.data_storage.TRegion;

public class RegionListViewComponent extends FlowLayout {

    public TextBoxComponent searchTextBox = Components.textBox(Sizing.fill(80));
    public FlowLayout tRegionComponentContainer = Containers.verticalFlow(Sizing.content(), Sizing.content());

    public RegionListViewComponent() {
        this(Sizing.fill(60), Sizing.content(), Algorithm.VERTICAL);


    }
    public RegionListViewComponent(Sizing horizontalSizing, Sizing verticalSizing, Algorithm algorithm) {
        super(horizontalSizing, verticalSizing, algorithm);
        searchTextBox.setPlaceholder(Text.translatable("tondoas-regions.search_placeholder"));
        searchTextBox.onChanged().subscribe(s -> updateTRegionComponentContainer());
        this.child(searchTextBox);
        this.child(Containers.verticalScroll(Sizing.content(), Sizing.fill(50), tRegionComponentContainer));

        updateTRegionComponentContainer();
        this.horizontalAlignment(HorizontalAlignment.CENTER);
        this.gap(5);
        this.gap(5);
        this.surface(Surface.VANILLA_TRANSLUCENT);
        this.padding(Insets.of(5, 2, 5, 5));
    }

    public void updateTRegionComponentContainer() {
        String searchKey = searchTextBox.getText().toLowerCase();
       tRegionComponentContainer.clearChildren();
        DataStorage.sortedRegions()
                .filter(t ->  t.name.toLowerCase().startsWith(searchKey) || t.getTranslatedBiome().getString().toLowerCase().startsWith(searchKey))
                .forEach(t -> tRegionComponentContainer.child(new TRegionComponent(t, this)));
    }
}
