<?php
include '../db/db_connect.php';
$result = array();
$userArray = array();
$response = array();
//Query to select movie id and movie name

//Check for mandatory parameters
if(isset($_GET['email'])
	&&isset($_GET['password'])){
	$email = $_GET['email'];
	$password = $_GET['password'];

	//Query to insert a movie
	$query = "SELECT * FROM customers WHERE email='$email' AND password='$password'";
	//Prepare the query
	if($stmt = $con->prepare($query)){
			$result = mysqli_query($con, $query);
			$user = mysqli_fetch_assoc($result);
			if($user) {
				$response["data"] = $user;
				//$result[]=$userArray;
				$stmt->close();
				$response["success"] = 1;
				$response["data"] = $user;
				$dob = substr($user["dob"], 5);
				$email = $user["email"];
				$today=date("Y-m-d");
				$today = substr($today, 5);
				if($today == $dob){
					$to = $email;
					$subject = "Happy Birthday";

					// use wordwrap() if lines are longer than 70 characters
					$msg = "Dear Customer,<br/><brThe TBA familly wishes you a happy birthday!<br>Thank you for your time and for shopping with us ^^.<br>TBA Ltd. ";

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
			}
			else {
				//Mandatory parameters are missing
				$response["success"] = 0;
				$response["data"] = $user;
				$response["message"] = "Sorry, you need to register first";
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