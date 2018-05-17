%{
	#include<stdio.h>
	int keywords=0, identifiers=0;
%}
ws [ \t\n]+
digit [+|-]?[0-9]+
alphabet [A-Za-z]+

%%

#.* printf("header files - %s\n", yytext);
void|auto|int|extern|if|else|short|break|case|typedef|goto|static|float|long|byte|struct|do|char|while|default|switch {printf("Keyword - %s\n",yytext); keywords++;}
{alphabet}({alphabet}|{digit})* {printf("Identifier - %s\n", yytext); identifiers++;}
{digit} printf("constants - %s\n", yytext);
[+|-|*|/|=]+ printf("operators - %s\n", yytext);
[<>=!]=? printf("relops - %s\n", yytext);
[(){};,] printf("delimiters - %s\n", yytext);
\".*\" printf("String const - %s\n", yytext);
{ws}+

%%

int main(void){
	yylex();
	printf("no.of keywords %d\n", keywords);
	printf("no.of identifiers %d\n", identifiers);
	return 0;
}
