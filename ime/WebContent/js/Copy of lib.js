
$(document).ready(function() {

	controleAbas();
	controleAberturaNos();
	controleSplitter();
	controleCTree();
	controleDragDrop();

	setTimeout(function() {$(".message_warning").hide("slow");}, 8000);


}); // Fim document.ready

function controleCTree() {
	var valcTreeEnv = ReadCookie("valcTreeEnv");
	var valcTreeRole = ReadCookie("valcTreeRole");
	if (valcTreeEnv == null || valcTreeEnv.length == 0) {
		SetCookie("valcTreeEnv","ON",5);
		valcTreeEnv = 'ON';
		$('#raizMIEdition .environment').css('display', 'inline');
	}
	else {
		$('#raizMIEdition .environment').css('display',valcTreeEnv=="OFF"?"none":"inline");
	}
	if (valcTreeRole == null || valcTreeRole.length == 0) {
		SetCookie("valcTreeRole","ON",5);
		$('#raizMIEdition .learner').css('display', 'inline');
		$('#raizMIEdition .staff').css('display', 'inline');
		valcTreeRole = 'ON';
	} else {
		$('#raizMIEdition .learner').css('display', valcTreeRole=="OFF"?"none":"inline");
		$('#raizMIEdition .staff').css('display', valcTreeRole=="OFF"?"none":"inline");
	}

	$("#cTree > a").remove();

	$('<a href="#">[Env '+(valcTreeEnv)+"]</a>").click(function() {
		valcTreeEnv = (valcTreeEnv=="OFF"?"ON":"OFF");
		SetCookie("valcTreeEnv",valcTreeEnv,5);
		$(event.currentTarget).html('[Env '+(valcTreeEnv)+"]");

		$('#raizMIEdition .environment').css('display',valcTreeEnv=="OFF"?"none":"inline");

	}).appendTo($("#cTree"));

	$('<a href="#" >[Role '+(valcTreeRole)+"]</a>").click(function() {
		valcTreeRole = (valcTreeRole=="OFF"?"ON":"OFF");
		SetCookie("valcTreeRole",valcTreeRole,5);
		$(event.currentTarget).html('[Role '+(valcTreeRole)+"]");

		$('#raizMIEdition .learner').css('display', valcTreeRole=="OFF"?"none":"inline");
		$('#raizMIEdition .staff').css('display', valcTreeRole=="OFF"?"none":"inline");

	}).appendTo($("#cTree"));

	resizeCTree();

	$(window).resize(function() {

		resizeCTree();


	});



}

function resizeCTree() {
	var widthLeftPane = ReadCookie("widthLeftPane");

	if (widthLeftPane == undefined || widthLeftPane.length == 0)
		widthLeftPane = 0;
	else 
		widthLeftPane = parseInt(ReadCookie("widthLeftPane"));

	if (widthLeftPane <= 0)
		widthLeftPane = $("#LeftPane").width();


	$("#cTree").css("right",this.innerWidth-widthLeftPane+17);


}

