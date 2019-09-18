package serviceImpl

import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerSuite
import play.api.Application
import play.modules.reactivemongo.ReactiveMongoApi

import scala.concurrent.ExecutionContext

class DefaultTestClass extends PlaySpec with MockitoSugar with  GuiceOneAppPerSuite{
 implicit lazy val application: Application = app
  implicit lazy val executionContext: ExecutionContext = app.injector.instanceOf[ExecutionContext]
 implicit lazy val helperService = app.injector.instanceOf[HelperService]
 implicit lazy val authenticationService = app.injector.instanceOf[AuthenticationService]
 implicit lazy val worldService = app.injector.instanceOf[WorldService]
 implicit lazy val reactiveMongoApi = app.injector.instanceOf[ReactiveMongoApi]
}