package domain.model.task

import domain.model.ID
import domain.model.user.User
import scalikejdbc.DBSession

import scala.util.Try

trait DoneRepository {
  def store(done: Done): Try[Unit]
  def listBy(userId: ID[User])(implicit session: DBSession): Try[List[Task]]
}
