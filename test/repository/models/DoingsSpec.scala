package repository.models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._
import java.time.{ZonedDateTime}


class DoingsSpec extends Specification {

  "Doings" should {

    val d = Doings.syntax("d")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Doings.find(1L, null, null)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Doings.findBy(sqls.eq(d.taskId, 1L))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Doings.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Doings.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Doings.findAllBy(sqls.eq(d.taskId, 1L))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Doings.countBy(sqls.eq(d.taskId, 1L))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Doings.create(taskId = 1L, start = null, end = null)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Doings.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Doings.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Doings.findAll().head
      val deleted = Doings.destroy(entity) == 1
      deleted should beTrue
      val shouldBeNone = Doings.find(1L, null, null)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = Doings.findAll()
      entities.foreach(e => Doings.destroy(e))
      val batchInserted = Doings.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
