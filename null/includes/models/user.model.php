<?php

class User
{
	public $lists;

	public static function login ($arr)
	{
		global $db;

		$st = $db->prepare("SELECT * FROM users WHERE username=:username");
		$st->execute( array(':username' => $arr['username']) );

		$res = $st->fetchAll(PDO::FETCH_CLASS, "User");

		if ( crypt($arr['password'], $res[0]->password) !== $res[0]->password )
			throw new Exception('You have entered an invalid username or password!');

		return $res[0];
	}
}

?>
