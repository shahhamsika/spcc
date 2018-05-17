#include<stdio.h>
#include<string.h>
#include<stdlib.h>
#define RX 1

struct MOT{
	char op[4];
	int frmt;
	int len;
}m[10];
struct SYM{
	char sym[20];
	int add;
	int len;
	char rel;
	int val;
}s[20];
struct LIT{
	char sym[20];
	int add;
	int len;
	char rel;
}l[20];
struct BT{
	int bit;
	int value;
}b[20];
void initMOT(){
	strcpy(m[0].op,"L");
	m[0].frmt=RX;
	m[0].len=4;
	strcpy(m[1].op,"A");
	m[1].frmt=RX;
	m[1].len=4;
	strcpy(m[2].op,"ST");
	m[2].frmt=RX;
	m[2].len=4;
}

int main(){
	char *POT[6]={"USING","START","EQU","DS","END\n","DC"};
	initMOT();
	int sCount=0,lCount=0,number=0,location=0,nextline=0,slen=0,x=0;
	size_t len = 0;
	ssize_t read;
	
	char * str;
	char * str1;
	char *line;
	int i,flag=0;
	FILE *file;
	file = fopen( "Code.asm" , "r");
	if(file){
		printf("\n---------PASS 1------------\n");
		while ((read = getline(&line, &len, file)) != -1){
			nextline=0;
			str=strtok(line," ");
			while(str!=NULL){
				flag=0;
				for(i=0;i<6 && flag!=1;i++)
				{
					if(strcmp(str,POT[i])==0)
					{
						flag=1;
						if(strcmp(str,"START")==0){			//get start location 
							str=strtok(NULL," ");
							location=atoi(str);
							nextline=1;
						}
						else if(strcmp(str,"USING")==0){		//do nothing
							nextline=1;
						}
						else if(strcmp(str,"EQU")==0){			//get number set value in ST
							nextline=1;
							str=strtok(NULL," ");
							number=atoi(str);
							s[sCount-1].val=number;
							s[sCount-1].rel='A';
						}
						else if(strcmp(str,"DC")==0){			//get F'4' and store 4
							nextline=1;
							str=strtok(NULL," ");
							if(str[0]=='F')
							{
							location=location+4;
							s[sCount-1].len=4;
							}
							else if(str[0]=='I')
							{
							location=location+2;
							s[sCount-1].len=2;
							}
							number=atoi(&str[2]);
							s[sCount-1].val=number;
							s[sCount-1].rel='R';
						}
						else if(strcmp(str,"DS")==0){				//store value and size
							nextline=1;
							str=strtok(NULL," ");
							slen=strlen(str);
							if(str[len-1]=='F')
							{
							number=0;
							for(i=0;i<len-1;i++)
							number=number*10+ atoi(&str[i]);
							}
							else
							number=atoi(str);
							s[sCount-1].val=number;
							s[sCount-1].rel='R';
						}
					}
				}
				for(i=0;i<3 && flag!=1;i++)
				{
					if(strcmp(str,m[i].op)==0)
					{
						flag=1;
						location=location+m[i].len;		//L<-length

						str=strtok(NULL," "); //r1		//check for literal
						str=strtok(NULL," "); //,
						str=strtok(NULL," ");

						if(str[0]=='='){
						x=0;
						number=strlen(str);
						for(i=1;i<strlen(str);i++){
							strcpy(&l[lCount].sym[x++],&str[i]);		//store literal
						}
						l[lCount].sym[number-2]='\0';				//to make it string
						l[lCount].len=4;
						l[lCount].rel='R';
						lCount++;
						}
						nextline=1;
					}
				}
				if(strcmp(str,"LTORG\n")==0){
					flag=1;
					for(i=0;i<lCount;i++)
					{
						l[i].add=location;
						location=location+l[i].len;
					}
					nextline=1;
				}

				if(flag==0)										//store symbols
				{
					strcpy(s[sCount].sym,str);
					s[sCount].add=location;
					s[sCount].val=location;
					s[sCount].len=1;
					s[sCount].rel='R';
					sCount++;
				}
				if(nextline==1)
				break;
				else
					str = strtok (NULL, " ");
			}
		}
	}
	fclose(file);
	
	printf(" SYMBOL TABLE\n");
	printf("\nADDRESS\tSYMBOL\tVALUE\tLENGTH\tRELOCATION\n");
	for(i=0;i<sCount;i++)
	printf("\n%d\t%s\t%d\t%d\t%c\n",s[i].add,s[i].sym,s[i].val,s[i].len,s[i].rel);
	printf("\n LITERAL TABLE\n");
	printf("\nADDRESS\tSYMBOL\tLENGTH\tRELOCATION\n");
	for(i=0;i<lCount;i++)
	printf("\n%s\t%d\t%d\t%c\n",l[i].sym,l[i].add,l[i].len,l[i].rel);
	
	
	
	
	
	

	printf("\n--------PASS 2-----------\n");
	int motfind=0;
	int k,ltrange,lflag,dis,d;
	int strange,sflag;
	number=0;location=0;nextline=0;slen=0;x=0;
	char strc[20],prestr[20];
	
	file = fopen( "Code.asm" , "r");
	if(file){
		while ((read = getline(&line, &len, file)) != -1){
			nextline=0;
			str=strtok(line," ");
			strcpy(prestr,str);

			while(str!=NULL){
				flag=0;
				for(i=0;i<6 && flag!=1;i++){
					if(strcmp(str,POT[i])==0)
					{
							flag=1;
							if(strcmp(str,"START")==0){
								str=strtok(NULL," ");
								location=atoi(str);
								nextline=1;
							}
							else if(strcmp(str,"USING")==0){		//using * , 15
								str=strtok(NULL," ");
								if(strcmp(str,"*")==0){
									str=strtok(NULL," ");//,
									str=strtok(NULL," ");//15
									number = atoi(str);
									b[number].bit=1;
									b[number].value=location;
								}
								nextline=1;
							}
							else if(strcmp(str,"DC")==0){			//scan ST and get value using prestr
								strange=sCount;
								sflag=0;
								
								
								for(k=0;k<=strange && sflag!=1;k++){
									if(strcmp(prestr,s[k].sym)==0){
										sflag=1;
										printf(" %d\n",s[k].val);
										location=location+s[k].len;
									}
								}
								nextline=1;
							}
							else if(strcmp(str,"DS")==0){				//simply print 
								printf(" -\n");
							}
					}
				}
				for(i=0;i<3 && flag!=1;i++)
				{
					if(strcmp(str,m[i].op)==0)
					{
						flag=1;
						location=location+m[i].len;					// LC increment

						printf("\n%s",str);
						str=strtok(NULL," "); //r1
						printf(" %s",str);
						str=strtok(NULL," "); //,
						printf(" %s",str);
						str=strtok(NULL," ");
												
						
								if(str[0]=='='){					//if literal
									ltrange=lCount;
									lflag=0;
									
									x=0;

									number=strlen(str);
									for(i=1;i<strlen(str);i++){
										strcpy(&strc[x++],&str[i]);
									}
									strc[number-2]='\0';
									for(k=0;k<ltrange && lflag!=1 ;k++){
										if(strcmp(strc,l[k].sym)==0){
											lflag=1;
												for(d=0;d<16;d++){
													if(b[d].bit==1){
														dis=l[k].add - 0 -b[d].value;
													}
												}
											printf(" %d(%d,%d)\n",dis,0,--d);
										}
									}
									nextline=1;
								}
								else
								{
									strange=sCount;
									sflag=0;
									strcpy(strc,str);
									strc[strlen(strc)-1] = '\0';  // removing \n that comes along with last word of sentence
									
									for(k=0;k<=strange && sflag !=1 ;k++){
									
										if(strcmp(strc,s[k].sym)==0){
										sflag=1;
										
										for(d=0;d<16;d++){
											if(b[d].bit==1){
												dis=s[k].add - 0 - b[d].value;
											}
										}
										printf(" %d(%d,%d)\n",dis,0,--d);
										}
									}
									nextline=1;
								}
					}
				}
				if(flag==0)
				{
				//printf("\nSYMBOL:%s\n",str);
				}
				if(nextline==1)
				break;
				else
				str = strtok (NULL, " ");
			}
		} 
	}
	fclose(file);
	printf("%d",location);
	return 0;
}
