package domain.model.auth

sealed abstract class OauthService (val name: String)

object OauthService {
  case object Google extends OauthService("Google")
}
