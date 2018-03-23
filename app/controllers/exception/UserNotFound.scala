package controllers.exception

import domain.model.ID
import domain.model.user.User

class UserNotFound(userId: ID[User]) extends Exception {
  override def getMessage: String = s"user not found. userId = ${userId.value}"
}
