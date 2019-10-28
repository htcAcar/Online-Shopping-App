<?php
include '../db/db_connect.php';
$response = array();
//Check for mandatory parameters
	//Prepare the query
	$RES = 2;
	$query = "SELECT `product_ID`,`category`,   IF(count=2, true, false) AS '$RES'\n". "FROM products";
	//Prepare the query
	if($stmt = $con->prepare($query)){
		//$stmt->bind_param("ii",$product_ID, $customer_ID);
		$res = $stmt->execute();
		//var_dump($RES);
		$stmt->bind_result($product_ID, $category, $RES);
		
		while($stmt->fetch()){
			if($RES){
				var_dump($product_ID);
				var_dump($category);
			}
		
		}
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

printf ("%s (%s)\n",$row["Lastname"],$row["Age"]);
			if($stmt = $con->prepare($condition_query)){
				$stmt->execute();

				//var_dump($RES);
				$stmt->bind_result($product_ID, $category, $RES);
				//$stmt->close();
				$con->next_result();
				while($stmt->fetch()){
					var_dump("expression1");
					if($RES){
						var_dump("expression2");
						var_dump($product_ID);
						var_dump($category);
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
				//Check if data got inserted
				
			}else{
				//Some error while inserting
				$response["success"] = 0;
				$response["message"] = mysqli_error($con)." of ".$condition_query;
			}



			$result=mysqli_query($con,$condition_query);

			// Associative array
			$row=mysqli_fetch_assoc($result);
			var_dump($row);
			$product_ID = $row['product_ID'];
			$category = $row['category'];

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





			foreach ($result as $value){
					$product_ID = $value['product_ID'];
					$category = $value['category'];

					$product_query="DELETE FROM products WHERE `product_ID`='$product_ID'; ";

					$query1 = "DELETE FROM ";
					$query2=" WHERE `product_ID` = '$product_ID'; ";
					$stock_query= $query1.$category.$query2;

					$cart_query2= "DELETE FROM carts WHERE `product_ID`='$product_ID' ";
					
					$favorites_query= "DELETE FROM favorites WHERE `product_ID`='$product_ID' ";

					//$total_query= $product_query.$stock_query.$cart_query2;
					//var_dump($total_query);

					$response=delete_query($con, $product_query);
					$con->next_result();
					$response=delete_query($con, $stock_query);
					$con->next_result();
					$response=delete_query($con, $cart_query2);
					$con->next_result();
					$response=delete_query($con, $favorites_query);
					$con->next_result();
				}