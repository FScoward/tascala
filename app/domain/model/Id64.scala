package domain.model

import scala.util.Random
import java.security.SecureRandom
import java.util.concurrent.atomic.AtomicLong
import org.joda.time.DateTimeUtils

/**
  * 64 bit unique id generator
  * Features:
  * 1. generate ascending or descending ids
  * 2. 64 bit id consists of:
  *    - 41 bits of time in ms since epoch
  *    - 23 bits of one of the following:
  *       1. incr or decr counter starting at a random value
  *       2. pseudo random value
  *       3. secure random value
  * 3. ascending id is backward compatible with Twitter Snowflake ids (https://github.com/twitter/snowflake).
  */
object Id64 {

  val EPOCH = 1288834974657L //Twepoch (4 Nov 2010 01:42:54.657 GMT)
  val RANDOM_BIT = 22
  val MAX_RANDOM = 1  << RANDOM_BIT
  val MAX_TIME   = 1L << (63 - RANDOM_BIT)

  private val sr = new SecureRandom()
  private val counterStart = Random.nextInt(MAX_RANDOM)
  private val counter = new AtomicLong(counterStart)

  def time() = DateTimeUtils.currentTimeMillis()
  def nextAscId() = nextSeqAscId()
  def nextDescId() = nextSeqDescId()

  def nextSeqAscId(now: Long = time()) =
    makeAscId(now, (counter.getAndIncrement % MAX_RANDOM).toInt)

  def nextSeqDescId(now: Long = time()) = {
    val r = (counterStart - counter.getAndIncrement) % MAX_RANDOM
    makeDescId(now, (if (r < 0) r + MAX_RANDOM else r).toInt)
  }

  def nextPseRandId(make: (Long,Int) => Long = makeAscId, now: Long = time()) =
    make(now, Random.nextInt(MAX_RANDOM))

  def nextSecRandId(make: (Long,Int) => Long = makeAscId, now: Long = time()) =
    make(now, sr.nextInt(MAX_RANDOM))

  def parseId(id: Long, descending: Boolean = false): (Long, Int) = {
    val ts = id >> RANDOM_BIT
    val time = if (descending) MAX_TIME - (ts - EPOCH) else EPOCH + ts
    val rand = id & (MAX_RANDOM - 1)
    (time, rand.toInt)
  }

  def makeAscId(now: Long, rnd: Int): Long = {
    val sinceEpoch = now - EPOCH
    (sinceEpoch << RANDOM_BIT) | rnd
  }

  def makeDescId(now: Long, rnd: Int): Long = {
    val ts = MAX_TIME - (now - EPOCH)
    (ts << RANDOM_BIT) | rnd
  }

}

