<?php

class TodoList
{

	public static function find ($arr = array())
	{
		global $db;

		if ( !isset($arr['lID']) )
		{
			$st = $db->prepare("SELECT * FROM lists WHERE uID=:uID AND parentList = -1 ORDER BY lID");
			$st->execute( array(':uID' => $arr['uID']) );
		}
		else if ( isset($arr['lID']) && is_numeric($arr['lID']) )
		{
			$st = $db->prepare("SELECT * FROM lists WHERE uID=:uID AND lID=:lID");
			$st->execute( array(':uID' => $arr['uID'], ':lID' => $arr['lID']) );
		}
		else
		{
			throw new Exception("Malformated values!");
		}

		return $st->fetchAll(PDO::FETCH_CLASS, "TodoList");
	}

	public static function insert ($arr = array())
	{
		global $db;

		if ( isset($arr['title']) && !empty($arr['title']) )
		{
			$st = $db->prepare("INSERT INTO lists (uID, ordered, title) VALUES(:uID, 0, :title)");
			$st->execute( array( ':uID' => $arr['uID'], ':title' => $arr['title'] ) );

			$newList = new TodoList();
			$newList->uID = $arr['uID'];
			$newList->lID = $db->lastInsertId();
			$newList->parentList = -1;
			$newList->ordered = 0;
			$newList->title = $arr['title'];
			$user = unserialize($_SESSION['user']);
			$user->lists[] = $newList;
			$_SESSION['user'] = serialize($user);

			echo '{ "status":"OK", "lID": '. $newList->lID .'}';
		}
		else
		{
			throw new Exception("Incomplete values!");
		}
	}

}

?>
