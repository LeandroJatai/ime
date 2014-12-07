var onDragDrop = false;
var envText = "Env";
var rpText = "RP";
var activityText = "Activities";
var roleText = "Role";


$(document).ready(function() {

	controleAbas();
	controleAberturaNos();
	controleSplitter();
	controleCTree();
	controleDragDrop();

	setTimeout(function() {$(".message_warning").hide("slow");}, 8000);

	ajusteServicosList();
	$("input[name='userTipoPerfil']").click(ajusteServicosList);



	$("#userIdPerfil").change(function() {

		var value = $('#userIdPerfil').val();
		if (value == "0") {
			$("input[name='servico.item']").attr('checked', null);
			return;
		}

		ajusteServicosList();

		$.getJSON("usuario.do?action=usuario.json.permissoes&idTipo="+value+"&random="+Math.random(), {}, function( jsonData ) {

			$("input[name='servico.item']").attr('checked', null);
			jsonData.forEach(function(event) {
				$("input[name='servico.item'][value='"+event.servico+"']")[0].checked = true;
			});

		});			
	});	



}); // Fim document.ready

function ajusteServicosList() {

	var value = $('#userIdPerfil').val();

	if (parseInt(value) > 1000) {
		$("#servicosList").css('visibility','visible');

		if ($("input[name='userTipoPerfil']").length > 0)
			$("input[name='userTipoPerfil']")[0].checked = true;
		return;
	}


	$("input[name='userTipoPerfil']").each(function(event) {
		if (this.checked && this.value == 'perfil.livre') {
			$("#servicosList").css('visibility','visible');
		}
		else if (this.checked && this.value == 'perfil.vinc') {
			$("#servicosList").css('visibility','visible');
		}
		else if (this.checked && this.value == 'perfil.fixo') {
			$("#servicosList").css('visibility','hidden');
		}
	});
}


