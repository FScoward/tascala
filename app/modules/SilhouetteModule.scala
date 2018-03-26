package modules

import javax.inject.Named

import appservice.auth.{UserService, UserServiceImpl}
import com.google.inject.{AbstractModule, Provides, TypeLiteral}
import com.mohiva.play.silhouette.api.crypto.{
  Crypter,
  CrypterAuthenticatorEncoder
}
import com.mohiva.play.silhouette.api.services.AuthenticatorService
import com.mohiva.play.silhouette.api.util.{Clock, IDGenerator}
import com.mohiva.play.silhouette.api.{
  Environment,
  EventBus,
  Silhouette,
  SilhouetteProvider
}
import com.mohiva.play.silhouette.crypto.{JcaCrypter, JcaCrypterSettings}
import com.mohiva.play.silhouette.impl.authenticators.{
  JWTAuthenticator,
  JWTAuthenticatorService,
  JWTAuthenticatorSettings
}
import com.mohiva.play.silhouette.impl.util.SecureRandomIDGenerator
import controllers.auth.JWTEnv
import play.api.Configuration

import scala.concurrent.ExecutionContext
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.FiniteDuration

class SilhouetteModule extends AbstractModule {
  override def configure(): Unit = {
    bind(new TypeLiteral[Silhouette[JWTEnv]] {})
      .to(new TypeLiteral[SilhouetteProvider[JWTEnv]] {})
    bind(classOf[UserService]).to(classOf[UserServiceImpl])
    bind(classOf[Clock]).toInstance(Clock())
    bind(classOf[IDGenerator]).toInstance(new SecureRandomIDGenerator())
    bind(classOf[EventBus]).toInstance(EventBus())
  }

  /**
    * Provides the Silhouette environment.
    *
    * @param userService The user service implementation.
    * @param authenticatorService The authentication service implementation.
    * @param eventBus The event bus instance.
    * @return The Silhouette environment.
    */
  @Provides
  def provideEnvironment(
      userService: UserService,
      authenticatorService: AuthenticatorService[JWTAuthenticator],
      eventBus: EventBus): Environment[JWTEnv] = {

    Environment[JWTEnv](
      userService,
      authenticatorService,
      Seq(),
      eventBus
    )
  }

  /**
    * Provides the crypter for the authenticator.
    *
    * @param configuration The Play configuration.
    * @return The crypter for the authenticator.
    */
  @Provides @Named("authenticator-crypter")
  def provideAuthenticatorCrypter(configuration: Configuration): Crypter = {
    val config =
      configuration.get[String]("silhouette.authenticator.crypter.key")

    new JcaCrypter(JcaCrypterSettings(config))
  }

  /**
    * Provides the authenticator service.
    *
    * @param crypter The crypter implementation.
    * @param idGenerator The ID generator implementation.
    * @param configuration The Play configuration.
    * @param clock The clock instance.
    * @return The authenticator service.
    */
  @Provides
  def provideAuthenticatorService(
      @Named("authenticator-crypter") crypter: Crypter,
      idGenerator: IDGenerator,
      configuration: Configuration,
      clock: Clock): AuthenticatorService[JWTAuthenticator] = {

    val headerName =
      configuration.get[String]("silhouette.authenticator.headerName")
    val issuerClaim =
      configuration.get[String]("silhouette.authenticator.issuerClaim")
    val authenticatorExpiry = configuration.get[FiniteDuration](
      ("silhouette.authenticator.authenticatorExpiry"))
    val sharedSecret =
      configuration.get[String](("silhouette.authenticator.sharedSecret"))
    val encoder = new CrypterAuthenticatorEncoder(crypter)

    new JWTAuthenticatorService(
      JWTAuthenticatorSettings(
        fieldName = headerName,
        requestParts = None,
        issuerClaim = issuerClaim,
        authenticatorIdleTimeout = None,
        authenticatorExpiry = authenticatorExpiry,
        sharedSecret = sharedSecret
      ),
      None,
      encoder,
      idGenerator,
      clock
    )
  }

}
