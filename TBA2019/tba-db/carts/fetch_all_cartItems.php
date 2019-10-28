
<?php
include '../db/db_connect.php';
//Query to select movie id and movie name
if(isset($_GET['customer_ID'])){
$customer_ID = $_GET['customer_ID'];
$query = "SELECT product_ID, customer_ID, category FROM carts WHERE customer_ID='$customer_ID'";
$result = array();
$userArray = array();
$response = array();
//Prepare the query
if($stmt = $con->prepare($query)){
	$stmt->execute();
	//Bind the fetched data to $movieId and $movieName
	$stmt->bind_result($product_ID, $customer_ID, $category);
	//Fetch 1 row at a time					
	while($stmt->fetch()){
		//Populate the movie array
		$userArray["product_ID"] = $product_ID;
		$userArray["category"] = $category;
		//$userArray["customer_ID"] = $customer_ID;
		$result[]=$userArray;
		
	}
	$itemsResult = array();
		$x = array();
		
		$itemArray = array();
		$itemsArray = array();
		$response = array();
		$stmt->close();
	foreach ($result as $value){
		$product_ID = $value['product_ID'];
		$category = $value['category'];
		$query1 = "SELECT * FROM ";
		$query2=" WHERE `product_ID` = '$product_ID'";
		$query3= $query1.$category.$query2;

			$result = mysqli_query($con, $query3);
			$user = mysqli_fetch_assoc($result);
			if($user) {

				$itemsArray[] = $user;
			}
			/*else {
				//Mandatory parameters are missing
				$response["success"] = 0;
				$response["data"] = $user;
				$response["message"] = "Sorry, you need to register first";
			}*/

	}
			$response["success"] = 1;
			$response["data"] = $itemsArray;

 
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

