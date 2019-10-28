<?php
include '../db/db_connect.php';
$response = array();
 
//Check for mandatory parameters
if(isset($_GET['email'])&&isset($_GET['product_ID'])&&isset($_GET['review'])){
	$email = $_GET['email'];
	$product_ID = $_GET['product_ID'];
	$review = $_GET['review'];

	//Prepare the query
	$query = "INSERT INTO reviews(product_ID, email, review) VALUES (?,?,?)";
	//Prepare the query
	if($stmt = $con->prepare($query)){

		//Bind parameters
		$stmt->bind_param("iss",$product_ID, $email, $review);
		//Exceting MySQL statement
		$res = $stmt->execute();
		//Check if data got inserted
		if($stmt->affected_rows == 1){
			$response["success"] = 1;			
			$response["message"] = "Your review has been saved!";			
			
		}else{
			//Some error while inserting
			$response["success"] = 0;
			$response["message"] = "Error while saving the review.";
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
//http://localhost/tba-db/carts/add_cart_headphone.php?email=2&product_ID=16
echo json_encode($response);
?>