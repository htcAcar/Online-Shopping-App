<?php
define('DB_USER', "root"); // db user
define('DB_PASSWORD', ""); // db password (mention your db password here)
define('DB_DATABASE', "tba-db"); // database name
define('DB_SERVER', "127.0.0.1"); // db server


$con = mysqli_connect(DB_SERVER,DB_USER,DB_PASSWORD,DB_DATABASE);
 
// Check connection
if(mysqli_connect_errno()){
	echo "Failed to connect to    ùùùù MySQL: " . mysqli_connect_error();
}

?>