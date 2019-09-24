<?php 

namespace ideath;

include 'path\str.php';

include $paths.'DB\UsersGMDB.php';

$db = new UsersGMDB();

$response = array();

$uniques_id = $_POST['id'];

$listUsers = $db->getListUsers($uniques_id);

if($listUsers != false) {

	$response["listUsers"] = array();

	foreach($listUsers as $use) {
		if($uniques_id == $use["uniques_id"]) {
			continue;
		}
		$user["uniques_id"] =  $use["uniques_id"];
		$user["nick"] =  $use["nick"];

		array_push($response["listUsers"], $user);
	}
	$response["error"] = false;
	echo json_encode($response);
} else {
	$response["error"] = true;
	$response["error_msg"] = "No such element";
	echo json_encode($response);
}


