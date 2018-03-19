package repository.models

import scalikejdbc._
import java.time.{ZonedDateTime}

case class Doings(
  taskId: Long,
  start: ZonedDateTime,
  end: ZonedDateTime) {

  def save()(implicit session: DBSession = Doings.autoSession): Doings = Doings.save(this)(session)

  def destroy()(implicit session: DBSession = Doings.autoSession): Int = Doings.destroy(this)(session)

}


object Doings extends SQLSyntaxSupport[Doings] {

  override val schemaName = Some("tascala")

  override val tableName = "Doings"

  override val columns = Seq("task_id", "start", "end")

  def apply(d: SyntaxProvider[Doings])(rs: WrappedResultSet): Doings = apply(d.resultName)(rs)
  def apply(d: ResultName[Doings])(rs: WrappedResultSet): Doings = new Doings(
    taskId = rs.get(d.taskId),
    start = rs.get(d.start),
    end = rs.get(d.end)
  )

  val d = Doings.syntax("d")

  override val autoSession = AutoSession

  def find(taskId: Long, start: ZonedDateTime, end: ZonedDateTime)(implicit session: DBSession = autoSession): Option[Doings] = {
    withSQL {
      select.from(Doings as d).where.eq(d.taskId, taskId).and.eq(d.start, start).and.eq(d.end, end)
    }.map(Doings(d.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Doings] = {
    withSQL(select.from(Doings as d)).map(Doings(d.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Doings as d)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Doings] = {
    withSQL {
      select.from(Doings as d).where.append(where)
    }.map(Doings(d.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Doings] = {
    withSQL {
      select.from(Doings as d).where.append(where)
    }.map(Doings(d.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Doings as d).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    taskId: Long,
    start: ZonedDateTime,
    end: ZonedDateTime)(implicit session: DBSession = autoSession): Doings = {
    withSQL {
      insert.into(Doings).namedValues(
        column.taskId -> taskId,
        column.start -> start,
        column.end -> end
      )
    }.update.apply()

    Doings(
      taskId = taskId,
      start = start,
      end = end)
  }

  def batchInsert(entities: Seq[Doings])(implicit session: DBSession = autoSession): List[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        'taskId -> entity.taskId,
        'start -> entity.start,
        'end -> entity.end))
    SQL("""insert into Doings(
      task_id,
      start,
      end
    ) values (
      {taskId},
      {start},
      {end}
    )""").batchByName(params: _*).apply[List]()
  }

  def save(entity: Doings)(implicit session: DBSession = autoSession): Doings = {
    withSQL {
      update(Doings).set(
        column.taskId -> entity.taskId,
        column.start -> entity.start,
        column.end -> entity.end
      ).where.eq(column.taskId, entity.taskId).and.eq(column.start, entity.start).and.eq(column.end, entity.end)
    }.update.apply()
    entity
  }

  def destroy(entity: Doings)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(Doings).where.eq(column.taskId, entity.taskId).and.eq(column.start, entity.start).and.eq(column.end, entity.end) }.update.apply()
  }

}
