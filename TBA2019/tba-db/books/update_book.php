<?php
include 'db/db_connect.php';
$response = array();
 
//Check for mandatory parameters
if(isset($_POST['userName'])&&isset($_POST['userID'])&&isset($_POST['password'])){
	$userName = $_POST['userName'];
	$password= $_POST['password'];
	$userID= $_POST['userID'];

	
	//Query to update a movie
	$query = "UPDATE users SET userName=?,password=? WHERE userID=?";
	//Prepare the query
	if($stmt = $con->prepare($query)){
		//Bind parameters
		$stmt->bind_param("ssisi",$userName,$password,$userID);
		//Exceting MySQL statement
		$stmt->execute();
		//Check if data got updated
		if($stmt->affected_rows == 1){
			$response["success"] = 1;			
			$response["message"] = "User successfully updated";
			
		}else{
			//When movie is not found
			$response["success"] = 0;
			$response["message"] = "User not found";
		}					
	}else{
		//Some error while updating
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