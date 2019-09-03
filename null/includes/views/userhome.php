<?php
$user = unserialize($_SESSION['user']);
render( '_header', array( 'title' => $title ) );
?>

<div id="lists">
<?php foreach ($user->lists as $list) {
	if ( isset($lID) && $lID == $list->lID ) $ordered = $list->ordered;
?>
	<a href="./?list=<?php echo $list->lID ?>" <?php echo tabTitleTip($list->title) ?>
	<?php echo (isset($lID) && $lID == $list->lID ? 'class="current"' : '')?>><?php echo tabTitle($list->title) ?></a>
<?php } ?>
</div>

<?php if ( isset($lID) ) { ?>

<h2><?php echo $listTitle ?></h2>
<script type="text/javascript" > var entries = <?php echo count($entries) ?>; </script>
<div id="entries">
	<div id="addEntry"><button>Add New Entry</button></div>

<?php foreach ($entries as $entryNumber => $entry) { ?>
	<div class="entry" id="entry<?php echo $entry->eID ?>">
<?php
	if ( $ordered ) echo '<strong>'.($entryNumber + 1) . '.</strong> ';
	echo '<span class="';
	if ( $entry->important ) echo 'important ';
	if ( !$entry->active ) echo 'done ';
	echo '">' . $entry->title . '</span>';
	if ( $entry->link == '#' ) echo '<a href=""></a>';
	else echo '<a href="'.$entry->link.'"><img src="assets/images/link.png" alt="link"/></a>';
?>
		<div>
			<img src="assets/images/important.png" class="importantButton" data-eID="<?php echo $entry->eID ?>" />
			<img src="assets/images/done.png" class="doneButton" data-eID="<?php echo $entry->eID ?>" />
			<img src="assets/images/delete.png" class="deleteButton" data-eID="<?php echo $entry->eID ?>" />
			<img src="assets/images/edit.png" class="editButton" data-eID="<?php echo $entry->eID ?>" />
		</div>
	</div>
<?php } ?>
</div>

<div class="popUpShader" id="confirmBox">
	<div class="popUpWindow">
		<h3 class="popUpWindowTitleBar"></h3>
		<div class="popUpContent">
			<form action="index.php?entry=1&action=add" method="POST" id="addContent">
				<div><label for="title"><img src="assets/images/pencil.png" alt="pencil" /></label><input type="text" name="title" id="title" /></div>
				<div><label for="link"><img src="assets/images/link.png" alt="link" /></label><input type="text" name="link" id="link" /></div>
				<input type="hidden" name="lID" value="<?php echo $lID ?>" />
			</form>

			<div id="deleteContent">Delete entry? <span></span></div>

			<form id="editContent">
				<div><label for="title2"><img src="assets/images/pencil.png" alt="pencil" /></label><input type="text" name="title" id="title2" /></div>
				<div><label for="link2"><img src="assets/images/link.png" alt="link" /></label><input type="text" name="link" id="link2" /></div>
			</form>

			<button id="cancel">Cancel</button> <button id="ok"></button>
		</div>
	</div>
</div>

<?php } ?>

<?php render('_footer')?>
