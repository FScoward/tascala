package repository.models

import scalikejdbc._

case class Tasks(
  taskId: Long,
  userId: Long,
  title: Option[String] = None,
  description: Option[String] = None,
  deadline: Option[String] = None,
  estimate: Option[String] = None) {

  def save()(implicit session: DBSession = Tasks.autoSession): Tasks = Tasks.save(this)(session)

  def destroy()(implicit session: DBSession = Tasks.autoSession): Int = Tasks.destroy(this)(session)

}


object Tasks extends SQLSyntaxSupport[Tasks] {

  override val schemaName = Some("tascala")

  override val tableName = "Tasks"

  override val columns = Seq("task_id", "user_id", "title", "description", "deadline", "estimate")

  def apply(t: SyntaxProvider[Tasks])(rs: WrappedResultSet): Tasks = apply(t.resultName)(rs)
  def apply(t: ResultName[Tasks])(rs: WrappedResultSet): Tasks = new Tasks(
    taskId = rs.get(t.taskId),
    userId = rs.get(t.userId),
    title = rs.get(t.title),
    description = rs.get(t.description),
    deadline = rs.get(t.deadline),
    estimate = rs.get(t.estimate)
  )

  val t = Tasks.syntax("t")

  override val autoSession = AutoSession

  def find(taskId: Long)(implicit session: DBSession = autoSession): Option[Tasks] = {
    withSQL {
      select.from(Tasks as t).where.eq(t.taskId, taskId)
    }.map(Tasks(t.resultName)).single.apply()
  }

  def findAll()(implicit session: DBSession = autoSession): List[Tasks] = {
    withSQL(select.from(Tasks as t)).map(Tasks(t.resultName)).list.apply()
  }

  def countAll()(implicit session: DBSession = autoSession): Long = {
    withSQL(select(sqls.count).from(Tasks as t)).map(rs => rs.long(1)).single.apply().get
  }

  def findBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Option[Tasks] = {
    withSQL {
      select.from(Tasks as t).where.append(where)
    }.map(Tasks(t.resultName)).single.apply()
  }

  def findAllBy(where: SQLSyntax)(implicit session: DBSession = autoSession): List[Tasks] = {
    withSQL {
      select.from(Tasks as t).where.append(where)
    }.map(Tasks(t.resultName)).list.apply()
  }

  def countBy(where: SQLSyntax)(implicit session: DBSession = autoSession): Long = {
    withSQL {
      select(sqls.count).from(Tasks as t).where.append(where)
    }.map(_.long(1)).single.apply().get
  }

  def create(
    taskId: Long,
    userId: Long,
    title: Option[String] = None,
    description: Option[String] = None,
    deadline: Option[String] = None,
    estimate: Option[String] = None)(implicit session: DBSession = autoSession): Tasks = {
    withSQL {
      insert.into(Tasks).namedValues(
        column.taskId -> taskId,
        column.userId -> userId,
        column.title -> title,
        column.description -> description,
        column.deadline -> deadline,
        column.estimate -> estimate
      )
    }.update.apply()

    Tasks(
      taskId = taskId,
      userId = userId,
      title = title,
      description = description,
      deadline = deadline,
      estimate = estimate)
  }

  def batchInsert(entities: Seq[Tasks])(implicit session: DBSession = autoSession): List[Int] = {
    val params: Seq[Seq[(Symbol, Any)]] = entities.map(entity =>
      Seq(
        'taskId -> entity.taskId,
        'userId -> entity.userId,
        'title -> entity.title,
        'description -> entity.description,
        'deadline -> entity.deadline,
        'estimate -> entity.estimate))
    SQL("""insert into Tasks(
      task_id,
      user_id,
      title,
      description,
      deadline,
      estimate
    ) values (
      {taskId},
      {userId},
      {title},
      {description},
      {deadline},
      {estimate}
    )""").batchByName(params: _*).apply[List]()
  }

  def save(entity: Tasks)(implicit session: DBSession = autoSession): Tasks = {
    withSQL {
      update(Tasks).set(
        column.taskId -> entity.taskId,
        column.userId -> entity.userId,
        column.title -> entity.title,
        column.description -> entity.description,
        column.deadline -> entity.deadline,
        column.estimate -> entity.estimate
      ).where.eq(column.taskId, entity.taskId)
    }.update.apply()
    entity
  }

  def destroy(entity: Tasks)(implicit session: DBSession = autoSession): Int = {
    withSQL { delete.from(Tasks).where.eq(column.taskId, entity.taskId) }.update.apply()
  }

}
