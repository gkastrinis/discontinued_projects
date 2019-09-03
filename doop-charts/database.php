<?php
function dbConnect()
{
	global $db_hostname, $db_username, $db_password, $db_database;
	
	if (($connection_id = mysql_connect($db_hostname, $db_username, $db_password)) == false)
		die(mysql_error());

	if (mysql_select_db($db_database, $connection_id) == false)
	{
		mysql_close($connection_id);
		die(mysql_error());
	}

	return $connection_id;
}


function dbQuery($query, $connection_id)
{
	if (($result = mysql_query($query, $connection_id)) == false)
	{
		mysql_close($connection_id);
		die(mysql_error() . "\n" . 'Whole query: ' . $query);
	}

	while ($array[] = mysql_fetch_assoc($result));
	array_pop($array);

	mysql_free_result($result);
	return $array;
}


function dbUpdate($query, $connection_id)
{
	if (mysql_query($query, $connection_id) == false)
	{
		mysql_close($connection_id);
		die(mysql_error() . "\n" . 'Whole query: ' . $query);
	}
}


function dbLastId($connection_id)
{
	return mysql_insert_id($connection_id);
}


function dbEsc($str)
{
	return mysql_real_escape_string($str);
}
?>
