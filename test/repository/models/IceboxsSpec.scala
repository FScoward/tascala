package repository.models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._


class IceboxsSpec extends Specification {

  "Iceboxs" should {

    val i = Iceboxs.syntax("i")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Iceboxs.find(1L)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Iceboxs.findBy(sqls.eq(i.taskId, 1L))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Iceboxs.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Iceboxs.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Iceboxs.findAllBy(sqls.eq(i.taskId, 1L))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Iceboxs.countBy(sqls.eq(i.taskId, 1L))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Iceboxs.create(taskId = 1L)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Iceboxs.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Iceboxs.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Iceboxs.findAll().head
      val deleted = Iceboxs.destroy(entity) == 1
      deleted should beTrue
      val shouldBeNone = Iceboxs.find(1L)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = Iceboxs.findAll()
      entities.foreach(e => Iceboxs.destroy(e))
      val batchInserted = Iceboxs.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
