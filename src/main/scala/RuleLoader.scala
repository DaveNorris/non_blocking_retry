package demo

import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods.parse
import org.mongodb.scala._
import org.mongodb.scala.model.Projections.excludeId

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}

object RuleLoader extends CustomExecutionContext {

  protected implicit val jsonFormats = DefaultFormats

  def load(limit: Int): Seq[Option[CurationMetadata]] = {
    val docList = Await.result(DB.ruleList.find().limit(limit).projection(excludeId).toFuture(), 60.minutes)
    println(s"*** docList size = ${docList.size}")
    docList map { doc =>
      Try {
        val docJson = doc.toJson
        val jValueDoc = parse(docJson)
        jValueDoc.extract[CurationMetadata]
      } match {
        case Success(r) => Some(r)
        case Failure(e) => None
      }
    }
  }

}
