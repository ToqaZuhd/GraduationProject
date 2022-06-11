<?php

	require("password.php");
	if($_SERVER['REQUEST_METHOD'] == "POST"){
		// Get data
		$user_name = isset($_POST['username']) ? $_POST['username'] : "";
		$IDNum = isset($_POST['IDNum']) ? $_POST['IDNum'] : "";
		$enteredpassword = isset($_POST['password']) ? $_POST['password'] : "";
		$phoneNum = isset($_POST['phoneNum']) ? $_POST['phoneNum'] : "";
		$email = isset($_POST['email']) ? $_POST['email'] : "";
		$image = isset($_POST['image']) ? $_POST['image'] : "";
        $role="user";

		$server_name = "localhost";
		$username = "root";
		$password = "";
		$dbname = "graduation_project";
		
		$conn = new mysqli($server_name, $username, $password, $dbname);
		if ($conn->connect_error) {
			die("Connection failed: " . $conn->connect_error);
		} 
		
		
        $passwordHash = password_hash($enteredpassword, PASSWORD_DEFAULT);
		
		$sql = "insert into userdata (Name,userId,phoneNum,email,password,role) values ('" . $user_name . "','" . $IDNum . "','" . $phoneNum . "','" .$email. "','" . $passwordHash . "','" . $role . "')";
		
		$sql2="update userdata set image='" .$image. "' where userId='" .$IDNum. "'";
	
		
		if(empty($image)){
		if ($conn->query($sql) === TRUE) {
			$response['error'] = false;
			
			$response['message'] = $passwordHash;
		} else {
			$response['error'] = true;
			$response['message'] = "Error, " . $conn->error;
			
		}
		}
		
        else{
			if ($conn->query($sql2) === TRUE) {
			$response['error'] = false;
			$response['message'] = "تمت إضافة الصورة بنجاح ";
		} else {
			$response['error'] = true;
			$response['message'] = "Error, " . $conn->error;
			
		}
		}		
        		
		echo json_encode($response);

		$conn->close();
	
	}


?>