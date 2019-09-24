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

$uniques_id = $_POST['id'];

$deleteMessage = $db->deleteMessageFriends($uniques_my_id,$uniques_friends_id,$uniques_id);

if($deleteMessage != false) {

	$firebase = new Firebase();

	$push = new MessageFriends();

	// Push Class
	$push->setType(MessageFriends::TYPE_MessageFriendsDeleteMessageActivity);

	$response["listMessage"] = array();

	foreach($deleteMessage as $gets) {
		if($uniques_my_id == $gets['uniques_id']) {
			//MessageFriends
			$push->setUserId($gets['user_id']);
			$push->setLastMessage($gets['last_message']);
			$push->setFrId($gets['frId']);
			$push->setMessageTime($gets['last_message_time']);
			$push->setNotifSubFriends($gets['notifSubFriends']);
			$push->setNick($gets['nick']);
		} else {
	    	$response["user_id"] = $gets["user_id"];
	    	$response["frId"] = $gets["frId"];
	    	$response["message"] = $gets["last_message"];
	    	$response["message_time"] = $gets["last_message_time"];
	    	$response["notifSubFriends"] = $gets["notifSubFriends"];
	    	$response["nick"] = $gets["nick"];
		}
	}


	foreach ($deleteMessage as $frTokens) {
		if($uniques_my_id != $frTokens['uniques_id']) {
			$firebase->send($frTokens['firebase_token'],$push->getMessageFriendsOrDeleteMessageLocal());
		}
	}

	$response["error"]  = false;
	echo json_encode($response);
} else {
	$response["error"] = true;
	$response["error_msg"] = "No such element";
	echo json_encode($response);
}

