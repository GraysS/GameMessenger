<?php 

namespace ideath;

include 'path\str.php';

include $paths.'DB\MyAccGMDB.php';

$db = new MyAccGMDB();

$response = array("error" => false);


if (isset($_POST['nick']) && isset($_POST['password']) && isset($_POST['firebase_token'])) {

	$nick = $_POST['nick'];
	$password = $_POST['password'];
	$firebase_token = $_POST['firebase_token'];

	if ($db->isUserExisted($nick)) {

		$response["error"] = true;
		$response["error_msg"] = "User already existed with ". $nick;
		echo json_encode($response);
	} else {

		$user = $db->registration($nick,$password,$firebase_token);
		if($user) {
			// user add successfully
			$response["error"] = false;
			$response["uniques_id"]   = $user["uniques_id"];
			$response["nick"] = $user["nick"];
			$response["firebase_token"] = $user["firebase_token"];
			echo json_encode($response);
		} else {
			// failed to create user
			$response["error"] = true;
			$response["error_msg"] = "Unknown error occurred in registration!";
			echo json_encode($response);
		}
	}


} else {
	$response["error"] = true;
	$response["error_msg"] = "Required parameters (name, email or password) is missing!";
	echo json_encode($response);
}