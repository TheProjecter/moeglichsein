/* Generated By:JJTree: Do not edit this line. ASTbiconditional.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package at.ac.univie.philo.mmr.server.parsetree;

public
class ASTbiconditional extends SimpleNode {
  public ASTbiconditional(int id) {
    super(id);
  }

  public ASTbiconditional(crflang p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(crflangVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=c71130bfd6f66cc5228dda8d42fd6a90 (do not edit this line) */
