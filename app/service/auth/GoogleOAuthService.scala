package service.auth

import java.math.BigInteger
import java.security.SecureRandom
import javax.inject.{Inject, Singleton}

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.http.GenericUrl
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import domain.model.auth.google.JsonIdentity
import play.api.Configuration
import repository.auth.CredentialRepositoryImpl

@Singleton
class GoogleOAuthService @Inject()(
    config: Configuration,
    credentialRepository: CredentialRepositoryImpl
) {

  private val clientId = config.getOptional[String]("oauth.google.client_id")
  private val clientSecret =
    config.getOptional[String]("oauth.google.client_secret")

  private val SCOPES = java.util.Arrays.asList("email", "profile")
  private val JSON_FACTORY = new JacksonFactory()
  private val HTTP_TRANSPORT = new NetHttpTransport()
  private val CALLBACK = "http://localhost:9000/auth/google/callback"
  private val USERINFO_ENDPOINT =
    "https://www.googleapis.com/plus/v1/people/me/openIdConnect"

  val flow =
    new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT,
                                            JSON_FACTORY,
                                            clientId.get,
                                            clientSecret.get,
                                            SCOPES).build

  def redirectUrl: String = {
    val state = new BigInteger(130, new SecureRandom()).toString(32); // prevent request forgery
    flow.newAuthorizationUrl
      .setRedirectUri(CALLBACK)
      .setState(state)
      .build // Prevent request forgery
  }

  def callback(code: String) = {
    val tokenResponse =
      flow
        .newTokenRequest(code)
        .setRedirectUri(CALLBACK)
        .execute

    val credential = flow.createAndStoreCredential(tokenResponse, null) // TODO

    val requestFactory = HTTP_TRANSPORT.createRequestFactory(credential)
    val url = new GenericUrl(USERINFO_ENDPOINT)
    val httpRequest = requestFactory.buildGetRequest(url)
    httpRequest.getHeaders.setContentType("application/json")
    val jsonIdentity = httpRequest.execute().parseAsString()

    import io.circe.parser._ // これがないと`parse`関数および`decode`関数が使えない
    import io.circe.generic.auto._ // これがないと`io.circe.Decoder[Person]`を自動定義してくれなくて、`as`メソッドに渡すDecoderがないと怒られる

    // TODO
    val result = decode[JsonIdentity](jsonIdentity)
    println(result)
//      credentialRepository.store(x)

    jsonIdentity
  }
}
