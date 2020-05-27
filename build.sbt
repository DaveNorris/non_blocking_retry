name := "dpub171-spike"

version := "0.1"

scalaVersion := "2.11.12"

val Json4sVersion = "3.6.7"

// set the main class for packaging the main jar
mainClass in (Compile, packageBin) := Some("demo.MyApp")

libraryDependencies ++= Seq(
  "org.mongodb.scala" %% "mongo-scala-driver" % "2.5.0",
  "org.json4s" %% "json4s-jackson" % Json4sVersion,
  "org.json4s" %% "json4s-native" % Json4sVersion,
  "org.json4s" %% "json4s-ext" % Json4sVersion,
  "org.dispatchhttp" %% "dispatch-core" % "1.0.0" exclude("com.sun.activation", "javax.activation"),
)

assembly / assemblyMergeStrategy := {
  case PathList("META-INF", "io.netty.versions.properties", xs@_*) => MergeStrategy.discard // xs @ _* means pick everything that matched
  case "mime.types" => MergeStrategy.filterDistinctLines
  case x =>
    val oldStrategy = (assembly / assemblyMergeStrategy).value
    oldStrategy(x)
}

resolvers ++= Seq(
  "BBC Forge Maven Releases" at "https://dev.bbc.co.uk/maven2/releases/",
  "BBC Forge Maven Snapshots" at "https://dev.bbc.co.uk/maven2/snapshots",
  "BBC Forge Artifactory" at "https://dev.bbc.co.uk/artifactory/repo",
  "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
)

