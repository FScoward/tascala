package appservice.auth
import com.mohiva.play.silhouette.api.LoginInfo
import com.mohiva.play.silhouette.impl.User
import com.mohiva.play.silhouette.impl.providers.CommonSocialProfile

import scala.concurrent.Future

class UserServiceImpl extends UserService {
  /**
    * Saves a user.
    *
    * @param user The user to save.
    * @return The saved user.
    */
  override def save(user: User): Future[User] = ???

  /**
    * Saves the social profile for a user.
    *
    * If a user exists for this profile then update the user, otherwise create a new user with the given profile.
    *
    * @param profile The social profile to save.
    * @return The user for whom the profile was saved.
    */
  override def save(profile: CommonSocialProfile): Future[User] = ???

  override def retrieve(loginInfo: LoginInfo): Future[Option[User]] = ???
}
