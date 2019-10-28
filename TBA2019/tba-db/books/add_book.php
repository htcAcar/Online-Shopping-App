<?php
include 'db/db_connect.php';
$response = array();
 
//Check for mandatory parameters
if(isset($_POST['userID'])&&isset($_POST['userName'])&&isset($_POST['password'])){
	$userID = $_POST['userID'];
	$userName = $_POST['userName'];
	$password = $_POST['password'];
	
	//Query to insert a movie
	$query = "INSERT INTO users( userID, userName, password) VALUES (?,?,?)";
	//Prepare the query
	if($stmt = $con->prepare($query)){
		//Bind parameters
		$stmt->bind_param("ssis",$userID, $userName, $password);
		//Exceting MySQL statement
		$stmt->execute();
		//Check if data got inserted
		if($stmt->affected_rows == 1){
			$response["success"] = 1;			
			$response["message"] = "User Successfully Added";			
			
		}else{
			//Some error while inserting
			$response["success"] = 0;
			$response["message"] = "Error while adding user";
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