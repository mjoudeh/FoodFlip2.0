<?php
    include_once 'ffdb_functions.php';
	if(isset($_POST["user_id"]) && !empty($_POST["user_id"])) {
		$ffdb = new FFDB_Functions();
		$user = $ffdb->getUser($_POST["user_id"]);
		$a = array();
		if ($user != false && $row = mysql_fetch_array($user)) {
			$a["user_id"] = $row["user_id"];
			$a["karma"] = $row["karma"];
			$a["comments"] = $row["comments"];
			$a["submissions"] = $row["submissions"];
			echo json_encode($a);
		} else { ?>
		 <div id="msg">user is false in getuser.php</div>
	<?php }
	} else { ?>
		 <div id="msg">id is not set or empty in getuser.php</div>
	<?php }
?>