package domain.model.auth

import domain.model.{ID, Id64}
import domain.model.user.User

trait Oauth {
  val oauthId: ID[Oauth]
  val service: OauthService
  val userId: ID[User]
}

case class GoogleOauth(
    oauthId: ID[Oauth] = ID[Oauth](Id64.nextAscId()),
    service: OauthService = OauthService.Google,
    userId: ID[User]
) extends Oauth
