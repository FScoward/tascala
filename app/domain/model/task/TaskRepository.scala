package domain.model.task

import domain.model.ID
import domain.model.user.User
import scalikejdbc.DBSession

import scala.util.Try

trait TaskRepository {
  def store(task: Task): Try[Unit]
  def save(task: Task): Unit
  def findBy(taskId: ID[Task]): Option[Task]
  def listBy(userId: ID[User]): Try[List[Task]]
  def delete(userId: ID[User], taskId: ID[Task])(implicit session: DBSession): Try[Unit]
}
