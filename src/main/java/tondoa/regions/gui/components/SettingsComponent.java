package tondoa.regions.gui.components;

import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.CheckboxComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.HorizontalAlignment;
import io.wispforest.owo.ui.core.Insets;
import io.wispforest.owo.ui.core.Sizing;
import io.wispforest.owo.ui.core.Surface;
import net.minecraft.text.Text;
import tondoa.regions.data_storage.DataStorage;

import java.util.Objects;

public class SettingsComponent extends FlowLayout {

    public LabelComponent label = Components.label(Text.translatable("tondoas-regions.settings"));

    public CheckboxComponent coloredBiomesCheckBox = Components.checkbox(Text.translatable("tondoas-regions.colored_biomes"));
    public CheckboxComponent currDimCheckBox = Components.checkbox(Text.translatable("tondoas-regions.current_dimension"));
    public CheckboxComponent roundCoordinatesCheckBox = Components.checkbox(Text.translatable("tondoas-regions.round"));
    public FlowLayout footerContainer = Containers.horizontalFlow(Sizing.fill(100), Sizing.content());
    public ButtonComponent closeButton = Components.button(Text.translatable("tondoas-regions.close"), b -> Objects.requireNonNull(this.parent()).removeChild(this));

    public SettingsComponent() {
        this(Sizing.fill(60), Sizing.content(), Algorithm.VERTICAL);
    }

    protected SettingsComponent(Sizing horizontalSizing, Sizing verticalSizing, Algorithm algorithm) {
        super(horizontalSizing, verticalSizing, algorithm);

        label.horizontalTextAlignment(HorizontalAlignment.CENTER).horizontalSizing(Sizing.fill(100));

        currDimCheckBox.checked(DataStorage.config.onlyShowCurrentDimensionRegions);
        currDimCheckBox.tooltip(Text.translatable("tondoas-regions.tooltip.only_show_current_dimension_regions"));
        currDimCheckBox.onChanged(b -> DataStorage.config.onlyShowCurrentDimensionRegions = b);

        coloredBiomesCheckBox.checked(DataStorage.config.coloredBiomes);
        coloredBiomesCheckBox.tooltip(Text.translatable("tondoas-regions.tooltip.colored-biomes"));
        coloredBiomesCheckBox.onChanged(b -> DataStorage.config.coloredBiomes = b);

        roundCoordinatesCheckBox.checked(DataStorage.config.roundCoordinates);
        roundCoordinatesCheckBox.tooltip(Text.translatable("tondoas-regions.tooltip.round"));
        roundCoordinatesCheckBox.onChanged(b -> DataStorage.config.roundCoordinates = b);

        footerContainer.horizontalAlignment(HorizontalAlignment.RIGHT);
        footerContainer.child(closeButton);


        this.child(label)
                .child(currDimCheckBox)
                .child(coloredBiomesCheckBox)
                .child(roundCoordinatesCheckBox)
                .child(footerContainer)
                .gap(2)
                .surface(Surface.DARK_PANEL)
                .padding(Insets.of(8));
    }


}
