grammar Antlr;

program
	: 'grammar' IDENTIFIER ';' ('@header' /*BLOCK_BRACES*/)? rule_* ;

rule_
	: 'fragment'? IDENTIFIER ':' ruleBody  ('->' 'skip')? ';' ;

ruleBody
	: IDENTIFIER
	| STRING
	| ruleBody ruleBody+
	| ruleBody '|' ruleBody+
	| '(' ruleBody ')'
	| ruleBody selector
	| RANGE
	;

selector
	: '?' | '+' | '*' ;




// Lexer

IDENTIFIER
	: [a-zA-Z_][a-zA-Z_0-9]* ;

STRING
	: '\'' ~[']* '\'' ;

RANGE
	: '~'? '[' .+? ']'
	| '.'
	;


LINE_COMMENT
	: '//' ~[\r\n]* -> skip ;

BLOCK_COMMENT
	: '/*' .*? '*/' -> skip ;

BLOCK_BRACES
	: '{' .*? '}' -> skip ;

WHITE_SPACE
	: [ \t\r\n]+ -> skip ;
