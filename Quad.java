import java.util.*;
import java.io.*;

public class Quad{
	public static void main(String args[]){
		Stack<String> stack = new Stack<String>();
		int count=1;
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter postfix String");
		String express = sc.next();

		for(int i=0; i<express.length(); i++){
			char ch = express.charAt(i);
			switch(ch){
				case '+':
				case '-':
				case '*':
				case '/':
					String op2 = stack.pop();
					String op1 = stack.pop();
					stack.push("t"+count);
					System.out.println(ch+"\t"+op1+"\t"+op2+"\tt"+count);
					count++;
				break;
				default:
					stack.push(ch+"");
			}
		}
	}
}
