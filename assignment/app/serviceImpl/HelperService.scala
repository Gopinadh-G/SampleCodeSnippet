package serviceImpl

import javax.inject.{Inject, Singleton}
import model._
import reactivemongo.api.commands.WriteResult
import service.HelperServiceT
import util.MyWorldConstants

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class HelperService  @Inject() (implicit exec: ExecutionContext) extends HelperServiceT {


  /**
   * This is a helper method to construct meaningful message for the sibling county query
   */

  def createSiblingResponse(firstResponse:ContinentResponseAPI,secondResponse:ContinentResponseAPI):Future[SiblingCountriesResponseAPI]={

    var firstCountryError:CountryNotFoundAPI=firstResponse.error
    var secondCountryError:CountryNotFoundAPI=secondResponse.error
    firstCountryError.message match {
      case "Not Found" =>
        Future{ new SiblingCountriesResponseAPI(MyWorldConstants.FALSE,"The input countries are not valid. Please input valid data.")}

      case _ =>{
        secondCountryError.message match {
          case "Not Found" =>
            Future{ new SiblingCountriesResponseAPI(MyWorldConstants.FALSE,"The input countries are not valid. Please input valid data.")}
          case _=>{
            firstResponse.data.region match {
              case secondResponse.data.region =>{
                firstResponse.data.subregion match{
                  case secondResponse.data.subregion =>
                    Future{ new SiblingCountriesResponseAPI(MyWorldConstants.TRUE,s" ${firstResponse.data.name} belongs to" +
                      s" ${firstResponse.data.region},${firstResponse.data.subregion} " +
                      s"and ${secondResponse.data.name} belongs to ${secondResponse.data.region},${secondResponse.data.subregion}"
                    )}

                  case _ =>
                    Future{ new SiblingCountriesResponseAPI(MyWorldConstants.TRUE, s" ${firstResponse.data.name} belongs to" +
                      s" ${firstResponse.data.region},${firstResponse.data.subregion} " +
                      s"and ${secondResponse.data.name} belongs to ${secondResponse.data.region},${secondResponse.data.subregion}"
                    )}
                }
              }
              case _ =>
                Future{ new SiblingCountriesResponseAPI(MyWorldConstants.FALSE, s" ${firstResponse.data.name} belongs to" +
                  s" ${firstResponse.data.region},${firstResponse.data.subregion} " +
                  s"while ${secondResponse.data.name} belongs to ${secondResponse.data.region},${secondResponse.data.subregion}"
                )}
            }
          }
        }
      }
    }
  }

  def createInsertCityResponse(writeRes:WriteResult,isCustomisedInput:Boolean):Future[CityInsertResponse]= {
    writeRes.ok match {
      case true => {
        isCustomisedInput match {
          case true => {
            Future {
              new CityInsertResponse(MyWorldConstants.SuccessText, MyWorldConstants.BlankString, MyWorldConstants.BlankString, "Successfully Updated document. The inputs have been edited to handle the blank requests")
            }
          }
          case false => {
            Future {
              new CityInsertResponse(MyWorldConstants.SuccessText, MyWorldConstants.BlankString, MyWorldConstants.BlankString, "Successfully Updated document.")
            }
          }
        }
      }
      case false => {
        Future {
          new CityInsertResponse(MyWorldConstants.FailureText, "DB error", "Error occurred", "Could not insert document")
        }
      }
    }
  }

  def createRemoveContinentResponse(writeRes:WriteResult):Future[RemoveResponse]={
    writeRes.ok match{
      case true => {
        writeRes.n match {
          case count if count>0 => {
            Future {
              new RemoveResponse(MyWorldConstants.SuccessText, s"Successfully Removed ${writeRes.n} document")
            }
          }
          case _ => {
            Future {
              new RemoveResponse(MyWorldConstants.SuccessText, s"Could not find the continent in the database to remove.")
            }
          }
        }
      }
      case false =>  Future{new RemoveResponse(MyWorldConstants.FailureText, "Could not remove document.Some error occurred.")}
    }
  }

  private def validateCityInfoForAddContinent(cityInfoInput:CityInformation):CityInformationInsert = {
    cityInfoInput.name match {
      case "" => {
        cityInfoInput.country match {
          case ""=> new CityInformationInsert(MyWorldConstants.TRUE,
            new CityInformation(s"${cityInfoInput.region}${MyWorldConstants.CityText}", s"${cityInfoInput.region}${MyWorldConstants.CountryText}", cityInfoInput.region))

          case _ => new CityInformationInsert(MyWorldConstants.TRUE,
            new CityInformation(s"${cityInfoInput.region}${MyWorldConstants.CityText}", cityInfoInput.country, cityInfoInput.region))

        }
      }
      case _ => {
        cityInfoInput.country match {
          case ""=> new CityInformationInsert(MyWorldConstants.TRUE,
            new CityInformation(cityInfoInput.name, s"${cityInfoInput.region}${MyWorldConstants.CountryText}", cityInfoInput.region))
          case _ => new CityInformationInsert(MyWorldConstants.FALSE,
            new CityInformation(cityInfoInput.name, cityInfoInput.country, cityInfoInput.region))
        }
      }
    }
    }

  private def validateCityInfoForAddCity(cityInfoInput:CityInformation):CityInformationInsert={

          cityInfoInput.region match {
            case "" => {
              cityInfoInput.country match {
                case "" => new CityInformationInsert(MyWorldConstants.TRUE,
                  new CityInformation(cityInfoInput.name, s"${cityInfoInput.name}${MyWorldConstants.CountryText}", s"${cityInfoInput.name}${MyWorldConstants.ContinentText}"))

                case _ => new CityInformationInsert(MyWorldConstants.TRUE,
                  new CityInformation(cityInfoInput.name, cityInfoInput.country, s"${cityInfoInput.name}${MyWorldConstants.ContinentText}"))

              }
            }
            case _ => {
              cityInfoInput.country match {
                case ""=> new CityInformationInsert(MyWorldConstants.TRUE,
                  new CityInformation(cityInfoInput.name, s"${cityInfoInput.name}${MyWorldConstants.CountryText}", cityInfoInput.region))
                 case _ => new CityInformationInsert(MyWorldConstants.FALSE,
                  new CityInformation(cityInfoInput.name, cityInfoInput.country, cityInfoInput.region))
              }
            }
          }
        }


  def createEmptyFieldResponse(cityInformation: CityInformation,entryType:String): Future[CityInsertResponse] ={

    entryType match {
      case "Continent"  => {
        cityInformation.region match {
          case "" => Future{new CityInsertResponse(MyWorldConstants.SuccessText, MyWorldConstants.BlankString, errorMessage = MyWorldConstants.BlankString, "Please ensure that the Continent name is valid")}
        }
      }
      case "City" =>{
        cityInformation.name match {
          case "" => Future{new CityInsertResponse(MyWorldConstants.SuccessText, MyWorldConstants.BlankString, errorMessage = MyWorldConstants.BlankString, "Please ensure that the City name is valid")}
       }
      }
      case _ =>{
        Future{new CityInsertResponse(MyWorldConstants.FailureText, "Not a valid request", errorMessage = "Please ensure either continent or City is being handled", "Invalid request")}
      }
    }
  }

  def createInsertCityRequest(entryType:String,cityInformation: CityInformation):Future[CityInformationInsert] = {
    entryType match {
      case "Continent"=> Future{ validateCityInfoForAddContinent(cityInformation)}
      case "City" =>Future{validateCityInfoForAddCity(cityInformation)}
      case _=>Future{new CityInformationInsert(MyWorldConstants.FALSE,new CityInformation(MyWorldConstants.BlankString,MyWorldConstants.BlankString,MyWorldConstants.BlankString))}
    }
  }



}



