name := "demo"

version := "0.1"

scalaVersion := "2.13.1"

// set the main class for packaging the main jar
mainClass in (Compile, packageBin) := Some("demo.MyApp")