function controleDragDrop() {

	$('#activitiesList').sortable();
	$( "#activitiesList" ).disableSelection();

	$('#envList').sortable();
	$( "#envList" ).disableSelection();

	$('#learnerList').sortable();
	$( "#learnerList" ).disableSelection();

	$('#staffList').sortable();
	$( "#staffList" ).disableSelection();

	$('.moveLdep').sortable({
		distance: 15,
		start: function(event, ui) {
			ui.item.startPos = ui.item.index();
		},
		stop: function(event, ui) {

			$.get("app.do?action=ldep.switch.pos.ldep&start="+ui.item.startPos+"&stop="+ui.item.index()+"&item="+ui.item[0].id, function(ev) {

				/*if (ev != "OK") {
        			$(".message_warning").html("Você não tem permissão para essa alteração!");
        			$(".message_warning").show("slow");

        			setTimeout(function() {$(".message_warning").hide("slow");}, 15000);
        		}*/

			});

		}
	});
	$( ".moveLdep" ).disableSelection();


	$('.movePlays').sortable({
		distance: 15,
		start: function(event, ui) {
			ui.item.startPos = ui.item.index();
		},
		stop: function(event, ui) {

			$.get("play.do?action=play.switch.pos.play&start="+ui.item.startPos+"&stop="+ui.item.index()+"&item="+ui.item[0].id, function(ev) {

				/*if (ev != "OK") {
    			$(".message_warning").html("Você não tem permissão para essa alteração!");
    			$(".message_warning").show("slow");

    			setTimeout(function() {$(".message_warning").hide("slow");}, 15000);
    		}*/
			});

		}
	});
	$( ".movePlays" ).disableSelection();


	$( '.moveActs' ).sortable({
		distance: 15,
		start: function(event, ui) {
			ui.item.startPos = ui.item.index();
		},
		stop: function(event, ui) {

			$.get("play.do?action=play.switch.pos.act&start="+ui.item.startPos+"&stop="+ui.item.index()+"&item="+ui.item[0].id, function(ev) {

				/*if (ev != "OK") {
    			$(".message_warning").html("Você não tem permissão para essa alteração!");
    			$(".message_warning").show("slow");

    			setTimeout(function() {$(".message_warning").hide("slow");}, 15000);
    		}*/
			});

		}
	});
	$( ".moveActs" ).disableSelection();

	$( '.moveRPs' ).sortable({
		distance: 15,
		start: function(event, ui) {
			ui.item.startPos = ui.item.index();
		},
		stop: function(event, ui) {

			$.get("act.do?action=act.switch.pos.rp&start="+ui.item.startPos+"&stop="+ui.item.index()+"&item="+ui.item[0].id, function(ev) {

				/*if (ev != "OK") {
    			$(".message_warning").html("Você não tem permissão para essa alteração!");
    			$(".message_warning").show("slow");

    			setTimeout(function() {$(".message_warning").hide("slow");}, 15000);
    		}*/

			});

		}
	});
	$( ".moveRPs" ).disableSelection();

}	

function controleAbas() {

	if ($(".TabControl").length > 0) {
		$(".TabControl #content ."+ $(".TabControl").attr("aba")).css("display", "block");
		$(".TabControl .abas ."+ $(".TabControl").attr("aba")).addClass("selected");  

		//$(".TabControl #content div:nth-child("+ $(".TabControl").attr("data")+")").css("display", "block");
		//$(".TabControl .abas li:nth-child("+ $(".TabControl").attr("data")+") div").addClass("selected");  
		//$(".TabControl .abas li:first div").addClass("selected");  

		$(".TabControl .aba").click(function() {

			$(".TabControl .aba").removeClass("selected");
			$(this).addClass("selected");
			var indice = $(this).parent().index();
			indice++;
			$(".TabControl #content div").css("display", "none");
			$(".TabControl #content div:nth-child("+indice+")").fadeIn("slow");//.css("display", "block");
		});

		$(".TabControl .aba").hover( function(){$(this).addClass("ativa");},  function(){$(this).removeClass("ativa");} );   
	}
}

function controleAberturaNos() {

	$(".arvoreMenos").click(function(e) {	
		onClickArvoreMenos(this);
		return false;
	});

	$(".arvoreMais").click(function(e) {		
		onClickArvoreMais(this);
		return false;

	});

}


function onClickArvoreMais(el) {

	$.get($(el).attr("href"), function(ev) {

	});

	$(el).unbind('click');


	var lenChilds = $($(el).parent("li")[0]).children().length;


	$($($(el).parent("li")[0]).children()[lenChilds-1]).toggleClass("ulFechado");



	$(el).addClass("arvoreMenos").removeClass("arvoreMais").click(function(e) {	
		onClickArvoreMenos(this);
		return false;
	});

}

