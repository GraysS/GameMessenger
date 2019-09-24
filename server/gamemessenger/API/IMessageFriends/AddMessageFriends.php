<?php 

namespace ideath;

include 'path\str.php';

include $paths.'DB\MessageFriendsGMDB.php';

include $paths.'FCM\Firebase.php';

include $paths.'FCM\MessageFriends.php';

$db = new MessageFriendsGMDB();

$response = array();

$uniques_my_id = $_POST['my_id'];

$uniques_friends_id = $_POST['friends_id'];	

$message = $_POST['message'];

$dates = $_POST['dates'];
	
$getMessage = $db->addMessageFriends($uniques_my_id,$uniques_friends_id,$message,$dates);

if($getMessage != false) {

	$firebase = new Firebase();

	$push = new MessageFriends();

	// Push Class
	$push->setType(MessageFriends::TYPE_MessageFriendsAddMessageActivity);

	$response["listMessage"] = array();

	foreach($getMessage as $gets) {
		if($uniques_my_id == $gets['uniques_id']) {
			// Push Class
			$push->setTitle($gets['nick']);		
			$push->setNotifSub($gets['notifSubFriends']);
			$push->setMessage($gets['last_message']);

			// MessageFriends
			$push->setId($gets['id']);
			$push->setUserId($gets['user_id']);
			$push->setLastMessage($gets['last_message']);
			$push->setFrId($gets['frId']);
			$push->setMessageTime($gets['last_message_time']);
			$push->setNotifSubFriends($gets['notifSubFriends']);
			$push->setNick($gets['nick']);
	    } else {
	    	$response["id"] = $gets["id"];
	    	$response["user_id"] = $gets["user_id"];
	    	$response["frId"] = $gets["frId"];
	    	$response["message"] = $gets["last_message"];
	    	$response["message_time"] = $gets["last_message_time"];
	    	$response["notifSubFriends"] = $gets["notifSubFriends"];
	    	$response["nick"] = $gets["nick"];
	    }
	}

	foreach ($getMessage as $frTokens) {
		if($uniques_my_id != $frTokens['uniques_id']) {
			$firebase->send($frTokens['firebase_token'],$push->getMessageFriendsOrAddMessageLocal());
		}
	}

	
	$response["error"] = false;
	echo json_encode($response);
} else {
	$response["error"] = true;
	$response["error_msg"] = "No such element";
	echo json_encode($response);
}

