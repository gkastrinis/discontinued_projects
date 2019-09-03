<?php

/* These are helper functions */

function render ($template, $vars = array())
{
	// This function takes the name of a template and
	// a list of variables, and renders it.

	// This will create variables from the array:
	extract($vars);

	// It can also take an array of objects
	// instead of a template name.
	if ( is_array($template) )
	{
		// If an array was passed, it will loop
		// through it, and include a partial view
		foreach ($template as $k)
		{
			// This will create a local variable
			// with the name of the object's class

			$cl = strtolower( get_class($k) );
			$$cl = $k;

			include "views/_$cl.php";
		}
	}
	else
	{
		include "views/$template.php";
	}
}

// Helper function for title formatting:
function formatTitle($title = '')
{
	$title = $GLOBALS['defaultTitle'] . $title;

	return $title;
}

function tabTitle($title)
{
	return ( strlen($title) <= 14 ? $title : substr($title, 0, 10) . '...' );
}

function tabTitleTip($title)
{
	return ( strlen($title) <= 14 ? '' : 'title="'.$title.'"' );
}

?>
