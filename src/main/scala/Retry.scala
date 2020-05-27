package demo

import java.util.concurrent.Executors

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, Future, Promise}


object Retryable {

  private implicit val ec = ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor)

  def apply[A](maxTries: Int, delay: FiniteDuration)(block: => Future[A]): Future[A] = {

    def loop(remainingTries: Int, promise: Promise[A]): Unit = {
      block map { result =>
        println(s"*** Succeeded: result = $result")
        promise.success(result)
      } recover {
        case e =>
          if (remainingTries > 1) {
            println(s"*** Retrying, remainingTries = $remainingTries")
            LiteScheduler(delay) {
              loop(remainingTries - 1, promise)
            }
          } else {
            println(s"*** Failed")
            promise.failure(e)
          }
      }
    }

    val promise = Promise[A]
    loop(maxTries, promise)
    promise.future
  }

}

