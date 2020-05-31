package demo

import org.mongodb.scala.{Document, MongoCollection}

object DB {

  import org.mongodb.scala.MongoDatabase

      val connectionUri = "mongodb://localhost:27017"
//
//  val connectionUri = "mongodb://admin:7yQE3CrrAE3XD5pb@mgdb508.int.mongodb.otg.bbc.co.uk:36035,mgdb112.int.mongodb.otg.bbc.co.uk:36035,mgdb509.int.mongodb.otg.bbc.co.uk:36035/?replicaSet=cps-int&ssl=true"

  private val mongoClient = MongoClientFactory.getClient(connectionUri)

  private val database: MongoDatabase = mongoClient.getDatabase("curation-rules-db")

  val ruleList: MongoCollection[Document] = database.getCollection("curationrules")
}
