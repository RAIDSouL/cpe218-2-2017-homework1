import java.util.Stack;

public class Homework1 {
    
    static Stack charholder = new Stack();
    public static Node temp;

    public static void main(String[] args) {
        String RPN;
        if(args.length <= 0){
            RPN = "251-*32*+";
        }
        else
            RPN = args[0];
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
    }
    
    public static class Node{
        public Node left;
        public Node right;
        char data;
        int value;
        public Node (char n){
            data = n;
            left = null;
            right = null;
        }
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
