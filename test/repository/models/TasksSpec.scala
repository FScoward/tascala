package repository.models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._


class TasksSpec extends Specification {

  "Tasks" should {

    val t = Tasks.syntax("t")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Tasks.find(1L)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Tasks.findBy(sqls.eq(t.taskId, 1L))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Tasks.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Tasks.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Tasks.findAllBy(sqls.eq(t.taskId, 1L))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Tasks.countBy(sqls.eq(t.taskId, 1L))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Tasks.create(taskId = 1L, userId = 1L)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Tasks.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Tasks.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Tasks.findAll().head
      val deleted = Tasks.destroy(entity) == 1
      deleted should beTrue
      val shouldBeNone = Tasks.find(1L)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = Tasks.findAll()
      entities.foreach(e => Tasks.destroy(e))
      val batchInserted = Tasks.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
