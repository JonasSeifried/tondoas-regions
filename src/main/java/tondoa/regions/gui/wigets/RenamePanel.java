package tondoa.regions.gui.wigets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import tondoa.regions.data_storage.DataStorage;
import tondoa.regions.data_storage.TRegion;
import tondoa.regions.gui.RegionGui;
import tondoa.regions.gui.RegionScreen;

public class RenamePanel extends InputPanel {

    TRegion tRegion;

    public RenamePanel(TRegion tRegion) {
        this.tRegion = tRegion;

        label.setText(Text.translatable("tondoas-regions.gui.rename"));

        textField.setSuggestion(Text.translatable("tondoas-regions.gui.search_placeholder"));
        acceptButton.setLabel(Text.translatable("tondoas-regions.gui.rename"));
        acceptButton.setOnClick(this::handleRename);

        cancelButton.setLabel(Text.translatable("tondoas-regions.cancel"));

    }

    private void handleRename() {
        String newName = textField.getText();

        if (newName.isEmpty()) {
            addErrorLabel(Text.translatable("tondoas-regions.no_empty_name"));
            return;
        }
        if(DataStorage.regions.containsKey(newName)) {
            addErrorLabel(Text.translatable("tondoas-regions.name_taken"));
            return;
        }

        DataStorage.regions.put(newName, new TRegion(tRegion, newName));
        DataStorage.regions.remove(tRegion.name);

        MinecraftClient.getInstance().setScreen(new RegionScreen(new RegionGui()));


    }

}
