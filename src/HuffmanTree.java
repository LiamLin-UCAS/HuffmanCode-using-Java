import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import Exceptions.*;
import nodes.*;
abstract class Tree{
    public Node root;
    public static void show(Node root){};
}
public class HuffmanTree extends Tree{
    //public Node root;
    public int index;
    private int length;
    private List<Node> elements;
    public HuffmanTree(){index=0;length=0;elements=new ArrayList<>();}
    public static void main(String[] args) {
        HuffmanTree tree=new HuffmanTree();
        Scanner sysScan=new Scanner(System.in);
        int indicator=0;
        while(indicator==0){
            System.out.println("\nPlease choose your beginning:\n" +
                    "1.From Initialization\n" +
                    "2.From Files:\"hfmtree.txt\"\n" +
                    "3.From Coding Files:\"tobetrans.txt\" to Files:\"codefile.txt\"\n" +
                    "4.From Decoding Files:\"codefile.txt\" to Files:\"textfile.txt\" and print \"codefile.txt\"\n" +
                    "5.From Tree Printing\n" +
                    "other num for exit");
            int choice=sysScan.nextInt();
            switch (choice){
                case 1:
                    tree.input();
                    String treeInString="";
                    treeInString=tree.treeToString(tree.root,treeInString);
                    try {
                        writer(treeInString,"hfmtree.txt");
                    } catch (IOException e) {
                        System.out.println("Files write error!");
                    }
                    break;
                case 2: {
                    String text;
                    try {
                        text = reader("hfmtree.txt");
                        tree.index=0;
                        tree.root=tree.readString(text);
                    } catch (IOException e) {
                        System.out.println("File not found");
                    }
                    break;
                }
                case 3:{
                    String text;
                    try {
                        text=reader("hfmtree.txt");
                        tree.index=0;
                        tree.root=tree.readString(text);
                        encoder(tree.root,tree.elements,"");
                        text=tree.coding("tobetrans.txt");
                        writer(text,"codefile.txt");
                    } catch (IOException e) {
                        System.out.println("File not found");
                    }
                    break;
                }
                case 4:{
                    String text;
                    try {
                        text=reader("hfmtree.txt");
                        tree.index=0;
                        tree.root=tree.readString(text);
                        encoder(tree.root,tree.elements,"");
                        writer(tree.decoding("codefile.txt"),"textfile.txt");
                        tree.print("codefile.txt");
                    } catch (IOException e) {
                        System.out.println("File not found");
                    }catch (DecodeException e) {
                        System.out.println("Wrong in codefile.txt");
                    }
                    break;
                }
                case 5:{
                    String text;
                    try {
                        text=reader("hfmtree.txt");
                        tree.index=0;
                        tree.root=tree.readString(text);
                        show(tree.root);
                    } catch (IOException e) {
                        System.out.println("File not found");
                    }
                    break;
                }
                default:
                    indicator=1;
            }
        }
    }
    public void input(){
        System.out.println("Input number of elements:");
        Scanner scan=new Scanner(System.in);
        length=scan.nextInt();
        character[] elements=new character[length];
        for(int i=0;i<length;i++){
            elements[i]=new character();
            System.out.print("Input character: ");
            String temp=scan.next();
            elements[i].aChar=temp.charAt(0);
            System.out.print("Input "+elements[i].aChar+"'s weigh: ");
            elements[i].weigh=scan.nextInt();
        }
        root=huffmanTree(elements);
    }
    public static Node huffmanTree(character[] arr){
        List<Node> nodes = new ArrayList<>();
        for (character data : arr) {
            nodes.add(new Node(data));
        }
        Node newNode = null;
        if(nodes.size()==1){
            newNode=new Node(arr[0]);
            return newNode;
        }
        while(nodes.size() > 1){
            Collections.sort(nodes);
            //System.out.println(nodes);
            Node left = nodes.remove(0);
            Node right = nodes.remove(0);
            newNode = new Node(left.value.weigh + right.value.weigh);
            newNode.left = left;
            newNode.right = right;
            nodes.add(newNode);
        }
        return newNode;
    }
    public String treeToString(Node root,String out){
        if (root.left==null&&root.right==null)
        {
            out+="1";
            out+=root.value.aChar;
        }
        else
        {
            out+="0";
            out=treeToString(root.left, out);
            out=treeToString(root.right, out);
        }
        return out;
    }
    public Node readString(String in){
        if(index<in.length()){
            if(in.charAt(index)=='1'){
                character temp=new character(in.charAt(++index));
                index++;
                return new Node(temp);
            }
            else{
                index++;
                Node left=readString(in);
                Node right=readString(in);
                return new Node(0,left,right);
            }
        }
        else{
            return null;
        }
    }
    public static String reader(String filename) throws IOException{
        String data="";
        File file=new File("D:"+File.separator+"山东大学"+File.separator+
                "软工大二下"+File.separator+"数据结构课设"+File.separator+"数据"+File.separator+filename);
            Scanner scan=new Scanner(file, StandardCharsets.UTF_8);
            data=scan.nextLine();
            scan.close();
        return data;
    }
    public static boolean writer(String data,String filename)throws IOException{
        if(data!=""){
            File file=new File("D:"+File.separator+"山东大学"+File.separator+
                    "软工大二下"+File.separator+"数据结构课设"+File.separator+"数据"+File.separator+filename);
            if(file.exists()){
                System.out.println(filename+" is replaced");
                file.delete();
            }
            FileOutputStream fileOutputStream=null;
            try {
                fileOutputStream=new FileOutputStream(file,true);
                byte[] bytes=data.getBytes(StandardCharsets.UTF_8);
                fileOutputStream.write(bytes);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                fileOutputStream.close();
            }
        }
        return false;
    }
    public static void encoder(Node node,List<Node> nodes,String route){
        if(node==null)
            return;
        if(node.isLeaf()){
            node.route=route;
            node.value.weigh=0;
            nodes.add(node);
        }
        else{
            encoder(node.left,nodes,route+"0");
            encoder(node.right,nodes,route+"1");
        }
    }
    private String coding(String filename) throws IOException{
        String result="";
            String data=reader(filename);
            for(int i=0;i<data.length();i++){
                char temp=data.charAt(i);
                for(int j=0;j<elements.toArray().length;j++){
                    if(elements.get(j).value.aChar==temp){
                        result+=elements.get(j).route;
                        break;
                    }
                }
            }
        return result;
    }//编码器
    private String decoding(String filename) throws DecodeException{
        String result="";
        try {
            String data=reader(filename);
            for(int i=0;i<data.length();){
                Node temp=root;
                while(!temp.isLeaf()){
                    if(data.charAt(i)=='0'){
                        temp=temp.left;
                        i++;
                    }
                    else{
                        if(data.charAt(i)=='1'){
                            temp=temp.right;
                            i++;
                        }
                        else{
                            DecodeException e=new DecodeException();
                            throw e;
                        }
                    }
                }
                result+=temp.value.aChar;
            }
        } catch (IOException e) {
            System.out.println("File not found");
        }
        return result;
    }//解码器
    public void print(String filename) throws DecodeException{
        String stringToFile="";
        try {
            String data=reader(filename);
            int counter=0,i=0;
            String result="";
            System.out.print(filename+": ");
            while(i<data.length()){
                while(counter<=50&&i<data.length()){
                    Node temp=root;
                    while(!temp.isLeaf()){
                        if(data.charAt(i)=='0'){
                            temp=temp.left;
                            i++;
                        }
                        else{
                            if(data.charAt(i)=='1'){
                                temp=temp.right;
                                i++;
                            }
                            else{
                                throw new DecodeException();
                            }
                        }
                    }
                    result+=temp.route;
                    counter++;
                }
                System.out.println(result);
                stringToFile+=result;
                counter=0;
            }
        } catch (IOException e) {
            System.out.println("File not found");
        }
        try {
            writer(stringToFile,"codeprint.txt");
        } catch (IOException e) {
            System.out.println("File write error");
        }
    }
    public static int getTreeDepth(Node root) {
        return root == null ? 0 : (1 + Math.max(getTreeDepth(root.left), getTreeDepth(root.right)));
    }
    private static void writeArray(Node currNode, int rowIndex, int columnIndex, String[][] res, int treeDepth) {
        // 保证输入的树不为空
        if (currNode == null) return;
        // 先将当前节点保存到二维数组中
        if(currNode.value.aChar!='\0')
            res[rowIndex][columnIndex] = String.valueOf(currNode.value.aChar);
        else
            res[rowIndex][columnIndex]="#";
        // 计算当前位于树的第几层
        int currLevel = ((rowIndex + 1) / 2);
        // 若到了最后一层，则返回
        if (currLevel == treeDepth) return;
        // 计算当前行到下一行，每个元素之间的间隔（下一行的列索引与当前元素的列索引之间的间隔）
        int gap = treeDepth - currLevel - 1;

        // 对左儿子进行判断，若有左儿子，则记录相应的"/"与左儿子的值
        if (currNode.left != null) {
            res[rowIndex + 1][columnIndex - gap] = "/";
            writeArray(currNode.left, rowIndex + 2, columnIndex - gap * 2, res, treeDepth);
        }

        // 对右儿子进行判断，若有右儿子，则记录相应的"\"与右儿子的值
        if (currNode.right != null) {
            res[rowIndex + 1][columnIndex + gap] = "\\";
            writeArray(currNode.right, rowIndex + 2, columnIndex + gap * 2, res, treeDepth);
        }
    }


