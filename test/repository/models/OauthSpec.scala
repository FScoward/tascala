package repository.models

import scalikejdbc.specs2.mutable.AutoRollback
import org.specs2.mutable._
import scalikejdbc._


class OauthSpec extends Specification {

  "Oauth" should {

    val o = Oauth.syntax("o")

    "find by primary keys" in new AutoRollback {
      val maybeFound = Oauth.find(1L, 1L)
      maybeFound.isDefined should beTrue
    }
    "find by where clauses" in new AutoRollback {
      val maybeFound = Oauth.findBy(sqls.eq(o.usersUserId, 1L))
      maybeFound.isDefined should beTrue
    }
    "find all records" in new AutoRollback {
      val allResults = Oauth.findAll()
      allResults.size should be_>(0)
    }
    "count all records" in new AutoRollback {
      val count = Oauth.countAll()
      count should be_>(0L)
    }
    "find all by where clauses" in new AutoRollback {
      val results = Oauth.findAllBy(sqls.eq(o.usersUserId, 1L))
      results.size should be_>(0)
    }
    "count by where clauses" in new AutoRollback {
      val count = Oauth.countBy(sqls.eq(o.usersUserId, 1L))
      count should be_>(0L)
    }
    "create new record" in new AutoRollback {
      val created = Oauth.create(oauthId = 1L, usersUserId = 1L)
      created should not beNull
    }
    "save a record" in new AutoRollback {
      val entity = Oauth.findAll().head
      // TODO modify something
      val modified = entity
      val updated = Oauth.save(modified)
      updated should not equalTo(entity)
    }
    "destroy a record" in new AutoRollback {
      val entity = Oauth.findAll().head
      val deleted = Oauth.destroy(entity) == 1
      deleted should beTrue
      val shouldBeNone = Oauth.find(1L, 1L)
      shouldBeNone.isDefined should beFalse
    }
    "perform batch insert" in new AutoRollback {
      val entities = Oauth.findAll()
      entities.foreach(e => Oauth.destroy(e))
      val batchInserted = Oauth.batchInsert(entities)
      batchInserted.size should be_>(0)
    }
  }

}
