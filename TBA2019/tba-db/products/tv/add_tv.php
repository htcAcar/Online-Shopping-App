<?php
include '../../db/db_connect.php';
$response = array();
 
//Check for mandatory parameters
if(isset($_GET['name'])
	&&isset($_GET['brand'])
	&&isset($_GET['screen_size'])
	&&isset($_GET['resolution'])
	&&isset($_GET['price'])
	&&isset($_GET['seller_ID'])){
	$name = $_GET['name'];
	$brand = $_GET['brand'];
	$screen_size = $_GET['screen_size'];
	$resolution = $_GET['resolution'];
	$price = $_GET['price'];
	$seller_ID = $_GET['seller_ID'];
	$category="television";
	//$arr = array(1, 2, 3);
	//$arr_str = serialize($arr);
	//Query to insert a movie
	$query = "INSERT INTO products( seller_ID, category) VALUES (?,?)";
	$query2 = "INSERT INTO television( product_ID, name, brand, screen_size, resolution, price) VALUES (?,?,?,?,?,?)";
	
	//Prepare the query
	if($stmt = $con->prepare($query)){

		//Bind parameters
		$stmt->bind_param("is", $seller_ID, $category);
		//Exceting MySQL statement
		$res = $stmt->execute();
		var_dump($stmt->insert_id);
		$product_ID=$stmt->insert_id;
		
		//Check if data got inserted
		if($stmt->affected_rows == 1){
			$response["success"] = 1;			
			$response["message"] = "TV Successfully Added to your products table";
			/////////
			if($stmt2 = $con->prepare($query2)){	

				//Bind parameters
				$stmt2->bind_param("isssss",$product_ID, $name, $brand, $screen_size, $resolution, $price);
				//Exceting MySQL statement
				$res = $stmt2->execute();
				//Check if data got inserted
				if($stmt2->affected_rows == 1){			
					$response["message2"] = "TV item Successfully Added";			
					
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
			////////			
			
		}else{
			//Some error while inserting
			$response["success"] = 0;
			$response["message"] = "Error while adding TV item";
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
//http://localhost/tba-db/products/tv/add_tv.php?name=SEG 32SCH5630 32" 81 Ekran LED TV&brand=Sunny&screen_size=32&resolution=1366 x 768 HD&price=800&seller_ID=2

//http://localhost/tba-db/products/tv/add_tv.php?name=Samsung 40N5300 40\" TV&brand=Samsung&screen_size=35&resolution=1566 x 564 Full HD&price=1500&seller_ID=2

//http://localhost/tba-db/products/tv/add_tv.php?name=Grundig 32VLE6730 BP&brand=Grundig&screen_size=32&resolution=1920 x 540 Full HD&price=1400&seller_ID=2

//http://localhost/tba-db/products/tv/add_tv.php?name=Philips 46PDL8908S 32" Full HD&brand=Philips&screen_size=32&resolution=1920 x 1080 Full HD&price=1400&seller_ID=2
echo json_encode($response);
?>
