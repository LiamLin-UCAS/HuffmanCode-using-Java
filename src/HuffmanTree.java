import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import Exceptions.*;
import nodes.*;

public class HuffmanTree {
    public Node root;
    public int index;
    private int length;
    private List<Node> elements;
    public HuffmanTree(){index=0;length=0;elements=new ArrayList<>();}
    public static void main(String[] args) {
        HuffmanTree tree=new HuffmanTree();
        Scanner sysScan=new Scanner(System.in);
        int indicator=0;
        while(indicator==0){
            System.out.println("Please choose your beginning:\n1.From Initialization\n2.From Files:\"hfmtree.txt\"\n" +
                    "3.From Coding Files:\"tobetrans.txt\"\n4.From Decoding Files:\"codefiles.txt\"\nother num for exit");
            int choice=sysScan.nextInt();
            switch (choice){
                case 1:
                    tree.input();
                    String treeInString="";
                    treeInString=tree.treeToString(tree.root,treeInString);
                    writer(treeInString,"hfmtree.txt");
                    break;
                case 2: {
                    String text;
                    try {
                        text = reader("hfmtree.txt");
                        tree.index=0;
                        tree.root=tree.readString(text);
                    } catch (FileNotFoundException e) {
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
                        writer(tree.coding("tobetrans.txt"),"codefile.txt");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
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
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }catch (DecodeException e) {
                        System.out.println("Wrong in codefile.txt");
                    }
                    break;
                }
                default:
                    indicator=1;
            }
        }
        //Node.preOrderTraversal(tree.root);

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
    public static String reader(String filename) throws FileNotFoundException{
        String data="";
        File file=new File("D:"+File.separator+"山东大学"+File.separator+
                "软工大二下"+File.separator+"数据结构课设"+File.separator+"数据"+File.separator+filename);
        try {
            Scanner scan=new Scanner(file, StandardCharsets.UTF_8);
            data=scan.nextLine();
        }catch (IOException e){
            e.printStackTrace();
        }catch (NoSuchElementException e){
            System.out.println("Empty File:"+filename);
        }
        return data;
    }
    public static boolean writer(String data,String filename){
        if(data!=""){
            File file=new File("D:"+File.separator+"山东大学"+File.separator+
                    "软工大二下"+File.separator+"数据结构课设"+File.separator+"数据"+File.separator+filename);
            if(file.exists()){
                System.out.println(filename+" is replaced");
                file.delete();
            }
            try {
                FileOutputStream fileOutputStream=new FileOutputStream(file,true);
                byte[] bytes=data.getBytes(StandardCharsets.UTF_8);
                fileOutputStream.write(bytes);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
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
    private String coding(String filename){
        String result="";
        try {
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }//解码器
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
