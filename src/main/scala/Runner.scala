package demo

import org.joda.time.DateTime

import scala.io.StdIn.readLine

object MyApp extends App with CustomExecutionContext {

  println(s"Limit?")
  val limit = readLine.toInt

  println(s"loading rules limited to $limit...")

  val startLoad = DateTime.now().getMillis
  val unfilteredRules = RuleLoader.load(limit)
  val rules = unfilteredRules.filter(_.isDefined).flatten
  val stopLoad = DateTime.now().getMillis

  println(s"*** finished loading ${rules.size} rule documents in ${stopLoad - startLoad} milliseconds")

  val tagging = Tagging("http://www.bbc.co.uk/ontologies/passport/predicate/About",
    "http://www.bbc.co.uk/things/aaa4bc67-8118-4f94-b91a-007467f685ba#id")
  val passport = Passport("loc:1234",
    "en-gb",
    "http://www.bbc.co.uk/ontologies/passport/home/News",
    List(tagging),
    Some("AVAILABLE"))

  val startMatch = DateTime.now().getMillis
  val matchedRules = RuleMatcher.matchRules(passport, rules)
  val stopMatch = DateTime.now().getMillis

  println(s"*** finished matching ${matchedRules.size} rule documents in ${stopMatch - startMatch} milliseconds")
}

