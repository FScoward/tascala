package repository.models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._


class OauthsSpec extends Specification {

  "Oauths" should {

    val o = Oauths.syntax("o")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Oauths.find(1L, 1L)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Oauths.findBy(sqls.eq(o.oauthId, 1L))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Oauths.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Oauths.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Oauths.findAllBy(sqls.eq(o.oauthId, 1L))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Oauths.countBy(sqls.eq(o.oauthId, 1L))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Oauths.create(oauthId = 1L, service = "MyString", userId = 1L)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Oauths.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Oauths.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Oauths.findAll().head
      val deleted = Oauths.destroy(entity) == 1
      deleted should beTrue
      val shouldBeNone = Oauths.find(1L, 1L)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = Oauths.findAll()
      entities.foreach(e => Oauths.destroy(e))
      val batchInserted = Oauths.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
