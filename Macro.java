import java.io.*;
import java.util.*;
class MNT{
	int addr;
	String name;
	String[] ala = new String[10];

	public String getName(){ return name; }
	public void setName(String name){ this.name = name; }

	public int getAddr(){ return addr;}
	public void setAddr(int i){ this.addr = i; }
	
	public String getAla(int i){ return ala[i]; }
	public void setAla(int i, String val){ ala[i] = val;}

	public int findInAla(String word){
		for(int i=0;i<ala.length;i++)
			if(word.equals(ala[i]))
				return i;
		return -1;
	}
}
public class Macro{
	static BufferedReader br;
	static boolean foundM = false, foundMend = false;
	static int i = 0, mntc = 1, mdtc = 1, mdtp = 0;
	static String mdt[] = new String[80];
	static MNT[] mnt = new MNT[10];
	public static void main(String[] args) {

		try{
			br = new BufferedReader(new FileReader("/home/hamsika/Desktop/mcode.asm"));
			String line = br.readLine();
			while(line!=null){
				
				String[] arrOfStr = line.split(" ");

				if(foundM){
					i=0;
					mnt[mntc] = new MNT();
					for(String word : arrOfStr){
						if(word.startsWith("&")){
							mnt[mntc].setAla(i, word); i++;
						}else{
							mnt[mntc].setName(word);
							mnt[mntc].setAddr(mdtc);
						}
					}
					mntc++;
					mdt[mdtc++] = line;
					foundM = false;
				}else{
					if(line.equals("MACRO")){
						foundM = true; foundMend = false;
					}
					else if(line.equals("MEND")){
						foundMend = true;
						mdt[mdtc++] = line;
					}else if(!foundMend){
						for(String word : arrOfStr){
							if(word.startsWith("&")){
								int index = mnt[mntc-1].findInAla(word);
								String temp = "#"+Integer.toString(index);
								line = line.replace(word, temp);
							}
						}
						mdt[mdtc++] = line;
					}else if(line.equals("END")){

					}
				}
				line = br.readLine();
			}
			System.out.println("----MDT-----");
			for(int k = 1; k<mdtc; k++){
				System.out.println(mdt[k]);
			}
			System.out.println("MNTC = "+mntc);
			System.out.println("MDTC = "+mdtc);

			System.out.println("----PASS 2-----");

			foundM = false; foundMend = false;
			br = new BufferedReader(new FileReader("/home/hamsika/Desktop/mcode.asm"));
			line = br.readLine();

			while(line!=null){
				String[] arrOfStr = line.split(" ");
				if(line.equals("MACRO")){
					foundMend = false; foundM = true;
				}
				else if(line.equals("MEND")){
					foundMend = true;
				}
				else if(foundM && foundMend){
					int index = 0;
					for(String word : arrOfStr){
						for(int k = 1; k<mntc; k++)
							if(word.equals(mnt[k].getName())){
								index = k; break;
							}
					}
					if(index>0){
						i = 0;
						mdtp = mnt[index].getAddr();
						for(String word : arrOfStr){
							if(!word.equals(mnt[index].getName())){
								mnt[index].setAla(i, word);
								i++;
							}
						}
						String temp = mdt[++mdtp];
						while(!temp.equals("MEND")){
							String[] arrOfReplace = temp.split(" ");
							for(String word : arrOfReplace){
								if(word.startsWith("#")){
									String sub = word.substring(1);
									int replaceIndex = Integer.parseInt(sub);
									temp = temp.replace(word, mnt[index].getAla(replaceIndex));
								}
							}
							System.out.println(temp);
							temp = mdt[++mdtp];
						}
					}else{
						System.out.println(line);
					}
				}
				line = br.readLine();
			}
			br.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}