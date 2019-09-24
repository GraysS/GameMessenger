<?php

namespace ideath;

include 'path\str.php';

include $paths.'DB\FriendsGMDB.php';
	
$db = new FriendsGMDB();
	
$response = array();

$uniques_my_id = $_POST['my_id'];

$listFriends = $db->getFriends($uniques_my_id);

if($listFriends != false) {

	$response["listFriends"] = array();

	foreach($listFriends as $use) {
		$user["user_id"] = $use["user_id"];
		$user["nick"] =  $use["nick"];
		$user["last_message"] = $use["last_message"];
		$user["frId"] = $use["frId"];
		$user["last_message_time"] = $use["last_message_time"];
		$user["notifSubFriends"] = $use["notifSubFriends"];

		array_push($response["listFriends"], $user);
	}
	$response["error"] = false;
	echo json_encode($response);
} else {
	$response["error"] = true;
	$response["error_msg"] = "No such element";
	echo json_encode($response);
}


