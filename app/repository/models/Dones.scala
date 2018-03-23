package repository.models

import scalikejdbc._
import java.time.{ZonedDateTime}

case class Dones(
  taskId: Long,
  requiredTime: Int,
  doneDateTime: ZonedDateTime) {

  def save()(implicit session: DBSession = Dones.autoSession): Dones = Dones.save(this)(session)

  def destroy()(implicit session: DBSession = Dones.autoSession): Int = Dones.destroy(this)(session)

}


object Dones extends SQLSyntaxSupport[Dones] {

  override val schemaName = Some("tascala")

  override val tableName = "Dones"

  override val columns = Seq("task_id", "required_time", "done_date_time")

  def apply(d: SyntaxProvider[Dones])(rs: WrappedResultSet): Dones = apply(d.resultName)(rs)
  def apply(d: ResultName[Dones])(rs: WrappedResultSet): Dones = new Dones(
    taskId = rs.get(d.taskId),
    requiredTime = rs.get(d.requiredTime),
    doneDateTime = rs.get(d.doneDateTime)
  )

  val d = Dones.syntax("d")

  override val autoSession = AutoSession

  def find(taskId: Long)(implicit session: DBSession = autoSession): Option[Dones] = {
    withSQL {
      select.from(Dones as d).where.eq(d.taskId, taskId)
    }.map(Dones(d.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Dones] = {
    withSQL(select.from(Dones as d)).map(Dones(d.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Dones as d)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Dones] = {
    withSQL {
      select.from(Dones as d).where.append(where)
    }.map(Dones(d.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Dones] = {
    withSQL {
      select.from(Dones as d).where.append(where)
    }.map(Dones(d.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Dones as d).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    taskId: Long,
    requiredTime: Int,
    doneDateTime: ZonedDateTime)(implicit session: DBSession = autoSession): Dones = {
    withSQL {
      insert.into(Dones).namedValues(
        column.taskId -> taskId,
        column.requiredTime -> requiredTime,
        column.doneDateTime -> doneDateTime
      )
    }.update.apply()

    Dones(
      taskId = taskId,
      requiredTime = requiredTime,
      doneDateTime = doneDateTime)
  }

  def batchInsert(entities: Seq[Dones])(implicit session: DBSession = autoSession): List[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        'taskId -> entity.taskId,
        'requiredTime -> entity.requiredTime,
        'doneDateTime -> entity.doneDateTime))
    SQL("""insert into Dones(
      task_id,
      required_time,
      done_date_time
    ) values (
      {taskId},
      {requiredTime},
      {doneDateTime}
    )""").batchByName(params: _*).apply[List]()
  }

  def save(entity: Dones)(implicit session: DBSession = autoSession): Dones = {
    withSQL {
      update(Dones).set(
        column.taskId -> entity.taskId,
        column.requiredTime -> entity.requiredTime,
        column.doneDateTime -> entity.doneDateTime
      ).where.eq(column.taskId, entity.taskId)
    }.update.apply()
    entity
  }

  def destroy(entity: Dones)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(Dones).where.eq(column.taskId, entity.taskId) }.update.apply()
  }

}
