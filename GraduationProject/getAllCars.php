<?php 


$cat = "";
if(isset($_GET['cat'])){
	$cat = $_GET['cat'];
}


	$server_name = "localhost";
	$username = "root";
	$password = "";
	$dbname = "graduation_project";
	
	$conn = new mysqli ($server_name, $username, $password, $dbname);

	if($conn->connect_error){
		die("Connection failed: " . $conn->connect_error);
	}

	$sql = "select * from car " ;

	$result = $conn->query($sql);
	$data = "";

	if ($result->num_rows >0) {
		while($row = $result->fetch_assoc()){
			$data.=$row["car_number"]. " , " . $row["car_image"]. " , " .$row["car_type"]. " , " . $row["car_price"]. " , " . $row["car_provider"] . " @ " ;
		}
		echo $data ;
	} else {
		echo "0 results";
	}
	$conn->close();


 ?>



 

