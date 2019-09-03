<?php

class Entry
{

	public static function find ($arr = array())
	{
		global $db;

		$st = $db->prepare("SELECT * FROM entries WHERE lID=:lID ORDER BY eID");
		$st->execute( array(':lID' => $arr['lID']) );

		$st->execute($arr);

		return $st->fetchAll(PDO::FETCH_CLASS, "Entry");
	}

	public static function insert ($arr = array())
	{
		global $db;

		if ( isset($arr['title']) && !empty($arr['title']) && isset($arr['lID']) && is_numeric($arr['lID']) )
		{
			$st = $db->prepare("SELECT lID FROM lists WHERE uID=:uID AND lID=:lID");
			$st->execute( array(':uID' => $arr['uID'], ':lID' => $arr['lID']) );

			$res = $st->fetchAll(PDO::FETCH_BOTH);

			if ( empty($res) )
				throw new Exception("Non-authorized action!");

			$st = $db->prepare("INSERT INTO entries (lID, title, link) VALUES(:lID, :title, :link)");
			$link = ( isset($arr['link']) && !empty($arr['link']) ) ? $arr['link'] : '#';
			$st->execute( array( ':lID' => $arr['lID'], ':title' => $arr['title'], ':link' => $link ) );

			echo '{ "status":"OK", "eID": '. $db->lastInsertId() .', "title": "'. $arr['title'] .'", "link": "'. $link .'"}';
		}
		else
		{
			throw new Exception("Incomplete values!");
		}
	}

	public static function update ($arr = array())
	{
		global $db;

		if ( isset($arr['title']) && !empty($arr['title']) && isset($arr['eID']) && is_numeric($arr['eID']) )
		{
			$st = $db->prepare("SELECT eID FROM entries, lists WHERE entries.lID = lists.lID AND lists.uID=:uID AND eID=:eID");
			$st->execute( array(':uID' => $arr['uID'], ':eID' => $arr['eID']) );

			$res = $st->fetchAll(PDO::FETCH_BOTH);

			if ( empty($res) )
				throw new Exception("Non-authorized action!");

			$st = $db->prepare("UPDATE entries SET title=:title, link=:link WHERE eID=:eID");
			$link = ( isset($arr['link']) && !empty($arr['link']) ) ? $arr['link'] : '#';
			$st->execute( array( ':title' => $arr['title'], ':link' => $link, ':eID' => $arr['eID'] ) );

			echo '{ "status":"OK", "eID": '. $arr['eID'] .', "title": "'. $arr['title'] .'", "link": "'. $link .'"}';
		}
		else
		{
			throw new Exception("Incomplete values!");
		}
	}

	public static function delete ($arr = array())
	{
		global $db;

		$st = $db->prepare("SELECT eID FROM entries, lists WHERE entries.lID = lists.lID AND lists.uID=:uID AND eID=:eID");
		$st->execute( array(':uID' => $arr['uID'], ':eID' => $arr['eID']) );

		$res = $st->fetchAll(PDO::FETCH_BOTH);

		if ( empty($res) )
			throw new Exception("Non-authorized action!");

		$st = $db->prepare("DELETE FROM entries WHERE eID=:eID");
		$st->execute( array(':eID' => $arr['eID']) );
	}

	public static function toggleImportant ($arr = array())
	{
		global $db;

		$st = $db->prepare("SELECT eID FROM entries, lists WHERE entries.lID = lists.lID AND lists.uID=:uID AND eID=:eID");
		$st->execute( array(':uID' => $arr['uID'], ':eID' => $arr['eID']) );

		$res = $st->fetchAll(PDO::FETCH_BOTH);

		if ( empty($res) )
			throw new Exception("Non-authorized action!");

		$st = $db->prepare("UPDATE entries SET important = important XOR true WHERE eID=:eID");
		$st->execute( array(':eID' => $arr['eID']) );
	}

	public static function toggleDone ($arr = array())
	{
		global $db;

		$st = $db->prepare("SELECT eID FROM entries, lists WHERE entries.lID = lists.lID AND lists.uID=:uID AND eID=:eID");
		$st->execute( array(':uID' => $arr['uID'], ':eID' => $arr['eID']) );

		$res = $st->fetchAll(PDO::FETCH_BOTH);

		if ( empty($res) )
			throw new Exception("Non-authorized action!");

		$st = $db->prepare("UPDATE entries SET active = active XOR true WHERE eID=:eID");
		$st->execute( array(':eID' => $arr['eID']) );
	}

}

?>
