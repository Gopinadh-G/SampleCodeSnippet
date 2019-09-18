
import model._
import org.mockito.Mockito._
import reactivemongo.api.commands.WriteResult
import serviceImpl.DefaultTestClass
import util.MyWorldConstants
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps
class HelperServiceSpec extends  DefaultTestClass {

  /***
   * createSiblingResponse test cases start here
   * */
  "The createSiblingResponse for both valid inputs, same continents, same subregion" should {
    "return a positive response " in {

      val firstResponse = createFirstSiblingRequest()
      val secondResponse = createSecondSiblingSuccRequest()
      var response: SiblingCountriesResponseAPI =
        Await.result(
          helperService.createSiblingResponse(firstResponse, secondResponse)
          , 500 millis)

      response.areSiblings mustBe true
    }
  }
  "The createSiblingResponse for both valid inputs, same continents, different subregion" should {
    "return positive response " in {

      val firstResponse = createFirstSiblingRequest()
      val secondResponse = createFirstSiblingSuccRequest()
      var response: SiblingCountriesResponseAPI =
        Await.result(
          helperService.createSiblingResponse(firstResponse, secondResponse)
          , 500 millis)

     response.areSiblings mustBe true
    }
  }

  "The createSiblingResponse for both valid inputs, different continents" should {
    "return a negative response " in {

      val firstResponse = createFirstSiblingRequest()
      val secondResponse = createSecondSiblingFailRequest()
      var response: SiblingCountriesResponseAPI =
        Await.result(
          helperService.createSiblingResponse(firstResponse, secondResponse)
          , 500 millis)

      response.areSiblings  mustBe false
    }
  }

  "The createSiblingResponse for invalid first country and valid second country " should {
    "return invalid response " in {

      val firstResponse = createSecondSiblingInvalidRequest()
      val secondResponse = createSecondSiblingSuccRequest()
      var response: SiblingCountriesResponseAPI =
        Await.result(
          helperService.createSiblingResponse(firstResponse, secondResponse)
          , 500 millis)

      var areSiblings: Boolean = response.areSiblings
      areSiblings mustBe false
    }
  }
  "The createSiblingResponse for valid first country and invalid second country  " should {
    "return an invalid response " in {

      val firstResponse = createFirstSiblingRequest()
      val secondResponse = createSecondSiblingInvalidRequest()
      val response: SiblingCountriesResponseAPI =
        Await.result(
          helperService.createSiblingResponse(firstResponse, secondResponse)
          , 500 millis)

      val areSiblings: Boolean = response.areSiblings
      areSiblings mustBe false
    }
  }

  private def createFirstSiblingRequest(): ContinentResponseAPI = {
    new ContinentResponseAPI(
      "Success",
      new CountryNotFoundAPI(-1, ""),
      "",
      new CountryInformationAPI("India", "Asia", "Southern Asia"),
      "Successfully found country"
    )
  }
  private def createFirstSiblingSuccRequest(): ContinentResponseAPI = {
    new ContinentResponseAPI(
      "Success",
      new CountryNotFoundAPI(-1, ""),
      "",
      new CountryInformationAPI("Vietnam", "Asia", "South-Eastern Asia"),
      "Successfully found country"
    )
  }
  private def createSecondSiblingSuccRequest(): ContinentResponseAPI = {
    new ContinentResponseAPI(
      "Success",
      new CountryNotFoundAPI(-1, ""),
      "",
      new CountryInformationAPI("Pakistan", "Asia", "Southern Asia"),
      "Successfully found country"
    )
  }

  private def createSecondSiblingFailRequest(): ContinentResponseAPI = {
    new ContinentResponseAPI(
      "Success",
      new CountryNotFoundAPI(-1, ""),
      "",
      new CountryInformationAPI("France", "Europe", "Western Europe"),
      "Successfully found country"
    )
  }

  private def createSecondSiblingInvalidRequest(): ContinentResponseAPI = {
    new ContinentResponseAPI(
      "Success",
      new CountryNotFoundAPI(-1, "Not Found"),
      "",
      new CountryInformationAPI("", "", ""),
      "Not valid"
    )
  }

  /***
   * createSiblingResponse test cases end here
   * */


  /***
   * createInsertCityResponse test cases start here
   * */

  "The createInsertCityResponse to check customized input and a successful DB write Operation" should {
    "returns a Success response " in {
      val writeRes:WriteResult =  mockWriResInsCityRes(true)
      val response: CityInsertResponse =
        Await.result(helperService.createInsertCityResponse(writeRes,true), 500 millis)
      response.status mustBe(MyWorldConstants.SuccessText)
      response.error mustBe(MyWorldConstants.BlankString)

    }
  }

