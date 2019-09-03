<?php
require_once('database.config');
require_once('database.php');

function createReport ()
{
	// TODO: perform validity tests

	$connection = dbConnect();

	$query = sprintf("INSERT INTO `reports` (title, comments) VALUES ('%s', '%s')", dbEsc($_POST['title']), dbEsc($_POST['comments']));
	dbUpdate($query, $connection);
	$rID = dbLastId($connection);

	$metrics = explode("\r\n", $_POST['metrics']);
	$metricsIDs = array();
	foreach ( $metrics as $metric )
	{
		if ( empty($metric) ) continue;
		$query = sprintf("INSERT INTO `metrics` (rID, title) VALUES (%d, '%s')", $rID, dbEsc($metric));
		dbUpdate($query, $connection);
		$mID = dbLastId($connection);
		$metricsIDs[$metric] = $mID;
	}

	$groupsIDs = array();
	for ($i = 1, $groups = dbEsc($_POST['groups']) ; $i <= $groups ; $i++)
	{
		$query = sprintf("INSERT INTO `groups` (rID, title) VALUES (%d, '%s')", $rID, dbEsc($_POST['group' . $i]));
		dbUpdate($query, $connection);
		$gID = dbLastId($connection);
		$groupsIDs[] = $gID;
	}

	$elementsIDs = array();
	for ($i = 1, $elements = dbEsc($_POST['elements']) ; $i <= $elements ; $i++)
	{
		$query = sprintf("INSERT INTO `elements` (rID, title) VALUES (%d, '%s')", $rID, dbEsc($_POST['element' . $i]));
		dbUpdate($query, $connection);
		$eID = dbLastId($connection);
		$elementsIDs[] = $eID;
	}

	$query = 'INSERT INTO `results` (rID, mID, gID, eID, value) VALUES ';
	for ($i = 1, $groups = dbEsc($_POST['groups']) ; $i <= $groups ; $i++)
	{
		for ($j = 0, $elements = dbEsc($_POST['elements']) ; $j < $elements ; $j++)
		{
			$id = ($i - 1) * $elements + $j + 1;
			$metricsResults = explode("\n", $_POST['results' . $id]);
			foreach ( $metricsResults as $result )
			{
				$result = trim( rtrim( $result ) );
				$position = strrpos($result, " ");
				$metric = rtrim(substr($result, 0, $position));
				$mID = $metricsIDs[$metric];
				$value = substr($result, $position + 1, -1);
				$query .= sprintf("(%d, %d, %d, %d, %d),", $rID, $mID, $groupsIDs[$i - 1], $elementsIDs[$j], $value);
			}
		}
	}
	$query[strlen($query) - 1] = ' ';
	dbUpdate($query, $connection);

	return $rID;
}


