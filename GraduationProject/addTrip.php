<?php

	
	if($_SERVER['REQUEST_METHOD'] == "POST"){
		// Get data

		$PassengerID = isset($_POST['PassengerID']) ? $_POST['PassengerID'] : "";
		$TicketID = isset($_POST['TicketID']) ? $_POST['TicketID'] : "";
		$NumOfBags = isset($_POST['NumOfBags']) ? $_POST['NumOfBags'] : "";
		$TripDate = isset($_POST['TripDate']) ? $_POST['TripDate'] : "";
		$rewardPoints = isset($_POST['rewardPoints']) ? $_POST['rewardPoints'] : "";

		$server_name = "localhost";
		$username = "root";
		$password = "";
		$dbname = "graduation_project";
		
		$conn = new mysqli($server_name, $username, $password, $dbname);
		if ($conn->connect_error) {
			die("Connection failed: " . $conn->connect_error);
		} 
		
		
		$sql = "insert into trip (PassengerID, TicketID, NumOfBags,TripDate,rewardPoints) values('". $PassengerID . "','" . $TicketID . "','" . $NumOfBags . "','" . $TripDate."','".$rewardPoints. "')" ;
		
		
		

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