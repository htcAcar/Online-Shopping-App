<?php
include '../db/db_connect.php';
$response = array();
 
//Check for mandatory parameters
if(isset($_GET['customer_ID'])&&isset($_GET['product_ID'])){
	$customer_ID = $_GET['customer_ID'];
	$product_ID = $_GET['product_ID'];
	$category = "headphone";

	//Prepare the query
	$query = "INSERT INTO favorites(product_ID, customer_ID, category) VALUES (?,?,?)";
	//Prepare the query
	if($stmt = $con->prepare($query)){

		//Bind parameters
		$stmt->bind_param("iis",$product_ID, $customer_ID, $category);
		//Exceting MySQL statement
		$res = $stmt->execute();
		//Check if data got inserted
		if($stmt->affected_rows == 1){
			$response["success"] = 1;			
			$response["message"] = "Headphone Successfully Added to your favorites list";			
			
		}else{
			//Some error while inserting
			$response["success"] = 0;
			$response["message"] = "Error while adding the item to your favorites list";
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
//http://localhost/tba-db/carts/add_cart_headphone.php?customer_ID=2&product_ID=16
echo json_encode($response);
?>