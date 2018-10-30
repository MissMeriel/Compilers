/**
 * This ast walker generates dot output for the AST.  
 *
 * 6/06 - Modified from Brian Richard's ParserTest.Java.
 *        Michelle Strout
 */
package ast_visitors;

import java.io.PrintWriter;
import java.util.Stack;
import java.util.HashMap;

import ast.visitor.DepthFirstVisitor;
import ast.node.*;
import symtable.SymTable;
import symtable.Type;

/**
 * We extend the DepthFirstAdapter.  
 * Visitors invoke a defaultCase method on each node they visit.  
 * We override this method so that it
 * prints out dot info about a node.
 */

public class BuildSymTable extends DepthFirstVisitor
{
    private SymTable symTable;

    public SymTable getSymTable() {
        return this.symTable;
    }   

   /** Constructor takes a PrintWriter, and stores in instance var. */
   public BuildSymTable() {
	HashMap<Node, Type> map = new HashMap();
	this.symTable = new SymTable(map);
   }

   
   /** Upon entering each node in AST, check of this node is the root
   to generate start of .dot file, output the dot output for the node.
   */
   public void defaultIn(Node node) {
   }
   
   public void defaultOut(Node node) {
   }
  

 
   /* A helper output routine that generates the
    * dot node and the dot edges from the parent
    * to the node.
    */
/*   private void nodeDotOutput(Node node)
   {
		if(node.getType() != null){
			Type expType = node.getType();
			this.symTable.setExpType(node, expType);
		}
   }
*/
   /** A helper that trims a node's class name before printing it.
    * (E.g., "node.Token" --> "Token".) 
    */
   private void printNodeName(Node node) {
      String fullName = node.getClass().getName();
      String name = fullName.substring(fullName.lastIndexOf('.')+1);
      System.out.print(name);
   }
   
   private String getNodeName(Node node) {
      String fullName = node.getClass().getName();
      String name = fullName.substring(fullName.lastIndexOf('.')+1);
      return name;
   }

}
