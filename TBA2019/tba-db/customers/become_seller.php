<?php
include '../db/db_connect.php';
$response = array();
 
//Check for mandatory parameters
if(isset($_GET['password'])
	&&isset($_GET['customer_ID'])
	&&isset($_GET['email'])){
	$email = $_GET['email'];
	$password = $_GET['password'];
	$seller_ID = $_GET['customer_ID'];
	//$arr = array(1, 2, 3);
	//$arr_str = serialize($arr);
	//Query to insert a movie
	$query = "INSERT INTO sellers( seller_ID, email, password) VALUES (?,?,?)";
	//Prepare the query
	if($stmt = $con->prepare($query)){

		//Bind parameters
		$stmt->bind_param("sss",$seller_ID,$email, $password);
		//Exceting MySQL statement
		$res = $stmt->execute();
		//Check if data got inserted
		if($stmt->affected_rows == 1){
			$response["success"] = 1;			
			$response["message"] = "Yay! You became a seller :)";			
			
		}else{
			//Some error while inserting
			$response["success"] = 0;
			$response["message"] = "You are already a seller :)";
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