package demo

import org.joda.time.DateTime

object RuleMatcher {

  def matchRules(passport: Passport, ruleList: Seq[CurationMetadata]): Seq[CurationMetadata] = {
    println(s"*** matching rules...")

    val startMatching = DateTime.now().getMillis

    // Hard-coded dummy match
    val matchedRuleList = ruleList.filter(rule => {
      val operands = rule.rules.operands
      if (operands.nonEmpty) {
        val firstOperand = operands(0)
        if (firstOperand.isLeft) {
          val x = firstOperand.left.get.associatedObject
          x == passport.home
        } else
          false
      } else
        false
    })

    val stopMatching = DateTime.now().getMillis

    println(s"*** matched ${matchedRuleList.size} rules in ${stopMatching - startMatching} milliseconds")
    matchedRuleList
  }
}

case class Tagging(value: String, predicate: String)

case class Passport(locator: String,
                    language: String,
                    home: String,
                    taggings: Seq[Tagging],
                    availability: Option[String])

