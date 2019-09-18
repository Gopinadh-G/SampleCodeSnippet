 $('[data-toggle="tooltip"]').tooltip();
    $("#_findCity" ).click(function() {
        var continentName=$("#findCityForContinent").val()
         if($.trim(continentName)=="") continentName="invalid"

            $.ajax({
                type:'GET',
                url: "/findCity/"+continentName,
                success: function(result){
                        var finalResult="";
                        $.each(result, function(index, item) {
                           finalResult+=result[index].name+" "
                        })
                if(finalResult=="")
                    $("#_findCityResult").text("No City found for ::"+continentName)
                     else
                     $("#_findCityResult").text("The List of cities are::"+finalResult)

                    }
                    ,
                      error: function(err){
                        $("#_findCityResult").text(  err.responseText)
                       }
             });
      });


 $("#_showContinents" ).click(function() {
                       $.ajax({
                            type:'GET',
                            url: "/getAllContinents/",
                            success: function(result){
                                  $("#_showAllContinentsResult").text(result.continentsList)
                                },error: function(err){
                                   $("#_showAllContinentsResult").text(  err.responseText)
                                }
                         });
                  });
      $("#_deleteContinent" ).click(function() {
                    var countryName=$("#deleteContinentName").val()
                     if(countryName=="") countryName="invalid"

                        $.ajax({
                            type:'GET',
                            url: "/deleteContinent/"+countryName,
                            success: function(result){
                                $("#_deleteContinentResult").text(result.displayMessage)
                                }
                                 ,
                                                      error: function(err){
                                                        $("#_deleteContinentResult").text(  err.responseText)
                                                       }
                         });
                  });
      $("#_findContinent" ).click(function() {
              var countryName=$("#countryName").val()
                if($.trim(countryName)=="") countryName="invalid"


                  $.ajax({
                      type:'GET',
                      url: "/getContinentForCountry/"+countryName,
                      success: function(result){
                               $("#_findContinentResult").text(result.displayMessage)
                          },
                      error: function(err){
                          $("#_findContinentResult").text(  err.responseText)
                        }
                   });
            });
            $("#_groupCity" ).click(function() {
            var userName=$("#userName").val();
                             $.ajax({
                                  type:'GET',
                                  url: "/sortCities/"+userName,
                                  success: function(result){
                                   if(result.isAuthenticated){
                                   var finalResult=""
                                   $.each( result.cityMap, function(index,value){
                                    finalResult+="Continent = " + index + " Cities = " + value+" !!!!";
                                   })

                                      $("#_groupCitiesResult").text(finalResult)
                                    }else{
                                         $("#groupCities").addClass("hidden")
                                          $("#_groupCitiesResult").addClass("hidden")
                                          $("#groupCitiesDisabled").removeClass("hidden")
                                     }
                                     },error: function(err){
                                                                 $("#_groupCitiesResult").text(  err.responseText)
                                                               }


                        });
                });

        $("#_addCity" ).click(function() {
         var name=$("#newCity").val()
         var country=$("#newCityCountry").val()
         var region=$("#newCityRegion").val()

              var response=
                  $.ajax({
                      type:'POST',
                      url: "/addCity/",
                       contentType: 'application/json',
                      data:JSON.stringify({
                                            "name":name,
                                            "country":country,
                                            "region":region
                                            }),
                      success: function(result){
                          $("#_addCityResult").text(result.displayMessage)
                          },error: function(err){
                                                                                            $("#_addCityResult").text(  err.responseText)
                                                                                          }

                   });
            });
  $("#_addContinent" ).click(function() {
         var name=$("#newContinentCity").val()
         var country=$("#newContinentCountry").val()
         var region=$("#newContinent").val()

              var response=
                  $.ajax({
                      type:'POST',
                      url: "/addContinent/",
                       contentType: 'application/json',
                      data:JSON.stringify({
                                            "name":name,
                                            "country":country,
                                            "region":region
                                            }),
                      success: function(result){
                          $("#_addContinentResult").text(result.displayMessage)
                          },error: function(err){
                            $("#_addContinentResult").text(  err.responseText)
                          }
                   });
            });
      $("#loginbutton" ).click(function() {
              var name=$("#loginname").val()
              var pwd=$("#loginpwd").val()

                  $.ajax({
                      type:'POST',
                      url: '/authenticateUser/',
                      contentType: 'application/json',
                      data:JSON.stringify({
                      "name":name,
                      "pwd":pwd,
                      }),
                      success: function(result){
                        if(result.isAuthenticated){
                        $("#welcomeMessage").text("Hello "+result.username+"!!You have full access to the application!!")
                        $("#userName").val(result.username)
                        $("#loginform").addClass("hidden")
                        $("#homemessage").removeClass("hidden")
                        }
                        else{

                         $("#welcomeMessage").text("Hello "+result.username+"!!You have limited access to  the application!!")
                         $("#userName").val(result.username)
                         $("#loginform").addClass("hidden")
                         $("#homemessage").removeClass("hidden")
                        }
                      }
                   });
            });
            $("#_findIfSibling" ).click(function() {
                    var firstCountryName=$("#firstCountry").val()
                     var secondCountryName=$("#secondCountry").val()
                    if(secondCountryName=="") secondCountryName="invalid"
                     if(firstCountryName=="") firstCountryName="invalid"

                        $.ajax({
                            type:'GET',
                            url: "/checkSiblingCountries/"+firstCountryName+"/"+secondCountryName,
                            success: function(result){
                                var message="Are Sibling countries? "+result.areSiblings+". "+result.displayMessage
                                $("#_findSiblingCountriesResult").text(message)
                                }
                                ,error: function(err){
                                                            $("#_findSiblingCountriesResult").text(  err.responseText)
                                                          }
                         });
                  });


