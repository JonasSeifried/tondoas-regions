package tondoa.regions.gui.components;

import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.TextBoxComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import tondoa.regions.data_storage.DataStorage;
import tondoa.regions.data_storage.TRegion;

import java.util.Objects;
import java.util.stream.Stream;

public class RegionListViewComponent extends FlowLayout {

    public TextBoxComponent searchTextBox = Components.textBox(Sizing.fill(40));
    public ButtonComponent createRegionButton = Components.button(Text.translatable("tondoas-regions.new"), b -> createAddRegionModal());
    TextBoxModalComponent addRegionModal = new TextBoxModalComponent();
    public FlowLayout tRegionComponentContainer = Containers.verticalFlow(Sizing.content(), Sizing.content());

    public RegionListViewComponent() {
        this(Sizing.fill(70), Sizing.content(), Algorithm.VERTICAL);
    }

    public RegionListViewComponent(Sizing horizontalSizing, Sizing verticalSizing, Algorithm algorithm) {
        super(horizontalSizing, verticalSizing, algorithm);

        FlowLayout container = Containers.horizontalFlow(Sizing.fill(100), Sizing.fixed(20));
        searchTextBox.setPlaceholder(Text.translatable("tondoas-regions.search_placeholder"));
        searchTextBox.onChanged().subscribe(s -> updateTRegionComponentContainer());
        searchTextBox.verticalSizing(Sizing.fill(100));
        createRegionButton.tooltip(Text.translatable("tondoas-regions.tooltip.add_regions"));
        createRegionButton.verticalSizing(Sizing.fill(100));
        container.child(searchTextBox)
                .child(createRegionButton).alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
        this.child(container);
        this.child(Containers.verticalScroll(Sizing.content(), Sizing.fill(50), tRegionComponentContainer));

        updateTRegionComponentContainer();
        this.horizontalAlignment(HorizontalAlignment.CENTER);
        this.surface(Surface.VANILLA_TRANSLUCENT);
        this.gap(2);
        this.padding(Insets.of(2));
    }

    public void updateTRegionComponentContainer() {
        String searchKey = searchTextBox.getText().toLowerCase();
        tRegionComponentContainer.clearChildren();
        Stream<TRegion> regionsStream = DataStorage.sortedRegions();
        if (DataStorage.config.onlyShowCurrentDimensionRegions) {
            assert MinecraftClient.getInstance().player != null;
            Identifier currWorldIdentifier = MinecraftClient.getInstance().player.getWorld().getRegistryKey().getValue();
            regionsStream = regionsStream.filter(t -> t.worldIdentifier.equals(currWorldIdentifier));
        }
        regionsStream.filter(t -> t.name.toLowerCase().contains(searchKey) ||
                        t.getTranslatedBiome().getString().toLowerCase().contains(searchKey) ||
                        t.worldIdentifier.getPath().contains(searchKey))
                .forEach(t -> tRegionComponentContainer.child(new TRegionComponent(t, this)));
    }

    public void createAddRegionModal() {
        addRegionModal.margins(Insets.of(20));
        addRegionModal.surface(Surface.DARK_PANEL);
        addRegionModal.padding(Insets.of(5));
        addRegionModal.textBox.setText("");
        addRegionModal.textBox.setPlaceholder(Text.translatable("tondoas-regions.placeholder_name"));
        addRegionModal.label.text(Text.translatable("tondoas-regions.add_region"));
        addRegionModal.label.color(Color.WHITE);
        addRegionModal.acceptButton.setMessage(Text.translatable("tondoas-regions.add"));
        addRegionModal.acceptButton.onPress(b -> handleAddRegion(addRegionModal.textBox.getText()));
        addRegionModal.cancelButton.onPress(b -> updateTRegionComponentContainer());
        Vec3d coords = Objects.requireNonNull(MinecraftClient.getInstance().player).getPos();
        addRegionModal.coordinateComponent.xTextBox.text(String.format("%.2f", coords.x));
        addRegionModal.coordinateComponent.yTextBox.text(String.format("%.2f", coords.y));
        addRegionModal.coordinateComponent.zTextBox.text(String.format("%.2f", coords.z));


        tRegionComponentContainer.clearChildren();
        tRegionComponentContainer.child(0, addRegionModal);
    }

    private void handleAddRegion(String name) {
        if (isEmptyOrContainsKey(name, addRegionModal)) return;
        if (validateCoordinateTextBoxes(addRegionModal)) return;
        double x = parseDouble(addRegionModal.coordinateComponent.xTextBox.getText());
        double y = parseDouble(addRegionModal.coordinateComponent.yTextBox.getText());
        double z = parseDouble(addRegionModal.coordinateComponent.zTextBox.getText());
        DataStorage.addTRegion(name, new Vec3d(x, y, z));
        updateTRegionComponentContainer();
    }

    public static boolean isEmptyOrContainsKey(String name, TextBoxModalComponent addRegionModal) {
        if (name.isEmpty()) {
            addRegionModal.label.text(Text.translatable("tondoas-regions.no_empty_name"));
            addRegionModal.label.color(Color.RED);
            return true;
        }
        if (DataStorage.regions.containsKey(name)) {
            addRegionModal.label.text(Text.translatable("tondoas-regions.name_taken"));
            addRegionModal.label.color(Color.RED);
            return true;
        }
        return false;
    }

    public static boolean validateCoordinateTextBoxes(TextBoxModalComponent modal) {
        CoordinateComponent coordinateComponent = modal.coordinateComponent;
        if (isNotDouble(coordinateComponent.xTextBox.getText())) {
            modal.label.text(Text.translatable("tondoas-regions.not_a_number", "x"));
            modal.label.color(Color.RED);
            return true;
        }
        if (isNotDouble(coordinateComponent.yTextBox.getText())) {
            modal.label.text(Text.translatable("tondoas-regions.not_a_number", "y"));
            modal.label.color(Color.RED);
            return true;
        }
        if (isNotDouble(coordinateComponent.zTextBox.getText())) {
            modal.label.text(Text.translatable("tondoas-regions.not_a_number", "z"));
            modal.label.color(Color.RED);
            return true;
        }
        return false;
    }

    public static boolean isNotDouble(String s) {
        try {
            Double.parseDouble(s.replace(",", ".").replace(" ", ""));
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    public static double parseDouble(String s) {
        try {
            return Double.parseDouble(s.replace(",", ".").replace(" ", ""));

        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
