<?php

	
	if($_SERVER['REQUEST_METHOD'] == "POST"){
		// Get data
		$id = isset($_POST['id']) ? $_POST['id'] : "";
		$date = isset($_POST['date']) ? $_POST['date'] : "";
		$post = isset($_POST['post']) ? $_POST['post'] : "";
		

		$server_name = "localhost";
		$username = "root";
		$password = "";
		$dbname = "graduation_project";
		$response  = array();
		
		$conn = new mysqli($server_name, $username, $password, $dbname);
		if ($conn->connect_error) {
			die("Connection failed: " . $conn->connect_error);
		} 
		
		$sql = "update daysandtimesopening set post='$post' where id='$id'";
		$sql1 = "update daysandtimesopening set date='$date' where id='$id'";
		if ($conn->query($sql) === TRUE) {
			$response['error'] = false;
			$response['message'] = "post updated!";
			
		} else {
			$response['error'] = true;
			$response['message'] = "Error, " . $conn->error;
			
		}
		if ($conn->query($sql1) === TRUE) {
			$response['error'] = false;
			$response['message'] = "post updated!";
			
		} else {
			$response['error'] = true;
			$response['message'] = "Error, " . $conn->error;
			
		}
		echo json_encode($response);

		$conn->close();
	
	}


?>