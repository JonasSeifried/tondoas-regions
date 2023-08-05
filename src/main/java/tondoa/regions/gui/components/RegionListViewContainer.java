package tondoa.regions.gui.components;

import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.*;
import net.minecraft.text.Text;
import tondoa.regions.data_storage.DataStorage;
import tondoa.regions.data_storage.TRegion;

public class RegionListViewContainer {


    public static Component build() {
        FlowLayout container =  Containers.verticalFlow(Sizing.content(), Sizing.content());
        DataStorage.regions.values().forEach(t -> container.child(buildChild(t)));
        return Containers.horizontalScroll(Sizing.fill(60), Sizing.fill(50),
                Containers.verticalScroll(Sizing.content(), Sizing.fill(96),
                        container))
                .surface(Surface.VANILLA_TRANSLUCENT)
                .padding(Insets.of(5,2,5,5));
    }

    private static Component buildChild(TRegion tRegion) {
        return Containers.horizontalFlow(Sizing.content(), Sizing.content())
                        .child(
                                Components.label(Text.literal(tRegion.name))
                                        .horizontalSizing(Sizing.fill(33))
                )
                        .child(
                                Components.label(Text.literal(String.format("%.0f %.0f %.0f",
                                                tRegion.x, tRegion.y, tRegion.z)))
                                        .horizontalTextAlignment(HorizontalAlignment.CENTER)
                                        .horizontalSizing(Sizing.fill(33))
                        )
                        .child(
                                Components.label(
                                        Text.translatable(String.format("biome.%s.%s",
                                                tRegion.biomeNamespace, tRegion.biomePath)))
                                        .color(Color.ofRgb(0x808080))
                                        .horizontalTextAlignment(HorizontalAlignment.RIGHT)
                                        .horizontalSizing(Sizing.content())
                        );
    }

}
