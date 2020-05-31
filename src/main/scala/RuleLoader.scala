package demo

import org.joda.time.DateTime
import org.json4s.DefaultFormats
import org.json4s.jackson.JsonMethods.parse
import org.mongodb.scala._
import org.mongodb.scala.model.Projections.excludeId

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Try

object RuleLoader extends CustomExecutionContext {

  protected implicit val jsonFormats = DefaultFormats

  def load(limit: Int): Seq[CurationMetadata] = {
    println(s"loading rules limited to $limit...")

    val startLoad = DateTime.now().getMillis
    val docList = Await.result(DB.ruleList.find().limit(limit).projection(excludeId).toFuture(), 60.minutes)
    val stopLoad = DateTime.now().getMillis

    println(s"*** loaded ${docList.size} rules in ${stopLoad - startLoad} milliseconds")

    val convertedList = convert(docList)
    filterOutErrors(convertedList)
  }

  protected def convert(docList: Seq[Document]): Seq[Try[CurationMetadata]] = {
    println(s"*** converting rules...")

    val startConvert = DateTime.now().getMillis
    val convertedList = docList map { doc =>
      Try {
        val docJson = doc.toJson
        val jValueDoc = parse(docJson)
        jValueDoc.extract[CurationMetadata]
      }
    }
    val stopConvert = DateTime.now().getMillis

    println(s"*** converted rules in ${stopConvert - startConvert} milliseconds")
    convertedList
  }

  protected def filterOutErrors(docList: Seq[Try[CurationMetadata]]): Seq[CurationMetadata] = {
    println(s"*** filtering out unconverted rules...")

    val startFilter = DateTime.now().getMillis
    val filteredList = docList.filter(_.isSuccess).map(_.get)
    val stopFilter = DateTime.now().getMillis

    println(s"*** filtered out unconverted rules in ${stopFilter - startFilter} milliseconds")
    filteredList
  }

}
