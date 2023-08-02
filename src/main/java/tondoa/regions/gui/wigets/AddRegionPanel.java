package tondoa.regions.gui.wigets;

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
import tondoa.regions.gui.RegionGui;
import tondoa.regions.gui.RegionScreen;

public class AddRegionPanel extends InputPanel {


    public AddRegionPanel() {
        label.setText(Text.translatable("tondoas-regions.gui.add_region"));

        textField.setSuggestion(Text.translatable("tondoas-regions.gui.placeholder_name"));

        acceptButton.setLabel(Text.translatable("tondoas-regions.add"));
        acceptButton.setOnClick(this::handleAdd);
    }

    private void handleAdd() {

        String name = textField.getText();

        if (name.isEmpty()) {
            addErrorLabel(Text.translatable("tondoas-regions.no_empty_name"));
            return;
        }
        if(DataStorage.regions.containsKey(name)) {
            addErrorLabel(Text.translatable("tondoas-regions.name_taken"));
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

        //Close after handling

        client.setScreen(new RegionScreen(new RegionGui()));


    }
}
