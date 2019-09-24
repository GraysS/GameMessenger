<?php

namespace ideath;

require_once "ParentDB\MyGMDB.php";

class MessageFriendsGMDB extends MyGMDB {
	
	public function __construct()
	{
	   	parent::__construct();
	}


	 public function addMessageFriends($my_id,$friends_id,$message,$dates)
   {
         $messageMy = "message_".$my_id."_".$friends_id;

         $stmt =  $this->myDb->prepare("INSERT INTO $messageMy(frId,message,message_time)
          VALUES(:my_id,:message,:dates)");

         $stmt->bindParam(":my_id",$my_id);

         $stmt->bindParam(":message",$message);

         $stmt->bindParam(":dates",$dates);

         $stmt->execute();

         $stmt->closeCursor();

         $messageFriends = "message_".$friends_id."_".$my_id;

         $stmt = $this->myDb->prepare("INSERT INTO $messageFriends(frId,message,message_time) 
            VALUES(:my_id,:message,:dates)");

         $stmt->bindParam(":my_id",$my_id);

         $stmt->bindParam(":message",$message);

         $stmt->bindParam(":dates",$dates);

         if($stmt->execute()) {
            $stmt->closeCursor();

           $friends = "friends_".$my_id;

           $stmt = $this->myDb->prepare("UPDATE $friends SET last_message = :message,frId = :my_id,last_message_time = :dates WHERE user_id = :friends_id");

           $stmt->bindParam(":message",$message);

           $stmt->bindParam(":my_id",$my_id);

           $stmt->bindParam(":friends_id",$friends_id);

           $stmt->bindParam(":dates",$dates);

           $stmt->execute();

            $stmt->closeCursor();

            $friendsYou = "friends_".$friends_id;

           $stmt = $this->myDb->prepare("UPDATE $friendsYou SET last_message = :message,frId = :my_id,last_message_time = :dates WHERE user_id = :my_id");

           $stmt->bindParam(":message",$message);

           $stmt->bindParam(":my_id",$my_id);

           $stmt->bindParam(":dates",$dates);

           $stmt->execute();

           $stmt->closeCursor();


           $stmt = $this->myDb->prepare("SELECT u.uniques_id,u.nick,u.firebase_token,fr.user_id,fr.last_message,fr.frId,fr.last_message_time,fr.notifSubFriends,myms.id FROM users AS u
                                        INNER JOIN
                                        $friendsYou AS fr
                                        INNER JOIN 
                                        $messageMy AS myms 
                                        WHERE u.uniques_id = :my_id AND fr.user_id = :my_id AND myms.id = (SELECT MAX(id) FROM $messageMy)
                                        UNION 
                                        SELECT us.uniques_id,us.nick,us.firebase_token,frs.user_id,frs.last_message,frs.frId,frs.last_message_time,frs.notifSubFriends,frms.id FROM users AS us
                                        INNER JOIN 
                                        $friends AS frs
                                        INNER JOIN 
                                        $messageFriends AS frms
                                        WHERE us.uniques_id = :friends_id AND frs.user_id = :friends_id AND frms.id = (SELECT MAX(id) FROM $messageFriends);");


           $stmt->bindParam(":my_id",$my_id);

           $stmt->bindParam(":friends_id",$friends_id);

           $stmt->execute();

           if($stmt->rowCount() == 2) { 
              $result = $stmt->fetchAll(); 
              $stmt->closeCursor();
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

      public function getMessageFriends($my_id,$friends_id)
      {
         $messageMy = "message_".$my_id."_".$friends_id;

         $stmt = $this->myDb->prepare("SELECT id,frId,message,message_time FROM $messageMy ORDER BY id ASC");

         $stmt->execute();

         if($stmt->rowCount() > 0) {
            $listMessageFriends = $stmt->fetchAll();
            $stmt->closeCursor();
            return $listMessageFriends;
         } else {
            $stmt->closeCursor();
            return false;
         }
      }

      public function deleteMessageFriends($my_id,$friends_id,$id)
      {

           $messageMy = "message_".$my_id."_".$friends_id;

           $stmt = $this->myDb->prepare("DELETE FROM $messageMy WHERE id = :id");

           $stmt->bindParam(":id",$id);

           $stmt->execute();

           $stmt->closeCursor();

           $messageFriends = "message_".$friends_id."_".$my_id;

           $stmt = $this->myDb->prepare("DELETE FROM $messageFriends WHERE id = :id");

           $stmt->bindParam(":id",$id);

           $stmt->execute();
        
           $stmt->closeCursor();

           $stmt = $this->myDb->prepare("SELECT message,frId,message_time FROM $messageMy 
            WHERE id = (SELECT MAX(id) FROM $messageMy)");

           $stmt->execute();

           $result = $stmt->fetch();

           $stmt->closeCursor();

            $friends = "friends_".$my_id;

           $stmt = $this->myDb->prepare("UPDATE $friends SET last_message = :message,frId = :my_id,last_message_time = :dates WHERE user_id = :friends_id");

           $stmt->bindParam(":message",$result["message"]);

           $stmt->bindParam(":my_id",$result["frId"]);

           $stmt->bindParam(":friends_id",$friends_id);

           $stmt->bindParam(":dates",$result["message_time"]);

           $stmt->execute();

            $stmt->closeCursor();

            $friendsYou = "friends_".$friends_id;

           $stmt = $this->myDb->prepare("UPDATE $friendsYou SET last_message = :message,frId = :fr_id,last_message_time = :dates WHERE user_id = :my_id");

           $stmt->bindParam(":message",$result["message"]);

           $stmt->bindParam(":fr_id",$result["frId"]);

           $stmt->bindParam(":my_id",$my_id);

           $stmt->bindParam(":dates",$result["message_time"]);

           $stmt->execute();

           $stmt->closeCursor();

           $stmt = $this->myDb->prepare("SELECT u.uniques_id,u.nick,u.firebase_token,fr.user_id,fr.last_message,fr.frId,fr.last_message_time,fr.notifSubFriends FROM users AS u
                                        INNER JOIN
                                        $friendsYou AS fr                             
                                        WHERE u.uniques_id = :my_id AND fr.user_id = :my_id
                                        UNION 
                                        SELECT us.uniques_id,us.nick,us.firebase_token,frs.user_id,frs.last_message,frs.frId,frs.last_message_time,frs.notifSubFriends FROM users AS us
                                        INNER JOIN 
                                        $friends AS frs
                                        WHERE us.uniques_id = :friends_id AND frs.user_id = :friends_id");


            $stmt->bindParam(":my_id",$my_id);

            $stmt->bindParam(":friends_id",$friends_id);

          if($stmt->execute()) {
             $resultArr = $stmt->fetchAll();
             $stmt->closeCursor();
             return $resultArr;
          } else {
             $stmt->closeCursor();
             return false;
          }
      }

}