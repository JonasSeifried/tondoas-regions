package tondoa.regions.gui.wigets;

import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import net.minecraft.text.Text;
import tondoa.regions.data_storage.DataStorage;
import tondoa.regions.data_storage.TRegion;

import java.util.Objects;

public class RenamePanel extends WPlainPanel {

    WTextField renameTextField;
    TRegion tRegion;

    WLabel renameLabel;
    public RenamePanel(TRegion tRegion) {
        this.tRegion = tRegion;
        renameLabel = new WLabel(Text.translatable("tondoas-regions.gui.rename"), 0xFFFFFF);
        renameLabel.setHorizontalAlignment(HorizontalAlignment.CENTER);
        this.add(renameLabel, 0, 0, 18*4, 18);
        renameTextField = new WTextField(Text.translatable("tondoas-regions.gui.search_placeholder"));
        this.add(renameTextField,0,18, 18*4, 18);
        WButton renameButton = new WButton(Text.translatable("tondoas-regions.gui.rename"));
        renameButton.setOnClick(this::handleRename);
        this.add(renameButton, 0, 18*2, 18*2, 18);
        WButton cancelButton = new WButton(Text.translatable("tondoas-regions.cancel"));
        cancelButton.setOnClick(() -> Objects.requireNonNull(this.getParent()).remove(this));
        this.add(cancelButton, 18*2, 18*2, 18*2, 18);
        this.setSize(18*4, 18*3);
    }

    private void handleRename() {
        String newName = renameTextField.getText();

        if (newName.isEmpty()) {
            WLabel errorLabel = new WLabel(Text.translatable("tondoas-regions.no_empty_name"), 0xFF0000);
            this.remove(renameLabel);
            this.add(errorLabel, 0,0, getWidth(), 18);
            return;
        }
        if(DataStorage.regions.containsKey(newName)) {
            WLabel errorLabel = new WLabel(Text.translatable("tondoas-regions.name_already_used", newName), 0xFF0000);
            this.remove(renameLabel);
            this.add(errorLabel, 0,0, getWidth(), 18);
            return;
        }

        DataStorage.regions.put(newName, new TRegion(tRegion, newName));
        DataStorage.regions.remove(tRegion.name);

        //Close after handling
        Objects.requireNonNull(getParent()).remove(this);


    }
}
