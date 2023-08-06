package tondoa.regions.gui.components;

import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.TextBoxComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.biome.Biome;
import tondoa.regions.data_storage.DataStorage;
import tondoa.regions.data_storage.TRegion;

public class RegionListViewComponent extends FlowLayout {

    public TextBoxComponent searchTextBox = Components.textBox(Sizing.fill(40));

    public ButtonComponent createRegionButton = Components.button(Text.translatable("tondoas-regions.new"), b -> createAddRegionModal());



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
        createRegionButton.verticalSizing(Sizing.fill(100));
        container
                .child(searchTextBox)
                .child(createRegionButton)
                .alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
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
        DataStorage.sortedRegions()
                .filter(t ->  t.name.toLowerCase().startsWith(searchKey) || t.getTranslatedBiome().getString().toLowerCase().startsWith(searchKey))
                .forEach(t -> tRegionComponentContainer.child(new TRegionComponent(t, this)));
    }

    public void createAddRegionModal() {
        TextBoxModalComponent addRegionModal = new TextBoxModalComponent();
        addRegionModal.margins(Insets.of(20));
        addRegionModal.surface(Surface.DARK_PANEL);
        addRegionModal.padding(Insets.of(5));
        addRegionModal.label.text(Text.translatable("tondoas-regions.add_region"));
        addRegionModal.acceptButton.setMessage(Text.translatable("tondoas-regions.add"));
        addRegionModal.acceptButton.onPress(b -> handleAddRegion(addRegionModal.textBox.getText()));
        addRegionModal.cancelButton.onPress(b -> updateTRegionComponentContainer());
        tRegionComponentContainer.clearChildren();
        tRegionComponentContainer.child(0, addRegionModal);

    }

    private void handleAddRegion(String name) {
        if(name.isEmpty()) {

            return;
        }
        if(DataStorage.regions.containsKey(name)) {

            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        assert player != null;

        Vec3d coords = player.getPos();

        RegistryEntry<Biome> biomeRegistry = player.getWorld().getBiome(BlockPos.ofFloored(coords));
        Identifier biomeIdentifier = biomeRegistry.getKey().orElseThrow().getValue();
        TRegion tRegion = new TRegion(coords, biomeIdentifier, name);
        DataStorage.regions.put(name, tRegion);

        updateTRegionComponentContainer();
    }
}
