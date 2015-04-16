<?php
    include_once 'ffdb_functions.php';
    $ffdb = new FFDB_Functions();
	if(isset($_POST["user_id"]) && !empty($_POST["user_id"])) {
		$entries = $ffdb->getAllEntriesAndUserVotes($_POST["user_id"]);
		$a = array();
		$b = array();
		if ($entries != false){
			$no_of_entries = mysql_num_rows($entries);
			while ($row = mysql_fetch_array($entries)) {		
				$b["building"] = $row["Building"];
				$b["location"] = $row["Location"];
				$b["foodType"] = $row["FoodType"];
				$b["price"] = $row["price"];
				$b["foodDescription"] = $row["FoodDescription"];
				$b["votes"] = $row["Votes"];
				$b["food_id"] = $row["food_id"];
				$b["id"] = $row["id"];
				$b["vote"] = $row["vote"];
				array_push($a,$b);
			}
			echo json_encode($a);
		}
	}
?>