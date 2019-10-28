<?php
include '../../db/db_connect.php';
$response = array();
 
//Check for mandatory parameters
if(isset($_GET['name'])
	&&isset($_GET['brand'])
	&&isset($_GET['screen_size'])
	&&isset($_GET['color'])
	&&isset($_GET['memory'])
	&&isset($_GET['camera'])
	&&isset($_GET['price'])
	&&isset($_GET['seller_ID'])){
	$name = $_GET['name'];
	$brand = $_GET['brand'];
	$screen_size = $_GET['screen_size'];
	$color = $_GET['color'];
	$memory = $_GET['memory'];
	$camera = $_GET['camera'];
	$price = $_GET['price'];
	$seller_ID = $_GET['seller_ID'];
	$category="phone";
	//$arr = array(1, 2, 3);
	//$arr_str = serialize($arr);
	//Query to insert a movie
	$query = "INSERT INTO products( seller_ID, category) VALUES (?,?)";
	$query2 = "INSERT INTO phone( product_ID, name, color, screen_size, brand, memory, camera, price) VALUES (?,?,?,?,?,?,?,?)";
	
	//Prepare the query
	if($stmt = $con->prepare($query)){

		//Bind parameters
		$stmt->bind_param("is", $seller_ID, $category);
		//Exceting MySQL statement
		$res = $stmt->execute();
		//var_dump($stmt->insert_id);
		$product_ID=$stmt->insert_id;
		
		//Check if data got inserted
		if($stmt->affected_rows == 1){
			$response["success"] = 1;			
			$response["message"] = "Phone Successfully Added to your products table";	
			///////////
			if($stmt2 = $con->prepare($query2)){
				//Bind parameters
				$stmt2->bind_param("isssssss", $product_ID, $name, $color, $screen_size, $brand, $memory, $camera, $price);
				//Exceting MySQL statement
				$res = $stmt2->execute();
				//Check if data got inserted
				if($stmt2->affected_rows == 1){			
					$response["message2"] = "Phone item Successfully Added";			
					
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
			///////////		
			
		}else{
			//Some error while inserting
			$response["success"] = 0;
			$response["message"] = "Error while adding phone item";
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
//http://localhost/tba-db/products/phone/add_phone.php?name=Samsung Galaxy J8&color=Black&screen_size=5.7&brand=Samsung&memory=4GB&camera=16MP&price=2000&seller_ID=2

//http://localhost/tba-db/products/phone/add_phone.php?name=Huawei Honor 10 lite&color=White&screen_size=6.21&brand=Huawei&memory=3GB&camera=13MP&price=1700&seller_ID=2

//http://localhost/tba-db/products/phone/add_phone.php?name=Samsung Galaxy A30&color=Blue&screen_size=6.1&brand=Samsung&memory=4GB&camera=16MP&price=1500&seller_ID=2

//http://localhost/tba-db/products/phone/add_phone.php?name=iPhone 6S 16GB&color=Pink&screen_size=5.5&brand=Apple&memory=4GB&camera=13MP&price=7000&seller_ID=2

echo json_encode($response);
?>