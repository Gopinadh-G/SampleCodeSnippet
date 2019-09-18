package service

import model.AuthenticationResponse

import scala.concurrent.Future

trait AuthenticationServiceT{
  def isAuthenticatedUser(name:String,pwd:String="admin"):Future[AuthenticationResponse]

}
