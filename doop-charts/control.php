<?php
require_once('actions.php');

if ( isset($_POST['reportForm']) )
{
	$rID = createReport();
	header('Location: index.php?report=' . $rID);
}
else if ( isset($_GET['comment']) && isset($_GET['report']) && is_numeric($_GET['report']) && isset($_GET['metric']) && is_numeric($_GET['metric']) )
{
	addComment($_GET['comment'], $_GET['report'], $_GET['metric']);
}
else if ( isset($_GET['report']) && is_numeric($_GET['report']) )
{
	drawReport( $_GET );
}
else if ( isset($_GET['list']) )
{
	previousReports();
}
else
{
	print_r($_GET);
	print_r($_POST);
	header('Location: index.php');
}
?>