  "The createInsertCityResponse to check customized input and a failed DB write Operation" should {
    "returns a Failure response " in {
      val writeRes:WriteResult =  mockWriResInsCityRes(false)
      val response: CityInsertResponse =
        Await.result(helperService.createInsertCityResponse(writeRes,true), 500 millis)
      response.status mustBe(MyWorldConstants.FailureText)
      response.error mustBe("DB error")
    }
  }

  "The createInsertCityResponse to check non-customized input and a successful DB write Operation" should {
    "returns a Success response " in {
      val writeRes:WriteResult =  mockWriResInsCityRes(true)
      val response: CityInsertResponse =
        Await.result(helperService.createInsertCityResponse(writeRes,false), 500 millis)
      response.status mustBe(MyWorldConstants.SuccessText)
      response.error mustBe(MyWorldConstants.BlankString)
    }
  }

  "The createInsertCityResponse to check non-customized input and a failed DB write Operation" should {
    "returns a Failure response " in {
val writeRes:WriteResult =  mockWriResInsCityRes(false)
val response: CityInsertResponse =
  Await.result(helperService.createInsertCityResponse(writeRes,false), 500 millis)
response.status mustBe(MyWorldConstants.FailureText)
response.error mustBe("DB error")
}
}

private def mockWriResInsCityRes(ok: Boolean, maybeN: Option[Int] = None, maybeCode: Option[Int] = None): WriteResult = {
  val mockResult = mock[WriteResult]
  when(mockResult.ok).thenReturn(ok)
  when(mockResult.n).thenReturn(maybeN.getOrElse(1))
  when(mockResult.code).thenReturn(maybeCode)
  mockResult
}
/***
 * createInsertCityResponse test cases end here
 * */

  /***
   * createRemoveContinentResponse test cases start here
   * */

  "The createRemoveContinentResponse to check a successful DB write Operation and number of documents deleted> 0" should {
    "returns a Success response " in {
      val numberOfDocs:Option[Int] = Some(1)
      val writeRes:WriteResult =  mockWriResRemCityRes(true,numberOfDocs)
      val response: RemoveResponse =
        Await.result(helperService.createRemoveContinentResponse(writeRes), 500 millis)
      response.status mustBe(MyWorldConstants.SuccessText)
      response.displayMessage contains("Successfully Removed ")
        //(MyWorldConstants.BlankString)
    }
  }

  "The createRemoveContinentResponse to check a successful DB write Operation and number of documents deleted is 0 i.e. document not found" should {
    "returns a Success response " in {
      val numberOfDocs:Option[Int] = Some(0)
      val writeRes:WriteResult =  mockWriResRemCityRes(true,numberOfDocs)
      val response: RemoveResponse =
        Await.result(helperService.createRemoveContinentResponse(writeRes), 500 millis)
      response.status mustBe(MyWorldConstants.SuccessText)
      response.displayMessage contains("Could not find the continent in the database to remove.")
      //(MyWorldConstants.BlankString)
    }
  }


  "The createRemoveContinentResponse to check a successful DB write Operation and number of documents deleted is 0 " should {
    "returns a Failure response " in {
      val numberOfDocs:Option[Int] = Some(0)
      val writeRes:WriteResult =  mockWriResRemCityRes(false,numberOfDocs)
      val response: RemoveResponse =
        Await.result(helperService.createRemoveContinentResponse(writeRes), 500 millis)
      response.status mustBe(MyWorldConstants.FailureText)
      response.displayMessage contains("Could not remove document.Some error occurred")

    }
  }

  private def mockWriResRemCityRes(ok: Boolean, maybeN: Option[Int] = None, maybeCode: Option[Int] = None): WriteResult = {
    val mockResult = mock[WriteResult]
    when(mockResult.ok).thenReturn(ok)
    when(mockResult.n).thenReturn(maybeN.getOrElse(1))
    when(mockResult.code).thenReturn(maybeCode)
    mockResult
  }
  /***
   * createRemoveContinentResponse test cases end here
   * */
  /***
   * createEmptyFieldResponse test cases start here
   * */

  "The createEmptyFieldResponse for Continent " should {
    "return a Success response " in {
      var inputData = new CityInformation("", "", "")
      val response: CityInsertResponse = Await.result(helperService.createEmptyFieldResponse(inputData,"Continent"),500 millis)
      response.status mustBe (MyWorldConstants.SuccessText)
      response.error mustBe(MyWorldConstants.BlankString)
      response.displayMessage must include ("Please ensure that the Continent name is valid")
    }
  }

  "The createEmptyFieldResponse for City " should {
    "return a Success response " in {
      var inputData = new CityInformation("", "", "")
      val response: CityInsertResponse = Await.result(helperService.createEmptyFieldResponse(inputData,"City"),500 millis)
      response.status mustBe (MyWorldConstants.SuccessText)
      response.error mustBe(MyWorldConstants.BlankString)
      response.displayMessage must include ("Please ensure that the City name is valid")
    }
  }

