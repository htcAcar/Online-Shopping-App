
<?php
include '../db/db_connect.php';
//Query to select movie id and movie name
if(isset($_GET['product_ID'])){
$product_ID = $_GET['product_ID'];
$query = "SELECT product_ID, email, review FROM reviews WHERE product_ID='$product_ID'";
$result = array();
$userArray = array();
$response = array();
//Prepare the query
if($stmt = $con->prepare($query)){
	$stmt->execute();
	//Bind the fetched data to $movieId and $movieName
	$stmt->bind_result($product_ID, $email, $review);
	//Fetch 1 row at a time					
	while($stmt->fetch()){
		//Populate the movie array
		$userArray["product_ID"] = $product_ID;
		$userArray["review"] = $review;
		$userArray["email"] = $email;
		//$userArray["customer_ID"] = $customer_ID;
		$result[]=$userArray;
		
	}
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

//Display JSON response
echo json_encode($response);
 
?>

