/* Generated By:JJTree: Do not edit this line. ASTquantification.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package at.ac.univie.philo.mmr.server.parsetree;

public
class ASTquantification extends SimpleNode {
  public ASTquantification(int id) {
    super(id);
  }

  public ASTquantification(crflang p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(crflangVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=f1951b5ac425ee57f92f0ed282f4aad9 (do not edit this line) */