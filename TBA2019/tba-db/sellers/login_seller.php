<?php
include '../db/db_connect.php';
$result = array();
$userArray = array();
$response = array();
//Query to select movie id and movie name

//Check for mandatory parameters
if(isset($_GET['email'])
	&&isset($_GET['password'])){
	$email = $_GET['email'];
	$password = $_GET['password'];

	//Query to insert a movie
	$query = "SELECT * FROM sellers WHERE email='$email' AND password='$password'";
	//Prepare the query
	if($stmt = $con->prepare($query)){
			$result = mysqli_query($con, $query);
			$user = mysqli_fetch_assoc($result);
			if($user) {
				$response["data"] = $user;
				//$result[]=$userArray;
				$stmt->close();
				$response["success"] = 1;
				$response["data"] = $user;
			}
			else {
				//Mandatory parameters are missing
				$response["success"] = 0;
				$response["data"] = $user;
				$response["message"] = "Sorry, you need to register first";
			}
			

	}else{
		//Some error while inserting
		$response["success"] = 0;
		$response["message"] = mysqli_error($con);
	}
 
}else{
	//Mandatory parameters are missing
	$response["success"] = 0;
	$response["message"] = "missing mandatory parameters";
}
//Displaying JSON response
echo json_encode($response);
?>