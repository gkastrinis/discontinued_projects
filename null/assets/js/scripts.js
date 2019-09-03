function setOptions (index, value) {

	$(value).find(".importantButton").click(function() {
		var eID = $(this).attr("data-eID");
		$("#entry" + eID + " span").toggleClass("important");
		$.ajax({
			url: "./?entry=" + eID + "&action=important",
			success: function( msg ) {  }
		});
	});

	$(value).find(".doneButton").click(function() {
		var eID = $(this).attr("data-eID");
		$("#entry" + eID + " span").toggleClass("done");
		$.ajax({
			url: "./?entry=" + eID + "&action=done",
			success: function( msg ) {  }
		});
	});

	$(value).find(".deleteButton").click(function() {
		var eID = $(this).attr("data-eID");
		$("#confirmBox .popUpWindowTitleBar").text( "Confirm Deletion" );
		$("#confirmBox .popUpContent > div > span").text( $("#entry" + eID + " > span").text() );

		$("#ok").text("Delete");
		$("#ok").unbind('click');
		$("#ok").click(function() {
			$("#entry" + eID).remove();
			$.ajax({
				url: "./?entry=" + eID + "&action=drop",
				success: function( msg ) {  }
			});
			$("#deleteContent").hide();
			$("#confirmBox").hide();
		});

		$("#deleteContent").show();
		$("#confirmBox").show();
	});

	$(value).find(".editButton").click(function() {
		var eID = $(this).attr("data-eID");
		$("#confirmBox .popUpWindowTitleBar").text( "Edit Entry" );
		$("#editContent > div > #title2").val( $("#entry" + eID + " > span").text() );
		$("#editContent > div > #link2").val( $("#entry" + eID + " > a").attr("href") );

		$("#ok").text("Edit");
		$("#ok").unbind('click');
		$("#ok").click(function() {
			$.ajax({
				url: "./?entry=" + eID + "&action=edit",
				data: $("#editContent").serialize(),
				dataType: "json",
				success: function( msg ) {
					if ( msg.status != "OK" ) return;

					$("#entry" + msg.eID + " > span").text( msg.title );
					if ( msg.link != "#" )
					{
						$("#entry" + msg.eID + " > a").attr( "href", msg.link );
						$("#entry" + msg.eID + " > a").html( '<img src="assets/images/link.png" alt="link"/>' );
					}
					else
					{
						$("#entry" + msg.eID + " > a").attr( "href", "" );
						$("#entry" + msg.eID + " > a").html( "" );
					}
				}
			});
			$("#editContent").hide();
			$("#confirmBox").hide();
		});

		$("#editContent").show();
		$("#confirmBox").show();
	});

}


$(document).ready(function() {

	$("#menu").click(function() {
		$("#settings").show();
	});

	$("#settings .xImage").click(function() {
		$("#settings").hide();
	});

	$("#logout").click(function() {
		window.location = "?user&action=logout";
	});

	$("#listUnordered").click(function() {
		var title = prompt ("Title for the new list", "");
		if ( title != null && title != "")
		{
			$.ajax({
				url: "./?list=1&action=addUnordered&title=" + title,
				dataType: "json",
				success: function( msg ) {
					if ( msg.status == "OK" ) window.location = "?list=" + msg.lID;
				}
			});
		}
	});


	if ( window['entries'] != undefined )
	{
		$("#addEntry > button").click(function() {
			$("#confirmBox .popUpWindowTitleBar").text( "Add New Entry" );
			$("#addContent > div > input").val( "" );

			$("#ok").text("Add");
			$("#ok").unbind('click');
			$("#ok").click(function() {
				$.ajax({
					url: "./?entry=1&action=add",
					data: $("#addContent").serialize(),
					dataType: "json",
					success: function( msg ) {
						if ( msg.status != "OK" ) return;

						var html = '<div class="entry" id="entry' + msg.eID + '"><span>' + msg.title + '</span>';
						if ( msg.link != "#" ) html += '<a href="' + msg.link + '"><img src="assets/images/link.png" alt="link"/></a>';
						html += '<div><img src="assets/images/important.png" class="importantButton" data-eID="' + msg.eID + '" />';
						html += '<img src="assets/images/done.png" class="doneButton" data-eID="' + msg.eID + '" />';
						html += '<img src="assets/images/delete.png" class="deleteButton" data-eID="' + msg.eID + '" />';
						html += '<img src="assets/images/edit.png" class="editButton" data-eID="' + msg.eID + '" />';
						html += '</div></div>';

						$("#entries").append( html );
						setOptions(0, $("#entry" + msg.eID));
					}
				});
				$("#addContent").hide();
				$("#confirmBox").hide();
			});

			$("#addContent").show();
			$("#confirmBox").show();
		});

		$("#cancel").click(function() {
			$("#addContent").hide();
			$("#deleteContent").hide();
			$("#editContent").hide();

			$("#confirmBox").hide();
		});

		$.each($("#entries .entry"), setOptions);
	}

});
