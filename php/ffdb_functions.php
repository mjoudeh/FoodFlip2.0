<?php

class FFDB_Functions {

    private $ffdb;

    // constructor
    function __construct() {
        include_once './ffdb_connect.php';
        // connecting to database
        $this->ffdb = new FFDB_Connect();
        $this->ffdb->connect();
    }

    // destructor
    function __destruct() {
        
    }
	
	/**
	 * Get entry comments
	 */
	public function getEntryComments($id) {
		$result = mysql_query("SELECT * FROM food_entry_comments WHERE id = '$id'");
		return $result;
	}
	
	/**
	 * Add a comment to an entry
	 */
	public function addEntryComment($id, $comment) {
		$result = mysql_query("INSERT INTO food_entry_comments(id, comment) VALUES ('$id', '$comment')");
		
		if ($result) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Get user
	 */
	public function getUser($user_id) {
		$result = mysql_query("SELECT * FROM users WHERE user_id = '$user_id'");
		return $result;
	}
	
	/**
	 * Store new user
	 */
	public function storeUser($user_id) {
		$result = mysql_query("INSERT INTO users(user_id, karma) VALUES('$user_id', '0')");
		
		if ($result) {
			return true;
		} else {
			return false;
		}
	}

    /**
     * Storing new entry
     * returns entry details
     */
    public function storeEntry($user_id, $building, $location, $foodType, $price, $foodDescription) {
        // Insert entry into database
        $result = mysql_query("INSERT INTO food_entries(user_id, Building, Location, FoodType, price, FoodDescription) VALUES('$user_id', '$building', '$location', '$foodType', '$price', '$foodDescription')");
		
        if ($result) {
			return true;
        } else {
			return false;
		}
    }
	
	public function storeVote($user_id, $id, $vote) {
		$result = mysql_query("INSERT INTO votes(user_id, id, vote) VALUES('$user_id', '$id', '$vote')");
		
		if ($result) {
			return true;
		} else {
			return false;
		}
	}
	
	 /**
     * Getting all entries
     */
    public function getAllEntries() {
        $result = mysql_query("SELECT * FROM food_entries");
        return $result;
    }
	/**
     * Get Yet to Sync row Count
     */
    public function getRowCount() {
        $result = mysql_query("SELECT * FROM food_entries");//  WHERE syncsts = FALSE");
        return $result;
    }
	
	public function getAllEntriesAndUserVotes($id) {
		$result = mysql_query("SELECT * FROM food_entries LEFT JOIN votes ON votes.user_id = '$id' AND food_entries.food_id = votes.id GROUP BY food_entries.food_id");
		return $result;
	}

	
	public function updateSyncSts($type, $sts){
		$result = mysql_query("UPDATE food_entries SET syncsts = $sts WHERE Type = $type");
		return $result;
	}
}

?>