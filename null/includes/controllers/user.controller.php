<?php

/* This controller handles users */

class UserController
{
	public function handleRequest()
	{
		$action = isset($_GET['action']) ? $_GET['action'] : '';

		if ( $action == 'login' && isset($_POST['username']) && isset($_POST['password']) )
		{
			if ( empty($_POST['username']) || empty($_POST['password']) )
				throw new Exception('You have entered incomplete credentials!');

			$user = User::login($_POST);
			$user->lists = TodoList::find( array('uID' => $user->uID) );

			$_SESSION['loggedIn'] = true;
			$_SESSION['user'] = serialize($user);
		}
		else if ( $action == 'logout' )
		{
			// UNSET ALL SESSION VARIABLES
			$_SESSION = array();
			// DELETE SESSION COOKIE
			if ( isset($_COOKIE[session_name()]) ) setcookie(session_name(), '', time()-42000, '/');
			// DESTROY THE SESSION
			session_destroy();
		}

		// Redirect to login screen
		if ( !isset($_SESSION['loggedIn']) )
		{
			$c = new HomeController();
			$c->handleRequest();
			return;
		}
		else if ( $action != '' && $action != 'login' )
			throw new Exception('Wrong action specified!');

		$user = unserialize($_SESSION['user']);

		render('userhome', array( 'title' => '.'. $user->username .'.home', 'elements' => array() ));
	}
}

?>
