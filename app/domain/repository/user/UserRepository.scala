package domain.repository.user

import domain.model.ID
import domain.model.user.User

import scala.util.Try

trait UserRepository {
  def findBy(id: ID[User]): Try[Option[User]]
}
