import java.util.*;
import java.io.*;

public class LeftRec{
	public static void main(String args[]){
		String[] initProductions = new String[10];
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter no. of productions");
		int n = sc.nextInt();
		System.out.println("Enter productions");
		for(int i=0;i<n;i++){
			initProductions[i] = sc.next();
		}
		System.out.println("--------------------");
		
		for(int i=0; i<n; i++){
			String p = initProductions[i];
			String prod[] = p.split("->");
			String left = prod[0];
			String right[] = prod[1].split("\\|");

			if(right[0].startsWith(left)){
				System.out.println(left+"->"+right[1]+left+"'");
				System.out.println(left+"'->"+right[0].substring(1)+left+"'|e");
			}else{
				System.out.println(p);
			}
		}
	}
}