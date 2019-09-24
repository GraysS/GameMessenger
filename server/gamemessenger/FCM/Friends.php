<?php 

namespace ideath;

require_once "ParentPush\Push.php";

class Friends extends Push {

	private $user_id;
	private $last_message;
	private $frId;
	private $last_message_time;
	private $notifSubFriends;
	private $nick;
	
	const TYPE_FriendsDeleteActivity = 'FriendsDeleteActivity';
	const TYPE_FriendsAddActivity = 'FriendsAddActivity';

	public function setUserId($user_id) {
        $this->user_id = $user_id;
    }

    public function setLastMessage($last_message) {
        $this->last_message = $last_message;
    }

    public function setFrId($frId){
    	$this->frId = $frId;
    }

    public function setLastMessageTime($last_message_time) {
    	$this->last_message_time = $last_message_time;
    }

    public function setNotifSubFriends($notifSubFriends){
    	$this->notifSubFriends = $notifSubFriends;
    }

    public function setNick($nick) {
    	$this->nick = $nick;
    }


	public function getFriendsAndDeleteLocal() {
		$resFriend = array();
		$resFriend['data']['type'] = parent::getType();
		$resFriend['data']['user_id'] = $this->user_id;	
		return $resFriend;
	}

	public function getFriendsAndAddLocal() {
		$resFriend = array();
		//Add friends
		//$resFriend['data']['type'] = parent::getType();
		$resFriend['data']['user_id'] = $this->user_id;	
		$resFriend['data']['last_message'] = $this->last_message;
		$resFriend['data']['frId']  = $this->frId;
		$resFriend['data']['last_message_time'] = $this->last_message_time;
		$resFriend['data']['notifSubFriends'] = $this->notifSubFriends;
		$resFriend['data']['nick'] = $this->nick;
		//Add Push
		$resFriend['data']['notification'] = parent::getPush();

		return $resFriend;
	}
}

