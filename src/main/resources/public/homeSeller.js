function nameUpdate(){
    var x = document.getElementById("gettingName");
    document.getElementById("titleplaceholder").innerHTML = x.value;
}
function priceUpdate(){
    var x = document.getElementById("gettingPrice");
    document.getElementById("priceplaceholder").innerHTML = "$" + x.value;
}
function descriptionUpdate(){
    var x = document.getElementById("gettingDescription");
    document.getElementById("descriptionplaceholder").innerHTML =  x.value;
}


/*
function showImage(src, target){
    var fr = new FileReader();
    fr.onload = function(){
        target.src = fr.result;
    }
    fr.readAsDataURL(src.files[0]);
}

function putImage(){
    var src = document.getElementById("file");
    var target = document.getElementById("target");
    showImage(src, target);
}
*/
