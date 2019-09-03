<?php
	$loggedIn = isset($_SESSION['loggedIn']);
	if ( $loggedIn ) $user = unserialize($_SESSION['user']);
?>
<!DOCTYPE html>
<html>
	<head>
	<title><?php echo formatTitle($title) ?></title>

	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1" />

	<link rel="stylesheet" href="assets/css/popUp.css" />
    <link rel="stylesheet" href="assets/css/styles.css" />
    <script type="text/javascript" src="assets/js/jquery-1.6.4.min.js"></script>
    <script type="text/javascript" src="assets/js/scripts.js"></script>
</head>
<body>

	<div class="popUpShader" id="settings">
		<div class="popUpWindow">
			<h3 class="popUpWindowTitleBar">Settings<img src="assets/images/close.png" alt="close" class="xImage" /></h3>
			<div class="popUpContent">
				<div class="menuBox" id="logout"><img src="assets/images/logout.png"><div>logout</div></div>
				<div class="menuBox" id="listUnordered" title="new list - unordered"><img src="assets/images/listUnordered.png"><div>new list ...</div></div>
				<div class="menuBox" id="listOrdered" title="new list - ordered"><img src="assets/images/listOrdered.png"><div>new list ...</div></div>
			</div>
		</div>
	</div>

	<div data-role="header">
		<h1>
<?php if ( $loggedIn ) { ?>
			<img src="assets/images/avatars/<?php echo $user->uID ?>.png" alt="avatar" title="<?php echo $user->username ?>" />
<?php } ?>
			<a href="./" id="logo" title="home"><img src="assets/images/banner/banner.png" alt="banner" /></a>
<?php if ( $loggedIn ) { ?>
			<img src="assets/images/settings.png" alt="menu" title="menu" id="menu" />
<?php } ?>
		</h1>
	</div>

	<div data-role="content">
