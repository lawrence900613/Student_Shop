console.log("HELLO OUTPUTTED!");
// var table = document.getElementById("Table").getElementsByTagName("td");
var table = document.getElementById("Table");
var ingredients = [];
//iterate trough rows
for (var i = 1, row; row = table.rows[i]; i++) {
    //check if category is food
    var category = row.cells[1];
    if (category.innerText == "Food"){
        console.log("YES FOODDD");
   
        //iterate trough columns
        var col = row.cells[0]
        // do something
        ingredients.push(col.innerText);
    }
      
}

const data = null;

const xhr = new XMLHttpRequest();
xhr.withCredentials = true;

// xhr.addEventListener("readystatechange", function () {
// 	if (this.readyState === this.DONE) {
// 		console.log(this.responseText);
// 	}
// });
var baseURL = "https://edamam-recipe-search.p.rapidapi.com/search?q=";
baseURL+=ingredients[0];

//now loop through rest of ingredients to put into parameter of api

for (var k =1; k<ingredients.length; k++){
    baseURL+="%2C"+ingredients[k];
}


xhr.open("GET", baseURL);
xhr.setRequestHeader("x-rapidapi-key", "b1e1ab1091mshcc9d4bcef44cbf0p1f1095jsn68c0743c6ce6");
xhr.setRequestHeader("x-rapidapi-host", "edamam-recipe-search.p.rapidapi.com");

// xhr.send(data); //prints out in the json format of the data
if(ingredients.length>=1){
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            if (xhr.status == 200) {
                var dataone = xhr.responseText;

                var jsonResponse = JSON.parse(dataone); // gets the whole json file that is returned
                console.log(jsonResponse);
                for (var l = 0; l<3; l++){
                    // console.log(jsonResponse["hits"][l]["recipe"]["label"]);
                    // console.log(jsonResponse["hits"][l]["recipe"]["image"]);
                    // console.log(jsonResponse["hits"][l]["recipe"]["url"]);

                    document.getElementById("pic"+l).src = jsonResponse["hits"][l]["recipe"]["image"];
                    document.getElementById("name"+l).innerText = jsonResponse["hits"][l]["recipe"]["label"];
                    document.getElementById("link"+l).href = jsonResponse["hits"][l]["recipe"]["url"];


                }
                //console.log(jsonResponse["hits"][0]["recipe"]["label"]);   //first returns all hits(10 results), get first item, 
                                                                //get recipe of first (access to label, pic, etc), get title 
            }
        }
    };
}
else{
    document.getElementById("recipeheader").innerText= "Add some food into your shopping cart to see generated recipes!";
    for (var l = 0; l<3; l++){
    
        document.getElementById("pic"+l).style.display = "none";
        document.getElementById("name"+l).innerText = " ";
        //document.getElementById("link"+l).href = jsonResponse["hits"][l]["recipe"]["url"];


    }
}

xhr.send(null);


// var datatwo=xhr.responseText;
// console.log(datatwo);
// var jsonResponse = JSON.parse(datatwo);
// console.log(jsonResponse["Hit"]);


// console.log(ingredients);