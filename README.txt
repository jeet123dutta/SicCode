# SicCode

Tools for SIC hypothetical computer from the Leland Beck's book System Software. Includes:

  * Assembler
  * Simulator
  
Assembler supports all instructions and directives described in the book. This includes load/store instructions ( LDA,LDX,LDL,LDCH,STA,STX,STL,STCH,ADD,SUB,MUL,DIV,AND,OR), arithmetic instructions, jumps (JUMP,JGT,JLT,JEQ) etc. And directives START, END . Some features:
  * DIRECT addressing ,INDEXED addressing
  * standard directives START, END
  * Full forward and backward references resolved via the 	algorithm described in the book
  * assembler syntax is more 	flexbile and free than 	original
  * generates debugging-friendly listing file showing original 	source and corresponding address and generated code
  * generates log file showing code statistics, list of 	blocks, list of sections, list of symbols, list of 	literals, list of relocations
  * generates Object Code
  * generates OPTAB , SYMTAB
  * and more
  
Simulator is user-friendly GUI based application that loads asm or sic files. Features:
  * CPU view of registers and current instructions, shows 	changed registers in text boxes, supports changing 	registers values
  * disassembly view and breakpoints
  * memory view with full edit support in hexadecimal and       	character mode
  * textual screen support
  * automatic execution with set speed (from 1 Hz to 1 MHz)
  * uses GUI in JFrame
  * stores data in memory address in TextFile
  * and more


Installation
------------

*   Download java 7 or any newer version .

Usage
-----

* Compile java code using javac
* Run the .java file


By,
Abhijit Dutta     001410501004
Gautam Kumar	  001410501005
Hare Ram		  001410501008
Suman Chaurasia	  001410501002
Swapnil Nandi	  001410501001

Group 4 / Year 2016
Jadavpur University, B.C.S.E. 3rd Year 1st Semester 