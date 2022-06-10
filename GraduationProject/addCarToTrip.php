<?php

	
	if($_SERVER['REQUEST_METHOD'] == "POST"){
		// Get data
		$TripID = isset($_POST['TripID']) ? $_POST['TripID'] : "";
		$carNumber = isset($_POST['carNumber']) ? $_POST['carNumber'] : "";
		

		$server_name = "localhost";
		$username = "root";
		$password = "";
		$dbname = "graduation_project";
		$response  = array();
		
		$conn = new mysqli($server_name, $username, $password, $dbname);
		if ($conn->connect_error) {
			die("Connection failed: " . $conn->connect_error);
		} 
		
		$sql = "update trip set carNumber='$carNumber' where TripID='$TripID'";
		
		if ($conn->query($sql) === TRUE) {
			$response['error'] = false;
			$response['message'] = "تم حجز السيارة";
			
		} else {
			$response['error'] = true;
			$response['message'] = "Error, " . $conn->error;
			
		}
		
		echo json_encode($response);

		$conn->close();
	
	}


?>



