var ws;

var myname ="";

var curstatus = "ON";

var username = "";

function changestatus(){


    if(curstatus =="ON"){

        //$("#changestatus").prop("value", "Go Do not Disturb");
        $("#changestatus").val("Go Online");
        curstatus = "OFF";
    }
    else
    {
        $("#changestatus").val("Go Do not disturb");
        curstatus = "ON";
    }
    sendNotification("STATUS");
}

function setConnected(connected) {
	$("#connect").prop("disabled", connected);
	$("#disconnect").prop("disabled", !connected);
}

function getAllUser(){

    $.ajax({
            url: "http://localhost:8080/getall"
        }).then(function(data) {
            //alert(data);

            $.each(data, function(i, obj) {
                var userlistname = obj.username;

                if(userlistname != username && userlistname!=null){
                    var userstatus = obj.status;
                    var classname ="status orange";
                    var userstatusui = "do not disturb";


                    if(userstatus=="ON"){
                        classname = "status green";
                        userstatusui = "Available";
                    }



                    $("#users").append(
                        '<li id="'+userlistname+'" class="available">\
                                        <img src="avatar1.png" alt="">\
                                        <div>\
                                            <h2>'+userlistname+'</h2>\
                                            <h3 id="'+userlistname+'-h3">\
                                                <span id="'+userlistname+'-span" class="'+classname+'"></span>\
                                                '+userstatusui+'\
                                            </h3>\
                                        </div>\
                                    </li>'
                        );


                }



            });



        });

}

function sendNotification(action){
    //alert(username);
    if(action=="ADD"){
        username = $("#name").val();
        var data = JSON.stringify({
        		'ADD' : username
        	})

        ws.send(data);

        return;
    }

    if(action=="STATUS"){

            var data = JSON.stringify({
            		'STATUS' : curstatus
            	})

            ws.send(data);

            return;
    }


    if(action=="SEND"){
            //alert(action);
            var data = JSON.stringify({
            		'SEND' : $("#txtChat").val()
            	})

            ws.send(data);




            return;
    }

    if(action=="REMOVE"){
                //alert(action);
                var data = JSON.stringify({
                		'REMOVE' : 'REMOVED'
                	})

                ws.send(data);
                ws.close();
                $("#username-page").show("slow");
                $("#container").hide("slow");

                myname ="";
                curstatus = "ON";
                username = "";

                return;
        }


}

function login(){


    var type = "add";
    var dataval = "Saad";
    //sendJsonData(type, dataval);

    sendNotification("ADD");

    $("#username-page").hide("slow");
    $("#container").show("slow");

    $("#welcome").html("Welcome, "+username);

    getAllUser();


}




function connect() {
	ws = new WebSocket('ws://localhost:8080/user');

    username = $("#name").val();
	// Connection opened
    ws.addEventListener('open', (event) => {
      login();
      //showUsers();
    });

    // receive message
	ws.onmessage = function(data) {




		const rcvdtxt = data.data
		const jsonob = JSON.parse(rcvdtxt);
		console.log(jsonob.action);




		msgreceived(jsonob);

	}
	setConnected(true);

}



function addchat(){

    var datetime = getCurrentDate();
    var fromuser = "Me";
    var data = $("#txtChat").val();


    $("#chat").append(

        '<li class="me">\
         <div class="entete">\
             <span class="status blue"></span>\
             <h2>'+ fromuser +'</h2>\
             <h3>'+ datetime +'</h3>\
         </div>\
         <div class="triangle"></div>\
         <div class="message">\
         '+data+'\
         </div>\
         </li>'
    );
    $("#txtChat").val("");

}

function msgreceived(data){

    var action = data.action;
    var actionmsg = data.msg;

    console.log(action);
    console.log(actionmsg);


    if(action=="add"){

        var userlistname = actionmsg;
        var classname = "status green";
        var userstatusui = "Available";
        if (username!=userlistname){
            $("#users").append(
                                    '<li id = "'+ actionmsg +'" class="available">\
                                                    <img src="avatar1.png" alt="">\
                                                    <div>\
                                                        <h2>'+userlistname+'</h2>\
                                                        <h3 id="'+userlistname+'-h3">\
                                                            <span id="'+userlistname+'-span" class="'+classname+'"></span>\
                                                            '+userstatusui+'\
                                                        </h3>\
                                                    </div>\
                                                </li>'
                                    );
            }

    }


    if(action=="remove"){
        var userlistnamedata = data.user;
        $('#'+userlistnamedata).remove();

    }

    if(action=="rcv"){
        var datetime = getCurrentDate();
        var userlistname = data.user;
        console.log(data);
        console.log("Sender: "+ userlistname + " - Receiver: "+ username);

            $("#chat").append(

                    '<li class="you">\
                     <div class="entete">\
                         <span class="status blue"></span>\
                         <h2>'+ userlistname +'</h2>\
                         <h3>'+ datetime +'</h3>\
                     </div>\
                     <div class="triangle"></div>\
                     <div class="message">\
                     '+actionmsg+'\
                     </div>\
                     </li>'
                );


    }

    if(action=="status"){

        var datetime = getCurrentDate();
        var userlistname = data.user;
        var userstatus = actionmsg;

        var classname ="status orange";
        var userstatusui = "do not disturb";


        if(userstatus=="ON"){
            classname = "status green";
            userstatusui = "Available";
        }

        var htmlval = '<span id="'+userlistname+'-span" class="'+classname+'"></span>'+userstatusui;
        $('#'+userlistname+'-h3').html(htmlval);


        //$('#'+userlistname).remove();



    }



    //addchat(data);

}

/*
function sendJsonData(action, text){
    var msg = JSON.stringify({

              		'data' : text
              	});



    ws.send(JSON.stringify(msg));


}
*/



function sendData() {



    //var msg = {user: "ADD", data: $("#txtChat").val()};
    //var msg = {user: "ADD", data: $("#txtChat").val()};
	//var msg = $("#txtChat").val();
    sendNotification("SEND");
	addchat();
	//ws.send(data);
}

function sendJsonData(action, text){
    var msg = JSON.stringify({
              		'type' : action,
              		'data' : text
              	})


    alert(msg);
    ws.send(JSON.stringify(msg));


}

//function helloWorld(message) {
//	$("#helloworldmessage").append("<tr><td> " + message + "</td></tr>");
//}

$(function() {
	$("form").on('submit', function(e) {
		e.preventDefault();
	});
	$("#connect").click(function() {
		connect();
	});

	$("#send").click(function() {
		sendData();
	});

	$("#login").click(function() {
        connect();
    });

    $("#changestatus").click(function() {
            changestatus();
        });

    $("#close").click(function() {
            sendNotification("REMOVE");
    });





});

function getCurrentDate(){
    var currentdate = new Date();
    var datetime = "" + currentdate.getDate() + "/"
                    + (currentdate.getMonth()+1)  + "/"
                    + currentdate.getFullYear() + " @ "
                    + currentdate.getHours() + ":"
                    + currentdate.getMinutes() + ":"
                    + currentdate.getSeconds();
    return datetime;
}
