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
		$sql = "SELECT userdata.name as name, userdata.phoneNum as number FROM userdata INNER JOIN car ON userdata.userId=car.providerID WHERE car.car_number = ". $id;



		$result = $conn->query($sql);
		
		$row =mysqli_fetch_assoc($result);
		
		if(!empty($row)){
		$response['name']= $row['name'];
		$response['number']= $row['number'];

	}
		
		
		echo json_encode($response);
		
		$conn->close();
		
	}
	
	
?>