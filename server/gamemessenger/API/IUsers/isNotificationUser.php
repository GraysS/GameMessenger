<?php

namespace ideath;

include 'path\str.php';

include $paths.'DB\UsersGMDB.php';

$db = new UsersGMDB();

$response = array("error" => false);

$uniques_my_id = $_POST['my_id'];

$uniques_friends_id = $_POST['friends_id'];	

$isNotif = $db->isNotificationUser($uniques_my_id,$uniques_friends_id);

if($isNotif != false) {
	$response["error"] = false;
	$response["uniques_id"] = $isNotif["notif_id"];
	echo json_encode($response);
} else {
	//$response["uniques_id"] = $isNotif["user_id"];
	$response["error"] = true;
	echo json_encode($response);
}

