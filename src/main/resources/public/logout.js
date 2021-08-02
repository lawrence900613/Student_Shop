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


