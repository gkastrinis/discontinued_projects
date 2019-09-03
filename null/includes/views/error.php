<?php

header("HTTP/1.0 404 Not Found");
render('_header', array('title' => '.dev.error'))

?>

<p class="error"><?php echo $message?></p>

<?php render('_footer')?>
