package demo

case class Provenance(confidence: Double)

case class MatchClause(predicate: String,
                       associatedObject: String,
                       provenance: Option[Provenance])

case class LogicClause(operator: String, operands: List[Either[MatchClause,LogicClause]])

case class CurationMetadata(id: String, mode: String, rules: LogicClause)
