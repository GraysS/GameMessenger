<?php

namespace ideath;

include 'path\str.php';

include $paths.'DB\FriendsGMDB.php';

include $paths.'FCM\Firebase.php';

include $paths.'FCM\Friends.php';

$db = new FriendsGMDB();	
	
$response = array("error" => false);

$uniques_my_id = $_POST['my_id'];

$uniques_friends_id = $_POST['friends_id'];

$deleteFriends = $db->deleteFriends($uniques_my_id,$uniques_friends_id);

if($deleteFriends != false) {

	$firebase = new Firebase();

	$friends = new Friends();

	$friends->setType(Friends::TYPE_FriendsDeleteActivity);

	$friends->setUserId($deleteFriends['user_id']);	

	$firebase->send($deleteFriends['firebase_token'],$friends->getFriendsAndDeleteLocal());

	$response["error"]  = false;
	echo json_encode($response);
} else {
	$response["error"] = true;
	$response["error_msg"] = "No such element";
	echo json_encode($response);
}
