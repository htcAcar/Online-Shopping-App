<?php
include '../db/db_connect.php';
$result = array();
$userArray = array();
$response = array();
//Query to select movie id and movie name

//Check for mandatory parameters
if(isset($_GET['email'])){
	$email = $_GET['email'];

	//Query to insert a movie
	$query = "SELECT password FROM customers WHERE email='$email'";
	//Prepare the query
	if($stmt = $con->prepare($query)){
		//$stmt->bind_param("ii",$product_ID, $customer_ID);
		$res = $stmt->execute();
		$stmt->bind_result($password);
		while($stmt->fetch()){
			//Populate the movie array
			//var_dump($password);
			$to = $email;
			$subject = "Password Recovery";

			// use wordwrap() if lines are longer than 70 characters
			$msg = "Dear Customer,<br><br>Seems like you forgot your password. We provide you your old password for you to be able to log in again : <b>".$password."</b>.<br><br>If you did not forget your passwword, you can safely ignore this email.<br><br>Happy Shopping!<br><br>TBA Ltd. ";

			$headers = "From: TBA Ltd. <tba.contact.system@gmail.com>\r\n";
			$headers .= "Reply-To: tba.contact.system@gmail.com\r\n";
			$headers .= "Content-type: text/html\r\n";

			// send email
			if(mail($to,$subject,$msg, $headers)){
				var_dump("expression");
			}else{
				var_dump("expression2");
			}
		}
		
		//Check if data got inserted
		if($stmt->affected_rows){
			$response["success"] = 1;			
			$response["message"] = "Password retrieved sucessfully!";			
			
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
 
}else{
	//Mandatory parameters are missing
	$response["success"] = 0;
	$response["message"] = "missing mandatory parameters";
}
//Displaying JSON response
echo json_encode($response);
?>