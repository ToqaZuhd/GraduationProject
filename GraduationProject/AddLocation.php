<?php

	if($_SERVER['REQUEST_METHOD'] == "POST"){
		// Get data
		$coordinate = isset($_POST['coordinate']) ? $_POST['coordinate'] : "";
		$IDNum = isset($_POST['IDNum']) ? $_POST['IDNum'] : "";
		

		$server_name = "localhost";
		$username = "root";
		$password = "";
		$dbname = "graduation_project";
		
		$conn = new mysqli($server_name, $username, $password, $dbname);
		if ($conn->connect_error) {
			die("Connection failed: " . $conn->connect_error);
		} 
		
		$sql = "insert into crisisdata (coordinate) values ('" . $coordinate . "') where userId='".$IDNum."'";
		
	
		
		
		if ($conn->query($sql) === TRUE) {
			$response['error'] = false;
			
			
		} else {
			$response['error'] = true;
			$response['message'] = "Error, " . $conn->error;
			
		}
	
		
      	echo json_encode($response);

		$conn->close();
	
	}


?>