<?php
include '../../db/db_connect.php';
$itemArray = array();
$response = array();
//Check for mandatory parameter product_ID
if(isset($_GET['product_ID'])){
	$product_ID = $_GET['product_ID'];
	//Query to fetch headphone details
	$query = "SELECT * FROM headphone WHERE product_ID='$product_ID'";
	if($stmt = $con->prepare($query)){
		//Bind product_ID parameter to the query
		$result = mysqli_query($con,$query);
		while($row = mysqli_fetch_array($result))
		{
			//var_dump($row);
		   $itemArray["product_ID"] = $row['product_ID'];
			$itemArray["name"] = $row['name'];
			$itemArray["color"] = $row['color'];
			$itemArray["brand"] = $row['brand'];
			$itemArray["cable_length"] = $row['cable_length'];
			$itemArray["price"] = $row['price'];
			$response["success"] = 1;
			$response["data"] = $itemArray;
		}
		$stmt->close();
		$response["success"] = 1;
		$response["message"] = "HeadPhone details";
 
 
	}else{
		//Whe some error occurs
		$response["success"] = 0;
		$response["message"] = mysqli_error($con);
		
	}
 
}else{
	//When the mandatory parameter product_ID is missing
	$response["success"] = 0;
	$response["message"] = "missing parameter product_ID";
}
//Display JSON response
echo json_encode($response);
?>