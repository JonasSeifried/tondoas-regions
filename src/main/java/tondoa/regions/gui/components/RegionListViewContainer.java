package tondoa.regions.gui.components;

import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.TextBoxComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.*;
import net.minecraft.text.Text;
import tondoa.regions.data_storage.DataStorage;
import tondoa.regions.data_storage.TRegion;

import java.util.function.BiPredicate;

public final class RegionListViewContainer {

    private static final BiPredicate<TRegion, String> searchFilter = (t, s) ->
            t.name.toLowerCase().startsWith(s) || t.getTranslatedBiome().getString().toLowerCase().startsWith(s);


    public static Component build() {
        FlowLayout parentContainer = Containers.verticalFlow(Sizing.fill(60), Sizing.content());
        parentContainer.horizontalAlignment(HorizontalAlignment.CENTER);
        TextBoxComponent textBox = Components.textBox(Sizing.fill(80));
        parentContainer.child(textBox);
        parentContainer.gap(5);


        FlowLayout container =  Containers.verticalFlow(Sizing.content(), Sizing.content());
        DataStorage.sortedRegions().forEach(t -> container.child(buildChild(t)));
        textBox.onChanged().subscribe(s -> {
            container.clearChildren();
            DataStorage.sortedRegions()
                    .filter(t -> searchFilter.test(t, s.toLowerCase()))
                    .forEach(t -> container.child(buildChild(t)));
        });

        parentContainer.child(Containers.horizontalScroll(Sizing.fill(100), Sizing.fill(50),
                Containers.verticalScroll(Sizing.content(), Sizing.fill(96), container)));

        return parentContainer
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
                                        tRegion.getTranslatedBiome())
                                        .color(Color.ofRgb(0x808080))
                                        .horizontalTextAlignment(HorizontalAlignment.RIGHT)
                                        .horizontalSizing(Sizing.content())
                        );
    }

}
