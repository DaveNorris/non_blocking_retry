package demo


import scala.concurrent.Future
import scala.concurrent.duration._
import scala.io.StdIn._


object MyApp extends App {

  def failedFunction = Future.failed(new Exception("An error"))

  println(s"Ready?")
  readLine

  (1 to 3500) foreach { _ =>
    OldRetryable.withRetries(60, 500.milliseconds) {
      failedFunction
    }
  }

  println(s"Done!")
}

