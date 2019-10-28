<?php
include '../../db/db_connect.php';
$response = array();
 
//Check for mandatory parameters
if(isset($_GET['name'])
	&&isset($_GET['brand'])
	&&isset($_GET['color'])
	&&isset($_GET['cable_length'])
	&&isset($_GET['price'])
	&&isset($_GET['seller_ID'])){
	$name = $_GET['name'];
	$brand = $_GET['brand'];
	$color = $_GET['color'];
	$cable_length = $_GET['cable_length'];
	$price = $_GET['price'];
	$seller_ID = $_GET['seller_ID'];
	$category="headphone";
	//$arr = array(1, 2, 3);
	//$arr_str = serialize($arr);
	//Query to insert a movie
	$query = "INSERT INTO products( seller_ID, category) VALUES (?,?)";
	$query2 = "INSERT INTO headphone( product_ID, name, color, brand, cable_length, price) VALUES (?,?,?,?,?,?)";
	
	//Prepare the query
	if($stmt = $con->prepare($query)){

		//Bind parameters
		$stmt->bind_param("is", $seller_ID, $category);
		//Exceting MySQL statement
		$res = $stmt->execute();
		//var_dump($stmt->insert_id);
		$product_ID=$stmt->insert_id;
		//Check if data got inserted
		
		//Check if data got inserted
		if($stmt->affected_rows == 1){
			$response["success"] = 1;			
			$response["message"] = "Headphone Successfully Added to your products table";	
 
 			////////////
			if($stmt2 = $con->prepare($query2)){

				//Bind parameters
				$stmt2->bind_param("isssss", $product_ID, $name, $brand, $color, $cable_length, $price);
				//Exceting MySQL statement
				$res = $stmt2->execute();
				
				if($stmt2->affected_rows == 1){			
					$response["message2"] = "Headphone item Successfully Added";			
					
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
			/////////////
	
		}else{
			//Some error while inserting
			$response["success"] = 0;
			$response["message"] = "Error while adding headphone item";
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
//http://localhost/tba-db/products/headphone/add_headphone.php?name=Sony MDR-ZX110&brand=Sony&color=Black&cable_length=125cm&price=10&seller_ID=2

//http://localhost/tba-db/products/headphone/add_headphone.php?name=Philips SHL5000&brand=Sony&color=White&cable_length=125cm&price=10&seller_ID=2

//http://localhost/tba-db/products/headphone/add_headphone.php?name=Philips SHL3075BL&brand=Sony&color=Pink&cable_length=125cm&price=10&seller_ID=2

//http://localhost/tba-db/products/headphone/add_headphone.php?name=Sony MDR-ZX310&brand=Sony&color=Green&cable_length=125cm&price=10&seller_ID=2

echo json_encode($response);



			
?>