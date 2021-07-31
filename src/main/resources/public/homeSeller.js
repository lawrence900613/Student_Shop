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
