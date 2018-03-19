package repository.models

import scalikejdbc._

case class Oauths(
  oauthId: Long,
  service: String,
  userId: Long) {

  def save()(implicit session: DBSession = Oauths.autoSession): Oauths = Oauths.save(this)(session)

  def destroy()(implicit session: DBSession = Oauths.autoSession): Int = Oauths.destroy(this)(session)

}


object Oauths extends SQLSyntaxSupport[Oauths] {

  override val schemaName = Some("tascala")

  override val tableName = "OAuths"

  override val columns = Seq("oauth_id", "service", "user_id")

  def apply(o: SyntaxProvider[Oauths])(rs: WrappedResultSet): Oauths = apply(o.resultName)(rs)
  def apply(o: ResultName[Oauths])(rs: WrappedResultSet): Oauths = new Oauths(
    oauthId = rs.get(o.oauthId),
    service = rs.get(o.service),
    userId = rs.get(o.userId)
  )

  val o = Oauths.syntax("o")

  override val autoSession = AutoSession

  def find(oauthId: Long, userId: Long)(implicit session: DBSession = autoSession): Option[Oauths] = {
    withSQL {
      select.from(Oauths as o).where.eq(o.oauthId, oauthId).and.eq(o.userId, userId)
    }.map(Oauths(o.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Oauths] = {
    withSQL(select.from(Oauths as o)).map(Oauths(o.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Oauths as o)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Oauths] = {
    withSQL {
      select.from(Oauths as o).where.append(where)
    }.map(Oauths(o.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Oauths] = {
    withSQL {
      select.from(Oauths as o).where.append(where)
    }.map(Oauths(o.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Oauths as o).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    oauthId: Long,
    service: String,
    userId: Long)(implicit session: DBSession = autoSession): Oauths = {
    withSQL {
      insert.into(Oauths).namedValues(
        column.oauthId -> oauthId,
        column.service -> service,
        column.userId -> userId
      )
    }.update.apply()

    Oauths(
      oauthId = oauthId,
      service = service,
      userId = userId)
  }

  def batchInsert(entities: Seq[Oauths])(implicit session: DBSession = autoSession): List[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        'oauthId -> entity.oauthId,
        'service -> entity.service,
        'userId -> entity.userId))
    SQL("""insert into OAuths(
      oauth_id,
      service,
      user_id
    ) values (
      {oauthId},
      {service},
      {userId}
    )""").batchByName(params: _*).apply[List]()
  }

  def save(entity: Oauths)(implicit session: DBSession = autoSession): Oauths = {
    withSQL {
      update(Oauths).set(
        column.oauthId -> entity.oauthId,
        column.service -> entity.service,
        column.userId -> entity.userId
      ).where.eq(column.oauthId, entity.oauthId).and.eq(column.userId, entity.userId)
    }.update.apply()
    entity
  }

  def destroy(entity: Oauths)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(Oauths).where.eq(column.oauthId, entity.oauthId).and.eq(column.userId, entity.userId) }.update.apply()
  }

}
