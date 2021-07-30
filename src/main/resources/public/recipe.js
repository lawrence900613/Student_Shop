console.log("HELLO OUTPUTTED!");
// var table = document.getElementById("Table").getElementsByTagName("td");
var table = document.getElementById("Table");
var ingredients = [];
//iterate trough rows
for (var i = 1, row; row = table.rows[i]; i++) {
//iterate trough columns
   var col = row.cells[0]
      // do something
      ingredients.push(col.innerText);
      
   }


console.log(ingredients);