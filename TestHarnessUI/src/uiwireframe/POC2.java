/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uiwireframe;

/**
 *
 * @author Aishu
 */
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.datatransfer.DataFlavor;
/*  w  w w.  j a va 2s.  com*/
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

public class POC2 extends JPanel {
  JTree tree;
  DefaultTreeModel treeModel;

  public static void main(String[] args) {
    JFrame f = new JFrame();
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    POC2 newContentPane = new POC2();
    newContentPane.setOpaque(true);
    f.setContentPane(newContentPane);

    f.pack();
    f.setVisible(true);
  }

  public POC2() {
    setLayout(new GridLayout(1, 3));
    tree = new JTree(getTreeModel());
    tree.setDragEnabled(true);
    tree.setPreferredSize(new Dimension(200, 400));
    JScrollPane scroll = new JScrollPane();
    scroll.setViewportView(tree);

    treeModel = getTreeModel();
    JTree secondTree = new JTree(treeModel);
    secondTree.setPreferredSize(new Dimension(200, 400));
    secondTree.setTransferHandler(new TransferHandler() {
      @Override
      public boolean importData(TransferSupport support) {
        if (!canImport(support)) {
          return false;
        }
        JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
        TreePath path = dl.getPath();
        int childIndex = dl.getChildIndex();

        String data;
        try {
          data = (String) support.getTransferable().getTransferData(
              DataFlavor.stringFlavor);
        } catch (Exception e) {
          return false;
        }
        if (childIndex == -1) {
          childIndex = tree.getModel().getChildCount(
              path.getLastPathComponent());
        }

        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(data);
        DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) path
            .getLastPathComponent();
        treeModel.insertNodeInto(newNode, parentNode, childIndex);

        tree.makeVisible(path.pathByAddingChild(newNode));
        tree.scrollRectToVisible(tree.getPathBounds(path
            .pathByAddingChild(newNode)));
        return true;
      }

      public boolean canImport(TransferSupport support) {
        if (!support.isDrop()) {
          return false;
        }
        support.setShowDropLocation(true);
        if (!support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
          System.out.println("only string is supported");
          return false;
        }
        JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
        TreePath path = dl.getPath();
        if (path == null) {
          return false;
        }
        return true;
      }
    });
    JScrollPane secondScroll = new JScrollPane();
    secondScroll.setViewportView(secondTree);

    JPanel topPanel = new JPanel(new BorderLayout());
    topPanel.add(scroll, BorderLayout.CENTER);
    JPanel btmPanel = new JPanel(new BorderLayout());
    btmPanel.add(secondScroll, BorderLayout.CENTER);

    add(topPanel);
    add(btmPanel);
  }
  private static DefaultTreeModel getTreeModel() {
    MutableTreeNode root = new DefaultMutableTreeNode("1");

    DefaultMutableTreeNode cover = new DefaultMutableTreeNode("A");
    cover.insert(new DefaultMutableTreeNode("P"), 0);
    cover.insert(new DefaultMutableTreeNode("S"), 0);
    root.insert(cover, 0);

    DefaultMutableTreeNode base = new DefaultMutableTreeNode("B");
    base.insert(new DefaultMutableTreeNode("S"), 0);
    base.insert(new DefaultMutableTreeNode("H"), 0);
    root.insert(base, 0);

    DefaultTreeModel model = new DefaultTreeModel(root);
    return model;
  }
}