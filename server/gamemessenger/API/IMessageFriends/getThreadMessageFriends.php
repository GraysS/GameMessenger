<?php

namespace ideath;

include 'path\str.php';

include $paths.'DB\MyGMDB.php';

$db = new MyGMDB();
	
$response = array();

$uniques_my_id = $_POST['my_id'];

$uniques_friends_id = $_POST['friends_id'];

$listThreadMessageFriends = $db->getThreadMessageFriends($uniques_my_id,$uniques_friends_id);

if($listThreadMessageFriends != false) {

	$response["listThreadMessageFriends"] = array();

	foreach($listThreadMessageFriends as $mess) {
		$message["message"] =  $mess["message"];

		array_push($response["listThreadMessageFriends"], $message);
	}
	$response["error"] = false;
	echo json_encode($response);

	$deleteThreadMessageFriends = $db->deleteThreadMessageFriends($uniques_my_id,$uniques_friends_id);

	if($deleteThreadMessageFriends != false) {
		$responses["deletes"] = true;
		echo json_encode($responses);
	}
} else {
	$response["error"] = true;
	$response["error_msg"] = "No such element";
	echo json_encode($response);
}