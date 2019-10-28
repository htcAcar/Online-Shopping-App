<?php
include '../db/db_connect.php';
$response = array();
 $userArray = array();
 $result = array();
 $result2 = array();
 $RES = 2;
 function delete_query($con, $query){
 	if($stmt = $con->prepare($query)){
				//$stmt->bind_param("ii",$product_ID, $customer_ID);
 				$response = array();
				$stmt->execute();
				//Check if data got inserted
				if($stmt->affected_rows){
					$response["success"] = 1;			
					$response["message"] = $query;			
					
				}else{
					//Some error while inserting
					$response["success"] = 0;
					$response["message"] = mysqli_error($con)." of ".$query;
				}					
			}else{
				//Some error while inserting
				$response["success"] = 0;
				$response["message"] = mysqli_error($con)." of2 ".$query;
			}
	return $response;
 }
//Check for mandatory parameters
if(isset($_GET['customer_ID'])&&isset($_GET['email'])){
	$customer_ID = $_GET['customer_ID'];
	$email = $_GET['email'];
	

	$query = "SELECT product_ID, category FROM `carts` WHERE `customer_ID`='$customer_ID' ";

	if($stmt = $con->prepare($query)){
		$stmt->execute();
		$stmt->bind_result($product_ID, $category);
		while($stmt->fetch()){
			//Populate the movie array
			$userArray["product_ID"] = $product_ID;
			$userArray["category"] = $category;
			$result[]=$userArray;
		
		}
		//var_dump($test);
		$response["success"] = $result;
		//
		foreach ($result as $value){

			$product_ID = $value['product_ID'];
			$category = $value['category'];
			//var_dump($product_ID);
			//var_dump($category);
			$cart_query = "DELETE FROM `carts` WHERE `customer_ID`='$customer_ID' AND `product_ID`='$product_ID' ";

			$decrement_query = "UPDATE products SET count = count - 1 WHERE `product_ID` = '$product_ID' and count > 0";

			$condition_query = "SELECT `product_ID`,`category`,  IF(count=0, 'less', 'greater') AS '$RES' FROM products";

			$response=delete_query($con, $cart_query);
			$to = $email;
			var_dump($to);
			$subject = "Shipment Notification";

			// use wordwrap() if lines are longer than 70 characters
			$msg = "Dear Customer,<br><br>We're happy to let you know that the items you purchased are on their way to you:)<br>Thank you for your time and for shopping with us ^^.<br><br>TBA Ltd. ";

			$headers = "From: TBA Ltd. <tba.contact.system@gmail.com>\r\n";
			$headers .= "Reply-To: tba.contact.system@gmail.com\r\n";
			$headers .= "Content-type: text/html\r\n";

			// send email
			if(mail($to,$subject,$msg, $headers)){
				var_dump("expression");
			}else{
				var_dump("expression2");
			}
			
			$response=delete_query($con, $decrement_query);
			$con->next_result();

			if($stmt = $con->prepare($condition_query)){
				$stmt->execute();

				//var_dump($RES);
				$stmt->bind_result($product_ID, $category, $RES);
				//$stmt->close();
				$con->next_result();
				while($stmt->fetch()){
					var_dump($RES);
					if($RES == 'less'){
						var_dump($RES);
						var_dump("you entered the if statement");
						//var_dump("expression1");
						$userArray["product_ID"] = $product_ID;
						$userArray["category"] = $category;
						//$userArray["customer_ID"] = $customer_ID;
						$result2[]=$userArray;
					}
				
				}
				foreach ($result2 as $value){
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
				
						$response["success"] = 1;
						$response["data"] = "Purchase Executed!";

				//Check if data got inserted
				
			}else{
				//Some error while inserting
				$response["success"] = 0;
				$response["message"] = mysqli_error($con)." of ".$condition_query;
			}


		}

	//Prepare the query		
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

//UPDATE `products` SET `count` = '2' WHERE `products`.`product_ID` = 7
echo json_encode($response);
?>