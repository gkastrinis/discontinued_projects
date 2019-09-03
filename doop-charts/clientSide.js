$(document).ready(function() {

	$("#new").click(function()
	{
		$("#menu").hide();
		$("#reportForm1").slideDown();
	});

	$("#list").click(function()
	{
		$.ajax({ type: "GET", url: 'control.php?list', success: function (msg)
		{
			var list = eval('(' + msg + ')');
			var html = '<h2>Previous Reports</h2>';
			for (var i in list)
				html += '<div><a href="?report=' + list[i].rID + '">' + list[i].title + '</a></div>';
			$("#previousReports").html(html);
		} });

		$("#menu").hide();
		$("#previousReports").slideDown();
	});

	var metricsOpen = false;
	$("#changeMetrics").click(function()
	{
		if ( !metricsOpen ) { $("#metrics").slideDown(); metricsOpen = true; }
		else { $("#metrics").slideUp(); metricsOpen = false; }
		return false;
	});

	$("#toStep2").click(function()
	{
		if ( !validateStep1() ) return false;

		createReportForm2( $("#groups").val(), $("#elements").val() );
		$("#reportForm1").hide();
		$("#reportForm2").slideDown();
		return false;
	});

	$("#toStep3").click(function()
	{
		if ( !validateStep2( $("#groups").val(), $("#elements").val() ) ) return false;

		createReportForm3( $("#groups").val(), $("#elements").val() );
		$("#reportForm2").hide();
		$("#reportForm3").slideDown();
		return false;
	});

	$("#complete").click(function()
	{
		if ( !validateStep3( $("#groups").val(), $("#elements").val() ) ) return false;

		$("#reportForm").submit();
	});


	$("#selectMetrics").click(function()
	{
		$("#selectElements").removeClass("selectedLink");
		$("#selectMetrics").addClass("selectedLink");
		$("#elementsEnabled").hide();
		$("#metricsEnabled").slideDown();
		return false;
	});
	
	$("#selectElements").click(function()
	{
		$("#selectMetrics").removeClass("selectedLink");
		$("#selectElements").addClass("selectedLink");
		$("#metricsEnabled").hide();
		$("#elementsEnabled").slideDown();
		return false;
	});

	$(".comment button").click(function()
	{
		var comment = $(this).parents().find("textarea:first").val();
		var report = $("#report input").val();
		var metric = $(this).parents().find("input:first").val();

		$.ajax({ type: "GET", url: 'control.php?comment=' + encodeURI( comment ) + '&report=' + report + '&metric=' + metric,
		success: function (msg)
		{
			alert("Your comment has been registered.");
		} });
	});

});


function createReportForm2 (groups, elements)
{
	var html = '';
	for (var i = 1 ; i <= groups ; i++)
	{
		html += '<div class="error" id="groupError' + i + '">cannot be empty</div>';
		html += '<div><label for="group' + i + '">Group #' + i + '</label> <input type="text" id="group' + i + '" name="group' + i + '"/></div>';
	}
	html += '<hr />';
	for (var i = 1 ; i <= elements ; i++)
	{
		html += '<div class="error" id="elementError' + i + '">cannot be empty</div>';
		html += '<div><label for="element' + i + '">Element #' + i + '</label> <input type="text" id="element' + i + '" name="element' + i + '"/></div>';
	}
	$("#elementsNames").html(html);
}

function createReportForm3 (groups, elements)
{
	var tabsHtml = '<span class="tab" id="tab0">Metrics</span>';
	var contentsHtml = '<div class="tabContent" id="content0"><textarea readonly="readonly">' + $("#metrics").val() + '</textarea></div>';
	for (var i = 1 ; i <= groups ; i++)
	{
		tabsHtml += '<span class="tab" id="tab' + i + '">' + $("#group" + i).val() + '</span>';
		contentsHtml += '<div class="tabContent" id="content' + i + '">';
		for (var j = 0 ; j < elements ; j++)
		{
			var id = (i - 1) * elements + j + 1;
			var elementName = $("#element" + (j + 1)).val();
			contentsHtml += '<div class="element"><div><label>Element</label><input type="text" value="' + elementName + '" readonly="readonly"/></div>';
			contentsHtml += '<div class="error" id="resultsError' + id + '">cannot be empty</div>';
			contentsHtml += '<div><label for="results' + id + '">Results</label><textarea id="results' + id + '" name="results' + id + '"></textarea></div></div>';
		}
		contentsHtml += '</div>';
	}
	$("#groupTabs").html(tabsHtml + contentsHtml);

	$("#tab1").addClass("selectedTab");

	var currentTab = 1;
	$(".tab").click(function()
	{
		idNumber = $(this).attr('id').substring(3);
		$("#tab" + currentTab).removeClass("selectedTab");
		$("#content" + currentTab).hide();
		$("#tab" + idNumber).addClass("selectedTab");
		$("#content" + idNumber).show();
		currentTab = idNumber;
	});
}


function validateStep1 ()
{
	var valid = true;

	if ( $("#title").val().length == 0 )
		{ $("#titleError").show(); valid = false; }
	else
		$("#titleError").hide();

	if ( $("#groups").val() < 1 )
		{ $("#groupsError").show(); valid = false; }
	else
		$("#groupsError").hide();

	if ( $("#elements").val() < 2 )
		{ $("#elementsError").show(); valid = false; }
	else
		$("#elementsError").hide();

	return valid;
}

function validateStep2 (groups, elements)
{
	var valid = true;

	for (var i = 1 ; i <= groups ; i++)
		if ( $("#group" + i).val().length == 0 )
			{ $("#groupError" + i).show(); valid = false; }
		else
			$("#groupError" + i).hide();

	for (var i = 1 ; i <= elements ; i++)
		if ( $("#element" + i).val().length == 0 )
			{ $("#elementError" + i).show(); valid = false; }
		else
			$("#elementError" + i).hide();

	return valid;
}

function validateStep3 (groups, elements)
{
	var valid = true;

	for (var i = 1 ; i <= groups ; i++)
	{
		var validTab = true;
		for (var j = 0 ; j < elements ; j++)
		{
			var id = (i - 1) * elements + j + 1;
			if ( $("#results" + id).val().length == 0 )
				{ $("#resultsError" + id).show(); validTab = false; valid = false; }
			else
				$("#resultsError" + id).hide();
		}
		if ( !validTab ) $("#tab" + i).addClass("errorTab");
		else $("#tab" + i).removeClass("errorTab");
	}

	return valid;
}
