<?php
include '../db/db_connect.php';
$response = array();
//Check for mandatory parameters
	//Prepare the query
	$RES = 2;
	$query = "UPDATE products SET count = count - 1 WHERE `product_ID` = 21 and count > 0";
	//Prepare the query
	if($stmt = $con->prepare($query)){
		//$stmt->bind_param("ii",$product_ID, $customer_ID);
		$res = $stmt->execute();
		//var_dump($RES);
		//Check if data got inserted
		if($stmt->affected_rows){
			$response["success"] = 1;			
			$response["message"] = "Cart Successfully deleted";			
			
		}else{
			//Some error while inserting
			$response["success"] = 0;
			$response["message"] = "Error while removing the item from your cart";
		}					
	}else{
		//Some error while inserting
		$response["success"] = 0;
		$response["message"] = mysqli_error($con);
	}
//Displaying JSON response
//http://localhost/tba-db/carts/remove_fromCart.php?customer_ID=2&product_ID=16
echo json_encode($response);
?>


while($stmt->fetch()){
					//var_dump("expression1");
					if($RES){
						//var_dump("expression2");
						//var_dump($product_ID);
						//var_dump($category);
						$product_query="DELETE FROM products WHERE `product_ID`='$product_ID'; ";

						$query1 = "DELETE FROM ";
						$query2=" WHERE `product_ID` = '$product_ID'; ";
						$stock_query= $query1.$category.$query2;

						$cart_query2= "DELETE FROM carts WHERE `product_ID`='$product_ID' ";

						//$total_query= $product_query.$stock_query.$cart_query2;
						//var_dump($total_query);

						$response=delete_query($con, $product_query);
						$con->next_result();
						$response=delete_query($con, $stock_query);
						$con->next_result();
						$response=delete_query($con, $cart_query2);
						$con->next_result();
						

						


					}
				
				}