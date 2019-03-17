package Test;
import java.util.PriorityQueue;
import java.util.Stack;

/*
哈夫曼编码：构造最优满二叉树
编码压缩法。
 */
public class Huffman {
    public static void main(String[] args)throws Exception{
        Huffman huffman = new Huffman();
        huffman.addNode('b',13);
        huffman.addNode('c',12);
        huffman.addNode('d',16);
        huffman.addNode('e',9);
        huffman.addNode('f',5);
        huffman.encode();
        huffman.display();
        System.out.println("---------------add new node--------------------");
        huffman.addNode('a',45);
        huffman.encode();
        huffman.display();
    }

    private PriorityQueue<Node>Q;
    public Huffman(){
        Q = new PriorityQueue<>();
    }
    public void addNode(char ch,int fre){
        Q.add(new Node(fre,ch));
    }
    public Node encode(){
        //构造哈弗曼树：频率最低两颗树合并，再入队，进行|Q|-1次
        Node root;
        for(int i=0,n=Q.size()-1;i<n;i++){
            root = new Node(0,'0');
            root.left = Q.poll();
            root.right = Q.poll();
            root.freq = root.left.freq+root.right.freq;
            Q.add(root);
        }
        // 求得编码值
        decode();
        return Q.peek();
    }
    public Node display(){
        if(Q.size()==0)
            System.out.println("Empty Nodes");
        //先序遍历
        Stack<Node> stack = new Stack<>();
        stack.push(Q.peek());
        while (!stack.isEmpty()){
            Node n = stack.pop();
            if(n.left==null&&n.right==null)
                System.out.println(n.data+": "+n.sb+": "+n.freq);
            if(n.right!=null)
                stack.push(n.right);
            if(n.left!=null)
                stack.push(n.left);
        }
        return Q.peek();
    }
    private void decode(){
        if(Q.peek()==null)
            System.out.println("Empty Nodes");
        //先序遍历
        Stack<Node> stack = new Stack<>();
        stack.push(Q.peek());
        while (!stack.isEmpty()){
            Node n = stack.pop();
            if(n.right!=null){
                n.right.sb = new StringBuilder().append(n.sb).append('1');
                stack.push(n.right);
            }
            if(n.left!=null){
                n.left.sb =  new StringBuilder().append(n.sb).append('0');
                stack.push(n.left);
            }
        }
    }
}
class Node implements Comparable<Node>{
    public char data; //字符
    public int freq;  //频率

    public StringBuilder sb; //编码
    public Node left,right;  //左树、右树
    public Node(int freq,char data){
        this.freq=freq;
        this.data=data;
        sb = new StringBuilder();
        left=right=null;
    }
    public int compareTo(Node b){
        return this.freq-b.freq;
    }
}
