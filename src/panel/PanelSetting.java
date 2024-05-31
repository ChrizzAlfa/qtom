package panel;

import java.awt.Font;
import java.awt.Color;
import java.awt.Insets;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;

public class PanelSetting {
    
    // Setting up some default values
    int screenWidth = 720;
    int screenHeight = 500;
    Dimension screenDimension = new Dimension(screenWidth, screenHeight);
    Color screenColor = new Color(220, 95, 0);

    private Container container;

    public PanelSetting(Container container) {
        this.container = container;
    }

    public void addComponent(int gridx, int gridy, int gridwidth, java.awt.Component component, Font font, Color foreground, Insets insets) {
        GridBagConstraints gbc = createGBC(gridx, gridy, gridwidth, insets);

        if (insets != null) {
            gbc.insets = insets;
        }
        if (font != null) {
            component.setFont(font);
        }
        if (foreground != null) {
            component.setForeground(foreground);
        }
        container.add(component, gbc);
    }

    private GridBagConstraints createGBC(int gridx, int gridy, int gridwidth, Insets insets) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.gridwidth = gridwidth;
        gbc.insets = (insets != null) ? insets : new Insets(5, 0, 5, 0);
        return gbc;
    }
}