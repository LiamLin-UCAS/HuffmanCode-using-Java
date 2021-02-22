package nodes;
public class character {
    public char aChar;
    public int weigh;
    public String code;
    public character(){
        aChar='\0';
        weigh=0;
        code="";
    }
    public character(char theChar){
        aChar=theChar;
        weigh=0;
        code="";
    }
    public character(char theChar,int theWeigh){
        aChar=theChar;
        weigh=theWeigh;
        code="";
    }
}
