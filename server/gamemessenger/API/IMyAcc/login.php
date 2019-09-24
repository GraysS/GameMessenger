<?php 

namespace ideath;

include 'path\str.php';

include $paths.'DB\MyAccGMDB.php';

$db = new MyAccGMDB();

//json response array
$response = array("error" => false);

if(isset($_POST['nick']) && isset($_POST['password']) && isset($_POST['firebase_token'])) {

	// receiving the post params
	$nick = $_POST['nick'];
	$password = $_POST['password'];
	$firebase_token = $_POST['firebase_token'];

	// get the user by email and password
	$user = $db->login($nick,$password,$firebase_token);

	if($user != false) {
		// use is found
		$response["error"] = false;
		$response["uniques_id"] = $user["uniques_id"];
		$response["nick"] = $user["nick"];
		$response["firebase_token"] = $firebase_token;
		echo json_encode($response);
	} else {
		// user is not found with the credentials
		$response["error"] = true;
		$response["error_msg"] = "Login credentials are wrong. Please try again!";
		echo json_encode($response);
	}	
} else {
	// required post params is missing
	$response["error"] = true;
	$response["error_msg"] = "Required parameters email or password is missing!";
	echo json_encode($response);
}
