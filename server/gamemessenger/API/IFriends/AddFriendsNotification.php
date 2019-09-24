<?php 

namespace ideath;

include 'path\str.php';

include $paths.'DB\FriendsGMDB.php';

include $paths.'FCM\Firebase.php';

include $paths.'FCM\Friends.php';

$db = new FriendsGMDB();

$response = array();

$uniques_my_id = $_POST['my_id'];

$uniques_friends_id = $_POST['friends_id'];

$getFriends = $db->addFriendsNotification($uniques_my_id,$uniques_friends_id);

if($getFriends != false) {

	$firebase = new Firebase();

	$friends = new Friends();

	$getAccMy = array();

	$getAccFr = array();

	$getAccMy = $getFriends["myAcc"];

	$getAccFr = $getFriends["friendAcc"];

	// PUSH CLass
	$friends->setType(Friends::TYPE_FriendsAddActivity);

	$friends->setTitle($getAccMy["nick"]);

	$friends->setMessage("language");

	$friends->setNotifSub($getAccFr["notifSub"]);


	// Friends Class
	$friends->setTitle($getAccMy["nick"]);

	$friends->setUserId($getAccMy["user_id"]);

	$friends->setLastMessage($getAccMy["last_message"]);

	$friends->setFrId($getAccMy["frId"]);

	$friends->setLastMessageTime($getAccMy["last_message_time"]);

	$friends->setNotifSubFriends($getAccMy["notifSubFriends"]);

	$friends->setNick($getAccMy["nick"]);

	$response["user_id"] = $getAccFr["user_id"];

	$response["frId"] = $getAccFr["frId"];

	$response["nick"] = $getAccFr["nick"];

	$response["last_message"] = $getAccFr["last_message"];

	$response["last_message_time"] = $getAccFr["last_message_time"];

	$response["notifSubFriends"] = $getAccFr["notifSubFriends"];

	$response["error"] = false;

	$firebase->send($getAccFr["firebase_token"],$friends->getFriendsAndAddLocal());

	echo json_encode($response);

} else {
	$response["error"] = true;
	$response["error_msg"] = "No such element";
	echo json_encode($response);
}