  "The createEmptyFieldResponse for invalid request " should {
    "return a Failure response " in {
      var inputData = new CityInformation("", "", "")
      val response: CityInsertResponse = Await.result(helperService.createEmptyFieldResponse(inputData,"ContinentInvalid"),500 millis)
      response.status mustBe (MyWorldConstants.FailureText)
      response.error must include("Not a valid request")
      response.displayMessage must include ("Invalid request")
    }
  }
  /***
   * createEmptyFieldResponse test cases end here
   * */



  /***
   * createInsertCityRequest test cases start here
   * */

  "The createInsertCityRequest for Continent to construct insert document for a valid continent, blank city name and a blank country name" should {
    "return a Success response " in {
      val inputData=new CityInformation("","","TestContinent")
      val response: CityInformationInsert =Await.result(helperService.createInsertCityRequest("Continent",inputData),500 millis)
      response.isEditedData mustBe(true)
      response.cityInformation.country must include("TestContinent")
      response.cityInformation.name must include("TestContinent")
    }
  }

  "The createInsertCityRequest for Continent to construct insert document for a valid continent, valid city name and a blank country name" should {
    "return a Success response " in {
      val inputData=new CityInformation("New City","","TestContinent")
      val response: CityInformationInsert =Await.result(helperService.createInsertCityRequest("Continent",inputData),500 millis)
      response.isEditedData mustBe(true)
      response.cityInformation.country must include("TestContinent")
      response.cityInformation.name mustNot contain("TestContinent")
    }
  }

  "The createInsertCityRequest for Continent to construct insert document for a valid continent, blank city name and a valid country name" should {
    "return a Success response " in {
      val inputData=new CityInformation("","New Country","TestContinent")
      val response: CityInformationInsert =Await.result(helperService.createInsertCityRequest("Continent",inputData),500 millis)
      response.isEditedData mustBe(true)
      response.cityInformation.name must include("TestContinent")
      response.cityInformation.country mustNot contain("TestContinent")
    }
  }

  "The createInsertCityRequest for Continent to construct insert document for a valid continent, valid city name and a valid country name" should {
    "return a Success response " in {
      val inputData=new CityInformation("New City","New Country","TestContinent")
      val response: CityInformationInsert =Await.result(helperService.createInsertCityRequest("Continent",inputData),500 millis)
      response.isEditedData mustBe(false)
      response.cityInformation.name mustNot include("TestContinent")
      response.cityInformation.country mustNot contain("TestContinent")
    }
  }


  "The createInsertCityRequest for City to construct insert document for a valid city name, valid country name and a valid continent name" should {
    "return a Success response " in {
      val inputData=new CityInformation("TestCity","New Country","New Continent")
      val response: CityInformationInsert =Await.result(helperService.createInsertCityRequest("City",inputData),500 millis)
      response.isEditedData mustBe(false)
      response.cityInformation.region mustNot include("TestCity")
      response.cityInformation.country mustNot contain("TestCity")
    }
  }

  "The createInsertCityRequest for City to construct insert document for a valid city name, blank country name and a valid continent name" should {
    "return a Success response " in {
      val inputData = new CityInformation("TestCity", "", "New Continent")
      val response: CityInformationInsert =Await.result(helperService.createInsertCityRequest("City",inputData),500 millis)
      response.isEditedData mustBe (true)
      response.cityInformation.region mustNot include("TestCity")
      response.cityInformation.country must include("TestCity")
    }
  }
  "The createInsertCityRequest for City  to construct insert document for a valid city name, valid country name and a blank continent name" should {
    "return a Success response " in {
      val inputData = new CityInformation("TestCity", "New Country", "")
      val response: CityInformationInsert =Await.result(helperService.createInsertCityRequest("City",inputData),500 millis)
      response.isEditedData mustBe (true)
      response.cityInformation.country mustNot include("TestCity")
      response.cityInformation.region must include("TestCity")
    }
  }

  "The createInsertCityRequest for City  to construct insert document for a valid city name, blank country name and a blank continent name" should {
    "return a Success response " in {
      val inputData = new CityInformation("TestCity", "", "")
      val response: CityInformationInsert =Await.result(helperService.createInsertCityRequest("City",inputData),500 millis)
      response.isEditedData mustBe (true)
      response.cityInformation.country must include("TestCity")
      response.cityInformation.region must include("TestCity")
    }
  }
  "The createInsertCityRequest for Invalid request" should {
    "return a Success response " in {
      val inputData = new CityInformation("TestCity", "", "")
      val response: CityInformationInsert =Await.result(helperService.createInsertCityRequest("invalid",inputData),500 millis)
      response.isEditedData mustBe (false)
      response.cityInformation.country mustBe(MyWorldConstants.BlankString)
      response.cityInformation.region must include(MyWorldConstants.BlankString)
      response.cityInformation.name must include(MyWorldConstants.BlankString)
    }
  }
  /***
   * createInsertCityRequest test cases end here
   * */




}