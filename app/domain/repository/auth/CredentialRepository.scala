package domain.repository.auth

import domain.model.auth.google.JsonIdentity

trait CredentialRepository {
  def store(credential: JsonIdentity)
  def load(id: Long) // TODO
}
