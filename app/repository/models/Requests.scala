package repository.models

import scalikejdbc._

case class Requests(
  requestId: Long,
  taskId: Long,
  destination: Option[String] = None) {

  def save()(implicit session: DBSession = Requests.autoSession): Requests = Requests.save(this)(session)

  def destroy()(implicit session: DBSession = Requests.autoSession): Int = Requests.destroy(this)(session)

}


object Requests extends SQLSyntaxSupport[Requests] {

  override val schemaName = Some("tascala")

  override val tableName = "Requests"

  override val columns = Seq("request_id", "task_id", "destination")

  def apply(r: SyntaxProvider[Requests])(rs: WrappedResultSet): Requests = apply(r.resultName)(rs)
  def apply(r: ResultName[Requests])(rs: WrappedResultSet): Requests = new Requests(
    requestId = rs.get(r.requestId),
    taskId = rs.get(r.taskId),
    destination = rs.get(r.destination)
  )

  val r = Requests.syntax("r")

  override val autoSession = AutoSession

  def find(requestId: Long)(implicit session: DBSession = autoSession): Option[Requests] = {
    withSQL {
      select.from(Requests as r).where.eq(r.requestId, requestId)
    }.map(Requests(r.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Requests] = {
    withSQL(select.from(Requests as r)).map(Requests(r.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Requests as r)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Requests] = {
    withSQL {
      select.from(Requests as r).where.append(where)
    }.map(Requests(r.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Requests] = {
    withSQL {
      select.from(Requests as r).where.append(where)
    }.map(Requests(r.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Requests as r).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    requestId: Long,
    taskId: Long,
    destination: Option[String] = None)(implicit session: DBSession = autoSession): Requests = {
    withSQL {
      insert.into(Requests).namedValues(
        column.requestId -> requestId,
        column.taskId -> taskId,
        column.destination -> destination
      )
    }.update.apply()

    Requests(
      requestId = requestId,
      taskId = taskId,
      destination = destination)
  }

  def batchInsert(entities: Seq[Requests])(implicit session: DBSession = autoSession): List[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        'requestId -> entity.requestId,
        'taskId -> entity.taskId,
        'destination -> entity.destination))
    SQL("""insert into Requests(
      request_id,
      task_id,
      destination
    ) values (
      {requestId},
      {taskId},
      {destination}
    )""").batchByName(params: _*).apply[List]()
  }

  def save(entity: Requests)(implicit session: DBSession = autoSession): Requests = {
    withSQL {
      update(Requests).set(
        column.requestId -> entity.requestId,
        column.taskId -> entity.taskId,
        column.destination -> entity.destination
      ).where.eq(column.requestId, entity.requestId)
    }.update.apply()
    entity
  }

  def destroy(entity: Requests)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(Requests).where.eq(column.requestId, entity.requestId) }.update.apply()
  }

}
