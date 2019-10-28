<?php
include '../../db/db_connect.php';
$itemArray = array();
$response = array();
//Check for mandatory parameter movie_id
if(isset($_GET['product_ID'])){
	$product_ID = $_GET['product_ID'];
	//Query to fetch movie details
	$query = "SELECT * FROM television WHERE product_ID='$product_ID'";
	if($stmt = $con->prepare($query)){
		//Bind movie_id parameter to the query
		$result = mysqli_query($con,$query);
		while($row = mysqli_fetch_array($result))
		{
			//var_dump($row);
		    $itemArray["product_ID"] = $row['product_ID'];
			$itemArray["name"] = $row['name'];
			$itemArray["brand"] = $row['brand'];
			$itemArray["screen_size"] = $row['screen_size'];
			$itemArray["resolution"] = $row['resolution'];
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
	//When the mandatory parameter movie_id is missing
	$response["success"] = 0;
	$response["message"] = "missing parameter product_ID";
}
//Display JSON response
echo json_encode($response);
?>