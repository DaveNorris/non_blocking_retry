package demo

import java.util.concurrent.{ConcurrentSkipListSet, Executors, TimeUnit}

import org.joda.time.DateTime

import scala.concurrent.duration.FiniteDuration

trait LiteScheduler {

  protected def period: Long

  protected def resolveTime: DateTime

  private val itemList = new ConcurrentSkipListSet[QueuedItem]
  private val serviceThread = Executors.newScheduledThreadPool(1)

  protected class QueuedItem(block: => Unit, val executionTime: Long) extends Comparable[QueuedItem] {
    val id = java.util.UUID.randomUUID.toString

    def runBlock: Unit = block

    override def compareTo(other: QueuedItem): Int = {
      if (this.executionTime > other.executionTime) 1
      else if (this.executionTime < other.executionTime) -1
      else {
        if (this.id < other.id) 1 else -1
      }
    }
  }

  def apply(delay: FiniteDuration)(block: => Unit): Unit = {
    val executionTime = resolveTime.getMillis + delay.toMillis
    val queuedItem = new QueuedItem(block, executionTime)
    itemList.add(queuedItem)
  }

  private def init = {
    val runnable = new Runnable {
      override def run = service
    }
    serviceThread.scheduleAtFixedRate(runnable, 0, period, TimeUnit.MILLISECONDS)
  }

  private def service: Unit =
    if (!itemList.isEmpty) {
      val lastItem = itemList.pollFirst
      val now = new DateTime().getMillis
      if (lastItem.executionTime <= now) {
        lastItem.runBlock
        service
      } else
        itemList.add(lastItem)
    }

  init
}

object LiteScheduler extends LiteScheduler {
  protected def period: Long = 200

  protected def resolveTime: DateTime = new DateTime()
}
