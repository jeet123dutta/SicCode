import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.util.*;
import java.lang.reflect.*;
import java.io.*;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
class Memory{
	public int A,X,L,CC;
	public int M[];
	public String operand;
	Memory(int size)
	{
		M = new int[size];
	}
}
class SIC{
	static Memory ram = new Memory(4000);
	Map<String , Method> OP;
	Map<String , String> OPTAB;
	public int START=0;
	SIC() throws Exception
	{
		OP = new HashMap<String , Method>();
		OPTAB = new HashMap<String , String>();
		String[] opcodes = {"ADD","AND","STA","STX","STCH","STL","LDA","LDCH","LDX","COMP","MUL","DIV","SUB","OR","LDL","TIX"};
		for(int i=0;i<opcodes.length;i++)
		{
			StringBuilder temp = new StringBuilder();
			temp.append("0A10");
			if(i<10) temp.append(i*10);
			else	temp.append(i);
			OPTAB.put(opcodes[i], temp.toString());
		}
		for(String i:opcodes)
			OP.put(i, SIC.class.getMethod(i));
	}
	int getMemory(int address){	return ram.M[address%4000];	}
	int getA(){	return ram.A;	}
	int getX(){	return ram.X;	}
	int getL(){	return ram.L;	}
	int getCC(){	return ram.CC;	}
	//int getLOCCTR(){return locctr;	}
	int getSTART(){ return START;	}
	void setOperand(String operand){	ram.operand = operand;	}
	void storeByte(char c,int index){	ram.M[index%4000] = c;	}
	void storeWord(int w,int index){	ram.M[index%4000] = w/65536;ram.M[index%4000+1] = (w%65536)/256;ram.M[index%4000+2] = w%256; 	}
	int opcodecount=0;
	void execute(String opcode) throws Exception
	{
		//System.out.print("opcode : "+opcode+" , operand(s) : "+ram.operand);

		if(opcode.equals("START"))	System.out.print("H^"+"MYPROG.\nT^");
		else if(opcode.equals("END"))	System.out.print("\nE^"+START+"\n");
		else if(OPTAB.containsKey(opcode))
		{
			opcodecount++;
			if(opcodecount%10==0)	System.out.print("\nT^");
			System.out.print(OPTAB.get(opcode)+".");
                ///        System.out.print(OPTAB.get(ram.operand)+".");
		}
		OP.get(opcode).invoke(null);
	}
	boolean checkOpCode(String op){	return OPTAB.containsKey(op);	}
	//fw.close();
	public static void LDA(){	ram.A = ram.M[Integer.parseInt(ram.operand)]*256*256+ram.M[Integer.parseInt(ram.operand)+1]*256+ram.M[Integer.parseInt(ram.operand)+2];	}
	public static void LDX(){	ram.X = ram.M[Integer.parseInt(ram.operand)]*256*256+ram.M[Integer.parseInt(ram.operand)+1]*256+ram.M[Integer.parseInt(ram.operand)+2];	}
	public static void LDL(){	ram.L = ram.M[Integer.parseInt(ram.operand)]*256*256+ram.M[Integer.parseInt(ram.operand)+1]*256+ram.M[Integer.parseInt(ram.operand)+2];	}
	public static void LDCH(){	ram.A = ram.M[Integer.parseInt(ram.operand)];	}
	public static void ADD(){	ram.A += (ram.M[Integer.parseInt(ram.operand)]*256*256+ram.M[Integer.parseInt(ram.operand)+1]*256+ram.M[Integer.parseInt(ram.operand)+2]);	}
	public static void SUB(){	ram.A -= (ram.M[Integer.parseInt(ram.operand)]*256*256+ram.M[Integer.parseInt(ram.operand)+1]*256+ram.M[Integer.parseInt(ram.operand)+2]);	}
	public static void MUL(){	ram.A *= (ram.M[Integer.parseInt(ram.operand)]*256*256+ram.M[Integer.parseInt(ram.operand)+1]*256+ram.M[Integer.parseInt(ram.operand)+2]);	}
	public static void DIV(){	ram.A /= (ram.M[Integer.parseInt(ram.operand)]*256*256+ram.M[Integer.parseInt(ram.operand)+1]*256+ram.M[Integer.parseInt(ram.operand)+2]);	}
	public static void AND(){	ram.A &= (ram.M[Integer.parseInt(ram.operand)]*256*256+ram.M[Integer.parseInt(ram.operand)+1]*256+ram.M[Integer.parseInt(ram.operand)+2]);	}
	public static void OR(){	ram.A |= (ram.M[Integer.parseInt(ram.operand)]*256*256+ram.M[Integer.parseInt(ram.operand)+1]*256+ram.M[Integer.parseInt(ram.operand)+2]);	}
	public static void STA(){	ram.M[Integer.parseInt(ram.operand)] = ram.A/65536;ram.M[Integer.parseInt(ram.operand)+1] = (ram.A%65536)/256;ram.M[Integer.parseInt(ram.operand)+2] = ram.A%256;	}
	public static void STX(){	ram.M[Integer.parseInt(ram.operand)] = ram.X/65536;ram.M[Integer.parseInt(ram.operand)+1] = (ram.X%65536)/256;ram.M[Integer.parseInt(ram.operand)+2] = ram.X%256;	}
	public static void STL(){	ram.M[Integer.parseInt(ram.operand)] = ram.L/65536;ram.M[Integer.parseInt(ram.operand)+1] = (ram.L%65536)/256;ram.M[Integer.parseInt(ram.operand)+2] = ram.L%256;	}
	public static void STCH(){	ram.M[Integer.parseInt(ram.operand)] = ram.A%256;	}
	public static void COMP(){
		if(ram.A>(ram.M[Integer.parseInt(ram.operand)]*256*256+ram.M[Integer.parseInt(ram.operand)+1]*256+ram.M[Integer.parseInt(ram.operand)+2])) ram.CC=1;
		else if(ram.A==(ram.M[Integer.parseInt(ram.operand)]*256*256+ram.M[Integer.parseInt(ram.operand)+1]*256+ram.M[Integer.parseInt(ram.operand)+2])) ram.CC=0;
		else	ram.CC=-1;
	}
	public static void TIX(){
		ram.X++;
		int op=ram.M[Integer.parseInt(ram.operand)]*256*256+ram.M[Integer.parseInt(ram.operand)+1]*256+ram.M[Integer.parseInt(ram.operand)+2];
		if(ram.X<op) ram.CC=-1;
		else if(ram.X==op) ram.CC=0;
		else	ram.CC=1;
	}

}
public class SicCode
{
	JFrame f;
        int fla=0;
	JButton run,show,step,stop;
	//JTextArea code;
        String code;
	JTable reg,mem,sym;
	JTextField memStart,text;
	SIC sic;
	int current,locctr;
	Map<String,Integer>	SYMTAB;
	Object last = null;
	SicCode()
	{
		try{
			sic = new SIC();
		}catch(Exception ec1){	/*System.exit(0);*/System.out.println(ec1.toString());	}
		current = 0;
		//locctr=1000;
		locctr=sic.getSTART();
		f = new JFrame("My Assembler");
                f.getContentPane().setBackground(Color.gray);
                       System.out.println("\t\tMY SIMULATOR\n\n");
        System.out.println("Give File Path");
        Scanner sc=new Scanner(System.in);
        String s=sc.nextLine();
            //File file = new File(s);
            BufferedReader fin;
        try {
            //fin = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            code=new Scanner(new File(s)).useDelimiter("\\Z").next();
           //code = org.apache.commons.io.IOUtils.toString(fin);
            //while((code = fin.readLine()) != null){}
            //System.out.println(code);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

		/*code = new JTextArea(600,500);
		code.setBounds(250,10,600,500);
		code.setBorder(BorderFactory.createCompoundBorder(code.getBorder(),BorderFactory.createEmptyBorder(10,50,50,50)));
		f.add(code);*/
		memStart = new JTextField();
		memStart.setBounds(400,10,100,25);
		f.add(memStart);
		show = new JButton("Get_Address");
		show.setBounds(300,10,100,25);
		show.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				String mems[][] = new String[27][2];
				String memHeads[] = {"Address","Value"};
				int start = sic.getSTART();
				try {	start = Integer.parseInt(memStart.getText());	}catch(Exception ec){}
				for(int i=0;i<mems.length;i++)
				{
					mems[i][0] = Integer.toString(start+i);
					mems[i][1] = Integer.toString(sic.getMemory(start+i));
				}
			        mem.setModel (new javax.swing.table.DefaultTableModel (mems,memHeads));
			}
		});
		f.add(show);
		run = new JButton("Run Full");
		run.setBounds(25,10,100,25);
		run.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				passOne();
				String[] lines = code.split("\n");
				//locctr = 1000;
				locctr=sic.getSTART();	//changed
				for(current=0;current<lines.length;current++)
				{
					exStep(lines[current]);
				}
                                text.setText("Pass 2 complete");
                                f.add(text);
                  //                             String mem_tab[][]=new String[4000][2];
                try{
                       PrintWriter writer = new PrintWriter("data.txt", "UTF-8");
                       for(int i=0;i<4000;i++)
                        {
                            writer.println(sic.getMemory(i));
                        }

                        writer.close();
                    } catch (IOException el) {
   // do something
}
			}
		});
		f.add(run);
		step = new JButton("Step");
		step.setBounds(125,10,75,25);
		f.add(show);
                
		step.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				String[] lines = code.split("\n");
                                if(fla==0){current=0;  passOne(); fla=1;text.setText("Symtab created");
                                f.add(text);}
				//Highlighter highlighter = code.getHighlighter();
				//HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.pink);
				//HighlightPainter eraser = new DefaultHighlighter.DefaultHighlightPainter(Color.white);
				if(current >= lines.length)
				{
					current = 0;
					//locctr=1000;
					locctr=sic.getSTART();	//changed
					passOne();
					return;
				}
				exStep(lines[current]);
				current++;
				           //    String mem_tab[][]=new String[4000][2];
                try{
                       PrintWriter writer = new PrintWriter("data.txt", "UTF-8");
                       for(int i=0;i<4000;i++)
                        {
                            writer.println(sic.getMemory(i));
                        }

                        writer.close();
                    } catch (IOException el) {
   // do something
}
			}
		});
		f.add(step);
		stop = new JButton("Pass 1");
		stop.setBounds(200,10,75,25);
		f.add(stop);
		stop.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				current = 0;
				passOne();
			}
		});
		f.add(stop);
		String regs[][] = {{"A","000000"},{"X","000000"},{"L","000000"},{"CC","0"}};
		String regHeads[] = {"Reg","Value"};
		reg = new JTable(regs,regHeads);
		reg.setBounds(50,50,150,70);
		f.add(reg);
		String syms[][] = {{"",""}};
		String symHeads[] = {"Symbol","Value"};
                sym = new JTable(syms,symHeads);
		sym.setBounds(50,140,150,270);
                
		f.add(sym);
                //JTextFild text=new JTextField(32);
		String mems[][] = {{"0000","00"},{"0001","00"},{"0002","00"},{"0003","00"},{"0004","00"},{"0005","00"},{"0006","00"},{"0007","00"},{"0008","00"},{"0009","00"},{"0010","00"},{"0011","00"},{"0012","00"},{"0013","00"},{"0014","00"},{"0015","00"},{"0016","00"},{"0017","00"},{"0018","00"},{"0019","00"},{"0020","00"},{"0021","00"},{"0022","00"},{"0023","00"},{"0024","00"},{"0025","00"},{"0026","00"},{"0027","00"},{"0028","00"},{"0029","00"},{"0030","00"},{"0031","00"}};
		String memHeads[] = {"Address","Value"};
		mem = new JTable(mems,memHeads);
		mem.setBounds(300,50,180,450);
		f.add(mem);
                //String mem_tab[][]=new String[4000][2];
                try{
                       PrintWriter writer = new PrintWriter("data.txt", "UTF-8");
                       for(int i=0;i<4000;i++)
                        {
                            writer.println(sic.getMemory(i));
                        }
                        
                        writer.close();
                    } catch (IOException e) {
   // do something
}
		f.setSize(600,600);
		f.setLayout(null);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	void passOne()
	{
		String[] lines = code.split("\n");
		SYMTAB = new HashMap<String ,Integer>();
		//locctr = 1000;
		String syms[][] = new String[25][2];
		String symHeads[] = {"Symbol","Value"};
		locctr=sic.getSTART();	//changed
		int cnt=0,symcnt=0;
		for(String i:lines)
		{
			String[] words = i.split("\\s");
			if(words.length==2 && words[0].equals("START"))
			{
				locctr=Integer.parseInt(words[1]);
				sic.START=locctr;
			}
			if(words.length > 2 && words[1].equals("BYTE"))
			{
				SYMTAB.put(words[0],locctr);
				syms[symcnt][0] = words[0];
				syms[symcnt][1] = Integer.toString(locctr);
				symcnt++;
				for(char c:words[2].toCharArray())
					sic.storeByte(c,locctr++);
			}
			else if(words.length > 2 && words[1].equals("WORD"))
			{
				SYMTAB.put(words[0],locctr);
				syms[symcnt][0] = words[0];
				syms[symcnt][1] = Integer.toString(locctr);
				symcnt++;
				sic.storeWord(Integer.parseInt(words[2]),locctr);
				locctr += 3;
			}
			else if(words.length > 2 && words[1].equals("RESW"))
			{
				SYMTAB.put(words[0],locctr);
				syms[symcnt][0] = words[0];
				syms[symcnt][1] = Integer.toString(locctr);
				symcnt++;
				locctr += Integer.parseInt(words[2])*3;
			}
			else if(words.length > 2 && words[1].equals("RESB"))
			{
				SYMTAB.put(words[0],locctr);
				syms[symcnt][0] = words[0];
				syms[symcnt][1] = Integer.toString(locctr);
				symcnt++;
				locctr += Integer.parseInt(words[2]);
			}
			else if(words.length >= 2 && !sic.checkOpCode(words[1]) && !words[1].equals("START") && !words[1].equals("END") && !words[1].equals("JLT") && !words[1].equals("JGT") && !words[1].equals("JEQ") && !words[1].equals("JUMP"))
				System.out.println("..!!..Error...!!..\nUndefined OpCode "+words[1]);
			else if(!words[0].equals(""))
			{
				SYMTAB.put(words[0],cnt);
				syms[symcnt][0] = words[0];
				syms[symcnt][1] = Integer.toString(cnt*3);
				symcnt++;
			}
			cnt++;
		}
		System.out.println(SYMTAB);
		sym.setModel (new javax.swing.table.DefaultTableModel (syms,symHeads));
                text=new JTextField();
                text.setBounds(50,450,100,50);
                text.setText("Pass 1 complete");
                f.add(text);
	}
	void exStep(String line)
	{
		String[] words = line.split("\\s");
		if(words.length < 2)	return;
		if(words[1].equals("BYTE") || words[1].equals("WORD") || words[1].equals("RESB") || words[1].equals("RESW")) return;

		String jumpcheck = words[1];
		if(jumpcheck.charAt(0)=='J'){
			int cc=sic.getCC();
			if(jumpcheck.equals("JUMP"))
			{
				int jpos = SYMTAB.get(words[2]);
				String[] lines = code.split("\n");
				words = lines[jpos].split("\\s");
				current = jpos;
			}
			else if(jumpcheck.equals("JGT") && cc==1)
			{
				int jpos = SYMTAB.get(words[2]);
				String[] lines = code.split("\n");
				words = lines[jpos].split("\\s");
				current = jpos;
			}
			else if(jumpcheck.equals("JEQ") && cc==0)
			{
				int jpos = SYMTAB.get(words[2]);
				String[] lines = code.split("\n");
				words = lines[jpos].split("\\s");
				current = jpos;
			}
			else if(jumpcheck.equals("JLT") && cc==-1)
			{
				int jpos = SYMTAB.get(words[2]);
				String[] lines = code.split("\n");
				words = lines[jpos].split("\\s");
				current = jpos;
			}


		}
		String opcode="",operand="";
		if(words[1].charAt(0) != '.')	opcode = words[1];
		if(words.length > 2 && words[2].charAt(0) != '.')
		{	int len=words[2].length();
			if(words[2].charAt(len-1)=='X' && words[2].charAt(len-2)==',')
			{
				int x=sic.getX();
				int val=SYMTAB.get(words[2].split(",")[0])+x;
				operand = Integer.toString(val);
				//System.out.println(operand);
			}
			else	operand = words[2];
		}
		if(SYMTAB.containsKey(operand))	operand = Integer.toString(SYMTAB.get(operand));
		sic.setOperand(operand);
		try{	sic.execute(opcode);}catch(Exception ec2){/*System.out.println(ec2.toString());*/}
		updateRegisters();
		updateMemory();
	}
	void updateRegisters()
	{
		String regs[][] = {{"A","000000"},{"X","000000"},{"L","000000"},{"CC","0"}};
		String regHeads[] = {"Reg","Value"};
		regs[0][1] = Integer.toString(sic.getA());
		regs[1][1] = Integer.toString(sic.getX());
		regs[2][1] = Integer.toString(sic.getL());
		regs[3][1] = Integer.toString(sic.getCC());
	        reg.setModel (new javax.swing.table.DefaultTableModel (regs,regHeads));
	}
	public void updateMemory()
	{
		String mems[][] = new String[28][2];
		String memHeads[] = {"Address","Value"};
		int start = sic.getSTART();
		try {	start = Integer.parseInt(memStart.getText());	}catch(Exception ec){}
		for(int i=0;i<mems.length;i++)
		{
			mems[i][0] = Integer.toString(start+i);
			mems[i][1] = Integer.toString(sic.getMemory(start+i));
		}
	        mem.setModel (new javax.swing.table.DefaultTableModel (mems,memHeads));
	}
	public static void main(String args[])
	{
		new SicCode();
	}
}
