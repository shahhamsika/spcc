import java.io.*;
import java.util.*;

public class FirstFollow{
	static String gram[][], fst[], fllw[];
	static char nonterms[], terms[];
	static int ntlen, tlen, n;
	public static void main(String[] args) throws IOException{
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Enter nonterms: ");
		String nt = br.readLine();
		ntlen = nt.length();
		nonterms = new char[ntlen];
		nonterms = nt.toCharArray();

		System.out.println("Enter terms: ");
		String t = br.readLine();
		tlen = t.length();
		terms = new char[tlen];
		terms = t.toCharArray();

		System.out.println("Specify grammar (9 for epsilon): ");
		gram = new String[ntlen][];

		for(int i = 0; i < ntlen; i++){
			System.out.println("Enter no. of productions for "+nonterms[i]);
			n = Integer.parseInt(br.readLine());
			gram[i] = new String[n];
			for(int j = 0; j < n; j++){
				gram[i][j] = br.readLine();
			}
		}

		fst = new String[ntlen];
		for(int i = 0; i < ntlen; i++)
			fst[i] = first(i);
		System.out.println("First set: ");
		for(int i = 0; i < ntlen; i++)
			System.out.println(removeDup(fst[i]));

		fllw = new String[ntlen];
		for(int i = 0; i < ntlen; i++)
			fllw[i] = follow(i);
		System.out.println("Follow set: ");
		for(int i = 0; i < ntlen; i++)
			System.out.println(removeDup(fllw[i]));
	}
	static String first(int i){
		int j, k, l, found = 0;
		String str="", temp = "";
		for(j = 0; j < gram[i].length; j++){
			for(k = 0; k < gram[i][j].length() ; k++){
				for(l = 0; l < ntlen; l++){
					if(gram[i][j].charAt(k) == nonterms[l]){
						str = first(l);
						if(!(str.length()==1 && str.charAt(0)=='9')){
							temp = temp + str;
						}
						found = 1;
						break;
					}
				}
				if(found == 1){
					if(str.contains("9"))
						continue;
				}else{
					temp = temp + gram[i][j].charAt(k);
				}
				break;
			}
		}
		return temp;
	}
	static String follow(int i){
		String temp = "";
		char[] pro, chr;
		int j, k, l, m, n, found = 0;
		
		if(i == 0)
			temp = "$";

		for(j = 0; j < ntlen; j++){
			for(k = 0; k < gram[j].length; k++){
				pro = new char[gram[j][k].length()];
				pro = gram[j][k].toCharArray();
				for(l = 0; l < pro.length; l++){
					if(pro[l] == nonterms[i]){
						if(l == pro.length - 1){
							if (j < i)
								temp = temp + fllw[j];
						}else{
							for(m = 0; m < ntlen; m++){
								if(pro[l+1] ==  nonterms[m]){
									chr = new char[fst[m].length()];
									chr = fst[m].toCharArray();
									for(n = 0; n < chr.length; n++){
										if (chr[n] == '9'){
											if(l+1 == pro.length-1)
												temp = temp + follow(j);
											else
												temp = temp + follow(m);
										}else{
											temp = temp + chr[n];
										}
									}
									found = 1;
								}
							}
							if(found!=1)
								temp = temp + pro[l+1];
						}
					}
				}
			}
		}
		return temp;
	}
	static String removeDup(String str){

		boolean[] seen = new boolean[256];
		StringBuilder sb = new StringBuilder(seen.length);

		for(int i = 0; i < str.length(); i++){
			char ch = str.charAt(i);
			if(!seen[ch]){
				seen[ch] = true;
				sb.append(ch);
			}
		}
		return sb.toString();
	}
}