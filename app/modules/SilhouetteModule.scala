package modules

import javax.inject.Named

import appservice.auth.{UserService, UserServiceImpl}
import com.google.inject.{AbstractModule, Provides, TypeLiteral}
import com.mohiva.play.silhouette.api.crypto.{
  Crypter,
  CrypterAuthenticatorEncoder
}
import com.mohiva.play.silhouette.api.services.AuthenticatorService
import com.mohiva.play.silhouette.api.util.{
  Clock,
  HTTPLayer,
  IDGenerator,
  PlayHTTPLayer
}
import com.mohiva.play.silhouette.api.{
  Environment,
  EventBus,
  Silhouette,
  SilhouetteProvider
}
import com.mohiva.play.silhouette.crypto.{
  JcaCrypter,
  JcaCrypterSettings,
  JcaSigner,
  JcaSignerSettings
}
import com.mohiva.play.silhouette.impl.authenticators.{
  JWTAuthenticator,
  JWTAuthenticatorService,
  JWTAuthenticatorSettings
}
import com.mohiva.play.silhouette.impl.providers.{
  OAuth1Settings,
  OAuth1TokenSecretProvider,
  SocialProviderRegistry
}
import com.mohiva.play.silhouette.impl.providers.oauth1.TwitterProvider
import com.mohiva.play.silhouette.impl.providers.oauth1.secrets.{
  CookieSecretProvider,
  CookieSecretSettings
}
import com.mohiva.play.silhouette.impl.providers.oauth1.services.PlayOAuth1Service
import com.mohiva.play.silhouette.impl.util.SecureRandomIDGenerator
import controllers.auth.JWTEnv
import play.api.Configuration
import play.api.libs.ws.WSClient

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
    * Provides the HTTP layer implementation.
    *
    * @param client Play's WS client.
    * @return The HTTP layer implementation.
    */
  @Provides
  def provideHTTPLayer(client: WSClient): HTTPLayer = new PlayHTTPLayer(client)

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
    * Provides the social provider registry.
    *
    * @param twitterProvider The Twitter provider implementation.
    * @return The Silhouette environment.
    */
  @Provides
  def provideSocialProviderRegistry(
      twitterProvider: TwitterProvider
  ): SocialProviderRegistry = {

    SocialProviderRegistry(
      Seq(
        twitterProvider
      ))
  }

  /**
    * Provides the cookie signer for the OAuth1 token secret provider.
    *
    * @param configuration The Play configuration.
    * @return The cookie signer for the OAuth1 token secret provider.
    */
  @Provides @Named("oauth1-token-secret-cookie-signer")
  def provideOAuth1TokenSecretCookieSigner(
      configuration: Configuration): JcaSigner = {

    val signerKey = configuration.get[String](
      s"silhouette.oauth1TokenSecretProvider.cookie.signer.key")

    new JcaSigner(JcaSignerSettings(key = signerKey))
  }

  /**
    * Provides the crypter for the OAuth1 token secret provider.
    *
    * @param configuration The Play configuration.
    * @return The crypter for the OAuth1 token secret provider.
    */
  @Provides @Named("oauth1-token-secret-crypter")
  def provideOAuth1TokenSecretCrypter(configuration: Configuration): Crypter = {
    val crypterKey = configuration.get[String](
      s"silhouette.oauth1TokenSecretProvider.crypter.key")

    new JcaCrypter(JcaCrypterSettings(key = crypterKey))
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

  /**
    * Provides the OAuth1 token secret provider.
    *
    * @param jcaSigner The cookie signer implementation.
    * @param crypter The crypter implementation.
    * @param configuration The Play configuration.
    * @param clock The clock instance.
    * @return The OAuth1 token secret provider implementation.
    */
  @Provides
  def provideOAuth1TokenSecretProvider(
      @Named("oauth1-token-secret-cookie-signer") jcaSigner: JcaSigner,
      @Named("oauth1-token-secret-crypter") crypter: Crypter,
      configuration: Configuration,
      clock: Clock): OAuth1TokenSecretProvider = {

    val prefix = "silhouette.oauth1TokenSecretProvider"
    val cookieName = configuration.get[String](s"${prefix}.cookieName")
    val cookiePath = configuration.get[String](s"${prefix}.cookiePath")
    val cookieDomain =
      configuration.getOptional[String](s"${prefix}.cookieDomain")
    val secureCookie = configuration.get[Boolean](s"${prefix}.secureCookie")
    val httpOnlyCookie = configuration.get[Boolean](s"${prefix}.httpOnlyCookie")
    val expirationTime =
      configuration.get[FiniteDuration](s"${prefix}.expirationTime")
    val settings = CookieSecretSettings(
      cookieName = cookieName,
      cookiePath = cookiePath,
      cookieDomain = cookieDomain,
      secureCookie = secureCookie,
      httpOnlyCookie = httpOnlyCookie,
      expirationTime = expirationTime
    )

    new CookieSecretProvider(settings, jcaSigner, crypter, clock)
  }

  /**
    * Provides the Twitter provider.
    *
    * @param httpLayer The HTTP layer implementation.
    * @param tokenSecretProvider The token secret provider implementation.
    * @param configuration The Play configuration.
    * @return The Twitter provider.
    */
  @Provides
  def provideTwitterProvider(httpLayer: HTTPLayer,
                             tokenSecretProvider: OAuth1TokenSecretProvider,
                             configuration: Configuration): TwitterProvider = {

    val keyPrefix = "silhouette.twitter"
    val requestTokenURL = s"${keyPrefix}.requestTokenURL"
    val accessTokenURL = s"${keyPrefix}.accessTokenURL"
    val authorizationURL = s"${keyPrefix}.authorizationURL"
    val callbackURL = s"${keyPrefix}.callbackURL"
    val apiURL = s"${keyPrefix}.apikURL"
    val consumerKey = s"${keyPrefix}.consumerKey"
    val consumerSecret = s"${keyPrefix}.consumerSecret"

    val settings = OAuth1Settings(
      requestTokenURL = configuration.get[String](requestTokenURL),
      accessTokenURL = configuration.get[String](accessTokenURL),
      authorizationURL = configuration.get[String](authorizationURL),
      callbackURL = configuration.get[String](callbackURL),
      apiURL = configuration.getOptional[String](apiURL),
      consumerKey = configuration.get[String](consumerKey),
      consumerSecret = configuration.get[String](consumerSecret)
    )

    new TwitterProvider(httpLayer,
                        new PlayOAuth1Service(settings),
                        tokenSecretProvider,
                        settings)
  }

}
