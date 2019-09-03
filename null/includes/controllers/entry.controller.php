<?php

/* This controller handles entries */

class EntryController
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
		$eID = $_GET['entry'];

		$action = isset($_GET['action']) ? $_GET['action'] : '';

		if ( $action == 'add' )
		{
			$_GET['uID'] = $user->uID;
			Entry::insert( $_GET );
		}
		else if ( $action == 'edit' )
		{
			$_GET['uID'] = $user->uID;
			$_GET['eID'] = $_GET['entry'];
			Entry::update( $_GET );
		}
		else if ( $action == 'drop' )
		{
			Entry::delete( array( 'uID' => $user->uID, 'eID' => $eID ) );
			render('_empty', array( 'eID' => $eID, 'msg' => 'OK' ));
		}
		else if ( $action == 'important' )
		{
			Entry::toggleImportant( array( 'uID' => $user->uID, 'eID' => $eID ) );
			render('_empty', array( 'eID' => $eID, 'msg' => 'OK' ));
		}
		else if ( $action == 'done' )
		{
			Entry::toggleDone( array( 'uID' => $user->uID, 'eID' => $eID ) );
			render('_empty', array( 'eID' => $eID, 'msg' => 'OK' ));
		}
		else
			throw new Exception('Wrong action specified!');
	}
}

?>
