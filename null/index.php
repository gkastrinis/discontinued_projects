<?php

/*
	Based on: Building a Website with PHP, MySQL and jQuery Mobile
	URL: http://tutorialzine.com/2011/08/jquery-mobile-product-website/

	This is the index file of our simple website.
	It routes requets to the appropriate controllers
*/

require_once 'includes/main.php';

session_start();

try
{
	if ( isset($_GET['user']) )
		$c = new UserController();

	else if ( $_GET['list'] )
		$c = new TodoListController();

	else if ( $_GET['entry'] )
		$c = new EntryController();

	else if ( !isset($_SESSION['loggedIn']) )
		$c = new HomeController();

	else
		$c = new UserController();

	$c->handleRequest();
}
catch(Exception $e)
{
	// Display the error page using the "render()" helper function:
	render( 'error', array( 'message' => $e->getMessage() ) );
}

?>
