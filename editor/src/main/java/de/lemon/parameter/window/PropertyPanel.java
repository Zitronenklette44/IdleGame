package de.lemon.parameter.window;

import com.badlogic.gdx.graphics.Color;
import de.lemon.editor.Logic;
import de.lemon.parameter.EditorNode;

import javax.swing.*;
import java.awt.*;

public class PropertyPanel extends JPanel {

    private final JLabel title;
    private final JLabel description;

    private JComponent valueEditor;
    private JPanel wrapper;

    public PropertyPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        title = new JLabel("");
        description = new JLabel("");

        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        description.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(Box.createVerticalStrut(10));
        add(title);
        add(Box.createVerticalStrut(5));
        add(description);
        add(Box.createVerticalStrut(20));
    }

    public void load(EditorNode node) {
        title.setText(node.getDisplayName());
        description.setText(node.getDescription());

        if (valueEditor != null) {
            remove(wrapper);
        }

        setValueEditor(node);

        if (valueEditor != null) {
            valueEditor.setAlignmentX(Component.LEFT_ALIGNMENT);
            wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
            wrapper.add(valueEditor);

            wrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

            add(wrapper);
        }

        revalidate();
        repaint();
    }

    private void setValueEditor(EditorNode node) {
        SpinnerModel model;
        EditorNode.ValueType type = node.getValueType();

        switch (type) {
            case FLOAT:
                model = new SpinnerNumberModel(
                    (Number) node.getDefaultValue(),
                    node.getMin(),
                    node.getMax(),
                    0.1f
                );
                valueEditor = new JSpinner(model);
                ((JSpinner) valueEditor).addChangeListener(e -> {
                    onValueChange(node, ((JSpinner) valueEditor).getValue());
                });
                break;

            case INTEGER:
                model = new SpinnerNumberModel(
                    ((Number) node.getDefaultValue()).intValue(),
                    node.getMin().intValue(),
                    node.getMax().intValue(),
                    1
                );
                valueEditor = new JSpinner(model);
                ((JSpinner) valueEditor).addChangeListener(e -> {
                    onValueChange(node, ((JSpinner) valueEditor).getValue());
                });
                break;

            case BOOLEAN:
                JCheckBox checkBox = new JCheckBox();
                checkBox.setSelected((Boolean) node.getDefaultValue());
                valueEditor = checkBox;
                ((JCheckBox) valueEditor).addActionListener(e -> {
                    boolean value = ((JCheckBox) valueEditor).isSelected();
                    onValueChange(node, value);
                });
                break;

            case COLOR:
                JButton colorButton = new JButton("Choose Color");

                java.awt.Color[] currentColor = {
                    node.getDefaultValue() instanceof Color ? toAwtColor((Color) node.getDefaultValue()) : null
                };

                colorButton.setBackground(currentColor[0]);

                colorButton.addActionListener(e -> {
                    java.awt.Color chosen = JColorChooser.showDialog(
                        this,
                        "Pick a Color",
                        currentColor[0]
                    );

                    if (chosen != null) {
//                        System.out.println("old Color:" + currentColor[0] + " new Color:" + chosen);
                        currentColor[0] = chosen;
                        colorButton.setBackground(chosen);
                        onValueChange(node, toGdxColor(chosen));
                    }
                });

                valueEditor = colorButton;
                break;

            case ENUM:
                JComboBox<Enum<?>> comboBox =
                    new JComboBox<>(node.getEnumValues());

                if (node.getDefaultValue() != null) {
                    comboBox.setSelectedItem(node.getDefaultValue());
                }

                valueEditor = comboBox;
                //noinspection unchecked
                ((JComboBox<Enum<?>>)valueEditor).addActionListener(e -> {
                Enum<?> selected = (Enum<?>) comboBox.getSelectedItem();
                onValueChange(node, selected);
            });
                break;
        }
        valueEditor.setPreferredSize(new Dimension((int) (getWidth() * 0.5), 20));
    }

    private java.awt.Color toAwtColor(com.badlogic.gdx.graphics.Color c) {
        return new java.awt.Color(c.r, c.g, c.b, c.a);
    }

    private Color toGdxColor(java.awt.Color c){
        return new Color(
            c.getRed() / 255f,
            c.getGreen() / 255f,
            c.getBlue() / 255f,
            c.getAlpha() / 255f
        );
    }

    private void onValueChange(EditorNode node, Object value){
        node.setDefaultValue(value);
        Logic._instance.changeValue(node, value);
        System.out.println("Node: " + node + " -> " + value);
    }
}
