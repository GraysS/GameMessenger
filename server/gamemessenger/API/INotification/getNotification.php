<?php

namespace ideath;

include 'path\str.php';

include $paths.'DB\NotificationGMDB.php';

$db = new NotificationGMDB();

$response = array();

$uniques_my_id = $_POST['my_id'];

$listNotification = $db->getNotification($uniques_my_id);

if($listNotification != false) {

	$response["listNotification"] = array();

	foreach($listNotification as $notif) {
		$notification["notif_id"] =  $notif["notif_id"];
		$notification["nick"] =  $notif["nick"];

		array_push($response["listNotification"], $notification);
	}
	$response["error"] = false;
	echo json_encode($response);
} else {
	$response["error"] = true;
	$response["error_msg"] = "No such element";
	echo json_encode($response);
}


