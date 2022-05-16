<?php

	
	if($_SERVER['REQUEST_METHOD'] == "POST"){
		// Get data
		$date = isset($_POST['date']) ? $_POST['date'] : "";
		$post = isset($_POST['post']) ? $_POST['post'] : "";
		
		

		$server_name = "localhost";
		$username = "root";
		$password = "";
		$dbname = "graduationproject";
		$response  = array();
		
		$conn = new mysqli($server_name, $username, $password, $dbname);
		if ($conn->connect_error) {
			die("Connection failed: " . $conn->connect_error);
		} 
		
		$sql = "insert into daysandtimesopening(date, post) values('" . $date . "','" . $post . "')";
		if ($conn->query($sql) === TRUE) {
			$response['error'] = false;
			$response['message'] = "post added successfully!";
			

			
		} else {
			$response['error'] = true;
			$response['message'] = "Error, " . $conn->error;
			
		}
		echo json_encode($response);

		$conn->close();
	
	}


?>