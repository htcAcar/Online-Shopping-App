<?php
include '../db/db_connect.php';
$response = array();
//Check for mandatory parameters
if(isset($_GET['customer_ID'])&&isset($_GET['product_ID'])){
	$customer_ID = $_GET['customer_ID'];
	$product_ID = $_GET['product_ID'];
	//Prepare the query
	$query = "DELETE FROM `carts` WHERE `customer_ID`='$customer_ID' AND  `product_ID`='$product_ID'";
	//Prepare the query
	if($stmt = $con->prepare($query)){
		//$stmt->bind_param("ii",$product_ID, $customer_ID);
		$stmt->execute();
		//Check if data got inserted
		if($stmt->affected_rows){
			$response["success"] = 1;			
			$response["message"] = "Cart Successfully deleted";			
			
		}else{
			//Some error while inserting
			$response["success"] = 0;
			$response["message"] = "Error while removing the item from your cart";
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
//http://localhost/tba-db/carts/remove_fromCart.php?customer_ID=2&product_ID=16
echo json_encode($response);
?>