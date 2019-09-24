<?php 

namespace ideath;

require_once "ParentDB\MyGMDB.php";

class FriendsGMDB extends MyGMDB {

	public function __construct()
	{
		  parent::__construct();
	}


	 public function addFriendsNotification($my_id,$friends_id)
     {   

        $friendsMy = "friends_".$my_id;

        $stmt = $this->myDb->prepare("INSERT INTO $friendsMy(user_id) VALUES(:friends_id)");

        $stmt->bindParam(":friends_id",$friends_id);

        $stmt->execute();

        $stmt->closeCursor();

        $friendsYou = "friends_".$friends_id;

        $stmt = $this->myDb->prepare("INSERT INTO $friendsYou(user_id) VALUES(:my_id)");

        $stmt->bindParam(":my_id",$my_id);

        $stmt->execute();
           
        $stmt->closeCursor();

        $notification = "notification_".$my_id;

        $deleteNotif = $this->myDb->prepare("DELETE FROM $notification WHERE notif_id = :notif_id");

        $deleteNotif->bindParam(":notif_id",$friends_id);

        if($deleteNotif->execute()) {
            $deleteNotif->closeCursor();

            $messageMy = "message_".$my_id."_".$friends_id;

            $this->myDb->exec("CREATE TABLE $messageMy
             (id INT AUTO_INCREMENT PRIMARY KEY,frId INT,message VARCHAR(500),
             message_time BIGINT)");

            $messageFriends = "message_".$friends_id."_".$my_id;

            $this->myDb->exec("CREATE TABLE $messageFriends
             (id INT AUTO_INCREMENT PRIMARY KEY,frId INT,message VARCHAR(500),
                message_time BIGINT)");

            // Push 
            $stmt = $this->myDb->prepare("
                    SELECT fr.user_id,fr.last_message,fr.frId,fr.last_message_time,fr.notifSubFriends,u.nick,u.firebase_token,u.notifSub 
                    FROM $friendsMy AS fr
                    INNER JOIN 
                    users AS u 
                    ON u.uniques_id = :friends_id AND fr.user_id = :friends_id");

            $stmt->bindParam(":friends_id",$friends_id);

            $stmt->execute();

            $addOk = $stmt->fetch();

            $stmt->closeCursor();

            // onResponse 
            $stmt = $this->myDb->prepare("
                    SELECT fr.user_id,fr.last_message,fr.frId,fr.last_message_time,fr.notifSubFriends,u.nick,u.firebase_token,u.notifSub
                    FROM $friendsYou AS fr
                    INNER JOIN 
                    users AS u 
                    ON u.uniques_id = :my_id AND fr.user_id = :my_id");

            $stmt->bindParam(":my_id",$my_id);

            $stmt->execute();

            $getOk = $stmt->fetch();

            $stmt->closeCursor();

            $addFriendsArray = array();

            $addFriendsArray["friendAcc"] = $addOk;

            $addFriendsArray["myAcc"] = $getOk;

            return $addFriendsArray;
        } else {
          $deleteNotif->closeCursor();
          return false;
        }

    }

    public function getFriends($my_id)
    {
       $friends = "friends_".$my_id;

       $stmt = $this->myDb->prepare("SELECT fr.user_id,u.nick,fr.last_message,fr.frId,fr.last_message_time,fr.notifSubFriends FROM users AS u
               INNER JOIN $friends AS fr
               ON u.uniques_id = fr.user_id");

       $stmt->execute();

        if($stmt->rowCount() > 0) {
           $listFriends = $stmt->fetchAll();
           $stmt->closeCursor();
           return $listFriends;
        } else {
           $stmt->closeCursor();
           return false;
        }
    }

    public function deleteFriends($my_id,$friends_id)
    {
         $friendsYou = "friends_".$friends_id;

         $stmt = $this->myDb->prepare("SELECT fr.user_id,u.firebase_token FROM users AS u
               INNER JOIN $friendsYou AS fr
               ON u.uniques_id = :friends_id AND fr.user_id = :my_id");

         $stmt->bindParam(":my_id",$my_id);

         $stmt->bindParam(":friends_id",$friends_id);

         $stmt->execute();

         if($stmt->rowCount() > 0) {

             $result = $stmt->fetch();
         
             $stmt->closeCursor();

             $friendsMy = "friends_".$my_id;

             $stmt = $this->myDb->prepare("DELETE FROM $friendsMy WHERE user_id = :friends_id");

             $stmt->bindParam(":friends_id",$friends_id);

             $stmt->execute();

             $stmt->closeCursor();

             $stmt = $this->myDb->prepare("DELETE FROM $friendsYou WHERE user_id = :my_id");

             $stmt->bindParam(":my_id",$my_id);

             if($stmt->execute()) {

                 $stmt->closeCursor();

                 $messageMy = "message_".$my_id."_".$friends_id;
     
                 $this->myDb->exec("DROP TABLE $messageMy");

                 $messageFriends = "message_".$friends_id."_".$my_id;

                 $this->myDb->exec("DROP TABLE $messageFriends");

                 return $result;
              } else {
                 $stmt->closeCursor();
                 return false;
              }
          } else {
             $stmt->closeCursor();
             return false;
          }
      }

      
}
