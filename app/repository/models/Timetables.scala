package repository.models

import scalikejdbc._
import java.time.{LocalDate}

case class Timetables(
  timeTableId: Option[String] = None,
  taskId: Long,
  order: Int,
  userId: Long,
  date: Option[LocalDate] = None) {

  def save()(implicit session: DBSession = Timetables.autoSession): Timetables = Timetables.save(this)(session)

  def destroy()(implicit session: DBSession = Timetables.autoSession): Int = Timetables.destroy(this)(session)

}


object Timetables extends SQLSyntaxSupport[Timetables] {

  override val schemaName = Some("tascala")

  override val tableName = "TimeTables"

  override val columns = Seq("time_table_id", "task_id", "order", "user_id", "date")

  def apply(t: SyntaxProvider[Timetables])(rs: WrappedResultSet): Timetables = apply(t.resultName)(rs)
  def apply(t: ResultName[Timetables])(rs: WrappedResultSet): Timetables = new Timetables(
    timeTableId = rs.get(t.timeTableId),
    taskId = rs.get(t.taskId),
    order = rs.get(t.order),
    userId = rs.get(t.userId),
    date = rs.get(t.date)
  )

  val t = Timetables.syntax("t")

  override val autoSession = AutoSession

  def find(timeTableId: Option[String], taskId: Long, order: Int, userId: Long, date: Option[LocalDate])(implicit session: DBSession = autoSession): Option[Timetables] = {
    withSQL {
      select.from(Timetables as t).where.eq(t.timeTableId, timeTableId).and.eq(t.taskId, taskId).and.eq(t.order, order).and.eq(t.userId, userId).and.eq(t.date, date)
    }.map(Timetables(t.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Timetables] = {
    withSQL(select.from(Timetables as t)).map(Timetables(t.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Timetables as t)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Timetables] = {
    withSQL {
      select.from(Timetables as t).where.append(where)
    }.map(Timetables(t.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Timetables] = {
    withSQL {
      select.from(Timetables as t).where.append(where)
    }.map(Timetables(t.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Timetables as t).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    timeTableId: Option[String] = None,
    taskId: Long,
    order: Int,
    userId: Long,
    date: Option[LocalDate] = None)(implicit session: DBSession = autoSession): Timetables = {
    withSQL {
      insert.into(Timetables).namedValues(
        column.timeTableId -> timeTableId,
        column.taskId -> taskId,
        column.order -> order,
        column.userId -> userId,
        column.date -> date
      )
    }.update.apply()

    Timetables(
      timeTableId = timeTableId,
      taskId = taskId,
      order = order,
      userId = userId,
      date = date)
  }

  def batchInsert(entities: Seq[Timetables])(implicit session: DBSession = autoSession): List[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        'timeTableId -> entity.timeTableId,
        'taskId -> entity.taskId,
        'order -> entity.order,
        'userId -> entity.userId,
        'date -> entity.date))
    SQL("""insert into TimeTables(
      time_table_id,
      task_id,
      order,
      user_id,
      date
    ) values (
      {timeTableId},
      {taskId},
      {order},
      {userId},
      {date}
    )""").batchByName(params: _*).apply[List]()
  }

  def save(entity: Timetables)(implicit session: DBSession = autoSession): Timetables = {
    withSQL {
      update(Timetables).set(
        column.timeTableId -> entity.timeTableId,
        column.taskId -> entity.taskId,
        column.order -> entity.order,
        column.userId -> entity.userId,
        column.date -> entity.date
      ).where.eq(column.timeTableId, entity.timeTableId).and.eq(column.taskId, entity.taskId).and.eq(column.order, entity.order).and.eq(column.userId, entity.userId).and.eq(column.date, entity.date)
    }.update.apply()
    entity
  }

  def destroy(entity: Timetables)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(Timetables).where.eq(column.timeTableId, entity.timeTableId).and.eq(column.taskId, entity.taskId).and.eq(column.order, entity.order).and.eq(column.userId, entity.userId).and.eq(column.date, entity.date) }.update.apply()
  }

}
