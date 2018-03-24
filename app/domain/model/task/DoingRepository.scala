package domain.model.task

import domain.model.ID
import domain.model.user.User
import scalikejdbc.DBSession

import scala.util.Try

trait DoingRepository {
  def store(task: Task): Try[Unit]
  def findBy(taskId: ID[Task], userId: ID[User])(implicit session: DBSession): Try[Option[Task]]
  def delete(task: Task)(implicit session: DBSession): Try[Unit]
}
