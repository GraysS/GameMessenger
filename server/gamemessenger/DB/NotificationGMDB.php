<?php 

namespace ideath;

require_once "ParentDB\MyGMDB.php";

class NotificationGMDB extends MyGMDB {

	public function __construct()
	{
	    parent::__construct();
	}


	public function getNotification($my_id) 
    {
   		$notification = "notification_".$my_id;

   		$stmt = $this->myDb->prepare("SELECT nt.notif_id,u.nick FROM users AS u
					INNER JOIN $notification AS nt
					ON u.uniques_id = nt.notif_id");

   		$stmt->execute();

  		if($stmt->rowCount() > 0) {
   			$listNotification = $stmt->fetchAll();
   			$stmt->closeCursor();
  			return $listNotification;
   		} else {
   			$stmt->closeCursor();
   			return false;
  		}
   	}

}