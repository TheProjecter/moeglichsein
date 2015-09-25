This is a first draft of an modal logic tool made for the department of philosophy at University Of Vienna to introduce Philosophy Students with modal logics. The main purpose is to view and edit models and to evaluate sentences by using LaTeX-like syntax.

It is written in Google Web Toolkit (GWT) and hosted on Google App Engine (GAE), although there is not much Server functionality needed yet (except parsing and evaluation of logic expressions).

Basic features:
  * Browse through a pre-defined modal logic model to understand basic elements (possible world, predicate, individual, extension)
  * Express modal logic expressions with a easy-to-learn syntax (based on LaTeX)
  * Automatic evaluation of expressions with stepwise log of the evaluation process
  * Explore the consequences of editing the model on evaluating formulas
  * Compliance-check with basic constraints for world accessibility relation (REFLEXIVE, S5)