function drawReport ($info)
{
	$report = $info['report'];

	$connection = dbConnect();

	$query = sprintf("SELECT * FROM `reports` WHERE rID = %d", $report);
	$rInfo = dbQuery($query, $connection);
	$query = sprintf("(SELECT `metrics`.mID, `metrics`.title, `chartComments`.comment FROM `metrics`, `chartComments` ".
			"WHERE `metrics`.rID = `chartComments`.rID AND `metrics`.mID = `chartComments`.mID AND `metrics`.rID = %d) ".
			"UNION (SELECT mID, title, '' FROM `metrics` WHERE `metrics`.rID = %d AND NOT EXISTS ".
			"(SELECT * FROM `chartComments`  WHERE mID = `metrics`.mID)) ORDER BY mID", $report, $report);
	$mInfo = dbQuery($query, $connection);
	$query = sprintf("SELECT gID, title FROM `groups` WHERE rID = %d ORDER BY gID", $report);
	$gInfo = dbQuery($query, $connection);
	$query = sprintf("SELECT eID, title FROM `elements` WHERE rID = %d ORDER BY eID", $report);
	$eInfo = dbQuery($query, $connection);

	$groups = count($gInfo);
	$elements = count($eInfo);

	// TODO: Return JSON representation of the data and make the javascript version to work correctly
?>
<div id="chartHead">
	<h2><?php echo $rInfo[0]['title']; ?></h2>
	<textarea readonly="readonly"><?php echo $rInfo[0]['comments']; ?></textarea>
	<a href="" id="selectMetrics">Select Metrics to display</a> <span>&diams;</span> <a href="" id="selectElements">Select Elements to display</a>
	<form action="" method="GET">
		<input type="hidden" name="report" value="<?php echo $report; ?>" />
			
		<div id="metricsEnabled">
			<input type="hidden" name="metrics" value="" />
<?php
	$metricsSelectionOn = isset( $info['metrics'] );
	foreach ( $mInfo as $metricNumber => $metric )
	{
		echo '<div><label for="m' . $metricNumber . '">' . $metric['title'] . '</label>';
		echo '<input type="checkbox" id="m' . $metricNumber . '" name="m' . $metricNumber . '" ';
		echo ( !$metricsSelectionOn || isset($info['m' . $metricNumber]) ? 'checked /></div>' : '/></div>' );
	}
?>
			<input type="submit" value="Select" />
		</div>
		<div id="elementsEnabled">
			<input type="hidden" name="elements" value="" />
<?php
	$elementsSelectionOn = isset( $info['elements'] );
	foreach ( $eInfo as $elementNumber => $element )
	{
		echo '<div><label for="e' . $elementNumber . '">' . $element['title'] . '</label>';
		echo '<input type="checkbox" id="e' . $elementNumber . '" name="e' . $elementNumber . '" ';
		echo ( !$elementsSelectionOn || isset($info['e' . $elementNumber]) ? 'checked /></div>' : '/></div>' );
	}
?>
			<input type="submit" value="Select" />
		</div>

	</form>
</div>
<?php
	foreach ( $mInfo as $metricNumber => $metric )
	{
		// Skip metric
		if ( $metricsSelectionOn && !isset( $info['m' . $metricNumber] ) ) continue;
?>
<script type="text/javascript">
	google.load("visualization", "1", {packages:["corechart"]});
	google.setOnLoadCallback(drawChart<?php echo $metricNumber; ?>);

	function drawChart<?php echo $metricNumber; ?>() {
		var data = new google.visualization.DataTable();
<?php
		$query = sprintf("SELECT mID, eID, gID, value FROM `results` WHERE `results`.rID = %d AND `results`.mid = " . $metric['mID'] . " ORDER BY mID, eID, gID", $report);
		$resInfo = dbQuery($query, $connection);

		echo "data.addColumn('string', 'Analysis');\n";
		foreach ( $gInfo as $group ) echo "data.addColumn('number', '" . $group['title'] . "');\n";

		$skipped = 0;
		foreach ( $eInfo as $elementNumber => $element )
			if ( $elementsSelectionOn && !isset( $info['e' . $elementNumber] ) ) $skipped++;
		echo "data.addRows($elements - $skipped);\n";

		$skipped = 0;
		foreach ( $eInfo as $elementNumber => $element )
		{
			// Skip element
			if ( $elementsSelectionOn && !isset( $info['e' . $elementNumber] ) ) { $skipped++; continue; }

			$fixedElementNumber = $elementNumber - $skipped;
			echo "data.setValue($fixedElementNumber, 0, '" . $element['title'] . "');\n";
			foreach ( $gInfo as $groupNumber => $group )
			{
				$index = $elementNumber * $groups + $groupNumber;
				echo "data.setValue($fixedElementNumber, " . ($groupNumber + 1) . ", " . $resInfo[$index]['value'] . ");\n";
			}
		}
?>
		var chart = new google.visualization.LineChart(document.getElementById('chart_div<?php echo $metricNumber; ?>'));
		chart.draw(data, {width: 1000, height: 450, title: '<?php echo $metric['title']; ?>'});
	}
</script>
<?php
	}
?>
	<div id="report">
		<input type="hidden" id="report" value="<?php echo $report; ?>" />
<?php
	foreach ( $mInfo as $metricNumber => $metric )
	{
		// Skip metric
		if ( $metricsSelectionOn && !isset( $info['m' . $metricNumber] ) ) continue;
?>
		<div class="first">
			<div class="chart" id="chart_div<?php echo $metricNumber; ?>"></div>
			<div class="comment" id="comment<?php echo $metricNumber;?>">
				<input type="hidden" id="metric" value="<?php echo $metric['mID']; ?>" />
				<textarea><?php echo $metric['comment']; ?></textarea><button>Comment</button>
			</div>
		</div>
		<div class="seperator"></div>
<?php
	}
?>
	</div>
<?php
}


function previousReports ()
{
	$connection = dbConnect();

	$rInfo = dbQuery('SELECT * FROM `reports`', $connection);

	// Return JSON representation of the data
	echo '[ ';
	foreach ($rInfo as $report)
		echo '{ "rID": ' . $report['rID'] . ', "title": "' . addslashes(htmlspecialchars($report['title'])) . '" },';
	echo ' ]';
}


function addComment ($comment, $rID, $mID)
{
	$connection = dbConnect();
	$tmpComment = trim($comment);

	$query = sprintf("SELECT DISTINCT `reports`.rID FROM `reports`, `metrics` WHERE `reports`.rID = %d AND `metrics`.mID = %d", $rID, $mID);
	$info = dbQuery($query, $connection);
	if ( empty($info) ) die;

	$query = sprintf("SELECT comment FROM `chartComments` WHERE rID = %d AND mID = %d", $rID, $mID);
	$info = dbQuery($query, $connection);

	// No comment in the past for the specified metric
	if ( empty($info) && !empty($tmpComment) )
	{
		$query = sprintf("INSERT INTO `chartComments` (rID, mID, comment) VALUES (%d, %d, '%s')", $rID, $mID, dbEsc($comment));
		dbUpdate($query, $connection);
	}
	// Current comment is empty; remove previous comment for the specified metric
	else if ( empty($tmpComment) )
	{
		$query = sprintf("DELETE FROM `chartComments` WHERE rID = %d AND mID = %d", $rID, $mID);
		dbUpdate($query, $connection);
	}
	// Update comment for the specified metric
	else
	{
		$query = sprintf("UPDATE `chartComments` SET comment = '%s' WHERE rID = %d AND mID = %d", dbEsc($comment), $rID, $mID);
		dbUpdate($query, $connection);
	}
}

?>
