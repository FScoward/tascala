package domain.repository.user

import domain.model.ID
import domain.model.user.User

trait UserRepository {
  def findBy(id: ID[User]): Option[User]
}
