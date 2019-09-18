package serviceImpl
import javax.inject.{Inject, Singleton}
import model.{ContinentResponseAPI, CountryInformationAPI, CountryNotFoundAPI}
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.{WSResponse, _}
import service.ContinentServiceT
import util.MyWorldConstants

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ContinentService @Inject()(ws: WSClient)(implicit exec: ExecutionContext) extends ContinentServiceT  {

def getContinentForCountry(name:String):Future[ContinentResponseAPI] = {

  val url = s"https://restcountries.eu/rest/v2/name/$name?fullText=true"
  for {
    response <- makeApiCall(url)
  } yield {
    constructResponse(response,name)
  }
}
private def constructResponse (wsResponse:WSResponse,name:String):ContinentResponseAPI ={
  val json: JsValue = Json.parse(wsResponse.body.toString() )
  val isError:Boolean =isErrorResponse(json)
  isError match {
    case true=> createResponse(name, Json.parse(json.toString()).as[CountryNotFoundAPI], null, MyWorldConstants.FALSE)
    case _ => createResponse(name, null, Json.parse(json.toString()).as[Array[CountryInformationAPI]], MyWorldConstants.TRUE)
  }

}
  private def makeApiCall(url:String):Future[WSResponse]={
    val request: WSRequest = ws.url(url)
    val complexRequest: WSRequest =
      request.addHttpHeaders(MyWorldConstants.HttpAcceptHeaderText -> MyWorldConstants.HttpHeaderJSONTypeText)
    for{
        res <- complexRequest.get()
         }
      yield{
        res
      }
  }

  /**
   * Helper method to create the dynamic response
   */
  private def createResponse(name:String,errorData:CountryNotFoundAPI,successData:Array[CountryInformationAPI],isSuccess:Boolean):ContinentResponseAPI={
    isSuccess match {
      case true => new ContinentResponseAPI(MyWorldConstants.SuccessText,new CountryNotFoundAPI(-1,MyWorldConstants.NoErrorFoundText),
        MyWorldConstants.BlankString,
        data= successData.head,
        s"The name of the continent for $name is ${successData.head.region}, " +
          s"${successData.head.subregion}")

      case false=>
        new ContinentResponseAPI(MyWorldConstants.FailureText, errorData,
          s"${errorData.message}", data = new CountryInformationAPI(MyWorldConstants.BlankString, MyWorldConstants.BlankString, MyWorldConstants.BlankString),
          s"The country by name $name does not exist, the api returned ${errorData.message}")

    }
  }
  private def isErrorResponse(json:JsValue):Boolean ={
    json.toString().startsWith("{")
  }
}
