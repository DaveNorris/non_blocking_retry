package demo

object RuleMatcher {

  def matchRules(passport: Passport, ruleList: Seq[CurationMetadata]): Seq[CurationMetadata] = {
    // Hard-coded dummy match
    ruleList.filter(rule => {
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
  }
}

case class Tagging(value: String, predicate: String)

case class Passport(locator: String,
                    language: String,
                    home: String,
                    taggings: Seq[Tagging],
                    availability: Option[String])

