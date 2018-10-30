/**
 * We extend the DepthFirstAdapter.  
 * Visitors invoke a defaultCase method on each node they visit.  
 * We override this method so that it
 * prints out dot info about a node.
 */


/**
 * This ast walker generates AVR output for the AST.  
 */

package ast_visitors;

import java.io.*;
import java.util.Stack;

import ast.node.*;
import symtable.*;
import ast.visitor.*;

public class AVRgenVisitor extends DepthFirstVisitor
{
    private SymTable st; 
    private PrintWriter out; 
    private Stack<Integer> nodeStack;
	private int nodeCount = 0;
	private int breakCount = 0;
    public AVRgenVisitor(PrintWriter pw, SymTable st) 
    {   
        this.out = pw; 
        this.st = st; 
        this.nodeStack = new Stack<Integer>();
    } 

   /** Upon entering each node in AST, check of this node is the root
   to generate start of .dot file, output the dot output for the node.
   */
   public void defaultIn(Node node) {
       if (nodeStack.empty()) {
           //out.println("digraph ASTGraph {");
		out.println("\t.file  \"main.java\""
			+"\n__SREG__ = 0x3f"
			+"\n__SP_H__ = 0x3e"
			+"\n__SP_L__ = 0x3d"
			+"\n__tmp_reg__ = 0"
			+"\n__zero_reg__ = 1"
			+"\n\t.global __do_copy_data"
			+"\n\t.global __do_clear_bss"
			+"\n\t.text"
			+"\n.global main"
			+"\n\t.type   main, @function"
			+"\nmain:"
			+"\n\tpush r29"
			+"\n\tpush r28"
			+"\n\tin r28,__SP_L__"
			+"\n\tin r29,__SP_H__"
			+"\n/* prologue: function */"
			+"\ncall _Z18MeggyJrSimpleSetupv");
       }

       nodeDotOutput(node);
       
       // store this node id on the nodeStack
       // the call to nodeDotOutput increments for
       // the next guy so we have to decrement here
       nodeStack.push(nodeCount-1);    
   }
   
   public void defaultOut(Node node) {

       nodeStack.pop();
     if (nodeStack.empty()) {
		out.println("/* epilogue start */"
			+"\n\tendLabel:"
			+"\n\tjmp endLabel"
			+"\n\tret"
			+"\n\t.size   main, .-main");
     }else{
		if(node instanceof MeggySetPixel){
			out.println("\n\t/* pop args off of stack */"
				+"\n\tpop r20"
				+"\n\tpop r22"
				+"\n\tpop r24"
				+"\n\t/* Draw pixels, display slate */"
				+"\n\tcall _Z6DrawPxhhh"
				+"\n\tcall _Z12DisplaySlatev"
				);
			
		}
		else if(node instanceof ByteCast){
			out.println("\n\t/* cast to byte */"
				+"\n\tpop r25"
				+"\n\tpop r24"
				+"\n\tpush r24"
				);
			
		}
		else if(node instanceof MeggyGetPixel){
			out.println("\n\t/* pop args off of stack */"
				+"\n\tpop r20"
				+"\n\tpop r22"
				+"\n\tcall _Z6ReadPxhh"
				+"\n\tpush r24"
				);
		}
		else if(node instanceof EqualExp){
			out.println("\n\t# equality check expression"
				+"\n\tpop    r18"
				+"\n\tpop    r24"
				+"\n\tcp    r24, r18"
				+"\nbreq MJ_L"+ (breakCount+2)
				+"\n"
				+"\n\t#result is false"
				+"\nMJ_L"+ (++breakCount) +":"
				+"\n\tldi     r24, 0"
				+"\n\tjmp      MJ_L"+(breakCount+2)
				+"\n"
				+"\n\t# result is true"
				+"\nMJ_L"+ (++breakCount) +":"
				+"\n\tldi     r24, 1"
				+"\n"
				+"\n\t# store result of equal expression"
				+"\nMJ_L"+ (++breakCount) +":"
				+"\n\tpush   r24"
				);
		}
		else if(node instanceof NotExp){
			out.println("\n\t# not operation"
				+"\n\tpop    r24"
				+"\n\tldi     r22, 1"
				+"\n\teor     r24,r22"
				+"\n\tpush   r24"
				);
		}
		else if(node instanceof IfStatement){
			out.println("\n\t# load condition and branch if false"
				+"\n\tpop    r24"
				+"\n\t#load zero into reg"
				+"\n\tldi    r25, 0"
				+"\n"
				+"\n\t#use cp to set SREG"
				+"\n\tcp     r24, r25"
				+"\n\t#WANT breq MJ_L"+ (++breakCount)
				+"\n\tbrne   MJ_L"+ (breakCount+1)
				+"\n\tjmp    MJ_L" + breakCount 
			);
		}/*
		else if(node instanceof ){
			out.println(
				+"\n"
				+"\n"
				+"\n"
				+"\n"
				);
		}

		else if(node instanceof ){
			out.println(
				+"\n"
				+"\n"
				+"\n"
				+"\n"
				);
		}

		else if(node instanceof ){
			out.println(
				+"\n"
				+"\n"
				+"\n"
				+"\n"
				);
		}

		else if(node instanceof ){
			out.println(
				+"\n"
				+"\n"
				+"\n"
				+"\n"
				);
		}
*/

	 }
     out.flush();
   }
   
