package model.dal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FileIO {

	public static String[] readAlllines(String filename) throws IOException {
		String[] s;
		Input in = new Input(filename);
		s = in.InputLines();
		in.close();
		return s;
	}
	
	public static String[] writeAlllines(Boolean flag, String filename, String s[]) throws IOException {
		Output out = new Output(filename, flag);
		out.outputlines(s);
		out.close();
		return s;
	}

	public static String read(String filename) {
		String[] s=null;
		try {
			s = readAlllines(filename);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(s == null)
			return null;
		
		String res = s[0];
		for(int i = 1; i < s.length; i++) {
			res += "\n"+s[i];
		}
		return res;
	}
	
	public static void write(String data, String filename) {
		Output out = null;
		try {
			out = new Output(filename, true);
			System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaa");
			out.outputline(data);
			System.out.println("write!");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class Input extends FileReader {
	String filename;
	Input(String filename) throws FileNotFoundException {
		super(filename);
		this.filename = filename;
	}
	private BufferedReader bf;
	private String s;
	private ArrayList<String> arraylist = new ArrayList<String>();
	
	public String[] InputLines() throws IOException {
		arraylist.clear();
		InputStreamReader isr = new InputStreamReader(new FileInputStream(this.filename), "UTF-8");
		 
		bf = new BufferedReader(isr);;
		while((s = bf.readLine()) != null) {
			arraylist.add(s);
		}
		bf.close();
		this.close();
		
		String s[] = new String[arraylist.size()];
		for(int i = 0; i < s.length; i++) {
			s[i] = arraylist.get(i);
		}
		
		return s;
	}
	
	public void inputdata(String filename) {
		try {
			FileInputStream fin = new FileInputStream(filename);
			DataInputStream din = new DataInputStream(fin);
			
			while(true) {
				try {
					din.readInt();
					din.readByte();
					din.readLong();
					din.readFloat();
					din.readFloat();
					din.readChar();
					
				} catch (IOException e) {
					/*   */
					break;
				}
			};
			
			try {
				fin.close();
				din.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}

class Output extends FileWriter {
	String filename;
	Output(String filename, boolean flag) throws IOException {
		super(filename, flag);
		this.filename = filename;
	}
	private PrintWriter pw;
	
	public void outputlines(String[] s) throws IOException {
		pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.filename), "utf-8"));;
		pw.println(s[0]);
		for(int i = 1; i < s.length; i++) {
			pw.println("\n" + s[i]);
			pw.flush();
		}
		pw.close();
	}
	@SuppressWarnings("resource")
	public void outputline(String s) throws IOException {
//		pw = new PrintWriter(
//		new OutputStreamWriter(new FileOutputStream(this.filename), "utf-8"));
//		//System.out.println("write!!!\n"+s+"\n"+this.filename);
//		pw.print(s);
//		pw.flush();
//		pw.close();
//		System.out.println("down");
		String fileName = this.filename;
		File file = new File(fileName);

		BufferedWriter writer = null;
		FileOutputStream writerStream = new FileOutputStream(file);
		writer = new BufferedWriter(new OutputStreamWriter(writerStream, "UTF-8"));

		writer.write(s);
		writer.flush();
		writer.close();
	}
	
	public void outputdata(String filename) {
		try {
			FileOutputStream fout = new FileOutputStream(filename);
			DataOutputStream dout = new DataOutputStream(fout);
			
			while(true) {
				try {
					dout.writeInt(1);
					dout.writeDouble(12.000);
					
				} catch (IOException e) {
					/*   */
					break;
				}
			};
			
			try {
				fout.close();
				dout.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void outputdata(char data) {
		pw = new PrintWriter(this);
		pw.print(data);
		pw.flush();
		pw.close();
	}
	public void outputdata(int data) {
		pw = new PrintWriter(this);
		pw.print(data);
		pw.flush();
		pw.close();
	}
	public void outputdata(double data) {
		pw = new PrintWriter(this);
		pw.print(data);
		pw.flush();
		pw.close();
	}
}
