package demo

import com.mongodb.{Block, ConnectionString}
import org.mongodb.scala.connection.{ClusterSettings, NettyStreamFactoryFactory, SslSettings}
import org.mongodb.scala.{MongoClient, MongoClientSettings, MongoCredential}

object MongoClientFactory {

  def getClient(connectionUri: String): MongoClient = {
    val user = "admin"
    val database = "admin"

    val passwordRegex = ":([^/].*)@".r
    val password = passwordRegex.findFirstMatchIn(connectionUri)

    val credential = password.map( pwdMatch => MongoCredential.createCredential(user, database, pwdMatch.group(1).toCharArray))

    val useSsl = connectionUri.contains("ssl=true")

    val initialSettings = MongoClientSettings.builder()
      .applyToClusterSettings(new Block[ClusterSettings.Builder]() {
        override def apply(b: ClusterSettings.Builder): Unit = b.applyConnectionString(new ConnectionString(connectionUri))
      })
      .applyToSslSettings(new Block[SslSettings.Builder]() {
        override def apply(b: SslSettings.Builder): Unit = b.enabled(useSsl)
      })
      .streamFactoryFactory(NettyStreamFactoryFactory())

    val finalSettings = credential match {
      case Some(cred) => initialSettings.credential(cred)
      case None => initialSettings
    }

    MongoClient(finalSettings.build())
  }

  def hidePasswordFromConnectionString(connectionString: String) : String = {
    connectionString.replaceFirst(":[^/]+@",":<hidden>@")
  }

}
