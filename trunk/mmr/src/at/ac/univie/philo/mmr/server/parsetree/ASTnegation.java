/* Generated By:JJTree: Do not edit this line. ASTnegation.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package at.ac.univie.philo.mmr.server.parsetree;

public
class ASTnegation extends SimpleNode {
  public ASTnegation(int id) {
    super(id);
  }

  public ASTnegation(crflang p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(crflangVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=d351a1d56eccaf6aa7f1f85b8bcf0610 (do not edit this line) */