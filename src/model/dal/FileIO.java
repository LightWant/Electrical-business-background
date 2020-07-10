package model.dal;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
	
	public static String[] writeAlllines(String filename, String s[]) throws IOException {
		Output out = new Output(filename, true);
		out.outputlines(s);
		out.close();
		return s;
	}

}

class Input extends FileReader {
	Input(String filename) throws FileNotFoundException {
		super(filename);
	}
	private BufferedReader bf;
	private String s;
	private ArrayList<String> arraylist = new ArrayList<String>();
	
	public String[] InputLines() throws IOException {
		arraylist.clear();
		bf = new BufferedReader(this);
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
	Output(String filename, boolean flag) throws IOException {
		super(filename, flag);
	}
	private PrintWriter pw;
	
	public void outputlines(String[] s) throws IOException {
		pw = new PrintWriter(this);
		pw.println(s[0]);
		for(int i = 1; i < s.length; i++) {
			pw.println("\n" + s[i]);
			pw.flush();
		}
		pw.close();
	}
	public void outputline(String s) throws IOException {
		pw = new PrintWriter(this);
		pw.println(s);
		pw.flush();
		pw.close();
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
