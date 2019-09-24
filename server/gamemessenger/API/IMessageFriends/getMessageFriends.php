<?php

namespace ideath;

include 'path\str.php';

include $paths.'DB\MessageFriendsGMDB.php';

$db = new MessageFriendsGMDB();
	
$response = array();

$uniques_my_id = $_POST['my_id'];

$uniques_friends_id = $_POST['friends_id'];

$listMessageFriends = $db->getMessageFriends($uniques_my_id,$uniques_friends_id);

if($listMessageFriends != false) {

	$response["listMessageFriends"] = array();

	foreach($listMessageFriends as $mess) {
		$message["id"] = $mess["id"];
		$message["frId"] =  $mess["frId"];
		$message["message"] =  $mess["message"];
		$message["message_time"] =  $mess["message_time"];

		array_push($response["listMessageFriends"], $message);
	}
	$response["error"] = false;
	echo json_encode($response);
} else {
	$response["error"] = true;
	$response["error_msg"] = "No such element";
	echo json_encode($response);
}

