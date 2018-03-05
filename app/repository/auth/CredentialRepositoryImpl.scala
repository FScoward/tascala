package repository.auth

import domain.model.auth.google.JsonIdentity
import domain.repository.auth.CredentialRepository

class CredentialRepositoryImpl extends CredentialRepository {

  override def store(credential: JsonIdentity): Unit = ???

  override def load(id: Long): Unit = ???

}
