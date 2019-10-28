<?php
include 'db/db_connect.php';
$userArrayArray = array();
$response = array();
//Check for mandatory parameter movie_id
if(isset($_GET['userID'])){
	$userID = $_GET['userID'];
	//Query to fetch movie details
	$query = "SELECT userName FROM users WHERE userID=?";
	if($stmt = $con->prepare($query)){
		//Bind movie_id parameter to the query
		$stmt->bind_param("i",$userID);
		$stmt->execute();
		//Bind fetched result to variables $movieName, $genre, $year and $rating
		$stmt->bind_result($userName);
		//Check for results		
		if($stmt->fetch()){
			//Populate the movie array
			$userArray["userName"] = $userName;
			$response["success"] = 1;
			$response["data"] = $userArray;
		
		
		}else{
			//When movie is not found
			$response["success"] = 0;
			$response["message"] = "User not found";
		}
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