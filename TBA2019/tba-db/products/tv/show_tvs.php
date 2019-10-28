<?php
include '../../db/db_connect.php';
$result = array();
$userArray = array();
$response = array();
//Query to select movie id and movie name

//Check for mandatory parameters

	//Query to insert a movie
	$query = "SELECT product_ID, name, brand, screen_size,  resolution, price FROM television ";
	//Prepare the query
	if($stmt = $con->prepare($query)){
	$stmt->execute();
	//Bind the fetched data to $movieId and $movieName
	$stmt->bind_result($product_ID, $name, $brand, $screen_size,  $resolution, $price);
	//Fetch 1 row at a time					
	while($stmt->fetch()){
		//Populate the movie array
		$userArray["product_ID"] = $product_ID;
		$userArray["name"] = $name;
		$userArray["brand"] = $brand;
		$userArray["screen_size"] = $screen_size;
		$userArray["resolution"] = $resolution;
		$userArray["price"] = $price;
		$result[]=$userArray;
		
	}
	$stmt->close();
	$response["success"] = 1;
	$response["data"] = $result;
	
 
}else{
	//Some error while fetching data
	$response["success"] = 0;
	$response["message"] = mysqli_error($con);
		
	
}
//Displaying JSON response
echo json_encode($response);
?>