   /* A helper output routine that generates the
    * dot node and the dot edges from the parent
    * to the node.
    */
   private void nodeDotOutput(Node node)
   {
       // dot node
       //out.print(nodeCount);
       //out.print(" [ label=\"");
       //printNodeName(node);
       //iif (node instanceof ILiteral) {
       //    out.print("\\n");
       //    out.print(node.toString());
       //}
/*		else if(node instanceof Program){
			out.println(""
				+""
				+""
				+""
				+""
				+""
				);
			
		}
		else if(node instanceof MainClass){
			out.println(""
				+""
				+""
				+""
				+""
				+""
				);
			
		}
		else if(node instanceof BlockStatement){
			out.println(""
				+""
				+""
				+""
				+""
				+""
				);
			
		}*/
		if(node instanceof IntLiteral){
			IntLiteral iNode = (IntLiteral) node;
			out.println("\n\t/* load int "+ iNode.getIntValue() + "*/"
				+"\n\tldi r24,lo8(" + iNode.getIntValue() + ")"
				+"\n\tldi r25,hi8("+ iNode.getIntValue() +")"
				+"\n\n\t/* push int "+ iNode.getIntValue() + "onto stack */"
				+"\n\tpush r24"
				+"\n\tpush r25"
				);
		}
		else if(node instanceof ColorLiteral){
			ColorLiteral clNode = (ColorLiteral) node;
			out.println("\n\t/* load " + clNode.getIntValue() + " for "+ clNode.getLexeme() +" */"
				+"\n\tldi r22, " + clNode.getIntValue() 
				+"\n\tpush r22"
				);
		}
		else if(node instanceof IfStatement){
			out.println("\n\t#### if statement");
		}
		else if(node instanceof MeggyDelay){
			out.println("\n\t### Meggy.delay() call"
				+"\n\tpop    r24"
				+"\n\tpop    r25"
				+"\n\tcall   _Z8delay_msj"
				+"\n\t"
				);
		}
		/*else if(node instanceof ){
			
		}
		else if(node instanceof ){
			
		}*/
       //out.println("\" ];");
       
       // print dot edge to parent
       //if (!nodeStack.empty()) {
           //out.print(nodeStack.peek());
           //out.print(" -> ");
           //out.println(nodeCount);
       //}
       
       // increment for ourseves
       nodeCount++;
   }

   /** A helper that trims a node's class name before printing it.
    * (E.g., "node.Token" --> "Token".) 
    */
   private void printNodeName(Node node) {
      String fullName = node.getClass().getName();
      String name = fullName.substring(fullName.lastIndexOf('.')+1);
      
      out.print(name);
   }
   
}
