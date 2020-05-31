package demo

import scala.io.StdIn.readLine

object MyApp extends App with CustomExecutionContext {

  println(s"Limit?")
  val limit = readLine.toInt

  val ruleList = RuleLoader.load(limit)
  val matchedRuleList = RuleMatcher.matchRules(passport, ruleList)

  println(s"Done!")

  def passport = {
    val tagging = Tagging("http://www.bbc.co.uk/ontologies/passport/predicate/About",
      "http://www.bbc.co.uk/things/aaa4bc67-8118-4f94-b91a-007467f685ba#id")
    Passport("loc:1234",
      "en-gb",
      "http://www.bbc.co.uk/ontologies/passport/home/News",
      List(tagging),
      Some("AVAILABLE"))
  }
}

