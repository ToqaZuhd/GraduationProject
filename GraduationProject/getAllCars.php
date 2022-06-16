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

	$sql2 = "select CarNumber,endRentalRequest from rentalrequest " ;
	$endRentalRequest = $conn->query($sql2);  // or mysqli_query($con, $tourquery);
	$currentdate = date("Y-m-d");

	foreach($endRentalRequest as $row2) {
		if ($currentdate>$row2['endRentalRequest']) {
			$sql3 = "UPDATE `car` SET `status`='available' WHERE car_number =" .$row2['CarNumber'];
			$res = $conn->query($sql3);
		}
	}
	
	$sql = "select * from car where status = 'available' " ;

	$result = $conn->query($sql);
	$data = "";

	if ($result->num_rows >0) {
		while($row = $result->fetch_assoc()){
			$data.=$row["car_number"]. " , " . $row["car_image"]. " , " .$row["car_type"]. " , " . $row["car_price"]. " , " . $row["seats_number"] . " , " .$row["gear_type"]. " , " .$row["providerID"]. " @ " ;
		}
		echo $data ;
	} else {
		echo "0 results";
	}


	

	$conn->close();


 ?>






 

