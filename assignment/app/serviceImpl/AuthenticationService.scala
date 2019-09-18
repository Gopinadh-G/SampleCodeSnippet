package serviceImpl
import javax.inject.Inject
import model.AuthenticationResponse
import service.AuthenticationServiceT
import util.MyWorldConstants

import scala.concurrent.{ExecutionContext, Future}

class AuthenticationService @Inject() (implicit exec:ExecutionContext) extends AuthenticationServiceT {
  def isAuthenticatedUser(name:String,pwd:String=MyWorldConstants.DefaultPasswordText):Future[AuthenticationResponse]={
    name match {
      case "admin" => Future{new AuthenticationResponse(name, MyWorldConstants.TRUE, MyWorldConstants.SuccessText)}
      case _ => Future {new AuthenticationResponse(name, MyWorldConstants.FALSE, MyWorldConstants.SuccessText)}
    }
  }
}
