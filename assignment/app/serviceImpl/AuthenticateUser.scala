package serviceImpl


import javax.inject.Inject
import play.api.mvc.Results._
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class AuthenticateUser  @Inject() (parser: BodyParsers.Default)(implicit ec: ExecutionContext)
  extends ActionBuilderImpl(parser) {

  override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
    val userName = request.session.get("user")
    userName match {
      case Some("admin")=> {
        val res: Future[Result] = block(request)
        res
      }
      case Some("valid")=> {
        val res: Future[Result] = block(request)
        res
      }
      case _ => {
        Future.successful(Forbidden("You are not a valid user"))
      }
    }
  }
}
