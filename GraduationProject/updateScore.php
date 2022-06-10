<?php

	
	if($_SERVER['REQUEST_METHOD'] == "POST"){
		
		$Id= isset($_POST['id']) ? $_POST['id'] : "";
		$score = isset($_POST['score']) ? $_POST['score'] : "";       

		
		$server_name = "localhost";
		$username = "root";
		$password = "";
		$dbname = "graduation_project";

		$conn = new mysqli($server_name, $username, $password, $dbname);
		if ($conn->connect_error) {
			die("Connection failed: " . $conn->connect_error);
		} 
		
		
	
		$sql = "update userdata set score='" . $score . "' where userId='" .$Id. "'";


        if(!empty($score)){
          if ($conn->query($sql) === TRUE) {
		     $response['error'] = false;
		     $response['message'] = "تمت عملية شراء التذاكر بنجاح";
		  }
		}
		

	
	
		echo json_encode($response);

		$conn->close();
	
	}


?>