<?php
include_once './ffdb_functions.php';
//Create Object for DB_Functions class
if(isset($_POST["Building"]) && !empty($_POST["Building"])
	&& ($_POST["price"]) && !empty($_POST["price"])
	&& isset($_POST["Location"]) && !empty($_POST["Location"])
	&& isset ($_POST["FoodType"]) && !empty($_POST["FoodType"]) ){
	$ffdb = new FFDB_Functions(); 
	//Store User into MySQL DB
	$user_id = $_POST["user_id"];
	$building = $_POST["Building"];
	$location = $_POST["Location"];
	$foodType = $_POST["FoodType"];
	$price = $_POST["price"];
	$foodDescription = $_POST["FoodDescription"];
	$res = $ffdb->storeEntry($user_id, $building, $location, $foodType, $price, $foodDescription);
	//Based on insertion, create JSON response
	if($res) { ?>
		 <div id="msg">Insertion successful</div>
	<?php } else { ?>
		 <div id="msg">Insertion failed</div>
	<?php } ?>
<?php }
?>