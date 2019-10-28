<?php
include '../db/db_connect.php';
$response = array();
 
//Check for mandatory parameters
if(isset($_GET['customer_ID'])&&isset($_GET['name'])){
	$customer_ID = $_GET['customer_ID'];
	$name = $_GET['name'];


	$query1 = "SELECT product_ID FROM products WHERE name ='$name' "; 

	if($stmt = $con->prepare($query1)){
		$stmt->execute();
		$stmt->bind_result($product_ID);
		while($stmt->fetch()){
			//Populate the movie array
			$product_ID = $product_ID;	
		}
	}else{
		//Some error while inserting
		$response["success"] = 0;
		$response["message"] = mysqli_error($con);
	}

	//Prepare the query
	$query = "INSERT INTO carts( product_ID, customer_ID) VALUES (?,?)";
	//Prepare the query
	if($stmt = $con->prepare($query)){

		//Bind parameters
		$stmt->bind_param("ii",$product_ID, $customer_ID);
		//Exceting MySQL statement
		$res = $stmt->execute();
		//Check if data got inserted
		if($stmt->affected_rows == 1){
			$response["success"] = 1;			
			$response["message"] = "Cart Successfully Added to your cart";			
			
		}else{
			//Some error while inserting
			$response["success"] = 0;
			$response["message"] = "Error while adding the item to your cart";
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