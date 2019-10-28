<?php
include '../db/db_connect.php';
$userArrayArray = array();
$response = array();
//Check for mandatory parameter movie_id
if(isset($_GET['customer_ID'])){
	$customer_ID = $_GET['customer_ID'];
	//Query to fetch movie details
	$query = "SELECT * FROM customers WHERE customer_ID='$customer_ID'";
	if($stmt = $con->prepare($query)){
		//Bind movie_id parameter to the query
		//$stmt->bind_param("i",$userID, $email, $password, $dob, $address);
		//$res=$stmt->execute();
		//Bind fetched result to variables $movieName, $genre, $year and $rating
		//$stmt->bind_result($customer_ID, $email, $password, $dob, $address);
		$result = mysqli_query($con,$query);
		while($row = mysqli_fetch_array($result))
		{
			//var_dump($row);
		    $userArray["customer_ID"] = $row['customer_ID'];
			$userArray["email"] = $row['email'];
			$userArray["password"] = $row['password'];
			$userArray["dob"] = $row['dob'];
			$userArray["address"] = $row['address'];
			$response["success"] = 1;
			$response["data"] = $userArray;
		}
		//Check for results	/	
		/**if($stmt->fetch()){
			//Populate the movie array
			
		
		
		}else{
			//When movie is not found
			$response["success"] = 0;
			$response["message"] = "User not found";
		}**/
		$stmt->close();
 
 
	}else{
		//Whe some error occurs
		$response["success"] = 0;
		$response["message"] = mysqli_error($con);
		
	}
 
}else{
	//When the mandatory parameter movie_id is missing
	$response["success"] = 0;
	$response["message"] = "missing parameter userID";
}
//Display JSON response
echo json_encode($response);
?>