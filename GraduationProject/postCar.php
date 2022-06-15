<?php

	if($_SERVER['REQUEST_METHOD'] == 'POST') {
		$car_number = isset($_POST['car_number']) ? $_POST['car_number'] : "";
		$car_image = isset($_POST['car_image']) ? $_POST['car_image'] : "";
		$car_type = isset($_POST['car_type']) ? $_POST['car_type'] : "";
		$car_price = isset($_POST['car_price']) ? $_POST['car_price'] : "";
		$seats_number = isset($_POST['seats_number']) ? $_POST['seats_number'] : "";
		$gear_type = isset($_POST['gear_type']) ? $_POST['gear_type'] : "";
		$providerID = isset($_POST['providerID']) ? $_POST['providerID'] : "";

		$server_name = "localhost";
		$username = "root";
		$password = "";
		$dbname = "graduation_project";
		
		$conn = new mysqli($server_name, $username, $password, $dbname);
		if($conn->connect_error) {
			die("Connection failed: " . $conn->connect_error);
		}

		$sql = "insert into car (car_number, car_image, car_type, car_price, seats_number, gear_type, providerID , status) values('".$car_number."','".$car_image."','".$car_type."','". $car_price . "','" . $seats_number . "','" . $gear_type . "','" . $providerID . "' , 'available')" ;

		if ($conn->query($sql) === TRUE) {
			$response['error'] = false;
			$response['message'] = "تم إضافة السيارة بنجاح ";
		} else {
			$response['error'] = true;
			$response['message'] = "Error " . $conn->error;
			
		} 

		echo json_encode($response);
		$conn->close();

	}	
		
?>


