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
		$sql = "select * from car where car_number = '" . $id . "'";

		$result = $conn->query($sql);
		
		$row =mysqli_fetch_assoc($result);
		
		if(!empty($row)){
		$response['car_image']= $row['car_image'];
		$response['car_type']= $row['car_type'];
		$response['car_price']= $row['car_price']; 
		$response['seats_number']= $row['seats_number'];
		$response['gear_type']= $row['gear_type'];
		$response['providerID']= $row['providerID'];
	}
		
		
		echo json_encode($response);
		
		$conn->close();
		
	}
	
	
?>