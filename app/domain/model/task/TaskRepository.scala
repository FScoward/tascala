package domain.model.task

import scalikejdbc.DBSession

import scala.util.Try

trait TaskRepository {
  def delete(task: Task)(implicit session: DBSession): Try[Unit]
}
