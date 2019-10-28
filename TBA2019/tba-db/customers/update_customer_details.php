<?php
include '../db/db_connect.php';
$response = array();
 
//Check for mandatory parameters
if(isset($_GET['customer_ID'])
	&&isset($_GET['email'])
	&&isset($_GET['password'])
	&&isset($_GET['dob'])
	&&isset($_GET['address'])){
	$email = $_GET['email'];
	$password = $_GET['password'];
	$dob = $_GET['dob'];
	$address = $_GET['address'];
	$customer_ID = $_GET['customer_ID'];

	
	//Query to update a movie
	$query = "UPDATE customers SET email='$email', password='$password',dob='$dob',address='$address' WHERE customer_ID='$customer_ID'";
	//Prepare the query
	if($stmt = $con->prepare($query)){
		
		//Bind parameters
		$result = mysqli_query($con,$query);
		$response["success"] = 1;
		$response["messageful"] = "Your information has been successfully updated :)";
		//Exceting MySQL statement
		
	}else{
		//Some error while updating
		$response["success"] = 0;
		$response["messageful"] = mysqli_error($con);
	}
 
}else{
	//Mandatory parameters are missing
	$response["success"] = 0;
	$response["message"] = "missing mandatory parameters";
}
//Displaying JSON response
echo json_encode($response);
?>