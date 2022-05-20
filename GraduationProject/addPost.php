<?php

	
	if($_SERVER['REQUEST_METHOD'] == "POST"){
		$user_id = isset($_POST['user_id']) ? $_POST['user_id'] : "";
		$post = isset($_POST['post']) ? $_POST['post'] : "";       
		$date = isset($_POST['date']) ? $_POST['date'] : "";

		
		$server_name = "localhost";
		$username = "root";
		$password = "";
		$dbname = "graduation_project";
		
		$conn = new mysqli($server_name, $username, $password, $dbname);
		if ($conn->connect_error) {
			die("Connection failed: " . $conn->connect_error);
		} 
		
		
		$sql = "insert into posts (userId,date,post) values ('" . $user_id . "','" . $date . "','" . $post . "')";
		if ($conn->query($sql) === TRUE) {
			$response['error'] = false;
			$response['message'] = "تمت إضافة المنشور";
		} else {
			$response['error'] = true;
			$response['message'] = "Error " . $conn->error;
			
		} 
		
	
		echo json_encode($response);

		$conn->close();
	
	}


?>