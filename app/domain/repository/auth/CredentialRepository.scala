package domain.repository.auth

import domain.model.auth.Oauth
import domain.model.user.User

trait CredentialRepository {
  def store(user: User, oauth: Oauth)
  def load(id: Long) // TODO
}
