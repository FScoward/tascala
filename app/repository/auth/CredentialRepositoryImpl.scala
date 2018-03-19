package repository.auth

import domain.model.auth.Oauth
import domain.model.user.User
import domain.repository.auth.CredentialRepository
import repository.models.{Oauths, Users}
import scalikejdbc.DB

class CredentialRepositoryImpl extends CredentialRepository {

  override def store(user: User, oauth: Oauth): Unit = DB localTx  { implicit session =>
    Users.create(userId = user.id.value, name = user.name)
    Oauths.create(oauthId =oauth.oauthId.value, service = oauth.service.name, userId = user.id.value)
  }

  override def load(id: Long): Unit = ???

}
