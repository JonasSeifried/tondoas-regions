package tondoa.regions.gui.components;

import io.wispforest.owo.ui.component.Components;
import io.wispforest.owo.ui.component.TextBoxComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.HorizontalAlignment;
import io.wispforest.owo.ui.core.Sizing;

public class CoordinateComponent extends FlowLayout {
    public TextBoxComponent xTextBox = Components.textBox(Sizing.fill(32));
    public TextBoxComponent yTextBox = Components.textBox(Sizing.fill(32));
    public TextBoxComponent zTextBox = Components.textBox(Sizing.fill(32));

    public CoordinateComponent() {
        this(Sizing.fill(100), Sizing.content(), Algorithm.HORIZONTAL);
    }

    protected CoordinateComponent(Sizing horizontalSizing, Sizing verticalSizing, Algorithm algorithm) {
        super(horizontalSizing, verticalSizing, algorithm);

        this.child(xTextBox).child(yTextBox).child(zTextBox).horizontalAlignment(HorizontalAlignment.CENTER);
    }
}
