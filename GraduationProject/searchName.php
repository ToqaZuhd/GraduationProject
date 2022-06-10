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
		$sql = "select * from userdata where userId = '" . $id . "'";
		$sql2 = "select rate from review where userId = '" . $id . "'";

		$result = $conn->query($sql);
		$result2=$conn->query($sql2);
		
		$row =mysqli_fetch_assoc($result);
	    $row2=mysqli_fetch_assoc($result2);
		
		if(!empty($row)){
		$response['name']= $row['Name'];
		$response['image']= $row['image'];
		$response['password']= $row['password']; 
		$response['phoneNum']= $row['phoneNum'];
		$response['email']= $row['email'];
		$response['score']= $row['score'];}
		
		if(!empty($row2)){
		$response['rate']=$row2['rate'];}
		echo json_encode($response);
		
		$conn->close();
		
	}
	
	
?>