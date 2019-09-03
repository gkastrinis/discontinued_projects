<!doctype html>
<html lang="en">
<head>
	<title>Doop Charts</title>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="keywords" content="program analysis, google charts, charts, Doop, George Kastrinis, gkastrinis" />
	<meta name="author" content="George Kastrinis" />
	<link href="favicon.ico" rel="shortcut icon" type="image/x-icon" />
	<link rel="stylesheet" href="style.css" type="text/css" media="all" />

	<script type="text/javascript" src="jquery-1.6.1.min.js"></script>
	<script type="text/javascript" src="clientSide.js"></script>
	<script type="text/javascript" src="https://www.google.com/jsapi"></script>
</head>

<body>
	<h1><a href="index.php">Doop Charts</a></h1>
	<?php
	if ( isset($_GET['report']) && is_numeric($_GET['report']) )
	{
		require_once('actions.php');
		drawReport( $_GET );
	}
	else
	{
	?>
		<div id="main">
			<div id="menu">
				<button id="new">New Report</button><h2>~ OR ~</h2><button id="list">Previous Reports</button>
			</div>

			<form action="control.php" method="POST" id="reportForm">
				<div id="reportForm1">
					<div>
						<h2>New Report - Step 1</h2>
							<div class="error" id="titleError">cannot be empty</div>
						<div><label for="title">Title</label> <input type="text" id="title" name="title" /></div>
						<div><label for="comments">Comments</label> <textarea id="comments" name="comments"></textarea></div>
							<div class="error" id="groupsError">should be greater than 1</div>
						<div><label for="groups"># of Groups</label> <input type="number" id="groups" name="groups" pattern="[1-10]" value="1" /></div>
							<div class="error" id="elementsError">should be greater than 2</div>
						<div><label for="elements"># of Elements</label> <input type="number" id="elements" name="elements" pattern="[1-30]" value="2" /></div>
						<div><a href="" id="changeMetrics">Change metrics used</a></div>
						<div>
<textarea id="metrics" name="metrics">elapsed time:
array index points-to
call graph edges (context-insensitive)
call graph edges (context-sensitive)
instance field points-to
reachable methods (context-insensitive)
reachable methods (context-sensitive)
reachable variables (context-insensitive)
reachable variables (context-sensitive)
static field points-to
var points-to
virtual call sites
reachable virtual call sites
polymorphic virtual call sites
(app) virtual call sites
(app) reachable virtual call sites
(app) polymorphic virtual call sites
casts
reachable casts
reachable casts that may fail
(app) casts
(app) reachable casts
(app) reachable casts that may fail
insensitive var points-to
reachable vars
(app) insensitive var points-to
(app) reachable vars</textarea>
						</div>
						<div><button id="toStep2">Next Step</button></div>
					</div>
				</div>
				<div id="reportForm2">
					<div>
						<h2>New Report - Step 2</h2>
						<div id="elementsNames"></div>
						<div><button id="toStep3">Next Step</button></div>
					</div>
				</div>
				<div id="reportForm3">
					<div>
						<h2>New Report - Step 3</h2>
						<div id="groupTabs"></div>
						<div style="clear: both;"><input type="submit" value="Complete" id="complete" /></div>
					</div>
				</div>
				<input type="hidden" name="reportForm" />
			</form>

			<div id="previousReports"><h2>Previous Reports</h2><img src="loading.gif" alt="loading" /></div>
		</div>
	<?php
	}
	?>
	<div id="footer">powered by <a href="http://code.google.com/apis/chart/">google charts</a> <span>&diams;</span> created by <a href="http://gkastrinis.info">George Kastrinis</a></div>
</body>
</html>
