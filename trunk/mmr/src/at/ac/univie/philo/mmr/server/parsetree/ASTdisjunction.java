/* Generated By:JJTree: Do not edit this line. ASTdisjunction.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package at.ac.univie.philo.mmr.server.parsetree;

public
class ASTdisjunction extends SimpleNode {
  public ASTdisjunction(int id) {
    super(id);
  }

  public ASTdisjunction(crflang p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(crflangVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=c68100ab127435f0a0d548d96b5581cb (do not edit this line) */
