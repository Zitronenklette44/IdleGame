package de.lemon.parameter.window;
import de.lemon.editor.Logic;
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

        DefaultMutableTreeNode others = new DefaultMutableTreeNode("Others");
        nodes = EditorNode.getOtherEditorNodes();
        for (EditorNode editorNode : nodes) {
            others.add(new DefaultMutableTreeNode(editorNode));
        }
        root.add(others);

        tree.setModel(new DefaultTreeModel(root));
        splitPane.setDividerLocation(200);

    }

    void changeSourceType() {

    }

}
