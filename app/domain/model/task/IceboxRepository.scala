package domain.model.task

import domain.model.ID
import domain.model.user.User
import scalikejdbc.DBSession

import scala.util.Try

trait IceboxRepository {
  def store(task: Task): Try[Unit]
  def save(task: Task): Try[Unit]
  def findBy(taskId: ID[Task]): Try[Option[Task]]
  def listBy(userId: ID[User])(implicit session: DBSession): Try[List[Task]]
  def delete(task: Task)(implicit session: DBSession): Try[Unit]
  def remove(userId: ID[User], taskId: ID[Task]): Try[Unit]
}
