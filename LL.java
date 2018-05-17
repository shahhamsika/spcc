import java.io.*;
import java.util.*;

public class LL{
	
	static HashMap<String, String> acctyp;

	public static void main(String[] args) {
		HashMap<String, HashMap<String, String>> gram = new HashMap<String, HashMap<String, String>>();

		acctyp = new HashMap<String, String>();
		acctyp.put("i", "TA");
		gram.put("E", acctyp);

		acctyp = new HashMap<String, String>();
		acctyp.put("+", "+TA");
		acctyp.put("$", "epsilon");
		gram.put("A", acctyp);

		acctyp = new HashMap<String, String>();
		acctyp.put("i", "FB");
		gram.put("T", acctyp);		

		acctyp = new HashMap<String, String>();
		acctyp.put("*", "*FB");
		acctyp.put("$", "epsilon");
		acctyp.put("+", "epsilon");
		gram.put("B", acctyp);		

		acctyp = new HashMap<String, String>();
		acctyp.put("i", "i");
		gram.put("F", acctyp);				

		Stack<String> stack = new Stack<String>();
		stack.push("$"); stack.push("E"); 

		Scanner sc = new Scanner(System.in);
		String input = sc.next();

		int point = 0; boolean err = false;

		while(!stack.empty()){
			String lhs = stack.peek();
			String ans = gram.get(lhs).get(input.charAt(point)+"");

			if(ans == null){
				err = true;
				System.out.println("Invalid String");
				break;
			}

			if(ans.equals("epsilon")){
				stack.pop();
			}

			if(stack.peek().charAt(0) >= 'A' && stack.peek().charAt(0) <= 'Z'){
				if(!ans.equals("epsilon")){
					stack.pop();		
					for(int k = ans.length()-1; k >= 0; k--)
						stack.push(ans.charAt(k)+"");
				}
			}

			if(stack.peek().charAt(0) == input.charAt(point)){
				stack.pop();		
				point++;
			}else if(stack.peek().charAt(0) >= 'A' && stack.peek().charAt(0) <= 'Z'){

			}else {
				if(!ans.equals("epsilon")){
					err = true;
					System.out.println("Invalid String");
					break;		
				}
			}
			printOp(stack, input, ans, point);
		}
	}
	static void printOp(Stack<String> stack, String input, String ans, int point){
		System.out.print(Arrays.toString(stack.toArray()));
		System.out.print("\t");
		for(int i = point; i < input.length(); i++)
			System.out.print(input.charAt(i));
		System.out.print("\t"+ans+"\n");
	}
}