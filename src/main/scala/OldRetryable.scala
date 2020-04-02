package demo

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration
import scala.util.{Failure, Success, Try}

object OldRetryable extends CustomExecutionContext {

  def withRetries(attemptsRemaining: Int, delay: FiniteDuration)(block: => Future[Boolean]): Future[Boolean] = {

    def retry =
      if (attemptsRemaining == 0) {
        println(s"*** Failed")
        Future.successful(false)
      } else {
        Thread.sleep(delay.toMillis)
        withRetries(attemptsRemaining - 1, delay)(block)
      }

    Try {
      block
    } match {
      case Success(result: Future[Boolean]) =>
        result.flatMap {
          case true =>
            println(s"*** Succeeded")
            Future.successful(true)
          case false =>
            retry
        } recoverWith {
          case _ => {
            println(s"*** Retrying, remainingTries = $attemptsRemaining")
            retry
          }
        }
      case Failure(error) =>
        retry
    }
  }

}
