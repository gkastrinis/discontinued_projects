<?php

/* This controller handles lists */

class TodoListController
{
	public function handleRequest()
	{
		// Redirect to login screen
		if ( !isset($_SESSION['loggedIn']) )
		{
			$c = new HomeController();
			$c->handleRequest();
			return;
		}

		$user = unserialize($_SESSION['user']);
		$list = $_GET['list'];
		$action = isset($_GET['action']) ? $_GET['action'] : '';

		if ( $action == '' && is_numeric($list) )
		{
			$listInfo = TodoList::find( array('uID' => $user->uID, 'lID' => $list) );
			if ( empty($listInfo) )
				throw new Exception('Malformated values!');

			$entries = Entry::find( array('lID' => $list) );

			render('userhome', array(
				'title' => '.'. $user->username .'.'.$listInfo[0]->title,
				'lID' => $list,
				'listTitle' => $listInfo[0]->title,
				'entries' => $entries
			));
		}
		else if ( $action == 'addUnordered' )
		{
			$_GET['uID'] = $user->uID;
			TodoList::insert($_GET);
		}
		else
			throw new Exception('Wrong action specified!');
	}
}

?>
