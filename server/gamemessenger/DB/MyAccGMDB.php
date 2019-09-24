<?php 

namespace ideath;

require_once "ParentDB\MyGMDB.php";

class MyAccGMDB extends MyGMDB {

	public function __construct()
	{
	    parent::__construct();
	}


	public function registration($nick,$pass,$firebase_token)
   	{	

		$stmt = $this->myDb->prepare("INSERT INTO users(nick,password,firebase_token) VALUES(:nick,:pass,:firebase_token)");
   		
   		$stmt->bindParam(':nick',$nick);
   		$stmt->bindParam(':pass',$pass);
       	$stmt->bindParam(':firebase_token',$firebase_token);

   		$result = $stmt->execute();
   		$stmt->closeCursor();
   			//check for succesful store
   		if($result) {
   			$stmt = $this->myDb->prepare("SELECT uniques_id,nick,firebase_token FROM users
   			  WHERE nick = :nick");

   			$stmt->bindParam(':nick', $nick);

   			$stmt->execute();

   			$user = $stmt->fetch();

   			$stmt->closeCursor();

   			$friends = "friends_".$user['uniques_id'];

   			$this->myDb->exec("CREATE TABLE $friends(
   					id INT AUTO_INCREMENT PRIMARY KEY,user_id INT NOT NULL,last_message VARCHAR(30) NOT NULL,frId INT NOT NULL,
            last_message_time BIGINT NOT NULL,notifSubFriends VARCHAR(5) NOT NULL DEFAULT 'TRUE')");

   			$notification = "notification_".$user['uniques_id'];

   			$this->myDb->exec("CREATE TABLE $notification(
   					id INT AUTO_INCREMENT PRIMARY KEY,notif_id INT NOT NULL)");

         /* $notificationSubscription = "notificationSubscription_".$user['uniques_id'];

          $this->myDb->exec("CREATE TABLE $notificationSubscription(
              id INT AUTO_INCREMENT PRIMARY KEY,notifSub_id INT NOT NULL)");*/

   			return $user;
   		} else {
   			return false;
   		}
   	}

		public function login($nick,$password,$firebase_token)
		{  
  			$stmt = $this->myDb->prepare("SELECT uniques_id,nick FROM users 
  				WHERE nick = :nick AND password = :password");

  			$stmt->bindParam(':nick',$nick);
  			$stmt->bindParam(':password',$password);
    
  			if($stmt->execute()) {
  				$user = $stmt->fetch(); 
  				$stmt->closeCursor();

        	    $stmt = $this->myDb->prepare("UPDATE users SET firebase_token = :firebase_token WHERE nick = :nick AND password = :password");
          		$stmt->bindParam(':nick',$nick);
        	    $stmt->bindParam(':password',$password);
          	    $stmt->bindParam(':firebase_token',$firebase_token);
          		$stmt->execute();
         	    $stmt->closeCursor();
          
  				return $user;
  			} else {
        	    $stmt->closeCursor();
  				return false;
  			}
		}

      public function isUserExisted($nick)
      {
        $stmt = $this->myDb->prepare("SELECT nick FROM users WHERE nick = :nick");

        $stmt->bindParam(":nick",$nick);

        $stmt->execute();

        if($stmt->rowCount() > 0) {
          // user existed
          $stmt->closeCursor();
          return true;
        } else {
          // user not existed
          $stmt->closeCursor();
          return false;
        }
     }

}