/* Generated By:JJTree: Do not edit this line. ASTexpression.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package at.ac.univie.philo.mmr.server.parsetree;

public
class ASTexpression extends SimpleNode {
  public ASTexpression(int id) {
    super(id);
  }

  public ASTexpression(crflang p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(crflangVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=c592e81aa1e6e3aae02a25dc25be88cd (do not edit this line) */