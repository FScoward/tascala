package repository.models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._
import java.time.{LocalDate}


class TimetablesSpec extends Specification {

  "Timetables" should {

    val t = Timetables.syntax("t")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Timetables.find(null, 1L, 123, 1L, null)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Timetables.findBy(sqls.eq(t.timeTableId, null))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Timetables.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Timetables.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Timetables.findAllBy(sqls.eq(t.timeTableId, null))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Timetables.countBy(sqls.eq(t.timeTableId, null))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Timetables.create(taskId = 1L, order = 123, userId = 1L)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Timetables.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Timetables.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Timetables.findAll().head
      val deleted = Timetables.destroy(entity) == 1
      deleted should beTrue
      val shouldBeNone = Timetables.find(null, 1L, 123, 1L, null)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = Timetables.findAll()
      entities.foreach(e => Timetables.destroy(e))
      val batchInserted = Timetables.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