$("#findContinentForCountryHeader").click(function() {
    $("#homemessage").addClass("hidden")
    $("#findContinentForCountry").removeClass("hidden")
    $("#findAllCitiesOfAContinent").addClass("hidden")
    $("#findSiblingCountries").addClass("hidden")
    $("#addContinent").addClass("hidden")
    $("#addCities").addClass("hidden")
    $("#groupCities").addClass("hidden")
    $("#groupCitiesDisabled").addClass("hidden")

});

$("#findAllCitiesOfAContinentHeader").click(function() {
    $("#homemessage").addClass("hidden")
    $("#findContinentForCountry").addClass("hidden")
    $("#findAllCitiesOfAContinent").removeClass("hidden")
    $("#findSiblingCountries").addClass("hidden")
    $("#addContinent").addClass("hidden")
    $("#addCities").addClass("hidden")
    $("#groupCities").addClass("hidden")
    $("#groupCitiesDisabled").addClass("hidden")

});

$("#knowearthheader").click(function() {
    $("#homemessage").removeClass("hidden")
    $("#findContinentForCountry").addClass("hidden")
    $("#findAllCitiesOfAContinent").addClass("hidden")
    $("#findSiblingCountries").addClass("hidden")
    $("#addContinent").addClass("hidden")
    $("#addCities").addClass("hidden")
    $("#groupCities").addClass("hidden")
    $("#groupCitiesDisabled").addClass("hidden")

});

$("#findSiblingCountriesHeader").click(function() {
    $("#homemessage").addClass("hidden")
    $("#findContinentForCountry").addClass("hidden")
    $("#findAllCitiesOfAContinent").addClass("hidden")
    $("#findSiblingCountries").removeClass("hidden")
    $("#addContinent").addClass("hidden")
    $("#addCities").addClass("hidden")
    $("#groupCities").addClass("hidden")
    $("#groupCitiesDisabled").addClass("hidden")

});

$("#addContinentHeader").click(function() {
    $("#homemessage").addClass("hidden")
    $("#findContinentForCountry").addClass("hidden")
    $("#findAllCitiesOfAContinent").addClass("hidden")
    $("#findSiblingCountries").addClass("hidden")
    $("#addContinent").removeClass("hidden")
    $("#addCities").addClass("hidden")
    $("#groupCities").addClass("hidden")
    $("#groupCitiesDisabled").addClass("hidden")

});

$("#addCitiesHeader").click(function() {
    $("#homemessage").addClass("hidden")
    $("#findContinentForCountry").addClass("hidden")
    $("#findAllCitiesOfAContinent").addClass("hidden")
    $("#findSiblingCountries").addClass("hidden")
    $("#addContinent").addClass("hidden")
    $("#addCities").removeClass("hidden")
    $("#groupCities").addClass("hidden")
    $("#groupCitiesDisabled").addClass("hidden")

});


$("#groupCitiesHeader").click(function() {
    $("#homemessage").addClass("hidden")
    $("#findContinentForCountry").addClass("hidden")
    $("#findAllCitiesOfAContinent").addClass("hidden")
    $("#findSiblingCountries").addClass("hidden")
    $("#addContinent").addClass("hidden")
    $("#addCities").addClass("hidden")
    $("#groupCities").removeClass("hidden")
    $("#groupCitiesDisabled").addClass("hidden")
});


$("#addContinentSubHeader").click(function() {
    $("#continentHomeMessage").addClass("hidden")
    $("#deleteAContinent").addClass("hidden")
    $("#addANewContinent").removeClass("hidden")
    $("#showAllContinents").addClass("hidden")
    $("#_deleteContinentResult").text("")
    $("#_addContinentResult").text("")
    $("#_showAllContinentsResult").text("")
});

$("#deleteContinentSubHeader").click(function() {
    $("#continentHomeMessage").addClass("hidden")
    $("#deleteAContinent").removeClass("hidden")
    $("#addANewContinent").addClass("hidden")
    $("#showAllContinents").addClass("hidden")
    $("#_deleteContinentResult").text("")
    $("#_addContinentResult").text("")
    $("#_showAllContinentsResult").text("")
});


$("#showContinentsSubHeader").click(function() {
    $("#continentHomeMessage").addClass("hidden")
    $("#deleteAContinent").addClass("hidden")
    $("#addANewContinent").addClass("hidden")
    $("#showAllContinents").removeClass("hidden")
    $("#_deleteContinentResult").text("")
    $("#_addContinentResult").text("")
    $("#_showAllContinentsResult").text("")
});