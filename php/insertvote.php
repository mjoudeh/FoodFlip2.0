<?php
include_once './ffdb_functions.php';
//Create Object for DB_Functions class
if(isset($_POST["user_id"]) && !empty($_POST["user_id"]) &&
	isset($_POST["id"]) && !empty($_POST["id"]) &&
	isset($_POST["vote"]) && !empty($_POST["vote"])) {
	$ffdb = new FFDB_Functions(); 
	$user_id = $_POST["user_id"];
	$id = $_POST["id"];
	$vote = $_POST["vote"];
	$res = $ffdb->storeVote($user_id, $id, $vote);
	//Based on insertion, create JSON response
	if($res) { ?>
		 <div id="msg">Insertion of vote successful</div>
	<?php } else { ?>
		 <div id="msg">Insertion of vote failed</div>
	<?php } ?>
<?php }
?>