package demo

import java.util.concurrent.Executors

import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContext, Future, Promise}


object Retryable {

  private implicit val ec = ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor)

  def apply[A](maxTries: Int, delay: FiniteDuration)(block: => Future[A]): Future[A] = {

    def loop(remainingTries: Int, promise: Promise[A]): Unit = {
      block map { result =>
        promise.success(result)
      } recover {
        case e => {
          remainingTries > 1 match {
            case false => {
              println(s"*** Finished retrying, thread ID = ${Thread.currentThread.getId}")
              promise.failure(e)
            }
            case true => {
              println(s"*** Retrying, try = $remainingTries thread ID = ${Thread.currentThread.getId}")
              LiteScheduler(delay.toMillis) {
                loop(remainingTries - 1, promise)
              }
            }
          }
        }
      }
    }

    val promise = Promise[A]()
    loop(maxTries, promise)
    promise.future
  }

}

