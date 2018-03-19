package repository.models

import scalikejdbc._

case class Iceboxs(
  taskId: Long) {

  def save()(implicit session: DBSession = Iceboxs.autoSession): Iceboxs = Iceboxs.save(this)(session)

  def destroy()(implicit session: DBSession = Iceboxs.autoSession): Int = Iceboxs.destroy(this)(session)

}


object Iceboxs extends SQLSyntaxSupport[Iceboxs] {

  override val schemaName = Some("tascala")

  override val tableName = "Iceboxs"

  override val columns = Seq("task_id")

  def apply(i: SyntaxProvider[Iceboxs])(rs: WrappedResultSet): Iceboxs = apply(i.resultName)(rs)
  def apply(i: ResultName[Iceboxs])(rs: WrappedResultSet): Iceboxs = new Iceboxs(
    taskId = rs.get(i.taskId)
  )

  val i = Iceboxs.syntax("i")

  override val autoSession = AutoSession

  def find(taskId: Long)(implicit session: DBSession = autoSession): Option[Iceboxs] = {
    withSQL {
      select.from(Iceboxs as i).where.eq(i.taskId, taskId)
    }.map(Iceboxs(i.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Iceboxs] = {
    withSQL(select.from(Iceboxs as i)).map(Iceboxs(i.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Iceboxs as i)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Iceboxs] = {
    withSQL {
      select.from(Iceboxs as i).where.append(where)
    }.map(Iceboxs(i.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Iceboxs] = {
    withSQL {
      select.from(Iceboxs as i).where.append(where)
    }.map(Iceboxs(i.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Iceboxs as i).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    taskId: Long)(implicit session: DBSession = autoSession): Iceboxs = {
    withSQL {
      insert.into(Iceboxs).namedValues(
        column.taskId -> taskId
      )
    }.update.apply()

    Iceboxs(
      taskId = taskId)
  }

  def batchInsert(entities: Seq[Iceboxs])(implicit session: DBSession = autoSession): List[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        'taskId -> entity.taskId))
    SQL("""insert into Iceboxs(
      task_id
    ) values (
      {taskId}
    )""").batchByName(params: _*).apply[List]()
  }

  def save(entity: Iceboxs)(implicit session: DBSession = autoSession): Iceboxs = {
    withSQL {
      update(Iceboxs).set(
        column.taskId -> entity.taskId
      ).where.eq(column.taskId, entity.taskId)
    }.update.apply()
    entity
  }

  def destroy(entity: Iceboxs)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(Iceboxs).where.eq(column.taskId, entity.taskId) }.update.apply()
  }

}
