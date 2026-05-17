package de.lemon.parameter.window;
import com.badlogic.gdx.math.Vector2;
import de.lemon.components.PreviewFrame;
import de.lemon.editor.Logic;
import de.lemon.editor.ParticleStartScreen;
import de.lemon.mechanics.particleSystem.GeneratorSettings;
import de.lemon.mechanics.particleSystem.ParticleManager;
import de.lemon.mechanics.particleSystem.ParticleSource;
import de.lemon.mechanics.particleSystem.sources.GeometricParticleSource;
import de.lemon.mechanics.particleSystem.sources.MovingParticleSource;
import de.lemon.mechanics.particleSystem.sources.StaticParticleSource;
import de.lemon.parameter.EditorNode;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.EnumSet;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;

public class Window extends JFrame {
    private final PropertyPanel panel;
    private static Window frame;

    public static void start() {
        EventQueue.invokeLater(() -> {
            try {
                frame = new Window();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static void stop(){
        frame.dispose();
    }

    public Window() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(50, 100, 600, 700);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                Logic._instance.end();
            }
        });

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu mnNewMenu = new JMenu("Datei");
        menuBar.add(mnNewMenu);

        JMenuItem mntmNewMenuItem = new JMenuItem("Neu");
        mntmNewMenuItem.addActionListener(e -> Logic._instance.btnNew());
        mnNewMenu.add(mntmNewMenuItem);

        JMenuItem mntmNewMenuItem_2 = new JMenuItem("Partikel laden");
        mntmNewMenuItem_2.addActionListener(e -> Logic._instance.btnLoad());
        mnNewMenu.add(mntmNewMenuItem_2);

        JSeparator separator = new JSeparator();
        mnNewMenu.add(separator);

        JMenuItem mntmNewMenuItem_1 = new JMenuItem("Code erstellen");
        mntmNewMenuItem_1.addActionListener(e -> Logic._instance.btnCreateCode());
        mnNewMenu.add(mntmNewMenuItem_1);

        JMenu source = new JMenu("Source");
        menuBar.add(source);

        JMenuItem item = new JMenuItem("Static Source");
        item.addActionListener( e -> changeSourceType(PreviewFrame.SourceType.STATIC));
        source.add(item);

        JMenuItem item1 = new JMenuItem("Moving Source");
        item1.addActionListener( e -> changeSourceType(PreviewFrame.SourceType.MOVING));
        source.add(item1);

        JMenuItem item2 = new JMenuItem("Geometric Source");
        item2.addActionListener( e -> changeSourceType(PreviewFrame.SourceType.GEOMETRIC));
        source.add(item2);

        source.add(new JSeparator());

        JMenu sourceSettings = new JMenu("Settings");
        source.add(sourceSettings);

        JLabel test = new JLabel("Geometric Source:");
        sourceSettings.add(test);
        sourceSettings.add(new JSeparator());

        JMenuItem item3 = new JMenuItem("Direction");
        sourceSettings.add(item3);

        JMenuItem item4 = new JMenuItem("Geometric");
        sourceSettings.add(item4);

        JMenuItem item5 = new JMenuItem("Rotation");
        sourceSettings.add(item5);


//        JMenuItem mntmNewMenuItem_6 = new JMenuItem("Code einfügen");
//        mnNewMenu.add(mntmNewMenuItem_6);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());

        JSplitPane splitPane = new JSplitPane();
        contentPane.add(splitPane, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane();
        splitPane.setRightComponent(scrollPane);

        panel = new PropertyPanel();
        scrollPane.setViewportView(panel);

        JScrollPane scrollPane_1 = new JScrollPane();
        splitPane.setLeftComponent(scrollPane_1);

        JTree tree = new JTree();
        tree.addTreeSelectionListener(e -> {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            if (node == null) return;
            if (!node.isLeaf()) return;
            Object userObject = node.getUserObject();
            if (userObject instanceof EditorNode) {
                EditorNode editorNode = (EditorNode) userObject;
                panel.load(editorNode);
            }
        });
        scrollPane_1.setViewportView(tree);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Editor");

        DefaultMutableTreeNode particle = new DefaultMutableTreeNode("Particle");
        EnumSet<EditorNode> nodes = EditorNode.getParticleEditorNodes();
        for (EditorNode editorNode : nodes) {
            particle.add(new DefaultMutableTreeNode(editorNode));
        }
        root.add(particle);

        DefaultMutableTreeNode generation = new DefaultMutableTreeNode("Generation");

        DefaultMutableTreeNode spawn = new DefaultMutableTreeNode("Spawn");
        nodes = EditorNode.getSpawnEditorNodes();
        for (EditorNode editorNode : nodes) {
            spawn.add(new DefaultMutableTreeNode(editorNode));
        }
        generation.add(spawn);

        nodes = EditorNode.getGenerationEditorNodes();
        for (EditorNode editorNode : nodes) {
            generation.add(new DefaultMutableTreeNode(editorNode));
        }

        DefaultMutableTreeNode burst = new DefaultMutableTreeNode("Burst");
        nodes = EditorNode.getBurstEditorNodes();
        for (EditorNode editorNode : nodes) {
            burst.add(new DefaultMutableTreeNode(editorNode));
        }
        generation.add(burst);

        root.add(generation);

        DefaultMutableTreeNode others = new DefaultMutableTreeNode("Source");
        nodes = EditorNode.getOtherEditorNodes();
        for (EditorNode editorNode : nodes) {
            others.add(new DefaultMutableTreeNode(editorNode));
        }
        root.add(others);

        tree.setModel(new DefaultTreeModel(root));
        splitPane.setDividerLocation(200);

    }

    void changeSourceType(PreviewFrame.SourceType type) {
        ParticleSource source = ParticleStartScreen._instance.source;
        ParticleStartScreen._instance.source.dispose();
//        System.out.println("sourceID: " + source + " called changeSource to: " + type);
        Vector2 pos = source.getPos();
        Vector2 relPos = source.getRelPos();
        Vector2 relSize = source.getRelSize();
        ParticleManager manager = ParticleStartScreen._instance.getParticleManager();
        GeneratorSettings settings = ParticleStartScreen._instance.particleSettings;
        switch (type) {
            case MOVING:
                source = new MovingParticleSource(pos, manager, settings);
                break;
            case GEOMETRIC:
                source = new GeometricParticleSource(manager, settings, ParticleStartScreen._instance.spawnArea);
                break;
            case STATIC:
                source = new StaticParticleSource(pos, manager, settings);
                break;
        }
        source.setRelLayout(relPos.x, relPos.y, relSize.x, relSize.y);
        ParticleStartScreen._instance.source = source;
//        System.out.println("New sourceID: " + source);
        ParticleStartScreen._instance.refreshLayout();
    }

}
