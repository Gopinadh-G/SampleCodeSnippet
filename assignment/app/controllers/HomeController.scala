package controllers

import javax.inject._
import model.{AuthenticationRequest, CityInformation}
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.WSClient
import play.api.mvc._
import serviceImpl._

import scala.concurrent.ExecutionContext

  @Singleton
  class HomeController @Inject()(cc: ControllerComponents,
                                 ws: WSClient,
                                 continentService: ContinentService,
                                 authenticationService: AuthenticationService,
                                 authenticateUser:AuthenticateUser,
                                 helperService: HelperService, worldService: WorldService)
                                (implicit exec: ExecutionContext) extends AbstractController(cc) {

    def index() = Action{ implicit request: Request[AnyContent] =>
      Ok({views.html.index()})
    }

  /**
   * This method is to get the continent for a given country name
   * This call is being made with a third party API
   */
  def getContinentForCountry(countryName:String)  =authenticateUser.async { implicit aRequest: Request[AnyContent] =>

      for {
      res <- continentService.getContinentForCountry(countryName)
    } yield {
      Ok(Json.toJson(res))
    }


  }

  /**
   * This method is to check if the countries belong to the same continent
   * This call is being made with a third party API
   */
  def checkSiblingCountries(firstCountryName:String,secondCountryName:String)=authenticateUser.async { implicit request: Request[AnyContent] =>
    for{
        firstResponse <-  continentService.getContinentForCountry(firstCountryName)
        secondResponse <-continentService.getContinentForCountry(secondCountryName)
        res <-helperService.createSiblingResponse(firstResponse,secondResponse)
    }yield{
      Ok(Json.toJson(res))
    }


  }

  /**
   * This method is to authenticate an user, to enable user based access
   */
  def authenticateUser()  =Action.async { implicit request: Request[AnyContent] =>
     val message: JsValue = request.body.asJson.get
    val authRequest : AuthenticationRequest = message.as[AuthenticationRequest]
    for {
      response <- authenticationService.isAuthenticatedUser(authRequest.name, authRequest.pwd)
    }
      yield {
      Ok(Json.toJson(response)).withSession("user" ->authRequest.name )
    }
  }

  /**
   * This method is to add a city to the database
   */
  def addCity()= authenticateUser.async { implicit request: Request[AnyContent] =>
    val message: JsValue = request.body.asJson.get
    val cityInfo: CityInformation = message.as[CityInformation]
    for {
      res <- worldService.addCity(new CityInformation(cityInfo.name, cityInfo.country, cityInfo.region))
    } yield {
      Ok(Json.toJson(res))
    }
  }

  /**
   * This method is to add a Continent to the database
   */
  def addContinent()= authenticateUser.async { implicit request: Request[AnyContent] =>
    val message: JsValue = request.body.asJson.get
    val cityInfo:CityInformation =message.as[CityInformation]
    for {
          res <- worldService.addContinent(new CityInformation(cityInfo.name,cityInfo.country, cityInfo.region))
    } yield {
          Ok(Json.toJson(res))
    }
  }

  /**
   * This method is to find a city from database
   */
  def findCity(name:String) =authenticateUser.async {implicit request: Request[AnyContent] =>

    for {
      res <- worldService.findCities(name)
    } yield {
      Ok(Json.toJson(res))
    }
  }

  /**
   * This method is to sort all cities alphabetically
   */
  def sortCities(name:String)=authenticateUser.async {implicit request: Request[AnyContent] =>
    for {
      res <- worldService.sortCities(name)
    } yield {
      Ok(Json.toJson(res))
    }
  }

  /**
   * This method is to delete a continent from database
   */
  def deleteContinent(name:String) =authenticateUser.async {implicit request: Request[AnyContent] =>
    for {
      res <- worldService.deleteContinent(name)
    } yield {
      Ok(Json.toJson(res))
    }
  }

  /**
   * This method is to get all the distinct continents from the database
   */
  def getAllContinents() =authenticateUser.async {implicit request: Request[AnyContent] =>
    for {
      res <- worldService.getAllContinents()
    } yield {
      Ok(Json.toJson(res))
    }
  }

}