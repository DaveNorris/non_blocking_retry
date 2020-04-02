package demo

import java.util.concurrent.{Executors, ThreadFactory}

import com.google.common.util.concurrent.ThreadFactoryBuilder

import scala.concurrent.ExecutionContext

trait CustomExecutionContext {
  implicit val executionContext: ExecutionContext = CustomThreadPool.pool
}

object CustomThreadPool {
  val threadFactory: ThreadFactory =
    new ThreadFactoryBuilder()
      .setNameFormat("vivo-stream-notification-bridge-custom-pool-%d")
      .build()

  val pool: ExecutionContext =
    ExecutionContext.fromExecutor(Executors.newFixedThreadPool(3500, threadFactory))
}