function controleCTree() { //class="activities" 
	var valcTreeEnv = ReadCookie("valcTreeEnv");
	var valcTreeRole = ReadCookie("valcTreeRole");
	var valcTreeRP = ReadCookie("valcTreeRP");
	var valcTreeActivities = ReadCookie("valcTreeActivities");

	if (valcTreeActivities == null || valcTreeActivities.length == 0) {
		SetCookie("valcTreeActivities","ON",5);
		valcTreeActivities = 'ON';
		$('#raizMIEdition .activities').css('display', 'block');
	}
	else {
		$('#raizMIEdition .activities').css('display',valcTreeActivities=="OFF"?"none":"block");

	}

	if (valcTreeRP == null || valcTreeRP.length == 0) {
		SetCookie("valcTreeRP","ON",5);
		valcTreeRP = 'ON';
		$('#raizMIEdition .rp').css('display', 'block');
	}
	else {
		$('#raizMIEdition .rp').css('display',valcTreeRP=="OFF"?"none":"block");

	}


	if (valcTreeEnv == null || valcTreeEnv.length == 0) {
		SetCookie("valcTreeEnv","ON",5);
		valcTreeEnv = 'ON';
		$('#raizMIEdition .environment').css('display', 'block');
	}
	else {
		$('#raizMIEdition .environment').css('display',valcTreeEnv=="OFF"?"none":"block");
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

	$('<a href="#"> ['+rpText+' '+(valcTreeRP)+"] </a>").click(function(event) {
		valcTreeRP = (valcTreeRP=="OFF"?"ON":"OFF");
		SetCookie("valcTreeRP",valcTreeRP,5);
		$(event.currentTarget).html('['+rpText+' '+(valcTreeRP)+"]");

		$('#raizMIEdition .rp').css('display',valcTreeRP=="OFF"?"none":"block");

	}).appendTo($("#cTree"));

	$('<a href="#"> ['+activityText+' '+(valcTreeActivities)+"] </a>").click(function(event) {
		valcTreeActivities = (valcTreeActivities=="OFF"?"ON":"OFF");
		SetCookie("valcTreeActivities",valcTreeActivities,5);
		$(event.currentTarget).html('['+activityText+' '+(valcTreeActivities)+"]");

		$('#raizMIEdition .activities').css('display',valcTreeActivities=="OFF"?"none":"block");

	}).appendTo($("#cTree"));

	$('<a href="#"> ['+envText+' '+(valcTreeEnv)+"] </a>").click(function(event) {
		valcTreeEnv = (valcTreeEnv=="OFF"?"ON":"OFF");
		SetCookie("valcTreeEnv",valcTreeEnv,5);
		$(event.currentTarget).html('['+envText+' '+(valcTreeEnv)+"]");

		$('#raizMIEdition .environment').css('display',valcTreeEnv=="OFF"?"none":"inline");

	}).appendTo($("#cTree"));

	$('<a href="#" > ['+roleText+' '+(valcTreeRole)+"] </a>").click(function(event) {
		valcTreeRole = (valcTreeRole=="OFF"?"ON":"OFF");
		SetCookie("valcTreeRole",valcTreeRole,5);
		$(event.currentTarget).html('['+roleText+' '+(valcTreeRole)+"]");

		$('#raizMIEdition .learner').css('display', valcTreeRole=="OFF"?"none":"inline");
		$('#raizMIEdition .staff').css('display', valcTreeRole=="OFF"?"none":"inline");

	}).appendTo($("#cTree"));


}

function resizeCTree() {
	return;
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
			onDragDrop = true;
			ui.item.startPos = ui.item.index();
			$(ui.item[0]).css("backgroundColor",'rgba(255,255,255,0.5)');
		},
		stop: function(event, ui) {

			$.get("app.do?action=ldep.switch.pos.ldep&start="+ui.item.startPos+"&stop="+ui.item.index()+"&item="+ui.item[0].id, function(ev) {

				/*if (ev != "OK") {
        			$(".message_warning").html("Você não tem permissão para essa alteração!");
        			$(".message_warning").show("slow");

        			setTimeout(function() {$(".message_warning").hide("slow");}, 15000);
        		}*/

			});
			onDragDrop = false;
		}
	});
	$( ".moveLdep" ).disableSelection();


	$('.movePlays').sortable({
		distance: 15,
		start: function(event, ui) {
			onDragDrop = true;
			ui.item.startPos = ui.item.index();
			$(ui.item[0]).css("backgroundColor",'rgba(255,255,255,0.5)');
		},
		stop: function(event, ui) {

			$.get("play.do?action=play.switch.pos.play&start="+ui.item.startPos+"&stop="+ui.item.index()+"&item="+ui.item[0].id, function(ev) {

				/*if (ev != "OK") {
    			$(".message_warning").html("Você não tem permissão para essa alteração!");
    			$(".message_warning").show("slow");

    			setTimeout(function() {$(".message_warning").hide("slow");}, 15000);
    		}*/
			});
			onDragDrop = false;
		}
	});
	$( ".movePlays" ).disableSelection();


	$( '.moveActs' ).sortable({
		distance: 15,
		start: function(event, ui) {
			onDragDrop = true;
			ui.item.startPos = ui.item.index();
			$(ui.item[0]).css("backgroundColor",'rgba(255,255,255,0.5)');
		},
		stop: function(event, ui) {

			$(ui.item[0]).css("backgroundColor",'transparent');

			$.get("play.do?action=play.switch.pos.act&start="+ui.item.startPos+"&stop="+ui.item.index()+"&item="+ui.item[0].id, function(ev) {

				/*if (ev != "OK") {
    			$(".message_warning").html("Você não tem permissão para essa alteração!");
    			$(".message_warning").show("slow");

    			setTimeout(function() {$(".message_warning").hide("slow");}, 15000);
    		}*/
			});
			onDragDrop = false;

		}
	});
	$( ".moveActs" ).disableSelection();

	$( '.moveRPs' ).sortable({
		distance: 15,
		start: function(event, ui) {
			onDragDrop = true;
			ui.item.startPos = ui.item.index();
			$(ui.item[0]).css("backgroundColor",'rgba(255,255,255,0.5)');
		},
		stop: function(event, ui) {

			$.get("act.do?action=act.switch.pos.rp&start="+ui.item.startPos+"&stop="+ui.item.index()+"&item="+ui.item[0].id, function(ev) {

				/*if (ev != "OK") {
    			$(".message_warning").html("Você não tem permissão para essa alteração!");
    			$(".message_warning").show("slow");

    			setTimeout(function() {$(".message_warning").hide("slow");}, 15000);
    		}*/

			});
			onDragDrop = false;
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
function controleSplitter() {
	// controle do splitter geral

	var widthLeft = ReadCookie("widthLeftPane"); 

	if (parseInt(ReadCookie("widthLeftPane")) > 0) {

		if ($("#vsplitbar").length > 0) {//.animate({
			$("#vsplitbar").css("left",parseInt(ReadCookie("widthLeftPane"))-3);
			$("#RightPane").css("left",parseInt(ReadCookie("widthLeftPane")));
			$("#LeftPane").css("width",parseInt(ReadCookie("widthLeftPane")));

			/*$("#vsplitbar").animate({"left" : parseInt(ReadCookie("widthLeftPane"))-3}, 1000);
			$("#RightPane").animate({"left" : parseInt(ReadCookie("widthLeftPane"))}, 1000);
			$("#LeftPane").animate({"width" : parseInt(ReadCookie("widthLeftPane"))}, 1000);*/
		}
		else  {
			$("#RightPane").css("left",0);
		}

		resizeCTree()
	} 
	var splitEnter = false;	



	$("#vsplitbar").mousedown(function(event){
		splitEnter = true;
	});

	$("#vsplitbar").mouseup(function(){
		splitEnter = false;
		$.get("app.do?action=ldep.vsplitbar&pos="+ReadCookie("widthLeftPane"), function(ev) {

		});
		
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
	//$("#LeftPane").fadeIn(1000);
	$("#panelWait").fadeOut(500);
	$("#LeftPane")[0].scrollTop = ReadCookie("scrollLeftPane");

	setTimeout(function() {
		$("#LeftPane").scroll(function(event) {

			SetCookie("scrollLeftPane",event.currentTarget.scrollTop,1);
		});

	}, 100);
}

function liHover(event, count) {
	
        if (event == undefined)
           return;
    
	if (count == undefined)
		count = 1;

	if (!onDragDrop)
		$(event.currentTarget).css('background-color','rgba(255,255,0,0.2)');

	for (var i = 0; i < count; i++) {
		var item = $(event.currentTarget).find(".controls")[i];
		$(item).clearQueue();
		$(item).animate({opacity:1}, 2000);
	}
}

function liOut(event) {

    if (event == undefined)
       return;
    
	if (!onDragDrop)
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