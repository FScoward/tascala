package repository.models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._
import java.time.{ZonedDateTime}


class DonesSpec extends Specification {

  "Dones" should {

    val d = Dones.syntax("d")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Dones.find(1L)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Dones.findBy(sqls.eq(d.taskId, 1L))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Dones.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Dones.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Dones.findAllBy(sqls.eq(d.taskId, 1L))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Dones.countBy(sqls.eq(d.taskId, 1L))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Dones.create(taskId = 1L, requiredTime = 123, doneDateTime = null)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Dones.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Dones.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Dones.findAll().head
      val deleted = Dones.destroy(entity) == 1
      deleted should beTrue
      val shouldBeNone = Dones.find(1L)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = Dones.findAll()
      entities.foreach(e => Dones.destroy(e))
      val batchInserted = Dones.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
