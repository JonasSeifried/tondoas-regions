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
        textBox.setPlaceholder(Text.translatable("tondoas-regions.gui.search_placeholder"));
        parentContainer.child(textBox);
        parentContainer.gap(5);


        FlowLayout container = Containers.verticalFlow(Sizing.content(), Sizing.content());
        DataStorage.sortedRegions().forEach(t -> container.child(buildChild(t)));
        textBox.onChanged().subscribe(s -> {
            container.clearChildren();
            DataStorage.sortedRegions()
                    .filter(t -> searchFilter.test(t, s.toLowerCase()))
                    .forEach(t -> container.child(buildChild(t)));
        });

        parentContainer.child(Containers.verticalScroll(Sizing.content(), Sizing.fill(50), container));

        return parentContainer
                .surface(Surface.VANILLA_TRANSLUCENT)
                .padding(Insets.of(5, 2, 5, 5));
    }

    private static FlowLayout buildChild(TRegion tRegion) {
        FlowLayout container = Containers.horizontalFlow(Sizing.fill(100), Sizing.content());

        container
                .child(Components.button(
                        Text.literal("X"),
                        button -> {
                            DataStorage.regions.remove(tRegion.name);
                            assert button.parent() != null;
                            assert button.parent().parent() != null;
                            button.parent().parent().removeChild(button.parent());
                        })
                        .horizontalSizing(Sizing.fill(5)))
                .child(Components.button(
                        Text.literal(".."),
                        button -> {

                        })
                        .horizontalSizing(Sizing.fill(5))
                        .margins(Insets.right(5)))
                .child(Components.label(Text.literal(tRegion.name))
                        .verticalTextAlignment(VerticalAlignment.CENTER)
                        .horizontalSizing(Sizing.fill(30)))
                .child(Components.label(Text.literal(String.format("%.0f %.0f %.0f", tRegion.x, tRegion.y, tRegion.z)))
                        .horizontalSizing(Sizing.fill(30)))
                .child(Components.label(tRegion.getTranslatedBiome()).color(Color.ofRgb(0x808080))
                        .horizontalSizing(Sizing.fill(30)));

        container
                .verticalAlignment(VerticalAlignment.CENTER)
                .surface(Surface.DARK_PANEL)
                .padding(Insets.of(4))
                .margins(Insets.bottom(2));

        return container;
    }

}
