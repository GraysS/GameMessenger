<?php

namespace ideath;

require_once "ParentPush\Push.php";

class MessageFriends extends Push {

	private $id;
	private $user_id;
	private $last_message;
	private $frId;
	private $message_time;
	private $notifSubFriends;
	private $nick;

 	const TYPE_MessageFriendsAddMessageActivity = 'MessageFriendsAddMessageActivity';
    const TYPE_MessageFriendsDeleteMessageActivity = 'MessageFriendsDeleteMessageActivity';

 	public function setId($id) {
 		$this->id = $id;
 	}

 	public function setUserId($user_id) {
        $this->user_id = $user_id;
    }

    public function setLastMessage($last_message) {
        $this->last_message = $last_message;
    }

    public function setFrId($frId){
    	$this->frId = $frId;
    }

    public function setMessageTime($message_time) {
    	$this->message_time = $message_time;
    }

    public function setNotifSubFriends($notifSubFriends){
    	$this->notifSubFriends = $notifSubFriends;
    }

    public function setNick($nick) {
    	$this->nick = $nick;
    }

    public function getMessageFriendsOrAddMessageLocal() 
    {
    	$resFriend = array();
		//Add friends
		$resFriend['data']['id'] = $this->id;
		$resFriend['data']['user_id'] = $this->user_id;	
		$resFriend['data']['last_message'] = $this->last_message;
		$resFriend['data']['frId']  = $this->frId;
		$resFriend['data']['message_time'] = $this->message_time;
		$resFriend['data']['notifSubFriends'] = $this->notifSubFriends;
		$resFriend['data']['nick'] = $this->nick;
		//Add Push
		$resFriend['data']['notification'] = parent::getPush();

		return $resFriend;
    }

    public function getMessageFriendsOrDeleteMessageLocal() 
    {
        $resFriend = array();

        $resFriend['data']['user_id'] = $this->user_id; 
        $resFriend['data']['last_message'] = $this->last_message;
        $resFriend['data']['frId']  = $this->frId;
        $resFriend['data']['message_time'] = $this->message_time;
        $resFriend['data']['notifSubFriends'] = $this->notifSubFriends;
        $resFriend['data']['nick'] = $this->nick;
        //Add Type
        $resFriend['data']['type'] = parent::getType();

        return $resFriend;
    }



}
