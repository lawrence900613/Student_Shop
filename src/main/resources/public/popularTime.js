
document.getElementById("locSubmit").addEventListener('click', function(event){ 
  myScript();
})

async function myScript(){

  var name = document.getElementById("name").value;
  var address = document.getElementById("address").value;

  console.log(name);
  console.log(address);

  const params = new URLSearchParams({ 
    'api_key_private': 'pri_6c87a5b42c61433cb350fbd5666bee7d',
     'venue_name': name,
     'venue_address': address
   });

  const response = await fetch(`https://besttime.app/api/v1/forecasts?${params}`, {method: 'POST'});
  
  const response2 = await fetch(`https://besttime.app/api/v1/forecasts/live?${params}`, {method: 'POST'});

  const data = await response.json();
  
  console.log(data); 
 
  const data2 = await response2.json();
  
  console.log(data2); 

  if(data.status == "error" || data.status == "Error"){
    alert(data.message);
    document.getElementById("venueName").innerHTML = "";
    document.getElementById("venueAddress").innerHTML = "";
    for (var i = 0; i < 7; i++) {
      document.getElementById("openTime"+i).innerHTML = ""; //RESET
      document.getElementById("closeTime"+i).innerHTML = "";
      document.getElementById("suggestTime"+i).innerHTML = "";
      document.getElementById("maxTime"+i).innerHTML = "";
    }
    return;
  }

  for (var i = 0; i < data.analysis.length; i++) {
    var counter = data.analysis[i];

    document.getElementById("venueName").innerHTML = data.venue_info.venue_name;
    document.getElementById("venueAddress").innerHTML = data.venue_info.venue_address;

    if(counter.day_info.venue_open  == 0 && counter.day_info.venue_closed == 0){
      document.getElementById("openTime"+i).innerHTML = "24 Hrs";
      document.getElementById("closeTime"+i).innerHTML = "24 Hrs";
    }
    else{
      document.getElementById("openTime"+i).innerHTML = counter.day_info.venue_open + ":00";
      document.getElementById("closeTime"+i).innerHTML = counter.day_info.venue_closed + ":00";
    }

    var suggestLength = parseInt(counter.quiet_hours.length / 2); //MIGHT NEED TO FIX
    console.log(suggestLength)

    document.getElementById("suggestTime"+i).innerHTML = counter.quiet_hours[suggestLength] + ":00";

    try{
      var temp = counter.peak_hours[0].peak_max;

      if(counter.peak_hours[0].peak_status == "error"){
        console.log("status error caught")
        document.getElementById("maxTime"+i).innerHTML = "unavailable";
      }
      else
        document.getElementById("maxTime"+i).innerHTML = temp + ":00";
    }
    catch{
      document.getElementById("maxTime"+i).innerHTML = "unavailable";
    }

  }

  if( data2.status == "error" || data2.status == "Error"){
    alert(data2.message);
    document.getElementById("liveStatus").innerHTML = "";
  }
  else{

    if(data2.analysis.venue_live_busyness < 40){
      document.getElementById("RowTwo").style.backgroundColor="#1fed64"
    }
    else if(data2.analysis.venue_live_busyness < 70){
      document.getElementById("RowTwo").style.backgroundColor="#f0fa2d"
    }
    else{
      document.getElementById("RowTwo").style.backgroundColor="#f54242"
    }

    document.getElementById("liveStatus").innerHTML = data2.analysis.venue_live_busyness + "% of store capacity";
  }

}

document.addEventListener("DOMContentLoaded", function(){
  //dom is fully loaded, but maybe waiting on images & css files
  var links=document.getElementById("log").href;
  console.log(links);
  var length =links.length;
  var lastelement = links[length-1];
  console.log(lastelement);
  
  if(lastelement == null){
      lastelement = 0;
  }else{
      if(parseInt(lastelement) > 0){
      document.getElementById("log").href = "/logout/" + lastelement;
      document.getElementById("log").innerHTML = "Logout";
  }
}

});
