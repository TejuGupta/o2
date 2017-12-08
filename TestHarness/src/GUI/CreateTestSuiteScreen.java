/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

import static com.sun.org.apache.bcel.internal.Repository.instanceOf;
import java.awt.datatransfer.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import static javax.swing.TransferHandler.COPY_OR_MOVE;
import static javax.swing.TransferHandler.MOVE;
import javax.swing.tree.*;
import javax.swing.ButtonGroup;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.datatransfer.DataFlavor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.JOptionPane;
import java.util.List;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map.Entry;
import static com.sun.org.apache.bcel.internal.Repository.instanceOf;
import java.io.InputStreamReader;

/**
 *
 * @author Aishu
 */
public class CreateTestSuiteScreen extends javax.swing.JFrame {

    private ArrayList<String> machines = new ArrayList<String>();
    private ArrayList<TestCase> testCases = new ArrayList<TestCase>();
    private static String hashValue;
    /**
     * Creates new form CreateTestSuiteScreen_new
     */
    public CreateTestSuiteScreen() {
    }
    
    public CreateTestSuiteScreen(ArrayList<String> machineList, String hashValue) throws IOException {
        initComponents();
        setName("Test Harness - Create test suite configuration");
        generateTestTree();
        groupButton();
        this.getContent();
        this.machines = machineList;
        this.hashValue = hashValue;
        addMachinesToList();
        
    }
    
