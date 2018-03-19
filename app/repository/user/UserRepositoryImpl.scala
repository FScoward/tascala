package repository.user

import domain.model.ID
import domain.model.user.User
import domain.repository.user.UserRepository
import repository.models.Users

class UserRepositoryImpl extends UserRepository {
  override def findBy(id: ID[User]): Option[User] = Users.find(id.value).map{ users =>
    User(ID[User](users.userId), users.name)
  }
}
