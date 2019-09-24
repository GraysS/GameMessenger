<?php 

namespace ideath;

include 'path\str.php';

include $paths.'DB\UsersGMDB.php';

$db = new UsersGMDB();

$response = array("error" => false);

$uniques_my_id = $_POST['my_id'];

$uniques_friends_id = $_POST['friends_id'];

$notifBool = $db->addNotificationUser($uniques_my_id,$uniques_friends_id);

if($notifBool != false) {

	$response["error"] = false;
	echo json_encode($response);
} else {
	$response["error"] = true;
	$response["error_msg"] = "No such element";
	echo json_encode($response);
}