    public static void show(Node root) {
        if (root == null) System.out.println("EMPTY!");
        // 得到树的深度
        int treeDepth = getTreeDepth(root);

        // 最后一行的宽度为2的（n - 1）次方乘3，再加1
        // 作为整个二维数组的宽度
        int arrayHeight = treeDepth * 2 - 1;
        int arrayWidth = (2 << (treeDepth - 2)) * 3 + 1;
        // 用一个字符串数组来存储每个位置应显示的元素
        String[][] res = new String[arrayHeight][arrayWidth];
        // 对数组进行初始化，默认为一个空格
        for (int i = 0; i < arrayHeight; i ++) {
            for (int j = 0; j < arrayWidth; j ++) {
                res[i][j] = " ";
            }
        }

        // 从根节点开始，递归处理整个树
        // res[0][(arrayWidth + 1)/ 2] = (char)(root.val + '0');
        writeArray(root, 0, arrayWidth/ 2, res, treeDepth);

        // 此时，已经将所有需要显示的元素储存到了二维数组中，将其拼接并打印即可
        for (String[] line: res) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < line.length; i ++) {
                sb.append(line[i]);
                if (line[i].length() > 1 && i <= line.length - 1) {
                    i += line[i].length() > 4 ? 2: line[i].length() - 1;
                }
            }
            System.out.println(sb.toString());
        }
    }
}
class Node implements Comparable<Node> {//节点
    character value;
    Node left;
    Node right;
    String route;
    public Node(int theWeigh){
        this.value=new character('\0',theWeigh);
        this.left=null;
        this.right=null;
        this.route="";
    }
    public Node(character value) {
        this.value = value;
        this.left=null;
        this.right=null;
        this.route="";
    }
    public Node(int temp,Node theLeft,Node theRight){
        this.value=new character('\0',temp);
        this.left=theLeft;
        this.right=theRight;
        this.route="";
    }
    public Node(char tempChar,Node theLeft,Node theRight){
        this.value=new character(tempChar,0);
        left=theLeft;
        right=theRight;
        route="";
    }
    public boolean isLeaf(){
        return left==null&&right==null;
    }
    @Override
    public String toString() {
        return "Node{character=" + value.aChar + ",weigh=" + value.weigh+"}";
    }

    @Override
    public int compareTo(Node o) {
        return this.value.weigh - o.value.weigh;//从小到大排序
    }

    //前序遍历方法
    public static void preOrderTraversal(Node root){
        if(root == null) return;
        System.out.println(root);
        preOrderTraversal(root.left);
        preOrderTraversal(root.right);
    }
}
