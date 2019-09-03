<?php render( '_header', array( 'title' => $title ) )?>

<div>
	<form action="index.php?user&action=login" method="POST" id="login">
		<div><label for="username">Username</label><input type="text" name="username" id="username" /></div>
		<div><label for="password">Password</label><input type="password" name="password" id="password" /></div>
		<div><input type="submit" value="Login" /></div>
	</form>
</div>

<?php render('_footer')?>
