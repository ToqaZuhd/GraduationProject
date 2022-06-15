<?php

	
	if($_SERVER['REQUEST_METHOD'] == "POST"){
		// Get data

		$rentRequestID = isset($_POST['rentRequestID']) ? $_POST['rentRequestID'] : "";
		$TripID = isset($_POST['TripID']) ? $_POST['TripID'] : "";
		$CarNumber = isset($_POST['CarNumber']) ? $_POST['CarNumber'] : "";
		$daysNumber = isset($_POST['daysNumber']) ? $_POST['daysNumber'] : "";
		$rentDate = isset($_POST['rentDate']) ? $_POST['rentDate'] : "";

		$server_name = "localhost";
		$username = "root";
		$password = "";
		$dbname = "graduation_project";
		
		$conn = new mysqli($server_name, $username, $password, $dbname);
		if ($conn->connect_error) {
			die("Connection failed: " . $conn->connect_error);
		} 
		
		
		$sql = "insert into rentalrequest (TripID, CarNumber,rentDate,daysNumber) values('". $TripID . "','" . $CarNumber . "','" . $rentDate. "','" . $daysNumber."')" ;
		
		$sql2 = "UPDATE trip SET trip.rentRequestID=(SELECT rentRequestID FROM rentalrequest WHERE trip.TripID= rentalrequest.TripID)";

		$sql3 = "UPDATE `car` SET `status`='unavailable' WHERE car_number = ". $CarNumber;
		$conn->query($sql3);

		$sql4 = "UPDATE `rentalrequest` SET endRentalRequest = rentDate + daysNumber ";
		$conn->query($sql4);
		

		if ($conn->query($sql) === TRUE) {
			$response['error'] = false;
			$response['message'] = "تم حجز السيارة بنجاح";
		} else {
			$response['error'] = true;
			$response['message'] = "Error " . $conn->error;
			
		}

		if ($conn->query($sql2) === TRUE) {
			$response['error'] = false;
			$response['message'] = "تم حجز السيارة بنجاح";
		} else {
			$response['error'] = true;
			$response['message'] = "Error " . $conn->error;
			
		}

		if ($conn->query($sql3) === TRUE) {
			$response['error'] = false;
			$response['message'] = "تم حجز السيارة بنجاح";
		} else {
			$response['error'] = true;
			$response['message'] = "Error " . $conn->error;
			
		}

		if ($conn->query($sql4) === TRUE) {
			$response['error'] = false;
			$response['message'] = "تم حجز السيارة بنجاح";
		} else {
			$response['error'] = true;
			$response['message'] = "Error " . $conn->error;
			
		}

		
		
        
		echo json_encode($response);

		$conn->close();
	
	}


?>