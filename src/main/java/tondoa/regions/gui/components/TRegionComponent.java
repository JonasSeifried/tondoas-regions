package tondoa.regions.gui.components;

import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import tondoa.regions.data_storage.DataStorage;
import tondoa.regions.data_storage.TRegion;

import java.util.Objects;

public class TRegionComponent extends FlowLayout {

    TRegion tRegion;
    RegionListViewComponent regionListViewComponent;
    ButtonComponent deleteButton = Components.button(Text.literal("X"), b -> handleDelete());
    ButtonComponent editButton = Components.button(Text.literal(".."), b -> createEditModal());
    LabelComponent nameLabel;
    LabelComponent coordsLabel;
    LabelComponent biomeLabel = Components.label(Text.translatable("tondoas-regions.unknown"));
    TextBoxModalComponent editModal = new TextBoxModalComponent();
    boolean coordsChanged = false;
    boolean nameChanged = false;

    public TRegionComponent(TRegion tRegion, RegionListViewComponent regionListViewComponent) {
        this(tRegion, regionListViewComponent, Sizing.fill(100), Sizing.content(), Algorithm.HORIZONTAL);
    }

    public TRegionComponent(TRegion tRegion, RegionListViewComponent regionListViewComponent, Sizing horizontalSizing, Sizing verticalSizing, Algorithm algorithm) {
        super(horizontalSizing, verticalSizing, algorithm);
        this.tRegion = tRegion;
        this.regionListViewComponent = regionListViewComponent;
        deleteButton.tooltip(Text.translatable("tondoas-regions.delete")).horizontalSizing(Sizing.content());
        editButton.tooltip(Text.translatable("tondoas-regions.rename")).horizontalSizing(Sizing.content());

        nameLabel = Components.label(Text.literal(tRegion.name));

        Vec3d playerCoords = Objects.requireNonNull(MinecraftClient.getInstance().player).getPos();
        double distance = Math.sqrt(Math.pow(playerCoords.x - tRegion.x, 2) + Math.pow(playerCoords.z - tRegion.z, 2));

        String format = DataStorage.config.roundCoordinates ? "%.0f %.0f %.0f" : "%.2f %.2f %.2f";
        if (DataStorage.config.showDistanceInGui)
            format += " (%.0fm)";

        coordsLabel = Components.label(Text.literal(String.format(format, tRegion.x, tRegion.y, tRegion.z, distance)));
        coordsLabel.horizontalTextAlignment(HorizontalAlignment.CENTER);
        biomeLabel.color(Color.ofFormatting(Formatting.DARK_GRAY));
        if (tRegion.biomeUnknown) {
            tRegion = DataStorage.updateIfInRange(tRegion);
        }

        if (!tRegion.biomeUnknown) {
            biomeLabel.text(tRegion.getTranslatedBiome());
            colorBiomeLabel();
        }

        this.child(nameLabel.verticalTextAlignment(VerticalAlignment.CENTER).horizontalSizing(Sizing.fill(20)))
                .child(coordsLabel.horizontalSizing(Sizing.fill(45)))
                .child(biomeLabel.horizontalSizing(Sizing.fill(25)))
                .child(deleteButton)
                .child(editButton).verticalAlignment(VerticalAlignment.CENTER)
                .surface(Surface.DARK_PANEL).padding(Insets.of(8)).margins(Insets.bottom(2));
    }

    private void colorBiomeLabel() {
        if (DataStorage.config.coloredBiomes) {
            if (tRegion.worldIdentifier.equals(World.OVERWORLD.getValue()))
                biomeLabel.color(Color.ofRgb(0x228B22));
            else if (tRegion.worldIdentifier.equals(World.NETHER.getValue()))
                biomeLabel.color(Color.ofRgb(0x960018));
            else if (tRegion.worldIdentifier.equals(World.END.getValue()))
                biomeLabel.color(Color.ofRgb(0x6F00FF));
        }
    }

    public void handleDelete() {
        DataStorage.regions.remove(tRegion.name);
        regionListViewComponent.tRegionComponentContainer.removeChild(this);
    }

    public void createEditModal() {
        editModal.label.text(Text.translatable("tondoas-regions.edit"));
        editModal.label.color(Color.WHITE);
        editModal.acceptButton.setMessage(Text.translatable("tondoas-regions.accept"));
        editModal.acceptButton.onPress(b -> handleEdit(editModal.textBox.getText()));
        editModal.coordinateComponent.xTextBox.text(String.format("%.2f", tRegion.x));
        editModal.coordinateComponent.yTextBox.text(String.format("%.2f", tRegion.y));
        editModal.coordinateComponent.zTextBox.text(String.format("%.2f", tRegion.z));
        editModal.coordinateComponent.xTextBox.onChanged().subscribe((s) -> coordsChanged = true);
        editModal.coordinateComponent.yTextBox.onChanged().subscribe((s) -> coordsChanged = true);
        editModal.coordinateComponent.zTextBox.onChanged().subscribe((s) -> coordsChanged = true);
        editModal.textBox.text(tRegion.name);
        editModal.textBox.onChanged().subscribe((s) -> nameChanged = true);
        editModal.horizontalSizing(Sizing.fill(100));
        this.child(0, editModal);
    }

    private void handleEdit(String newName) {
        //nothing changed
        if (!nameChanged && !coordsChanged) {
            regionListViewComponent.updateTRegionComponentContainer();
            return;
        }
        //check if name is available
        if (nameChanged && RegionListViewComponent.isEmptyOrContainsKey(newName, editModal))
            return;

        if (coordsChanged) {
            if (RegionListViewComponent.validateCoordinateTextBoxes(editModal))
                return;
            double x = RegionListViewComponent.parseDouble(editModal.coordinateComponent.xTextBox.getText());
            double y = RegionListViewComponent.parseDouble(editModal.coordinateComponent.yTextBox.getText());
            double z = RegionListViewComponent.parseDouble(editModal.coordinateComponent.zTextBox.getText());
            tRegion = DataStorage.editTRegion(tRegion.name, newName, new Vec3d(x, y, z));
        } else
            tRegion = DataStorage.editTRegion(tRegion.name, newName, null);
        regionListViewComponent.updateTRegionComponentContainer();
    }
}
