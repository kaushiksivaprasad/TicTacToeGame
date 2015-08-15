<!DOCTYPE html>
<html lang="en">
<head>
<title>Tic Tac Toe..!</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script
	src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<style>
table {
	width: 100%;
	border-collapse: separate;
}

td {
	width: 33%;
	line-height: 4.5;
	background-color: #C0C0C0;
	border: 1px solid grey;
	box-shadow: 0px 0px 2px 1px #000;
	cursor: pointer;
}

tr {
	width: 100%;
}

img {
	width: 30px;
	height: 30px;
}
</style>
</head>

<body>
	<div class="text-center">
		<blockquote>
			<h2>Tic Tac Toe..!</h2>
		</blockquote>
	</div>
	<div>
		<div class="text-center">
			<blockquote>
				<span id="playerDisplay"></span>
			</blockquote>
		</div>
		<div id="#ticTacToeContentDiv" class="row" style="margin-top: 10%">
			<div class="col-md-4 col-sm-4 col-lg-4 col-xs-2">&nbsp;</div>
			<div class="col-md-4 col-sm-4 col-lg-4 col-xs-8">
				<table class="text-center">
					<tr>
						<td id="0,0">&nbsp;</td>
						<td id="0,1">&nbsp;</td>
						<td id="0,2">&nbsp;</td>
					</tr>
					<tr>
						<td id="1,0">&nbsp;</td>
						<td id="1,1">&nbsp;</td>
						<td id="1,2">&nbsp;</td>
					</tr>
					<tr>
						<td id="2,0">&nbsp;</td>
						<td id="2,1">&nbsp;</td>
						<td id="2,2">&nbsp;</td>
					</tr>
				</table>
			</div>
			<div class="col-md-4 col-sm-4 col-lg-4 col-xs-2">&nbsp;</div>
		</div>
		<div id="userSelection" class="modal fade" role="dialog">
			<div class="modal-dialog">

				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<!-- <button type="button" class="close" data-dismiss="modal">&times;</button> -->
						<h4 class="modal-title">Player Selection:</h4>
					</div>
					<div class="modal-body">
						<div class="text-center">
							<p>Pls Enter your name against the symbol:</p>
							<br> <img src="./resources/circle.png"
								style="width: 30px; height: 30px;" /> <input type="text"
								id="user1" /> <br> <img src="./resources/cross.png"
								style="width: 30px; height: 30px;" /> <input type="text"
								id="user2" />
						</div>
					</div>
					<div class="modal-footer">
						<button id="submitBtn" type="button" class="btn btn-default">Submit</button>
					</div>
				</div>
			</div>
		</div>

		<div id="resultPopup" class="modal fade" role="dialog">
			<div class="modal-dialog">

				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-body">
						<div class="text-center" id="resultContent"></div>
					</div>
				</div>
				<div class="modal-footer">
					<button id="playAgain" type="button" class="btn btn-default">Play
						Again!</button>
				</div>
			</div>
		</div>
</body>
<script>
	$(document).ready(function(){
		var user1 = "";
		var user2 = "";
		var count = 0;
		var killSession = true;
		initialize();
		$("#userSelection").modal({"backdrop" : "static"});
		

		$("#submitBtn").click(function(){
			var userName = $("#user1").val();
			if(userName){
				user1 = userName.trim();
			}
			userName = $("#user2").val();
			if(userName){
				user2 = userName.trim();
			}
			$("#userSelection").modal("hide");
			displayUserTurn();
		});
		function displayUserTurn(){
			if(count %2 == 0){
				var spanContent = "<span><img src='resources/circle.png'>&nbsp;"+user1+"'s turn"+"</span>";
				$("#playerDisplay").html(spanContent);
			}
			else{
				var spanContent = "<span><img src='resources/cross.png'>&nbsp;"+user2+"'s turn"+"</span>";
				$("#playerDisplay").html(spanContent);
			}
		}
		function initialize(){
			killSession = true;
			count = 0;
			$("td").each(function() {
				$(this).click(function(){
					var coOrds = $(this).attr("id");
					coOrds = coOrds.split(",");
					var userId = (count % 2 == 0)? 1 : 2;
					var requestParam = {
							"userId" : userId,
							"x"	: coOrds[0],
							"y" : coOrds[1],
							"killSession" : killSession
					};
					var fetchJsonAsync = function(obj){
						var tdObj = obj; 
						$.getJSON("/TicTacToe-1/GameService",requestParam, function(data){
							if(killSession){
								killSession = false;
							}
							var result = data.result;
							if(!data.error){
								if(count % 2 == 0){
						    		$(tdObj).html("<img src='resources/circle.png'>");
						    		$(tdObj).unbind();
						    		$(tdObj).css("box-shadow","0px 0px 2px 1px #000");
						    	}
						    	else{
						    		$(tdObj).html("<img src='resources/cross.png'>");
						    		$(tdObj).unbind();
						    		$(tdObj).css("box-shadow","0px 0px 2px 1px #000");
						    	}
								if(result == -1){
							    	count++;
							    	displayUserTurn();
								}
								else {
						    		displayResultMessage(result);
						    	}
							}
							else{
								alert(result);
								alert(data.error);
							}
							
						});
					}
					fetchJsonAsync($(this));
			    });
				$(this).hover(function(){
					$(this).css("box-shadow","0px 0px 1px 1px #00FFFF inset");
				},function(){
					$(this).css("box-shadow","0px 0px 2px 1px #000");
				});
				$(this).html("&nbsp");
			});

		}
		$("#playAgain").click(function(){
			initialize();
			$("#resultPopup").modal("hide");
			$("#userSelection").modal({"backdrop" : "static"});
		});
		function displayResultMessage(result){
			if(result == 0){
				$("#resultContent").html("The Game has been drawn.");
			}
			else if(result == 1){
				$("#resultContent").html("Congrats "+user1+"!");
			}
			else{
				$("#resultContent").html("Congrats "+user2+"!");
			}
			$("#resultPopup").modal({"backdrop" : "static"});
		}
	});
</script>

</html>
