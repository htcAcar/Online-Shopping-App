<?php
include '../db/db_connect.php';
$response = array();
//Check for mandatory parameter movie_id
if(isset($_GET['customer_ID'])){
	$userID = $_GET['customer_ID'];
	$query = "DELETE FROM customers WHERE customer_ID=?";
	if($stmt = $con->prepare($query)){
		//Bind movie_id parameter to the query
		$stmt->bind_param("i",$userID);
		$stmt->execute();
		//Check if the movie got deleted
		if($stmt->affected_rows == 1){
			$response["success"] = 1;			
			$response["message"] = "User got deleted successfully";
			
		}else{
			$response["success"] = 0;
			$response["message"] = "User not found";
		}					
	}else{
		$response["success"] = 0;
		$response["message"] = mysqli_error($con);
	}
 
}else{
	$response["success"] = 0;
	$response["message"] = "missing parameter userID";
}
echo json_encode($response);
?>