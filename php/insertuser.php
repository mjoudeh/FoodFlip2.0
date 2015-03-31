<?php
include_once './ffdb_functions.php';
//Create Object for DB_Functions class
if(isset($_POST["user_id"]) && !empty($_POST["user_id"])) {
	$ffdb = new FFDB_Functions(); 
	//Store User into MySQL DB
	$user_id = $_POST["user_id"];
	$res = $ffdb->storeUser($user_id);
	//Based on insertion, create JSON response
	if($res) { ?>
		 <div id="msg">Insertion of user successful</div>
	<?php } else { ?>
		 <div id="msg">Insertion of user failed</div>
	<?php } ?>
<?php }
?>