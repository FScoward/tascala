package repository.user

import domain.model.ID
import domain.model.user.User
import domain.repository.user.UserRepository
import repository.models.Users

import scala.util.Try

class UserRepositoryImpl extends UserRepository {
  override def findBy(id: ID[User]): Try[Option[User]] = Try {
    Users.find(id.value).map{ users =>
      User(ID[User](users.userId), users.name)
    }
  }
}
