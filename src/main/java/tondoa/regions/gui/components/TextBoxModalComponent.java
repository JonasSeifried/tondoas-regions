package tondoa.regions.gui.components;

import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.LabelComponent;
import io.wispforest.owo.ui.component.TextBoxComponent;
import io.wispforest.owo.ui.container.Containers;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.HorizontalAlignment;
import io.wispforest.owo.ui.core.Sizing;
import io.wispforest.owo.ui.core.VerticalAlignment;
import net.minecraft.text.Text;

public class TextBoxModalComponent extends FlowLayout{

    public LabelComponent label = Components.label(Text.literal("Label"));

    public FlowLayout horizontalContainer = Containers.horizontalFlow(Sizing.content(), Sizing.content());
    public TextBoxComponent textBox = Components.textBox(Sizing.fill(30));
    public ButtonComponent acceptButton = Components.button(Text.literal("accept"), b -> {});
    public ButtonComponent cancelButton = Components.button(Text.translatable("tondoas-regions.cancel"), b -> closeModal());


    public TextBoxModalComponent() {
        this(Sizing.content(), Sizing.content(), Algorithm.VERTICAL);
    }

    public TextBoxModalComponent(Sizing horizontalSizing, Sizing verticalSizing, Algorithm algorithm) {
        super(horizontalSizing, verticalSizing, algorithm);

        horizontalContainer
                .child(acceptButton)
                .child(cancelButton);
        this
                .child(label)
                .child(textBox)
                .child(horizontalContainer)
                .alignment(HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
    }

    private void closeModal() {
        assert this.parent() != null;
        this.parent().removeChild(this);
    }
}
