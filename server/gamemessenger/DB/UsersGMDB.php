<?php 

namespace ideath;

require_once "ParentDB\MyGMDB.php";

class UsersGMDB extends MyGMDB {

	public function __construct()
	{
	    parent::__construct();
	}

	 public function getListUsers($my_id)
	 {
         $friends = "friends_".$my_id;

         $stmt = $this->myDb->prepare("SELECT uniques_id,nick FROM users AS u WHERE NOT EXISTS 
                (
                  SELECT * FROM $friends AS fr
                  WHERE u.uniques_id = fr.user_id
                 ); ");

         $stmt->execute();

         if($stmt->rowCount() > 0) {
            $listUsers = $stmt->fetchAll();
            $stmt->closeCursor();
            return $listUsers;
         } else {
            $stmt->closeCursor();
            return false;
         }

	 }

      public function isNotificationUser($my_id,$friends_id)
      {  
         $notificationFr = "notification_".$friends_id;

         $stmt = $this->myDb->prepare("SELECT notif_id FROM $notificationFr WHERE notif_id = :my_id");

         $stmt->bindParam(":my_id",$my_id);

         $stmt->execute();

         $notificationMy = "notification_".$my_id;

         $sqmt = $this->myDb->prepare("SELECT notif_id FROM $notificationMy WHERE notif_id = :friends_id");

         $sqmt->bindParam(":friends_id",$friends_id);

         $sqmt->execute();

         if($stmt->rowCount() > 0 ) {
            $user = $stmt->fetch();
            $stmt->closeCursor();
            $sqmt->closeCursor();
            return $user;
         } elseif($sqmt->rowCount() > 0){
            $user = $sqmt->fetch();
            $stmt->closeCursor();
            $sqmt->closeCursor();
            return $user;
         } else {
            $stmt->closeCursor();
            $sqmt->closeCursor();
            return false;
         }

      }


   	public function addNotificationUser($my_id,$friends_id)
   	{
   		$notification = "notification_".$friends_id;

   		$stmt = $this->myDb->prepare("INSERT INTO $notification(notif_id) VALUES(:my_id)");

   		$stmt->bindParam(":my_id",$my_id);

  		if($stmt->execute()) {
   			$stmt->closeCursor();
   			return true;
  		} else {
  			$stmt->closeCursor();
   			return false;
   		}
   	}


}