    private void getContent(){
        jTree1.setDropMode(DropMode.ON_OR_INSERT);
        final DefaultTreeModel treeModel = (DefaultTreeModel) jTree1.getModel();
        jTree1.setTransferHandler(new TransferHandler() {
                            
                @Override
                public boolean importData(TransferHandler.TransferSupport support) {
                    if (!canImport(support)) {
                      return false;
                    }
                    JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
                    TreePath path = dl.getPath();
                    int childIndex = dl.getChildIndex();
                    
                    // from drag and drop poc
                    String mimeType = DataFlavor.javaJVMLocalObjectMimeType + ";class=\"" + javax.swing.tree.DefaultMutableTreeNode[].class.getName() + "\"";
                    DataFlavor nodesFlavor = null;
                    try {
                        nodesFlavor = new DataFlavor(mimeType);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(CreateTestSuiteScreen.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    DefaultMutableTreeNode[] nodes = null;
                    String data;
                    try {  
                      data = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
                    } catch (Exception e) {
                      return false;
                    }
                    
                    DefaultMutableTreeNode parentNode = (DefaultMutableTreeNode) path.getLastPathComponent();
                    int index = childIndex; 
                    if (childIndex == -1) {
                      childIndex = jTree1.getModel().getChildCount(path.getLastPathComponent());
                      index = parentNode.getChildCount();
                    }

                    DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(data);
                    treeModel.insertNodeInto(newNode, parentNode, childIndex);
                    
                    // from drag and drop poc
                    /*
                    for(int i = 0; i < nodes.length; i++){
                        treeModel.insertNodeInto(nodes[i], parentNode, index++);
                    }*/

                    //jTree1.makeVisible(path.pathByAddingChild(newNode));
                    //jTree1.scrollRectToVisible(jTree1.getPathBounds(path
                        //.pathByAddingChild(newNode)));
                    return true;
                }

                public boolean canImport(TransferHandler.TransferSupport support) {
                    if (!support.isDrop()) {
                      return false;
                    }
                    support.setShowDropLocation(true);
                    
                    // from drag and drop poc
                    String mimeType = DataFlavor.javaJVMLocalObjectMimeType + ";class=\"" + javax.swing.tree.DefaultMutableTreeNode[].class.getName() + "\"";
                    DataFlavor nodesFlavor = null;
                    try {
                        nodesFlavor = new DataFlavor(mimeType);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(CreateTestSuiteScreen.class.getName()).log(Level.SEVERE, null, ex);
                    }
                 
                    if (!support.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                      System.out.println("only string is supported");
                      return false;
                    }
                    JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
                    TreePath path = dl.getPath();
                    if (path == null) {
                      return false;
                    }
                    int action = support.getDropAction();
                    if(action == MOVE) {
                        return haveCompleteNode((JTree)support.getComponent());
                    }
                    return true;
                 }
                
                private boolean haveCompleteNode(JTree tree) {
                        int[] selRows = tree.getSelectionRows();
                        TreePath path = tree.getPathForRow(selRows[0]);
                        DefaultMutableTreeNode first =
                            (DefaultMutableTreeNode)path.getLastPathComponent();
                        int childCount = first.getChildCount();
                        // first has children and no children are selected.
                        if(childCount > 0 && selRows.length == 1)
                            return false;
                        // first may have children.
                        for(int i = 1; i < selRows.length; i++) {
                            path = tree.getPathForRow(selRows[i]);
                            DefaultMutableTreeNode next =
                                (DefaultMutableTreeNode)path.getLastPathComponent();
                            if(first.isNodeChild(next)) {
                                // Found a child of first.
                                if(childCount > selRows.length-1) {
                                    // Not all children of first are selected.
                                    return false;
                                }
                            }
                        }
                        return true;
                }
        });
        jTree1.getSelectionModel().setSelectionMode(
                TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
        expandTree(jTree1);
        //jScrollPane1.add(jTree1);
    }
    
    private void expandTree(JTree tree) {
        DefaultMutableTreeNode root =
            (DefaultMutableTreeNode)tree.getModel().getRoot();
        Enumeration e = root.breadthFirstEnumeration();
        while(e.hasMoreElements()) {
            DefaultMutableTreeNode node =
                (DefaultMutableTreeNode)e.nextElement();
            if(node.isLeaf()) continue;
            int row = tree.getRowForPath(new TreePath(node.getPath()));
            tree.expandRow(row);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTree2 = new javax.swing.JTree();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTree3 = new javax.swing.JTree();
        jButton2 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Test Harness - Create test suite");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Test Harness - Test Suite Creation and Configuration");

        jButton1.setText("Back");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel2.setText("Mode to run the test suite: ");

        jRadioButton1.setSelected(true);
        jRadioButton1.setText("Round Robin");
        jRadioButton1.setToolTipText("Click here to run the test cases in all available test machines randomly");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        jRadioButton2.setText("Selected Mode");
        jRadioButton2.setToolTipText("Click here to enable configuration of test machines for test execution");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        jLabel4.setText("Test Suite created so far ...");

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("New Test Suite");
        jTree1.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jScrollPane1.setViewportView(jTree1);

        jLabel6.setText("Machines to select from:");

        treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("Machines");
        jTree2.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jTree2.setDragEnabled(true);
        jTree2.setEnabled(false);
        jScrollPane2.setViewportView(jTree2);

        jLabel7.setText("Test cases to select from:");

        treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("TestCases");
        jTree3.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jTree3.setDragEnabled(true);
        jScrollPane3.setViewportView(jTree3);

        jButton2.setText("Remove selected machine/testcase");
        jButton2.setToolTipText("Click here to remove the selected rows");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton4.setText("Execute test suite");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel5.setText("Select the value of N for 'multiple' run : ");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(jLabel3)
                .addGap(3, 3, 3)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addGap(28, 28, 28)
                                        .addComponent(jLabel6))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(31, 31, 31)
                                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(9, 9, 9)
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel7)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jButton4)
                                        .addGap(38, 38, 38))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton1)
                                .addGap(234, 234, 234)
                                .addComponent(jLabel1)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jRadioButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jRadioButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 177, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(37, 37, 37))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jButton1))
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2)
                    .addComponent(jLabel5)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 322, Short.MAX_VALUE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addComponent(jLabel3)
                        .addContainerGap(28, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2)
                            .addComponent(jButton4))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1040, 560));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        
        ConfigureMachinesScreen.showFrame();
        ArrayList<String> machinesList = new ArrayList<String>(machines);
        ConfigureMachinesScreen.RenderTable(machinesList);
        this.setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        DefaultTreeModel model = (DefaultTreeModel) jTree1.getModel();
        
        TreePath[] paths = jTree1.getSelectionPaths();
        
        for (TreePath path : paths) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
            if (node.getParent() != null) {
                model.removeNodeFromParent(node);
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        // TODO add your handling code here:
        jTree2.setDragEnabled(false);
        jTree2.setEnabled(false);
        // we remove all the nodes in the jtree 
        // when switching modes. 
        // Otherwise, we need to validate the model 
        // contents before clicking execute 
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) jTree1.getModel().getRoot();
        root.removeAllChildren();
        DefaultTreeModel model = (DefaultTreeModel) jTree1.getModel();
        model.reload();
        jTextField1.setVisible(true);
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        // TODO add your handling code here:
        jTree2.setDragEnabled(true);
        jTree2.setEnabled(true);
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) jTree1.getModel().getRoot();
        root.removeAllChildren();
        DefaultTreeModel model = (DefaultTreeModel) jTree1.getModel();
        model.reload();
        
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        if(getNumberOfNodes(jTree1.getModel()) == 1 || getNumberOfNodes(jTree1.getModel()) == 0){
            JOptionPane.showMessageDialog(null, "Please create a valid test suite to continue with execution.");
            return;
        }
      //  TestResultsScreen testexecScreen = new TestResultsScreen();
       // testexecScreen.setVisible(true);
        //this.setVisible(false);
        this.testCases = populateWithSelectedTestCases();
        try {
            allocateMachinesForExec();
        } catch (IOException ex) {
            Logger.getLogger(CreateTestSuiteScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
        jTextField1.setColumns(20);
    }//GEN-LAST:event_jTextField1ActionPerformed

    private int getNumberOfNodes(TreeModel model){
        return getNumberOfNodes(model, model.getRoot());
    }
    
    private int getNumberOfNodes(TreeModel model, Object node){
        int count = 1;
        int numberOfChildren = model.getChildCount(node);
        for(int i=0; i<numberOfChildren; i++){
            count += getNumberOfNodes(model, model.getChild(node, i));
        }
        return count;
    }
    
    private void generateTestTree() throws IOException {
        System.out.println("Inside generate test tree method .. ");
        File testsFile = new File("Inputs/TestConfiguration.txt");
        DefaultMutableTreeNode myTreeNode = (DefaultMutableTreeNode)jTree3.getModel().getRoot();
        
        myTreeNode = StructureBuilder.getTreeNode(testsFile);   
        jTree3.setModel(new javax.swing.tree.DefaultTreeModel(myTreeNode));
    }
    
    private void groupButton(){
        ButtonGroup bg1 = new ButtonGroup();
        bg1.add(jRadioButton1);
        bg1.add(jRadioButton2);
    }
    
    public void addMachinesToList(){
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) jTree2.getModel().getRoot();
        javax.swing.tree.DefaultMutableTreeNode childNode;
        for(String mac : machines) {
            childNode = new javax.swing.tree.DefaultMutableTreeNode(mac);
            root.add(childNode);
        }
        String localhostDetails = getLocalHostDetails();
       childNode = new javax.swing.tree.DefaultMutableTreeNode(localhostDetails);
        root.add(childNode);
        jTree2.setModel(new javax.swing.tree.DefaultTreeModel(root));
        machines.add(localhostDetails);
        this.machines.addAll(machines);
       this.machines.add(localhostDetails);
    }
    
    // for random mode
    public ArrayList<TestCase> populateWithSelectedTestCases(){
        ArrayList<TestCase> tests = new ArrayList<TestCase>();
        
        DefaultMutableTreeNode testSuiteRootNode = (DefaultMutableTreeNode) jTree1.getModel().getRoot();
        Enumeration testSuite = testSuiteRootNode.children();
        List<String> testSuiteElements = new ArrayList<String>();
        while(testSuite.hasMoreElements()){
            testSuiteElements.add(testSuite.nextElement().toString());
        }
        
        DefaultMutableTreeNode testCasesRootNode = (DefaultMutableTreeNode) jTree3.getModel().getRoot();
        
        int numberOfTestCases = testCasesRootNode.getChildCount();
        for(String s : testSuiteElements){
            String[] splitStr = s.split(" ");
            String st = splitStr[1];
            for(int i=0; i<numberOfTestCases; i++){
                
                TreeNode thisTestCase = testCasesRootNode.getChildAt(i);
                if(s.equals(thisTestCase.toString()) && !thisTestCase.isLeaf()) {
                    System.out.println("It's a match! : " + st);
                    
                    // Set the test case name
                    TestCase newTestCase = new TestCase();
                    newTestCase.setTestCaseName(st);
                    
                    // Set the test executables
                    int numberOfExecutables = thisTestCase.getChildCount();
                    List<String> testExecutables = new ArrayList<String>();
                    for(int j=0; j<numberOfExecutables; j++){
                        testExecutables.add(thisTestCase.getChildAt(j).toString().trim());
                    }
                    newTestCase.setTestExecutables(testExecutables);
                    
                    // set the local attribute of test case
                    if(s.contains("local"))
                        newTestCase.setLocal(true);
                    else 
                        newTestCase.setLocal(false);
                    
                    // set the multiple attribute of test case
                    if(s.contains("multiple")){
                        newTestCase.setMultiple(true);
                        // set multiple count value from combo box!
                        int m = Integer.parseInt(jTextField1.getText());
                        newTestCase.setMultipleCount(m);
                    }
                    else {
                        newTestCase.setMultiple(false);
                        newTestCase.setMultipleCount(1);
                    }
                    tests.add(newTestCase);
                }
            }
        }
        
        return tests;
    }
    
    // applicable only for random mode - need to verify this code
    public void allocateMachinesForExec() throws IOException {
        
        String localhostDetails = getLocalHostDetails();
      //  System.out.println(localhostDetails);
        List<TestCase> tests = new ArrayList<TestCase>();
        tests = this.testCases;
        List<String> machinesSelected = new ArrayList<String>();
        machinesSelected = this.machines;
     //   System.out.println(machinesSelected);
        List<String> localExecutables = new ArrayList<String>();
        
        // when there are no other available machinesSelected and everything should be run on the master machine
        // the only machine available is the local machine and its details
        if(machinesSelected.size() == 0){
            for(TestCase test : tests){
                test.setLocal(true);
            }
        }
        
        // flatten all the test executables if there is a multiple flag
        for(TestCase test : tests){
            if(test.isMultiple()){
                List<String> thisTestExec = test.getTestExecutables();
                List<String> dummyList = new ArrayList<String>();
                for(String executable : thisTestExec){
                    for(int i=1; i<=test.getMultipleCount(); i++){
                        StringBuilder s = new StringBuilder();
                       
                        String temp=executable+" "+i+" "+test.getMultipleCount();
                        System.out.println("Temp value is"+temp);
                        
                        //System.out.println("Executable name is:"+executable);
                        s.append(executable.trim()+" "+i+" "+test.getMultipleCount());
                       // System.out.println("Arul u r it"+s.toString().trim());
                        dummyList.add(temp.trim());
                        System.out.println("dummy list is"+dummyList);
                    }
                }
                test.setTestExecutables(dummyList);
            }
           // System.out.println(""+test);
        }
        

        // the test executables have to be allocated to all the available machinesSelected
        // Map: a machine and list of test executables to be run
        HashMap<String, List<String>> myMap = new HashMap<String, List<String>>();
        List<String> listOfAllTestExecutables = new ArrayList<String>();
        for(TestCase test : tests){   
            if(!test.isLocal())
            {  
                System.out.println("look now"+test.getTestExecutables());
                listOfAllTestExecutables.addAll(test.getTestExecutables());
                int i=0, j=0;
                while(i<machinesSelected.size() && j<listOfAllTestExecutables.size()){
                    if(myMap.containsKey(machinesSelected.get(i))){
                        List<String> progs = myMap.get(machinesSelected.get(i));
                        System.out.println("all tests"+listOfAllTestExecutables);
                        progs.add(listOfAllTestExecutables.get(j));
                        myMap.put(machinesSelected.get(i), progs);
                        i++;
                        j++;
                     }
                    else {
                        List<String> progs = new ArrayList<String>();
                        progs.add(listOfAllTestExecutables.get(j));
                        myMap.put(machinesSelected.get(i), progs);
                        i++;
                        j++;
                    }       
                    // go back to the first machine if the number of test machinesSelected are
                    // lower than the no. of executables for round-robin like allocation
                    if(i >= machinesSelected.size())
                        i=0; 
                }   
            }         
            else
            {
                 localExecutables.addAll(test.getTestExecutables());
                 myMap.put(localhostDetails, localExecutables);
            }   
            System.out.println("arul Map"+myMap);
          displayMap(myMap);
          executeTestRun(myMap, System.getProperty("os.name"), test.getTestCaseName().trim(), hashValue);
          listOfAllTestExecutables.clear();
          myMap.clear();
        }
        
        TestResultsScreen testexecScreen = new TestResultsScreen();
        testexecScreen.setVisible(true);
        this.setVisible(false);
         
      /*  // write map contents to a file
        BufferedWriter bufwriter = new BufferedWriter(new FileWriter("MachineAllocation.txt"));
        ArrayList <String> machineList = new ArrayList();
        ArrayList <String> testcaseList = new ArrayList();
        Iterator<Entry<String, List<String>>> it = myMap.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String, List<String>> pairs = it.next();
           // machineList.add(pairs.getKey());
            //testcaseList.add(pairs.getValue());
            System.out.println("Key is : " + pairs.getKey());
            System.out.println("Value is : " + pairs.getValue());
                
            bufwriter.write(pairs.getKey() + " " + pairs.getValue() + " \n");
        }
        bufwriter.close(); */
    }
    
    public static String getLocalHostDetails()
    {
                        
     StringBuilder myMachineDetails = new StringBuilder();
     String interfaceName;
     String osname = System.getProperty("os.name").toLowerCase();
     if(osname.indexOf("win") > 0) 
     {
         interfaceName = "eth0";
         osname = "windows";
     }
     else if(osname.indexOf("x") > 0) 
     {
         interfaceName = "en0";
         osname = "mac";
     }
     else
     {
         interfaceName = "en0";
         osname = "ubuntu";
     }
    
     String ip = null;
     try
    {
          NetworkInterface networkInterface = NetworkInterface.getByName(interfaceName);
          Enumeration<InetAddress> inetAddress = networkInterface.getInetAddresses();
          InetAddress currentAddress;
          currentAddress = inetAddress.nextElement();
          while(inetAddress.hasMoreElements())
          {
              currentAddress = inetAddress.nextElement();
              if(currentAddress instanceof Inet4Address && !currentAddress.isLoopbackAddress())
              {
                   ip = currentAddress.toString();
                  //System.out.println(ip.substring(1));
                  break;
              }
          } 
     }
     catch(Exception e)
     {
         e.getMessage();
     } 
 
     String username = System.getProperty("user.name");
      ip = ip.substring(1);
      myMachineDetails.append(ip);
      myMachineDetails.append(" ");
      myMachineDetails.append(osname);
      myMachineDetails.append(" ");
      myMachineDetails.append(username);
      return myMachineDetails.toString();
    }
    
    public static void executeTestRun(HashMap <String, List<String>> h, String localOS, String testID, String hashValue)
    {
        System.out.println(hashValue);
        StringBuilder IPs = new StringBuilder();
        StringBuilder tests = new StringBuilder();
         ProcessBuilder p = new ProcessBuilder();
       // String[] command = new String[6];
        for (String k : h.keySet())
        {
            IPs.append(k.trim());
            if(h.keySet().size() > 1)IPs.append(",");
        }
        for(List <String> s : h.values())
        {
            //String[] testcases = s.toArray(new String[s.size()]);
                for(int i=0;i<s.size();i++)
                {
                    if(i==s.size()-1)
                    {
                        tests.append(s.get(i));
                    }else
                    {
                        tests.append(s.get(i)+",");
                    }
                    
                }
            
           // System.out.println("S value is"+s);
           if(h.values().size() > 1 ) tests.append(";");
        }
       // System.out.println(IPs.toString());
        //System.out.println(tests.toString());
        
             String[] command = {"src/scripts/dispatchscript.sh", IPs.toString().trim(), tests.toString(),localOS,testID, hashValue};
             p = new ProcessBuilder(command);
             Process p2 = null;
                try {
                    p2 = p.start();
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                    
                }
                BufferedReader br = new BufferedReader(new InputStreamReader(p2.getInputStream()));
                String line;
                p2.getInputStream();
                p.redirectErrorStream(true);
                System.out.println("Output of running command is: ");
                try {
                    while ((line = br.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
                
               System.out.println("Error in running command is: ");
                try {
                BufferedReader br2 = new BufferedReader(new InputStreamReader(p2.getErrorStream()));
                while ((line = br2.readLine()) != null) {
                System.out.println(line);
                }
                } catch (IOException ex) {
                System.out.println(ex.getMessage());
                }
                
                
                
    }
    public static void displayMap(HashMap <String,List<String>> hm)
    {
        System.out.println();
        System.out.println("******Displaying map for test case: ********");
        Iterator<Entry<String, List<String>>> it = hm.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<String, List<String>> pairs = it.next();
            System.out.println("Key is : " + pairs.getKey());
            System.out.println("Value is : " + pairs.getValue());
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CreateTestSuiteScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CreateTestSuiteScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CreateTestSuiteScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CreateTestSuiteScreen.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new CreateTestSuiteScreen().setVisible(true);
                ArrayList<String> machineList = new ArrayList<String>();
                CreateTestSuiteScreen myScreen = null;
                try {
                    myScreen = new CreateTestSuiteScreen(machineList, hashValue);
                } catch (IOException ex) {
                    Logger.getLogger(CreateTestSuiteScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
                //myScreen.getContent();
                myScreen.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTree jTree1;
    private javax.swing.JTree jTree2;
    private javax.swing.JTree jTree3;
    // End of variables declaration//GEN-END:variables
} 

// to find out the number of nodes in a tree
class StructureBuilder {

    public static final DefaultMutableTreeNode getTreeNode(File file) throws IOException  {

        DefaultMutableTreeNode rootNode = null;
        Map<Integer, DefaultMutableTreeNode> levelNodes = new HashMap<Integer, DefaultMutableTreeNode>();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;

        while( (line = reader.readLine()) != null ) {

            int level = getLevel(line);
            String nodeName = getNodeName(line, level);
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(nodeName);               
            levelNodes.put(level, node);
            DefaultMutableTreeNode parent = levelNodes.get(level - 1);

            if( parent != null ) {
                parent.add(node);
            }
            else {
                rootNode = node;
            }
        }    
        reader.close();
        return rootNode;
    }

    private static final int getLevel(String line) {

        int level = 0;
        for ( int i = 0; i < line.length(); i++ ) {
            char c = line.charAt(i);
            if( c == '\t') {
                level++;
            }
            else {
                break;
            }
        }
        return level;
    }

    private static final String getNodeName(String line, int level) {
        return line.substring(level);
    }      
}

// class Test case to store all the test related details
class TestCase {
    String testCaseName;
    List<String> testExecutables;
    boolean local;
    boolean multiple;
    int multipleCount;

    public TestCase(){
    }
    
    // getters and setters
    public String getTestCaseName() {
        return testCaseName;
    }

    public void setTestCaseName(String testCaseName) {
        this.testCaseName = testCaseName;
    }

    public List<String> getTestExecutables() {
        return testExecutables;
    }

    public void setTestExecutables(List<String> testExecutables) {
        this.testExecutables = testExecutables;
    }

    public boolean isLocal() {
        return local;
    }

    public void setLocal(boolean isLocal) {
        this.local = isLocal;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public void setMultiple(boolean isMultiple) {
        this.multiple = isMultiple;
    }

    public int getMultipleCount() {
        return multipleCount;
    }

    public void setMultipleCount(int multipleCount) {
        this.multipleCount = multipleCount;
    }    
}