function onClickArvoreMenos(el) {

	$.get($(el).attr("href"), function(ev) {

	});

	$(el).unbind('click');

	var lenChilds = $($(el).parent("li")[0]).children().length;	

	$($($(el).parent("li")[0]).children()[lenChilds-1]).toggleClass("ulFechado");
	$(el).addClass("arvoreMais").removeClass("arvoreMenos").click(function(e) {		
		onClickArvoreMais(this);
		return false;
	});

}

function controleSplitter() {
	// controle do splitter geral

	var widthLeft = ReadCookie("widthLeftPane"); 

	if (parseInt(ReadCookie("widthLeftPane")) > 0) {

		if ($("#vsplitbar").length > 0) {
			$("#vsplitbar").css("left",parseInt(ReadCookie("widthLeftPane"))-3);
			$("#RightPane").css("left",parseInt(ReadCookie("widthLeftPane")));
			$("#LeftPane").css("width",parseInt(ReadCookie("widthLeftPane")));
		}
		else  {
			$("#RightPane").css("left",0);
		}

		resizeCTree()
	} 
	var splitEnter = false;	

	function deslocamento(event) {

		// $("#RightPane").html(event.clientX + " - "+innerWidth);

		if (event.clientX < innerWidth * 0.667 && event.clientX > 200) {
			$("#vsplitbar").css("left",event.clientX-3);
			$("#RightPane").css("left",event.clientX);
			$("#LeftPane").css("width",event.clientX);

			SetCookie("widthLeftPane",event.clientX,1);

			resizeCTree();
		}
	}	

	$("#vsplitbar").mousedown(function(event){
		splitEnter = true;
	});

	$("#vsplitbar").mouseup(function(){
		splitEnter = false;
	});

	$("#RightPane").mouseup(function(){
		splitEnter = false;
	});

	$("#LeftPane").mouseup(function(){
		splitEnter = false;
	});

	$("#vsplitbar").mousemove(function(event){		
		if (splitEnter) {
			deslocamento(event);
		}
	});

	$("#RightPane").mousemove(function(event){
		if (splitEnter) {
			deslocamento(event);
		}
	});

	$("#LeftPane").mousemove(function(event){
		if (splitEnter) {
			deslocamento(event);
		}
	});

	$("#LeftPane")[0].scrollTop = ReadCookie("scrollLeftPane");

	setTimeout(function() {
		$("#LeftPane").scroll(function(event) {

			SetCookie("scrollLeftPane",event.currentTarget.scrollTop,1);
		});

	}, 100);
}

function liHover(event, count) {
	if (count == undefined)
		count = 1;
	$(event.currentTarget).css('background-color','rgba(255,255,0,0.2)');
	for (var i = 0; i < count; i++) {
		var item = $(event.currentTarget).find(".controls")[i];
		$(item).clearQueue();
		$(item).animate({opacity:1}, 2000);
	}
}

function liOut(event) {
	$(event.currentTarget).css('background-color','rgba(255,255,0,0)');
	var item = $(event.currentTarget).find(".controls");
	$(item).animate({opacity:0}, 2000);

}

//cookies
function SetCookie(cookieName,cookieValue,nDays) {
	var today = new Date();
	var expire = new Date();
	if (nDays==null || nDays==0) nDays=1;
	expire.setTime(today.getTime() + 3600000*24*nDays);
	document.cookie = cookieName+"="+escape(cookieValue)
	+ ";expires="+expire.toGMTString();
}

function ReadCookie(cookieName) {
	var theCookie=" "+document.cookie;
	var ind=theCookie.indexOf(" "+cookieName+"=");
	if (ind==-1) ind=theCookie.indexOf(";"+cookieName+"=");
	if (ind==-1 || cookieName=="") return "";
	var ind1=theCookie.indexOf(";",ind+1);
	if (ind1==-1) ind1=theCookie.length; 
	return unescape(theCookie.substring(ind+cookieName.length+2,ind1));
}
