<?php

/* This controller renders the home page */

class HomeController
{
	public function handleRequest()
	{
		render('home', array(
			'title'		=> '.visitor.login',
			'content'	=> $content
		));
	}
}

?>
