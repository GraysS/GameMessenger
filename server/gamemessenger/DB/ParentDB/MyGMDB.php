<?php

	namespace ideath;

	use \PDO;

	class MyGMDB {

		const  DBName = 'mysql:host=localhost;dbname=gamemessenger';

		const  DBAdmin = 'root';
		
		const  DBPassword = '';

		protected $myDb;

		public function __construct()
		{
			$this->myDb = new PDO(self::DBName,self::DBAdmin,self::DBPassword,
					   [PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION]
					 );

			$this->myDb->query("SET NAMES utf8");

		}

     /*
          START IMyAcc

      */



       /*
          END IMyAcc

      */



       /*
          START IUsers

      */

		

     /*
          END IUsers

      */


      /*
          START INotification

      */


    /*
          END INotification

      */





      /*
          START IMessageFriends

      */
    
     
       /*
          END IMessageFriends

      */

      /*
          START IFriends

      */

   


      /*
          END IFriends

      */

}

