package serviceImpl

import model.AuthenticationResponse
import util.MyWorldConstants

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

class AuthenticationServiceSpec extends DefaultTestClass {
  "The isAuthenticatedUser for admin user" should {
    "returns a Success response " in {
      val response: AuthenticationResponse =
        Await.result(authenticationService.isAuthenticatedUser("admin"), 500 millis)
      response.status mustBe(MyWorldConstants.SuccessText)
      response.isAuthenticated mustBe(true)

    }
  }

  "The isAuthenticatedUser for any other user" should {
    "returns a Success response " in {
      val response: AuthenticationResponse =
        Await.result(authenticationService.isAuthenticatedUser("admind"), 500 millis)
      response.status mustBe(MyWorldConstants.SuccessText)
      response.isAuthenticated mustBe(false)

    }
  }
}
