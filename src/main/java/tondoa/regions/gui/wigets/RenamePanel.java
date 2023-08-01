package tondoa.regions.gui.wigets;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WTextField;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import net.minecraft.text.Text;
import tondoa.regions.data_storage.DataStorage;
import tondoa.regions.data_storage.TRegion;

import java.util.Objects;

public class RenamePanel extends WPlainPanel {

    WTextField renameTextField;

    WLabel label;
    TRegion tRegion;

    public RenamePanel(TRegion tRegion) {
        this.tRegion = tRegion;
        this.setInsets(new Insets(3));

        label = new WLabel(Text.translatable("tondoas-regions.gui.rename"));
        label.setHorizontalAlignment(HorizontalAlignment.CENTER);
        label.setVerticalAlignment(VerticalAlignment.CENTER);
        this.add(label, 0, 0, 18*4, 18);

        renameTextField = new WTextField(Text.translatable("tondoas-regions.gui.search_placeholder"));
        this.add(renameTextField,2,18, 18*4-4, 18);

        WButton renameButton = new WButton(Text.translatable("tondoas-regions.gui.rename"));
        renameButton.setOnClick(this::handleRename);
        this.add(renameButton, 0, 18*2, 18*2, 18);

        WButton cancelButton = new WButton(Text.translatable("tondoas-regions.cancel"));
        cancelButton.setOnClick(() -> Objects.requireNonNull(this.getParent()).remove(this));
        this.add(cancelButton, 18*2, 18*2, 18*2, 18);

        this.setBackgroundPainter(BackgroundPainter.VANILLA);
        this.setSize(18*4, 18*3);
    }

    private void handleRename() {
        String newName = renameTextField.getText();

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

        //Close after handling
        Objects.requireNonNull(getParent()).remove(this);


    }

    private void addErrorLabel(Text error) {
        label.setText(error);
        label.setColor(0xFF0000);
        Objects.requireNonNull(this.getParent()).layout();
    }
}
