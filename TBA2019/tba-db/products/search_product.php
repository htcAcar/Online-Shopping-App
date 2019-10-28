<?php
include '../db/db_connect.php';
$result = array();
$userArray = array();
$response = array();
//Query to select movie id and movie name
//Check for mandatory parameters
if(isset($_GET['search_word'])){
	$search_word = $_GET['search_word'];
//Check for mandatory parameters

	//Query to insert a movie
	$query = "SELECT  product_ID, name, price FROM `tba-db`.`phone` WHERE (CONVERT(`product_ID` USING utf8) LIKE '%$search_word%' OR CONVERT(`name` USING utf8) LIKE '%$search_word%' OR CONVERT(`color` USING utf8) LIKE '%$search_word%' OR CONVERT(`screen_size` USING utf8) LIKE '%$search_word%' OR CONVERT(`brand` USING utf8) LIKE '%$search_word%' OR CONVERT(`memory` USING utf8) LIKE '%$search_word%' OR CONVERT(`camera` USING utf8) LIKE '%$search_word%' OR CONVERT(`price` USING utf8) LIKE '%$search_word%') UNION ALL SELECT product_ID, name, price FROM `tba-db`.`headphone` WHERE (CONVERT(`product_ID` USING utf8) LIKE '%$search_word%' OR CONVERT(`name` USING utf8) LIKE '%$search_word%' OR CONVERT(`color` USING utf8) LIKE '%$search_word%' OR CONVERT(`brand` USING utf8) LIKE '%$search_word%' OR CONVERT(`cable_length` USING utf8) LIKE '%$search_word%' OR CONVERT(`price` USING utf8) LIKE '%$search_word%')";
	//Prepare the query
	if($stmt = $con->prepare($query)){
	$stmt->execute();
	//Bind the fetched data to $movieId and $movieName
	$stmt->bind_result($product_ID, $name,$price );
	//Fetch 1 row at a time					
	while($stmt->fetch()){
		//Populate the movie array
		$userArray["product_ID"] = $product_ID;
		$userArray["name"] = $name;
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
}else{
	//Mandatory parameters are missing
	$response["success"] = 0;
	$response["message"] = "missing mandatory parameters";
}

//Displaying JSON response
echo json_encode($response);
?>