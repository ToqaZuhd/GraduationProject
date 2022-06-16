<?php



	$id = "";
	
	if(isset($_GET['id'])){
		$id = $_GET['id'];
	}
	
	if(!empty($id)){
		$server_name = "localhost";
		$username = "root";
		$password = "";
		$dbname = "graduation_project";
		$response=array();
		
		$conn = new mysqli($server_name, $username, $password, $dbname);
		if ($conn->connect_error) {
			die("Connection failed: " . $conn->connect_error);
		} 
		$sql = "select TripID from trip where TicketID = '" . $id . "'";

		$result = $conn->query($sql);
		
		$row =mysqli_fetch_assoc($result);
		
		if(!empty($row)){
		$response['TripID']= $row['TripID'];
		
	}
		
		
		echo json_encode($response);
		
		$conn->close();
		
	}
	
	
?>