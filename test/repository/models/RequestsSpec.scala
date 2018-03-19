package repository.models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._


class RequestsSpec extends Specification {

  "Requests" should {

    val r = Requests.syntax("r")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Requests.find(1L)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Requests.findBy(sqls.eq(r.requestId, 1L))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Requests.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Requests.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Requests.findAllBy(sqls.eq(r.requestId, 1L))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Requests.countBy(sqls.eq(r.requestId, 1L))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Requests.create(requestId = 1L, taskId = 1L)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Requests.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Requests.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Requests.findAll().head
      val deleted = Requests.destroy(entity) == 1
      deleted should beTrue
      val shouldBeNone = Requests.find(1L)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = Requests.findAll()
      entities.foreach(e => Requests.destroy(e))
      val batchInserted = Requests.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
