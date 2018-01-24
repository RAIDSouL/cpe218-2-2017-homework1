import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.IOException;
import java.net.URL;
import javax.swing.*;
import java.util.Stack;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;


public class Homework1 {
    
    static Stack charholder = new Stack();
    public static Node temp;
    
    public static void main(String[] args) {
        String RPN;
        if(args.length > 0){
            RPN = args[0];
        }
        else{
            RPN = "251-*32*+";
        }
        
        //String RPN = args[0];
        
        for(int i = 0; i < RPN.length(); i++){
            charholder.add(RPN.charAt(i));
        }
        //System.out.print(charholder);
        temp = new Node((char) charholder.pop());
        //System.out.print(temp.data);
        infix(temp);
        inorder(temp);
        System.out.print('=');
        System.out.print(calculate(temp));
        
         javax.swing.SwingUtilities.invokeLater(TreeDemo::createAndShowGUI);
    }
    
    public static void infix(Node n){
        //System.out.print(n.data);
        if(Isoperation(n)){
            n.right = new Node((char) charholder.pop());
            infix(n.right);
            n.left = new Node((char) charholder.pop());
            infix(n.left);
        }
    }
    
    public static void inorder(Node n){
    //    System.out.print(n.data);
        if(Isoperation(n)){
            if(n != temp)
                System.out.print('(');
            inorder(n.left);
            System.out.print(n.data);
            inorder(n.right);
            if(n != temp)
                System.out.print(')');
        }
        else
            System.out.print(n.data);
    }
    
    public static int calculate(Node n){
        if(Isoperation(n)){
            n.left.value = calculate(n.left);
            n.right.value = calculate(n.right);
            switch (n.data) {
                case '+':
                    return n.left.value + n.right.value;
                case '-':
                    return n.left.value - n.right.value;
                case '*':
                    return n.left.value * n.right.value;
                case '/':
                    return n.left.data / n.right.value;
                default:
                    return -1;
                }
        }
        else{
            n.value = n.data-'0';
            return n.value;
        }
    }
    public static boolean Isoperation(Node n){
        return n.data == '+' || n.data == '-' || n.data == '*' || n.data == '/';
    }
}
    
class TreeDemo extends JPanel implements TreeSelectionListener {
    private JEditorPane htmlPane;
    private JTree tree;
    private URL helpURL;
    private static boolean DEBUG = false;
 
    //Optionally play with line styles.  Possible values are
    //"Angled" (the default), "Horizontal", and "None".
    private static boolean playWithLineStyle = false;
    private static String lineStyle = "Horizontal";
     
    //Optionally set the look and feel.
    private static boolean useSystemLookAndFeel = false;
 
    public TreeDemo() {
        super(new GridLayout(1,0));
 
        //Create the nodes.
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(Homework1.temp.data);
        createNodes(top,Homework1.temp);
 
        //Create a tree that allows one selection at a time.
        tree = new JTree(top);
        tree.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);
        
        //Listen for when the selection changes.
        tree.addTreeSelectionListener(this);
 
        if (playWithLineStyle) {
            System.out.println("line style = " + lineStyle);
            tree.putClientProperty("JTree.lineStyle", lineStyle);
        }
 
        //Create the scroll pane and add the tree to it. 
        JScrollPane treeView = new JScrollPane(tree);
 
        //Create the HTML viewing pane.
        htmlPane = new JEditorPane();
        htmlPane.setEditable(false);
        initHelp();
        JScrollPane htmlView = new JScrollPane(htmlPane);
 
        //Add the scroll panes to a split pane.
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(treeView);
        splitPane.setBottomComponent(htmlView);
 
        Dimension minimumSize = new Dimension(100, 50);
        htmlView.setMinimumSize(minimumSize);
        treeView.setMinimumSize(minimumSize);
        splitPane.setDividerLocation(100); 
        splitPane.setPreferredSize(new Dimension(500, 300));
 
        //Set the icon for leaf nodes.
        ImageIcon leafIcon = createImageIcon("middle.gif");
        if (leafIcon != null) {
            DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
            renderer.setClosedIcon(leafIcon);
            renderer.setOpenIcon(leafIcon);
            tree.setCellRenderer(renderer);
        }
        
        //Add the split pane to this panel.
        add(splitPane);
    }
 
    /** Required by TreeSelectionListener interface. */
    @Override
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
        tree.getLastSelectedPathComponent();
 
        if (node == null) return;
 
        Object nodeInfo = node.getUserObject();
        Node temp = (Node)nodeInfo;
        Homework1.inorder(temp);
        htmlPane.setText(Integer.toString(temp.value));
    }
 
    private class BookInfo {
        public String bookName;
        public URL bookURL;
 
        public BookInfo(String book, String filename) {
            bookName = book;
            bookURL = getClass().getResource(filename);
            if (bookURL == null) {
                System.err.println("Couldn't find file: "+ filename);
            }
        }
 
        public String toString() {
            return bookName;
        }
    }
 
    private void initHelp() {
        String s = "TreeDemoHelp.html";
        helpURL = getClass().getResource(s);
        if (helpURL == null) {
            System.err.println("Couldn't open help file: " + s);
        } else if (DEBUG) {
            System.out.println("Help URL is " + helpURL);
        }
 
        displayURL(helpURL);
    }
 
    private void displayURL(URL url) {
        try {
            if (url != null) {
                htmlPane.setPage(url);
            } else { //null url
        htmlPane.setText("File Not Found");
                if (DEBUG) {
                    System.out.println("Attempted to display a null URL.");
                }
            }
        } catch (IOException e) {
            System.err.println("Attempted to read a bad URL: " + url);
        }
    }
 
    private void createNodes(DefaultMutableTreeNode top,Node tree) {
        if(tree.left != null){
                 DefaultMutableTreeNode LNode = new DefaultMutableTreeNode(tree.left.data);
                 top.add(LNode);
                 createNodes(LNode,tree.left);
             }
             if(tree.right != null){
                 DefaultMutableTreeNode RNode = new DefaultMutableTreeNode(tree.right.data);
                 top.add(RNode);
                 createNodes(RNode,tree.right);
             }
    }
         
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    public static void createAndShowGUI() {
        if (useSystemLookAndFeel) {
            try {
                UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.err.println("Couldn't use system look and feel.");
            }
        }
 
        //Create and set up the window.
        JFrame frame = new JFrame("TreeDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Add content to the window.
        frame.add(new TreeDemo());
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = TreeDemo.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
}
