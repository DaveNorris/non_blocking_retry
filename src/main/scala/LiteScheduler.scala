package demo

import java.util.concurrent.{ConcurrentSkipListSet, Executors, TimeUnit}

import org.joda.time.DateTime

trait LiteScheduler {

  protected def resolveTime: DateTime

  private val itemList = new ConcurrentSkipListSet[QueuedItem]
  private val serviceThread = Executors.newScheduledThreadPool(1)

  private class QueuedItem(block: => Unit, val executionTime: Long) extends Comparable[QueuedItem] {
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

  def apply(delay: Long)(block: => Unit) = {
    val executionTime = resolveTime.getMillis + delay
    val queuedItem = new QueuedItem(block, executionTime)
    val added = itemList.add(queuedItem)
    assert(added)
  }

  private def init = {
    val runnable = new Runnable {
      override def run = service
    }
    serviceThread.scheduleAtFixedRate(runnable, 100, 100, TimeUnit.MILLISECONDS)
  }

  private def service = {
    var doIt: Boolean = true
    var count = 0

    while (doIt) {
      if (!itemList.isEmpty) {
        val lastItem = itemList.pollFirst
        assert(lastItem != null)
        val now = new DateTime().getMillis
        if (lastItem.executionTime <= now) {
          count = count + 1
          lastItem.runBlock
        } else {
          val added = itemList.add(lastItem)
          assert(added)
          doIt = false
        }
      }
      else
        doIt = false
    }
  }

  init
}

object LiteScheduler extends LiteScheduler {
  protected def resolveTime: DateTime = new DateTime()
}
