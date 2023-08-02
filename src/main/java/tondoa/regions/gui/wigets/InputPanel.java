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

import java.util.Objects;

public class InputPanel extends WPlainPanel {

    WTextField textField;

    WLabel label;

    WButton acceptButton;
    WButton cancelButton;

    public InputPanel() {
        this.setInsets(new Insets(3));

        label = new WLabel(Text.literal("label"));
        label.setHorizontalAlignment(HorizontalAlignment.CENTER);
        label.setVerticalAlignment(VerticalAlignment.CENTER);
        this.add(label, 0, 0, 18*4, 18);

        textField = new WTextField(Text.literal("placeholder"));
        this.add(textField,2,18, 18*4-4, 18);

        acceptButton = new WButton(Text.literal("Ok"));
        this.add(acceptButton, 0, 18*2, 18*2, 18);

        cancelButton = new WButton(Text.translatable("tondoas-regions.cancel"));
        cancelButton.setOnClick(() -> Objects.requireNonNull(this.getParent()).remove(this));
        this.add(cancelButton, 18*2, 18*2, 18*2, 18);

        this.setBackgroundPainter(BackgroundPainter.VANILLA);
        this.setSize(18*4, 18*3);
    }



    protected void addErrorLabel(Text error) {
        label.setText(error);
        label.setColor(0xFF0000);
        Objects.requireNonNull(this.getParent()).layout();
    }
}
